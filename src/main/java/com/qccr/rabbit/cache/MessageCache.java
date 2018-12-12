package com.qccr.rabbit.cache;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.qccr.rabbit.common.Constant;
import com.qccr.rabbit.common.SuccessFlag;
import com.qccr.rabbit.domain.Sender;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MessageCache {
  private Sender sender;
  private boolean stop = false;
  private Map<String, MessageWithTime> map = new ConcurrentHashMap<>();
  private AtomicLong id = new AtomicLong();

  public MessageCache() {}

  private class MessageWithTime {
    long time;
    Object message;

    public long getTime() {
      return time;
    }

    public MessageWithTime(long time, Object message) {
      this.time = time;
      this.message = message;
    }

    public Object getMessage() {
      return message;
    }


  }

  public void setSender(Sender sender) {
    this.sender = sender;
    startRetry();
  }

  public String generateId() {
    return "" + id.incrementAndGet();
  }

  public void add(String id, Object message) {
    map.put(id, new MessageWithTime(System.currentTimeMillis(), message));
  }

  public void del(String id) {
    map.remove(id);
  }

  private void startRetry() {
    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
    ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    singleThreadPool.execute(new Runnable() {
      @Override
      public void run() {
        while (!stop) {
          try {
            Thread.sleep(Constant.RETRY_TIME_INTERVAL);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }

          long now = System.currentTimeMillis();

          for (String key : map.keySet()) {
            MessageWithTime messageWithTime = map.get(key);

            if (null != messageWithTime) {
              if (messageWithTime.getTime() + 1440 * Constant.VALID_TIME < now) {
                log.info("send message failed after 1 days " + messageWithTime);
                del(key);
              } else if (messageWithTime.getTime() + Constant.VALID_TIME < now) {
                SuccessFlag successFlag = sender.send(messageWithTime.getMessage());

                if (successFlag.isSuccess()) {
                  del(key);
                }
              }
            }
          }
        }
      }
    });
    singleThreadPool.shutdown();
  }
}
