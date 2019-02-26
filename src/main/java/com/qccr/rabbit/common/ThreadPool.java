package com.qccr.rabbit.common;

import com.qccr.rabbit.handle.MessageProcessInter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.rabbit.connection.Connection;

import java.util.concurrent.ExecutorService;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadPool {

  int threadCount;
  long interval;
  // String exchange;
  // String routingKey;
  String queue;
  Connection connection;
  MessageProcessInter messageProcess;
  public ExecutorService executor;

}
