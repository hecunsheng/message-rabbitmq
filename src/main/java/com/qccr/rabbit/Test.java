package com.qccr.rabbit;

import com.qccr.rabbit.common.Constant;
import com.qccr.rabbit.domain.ConsumerInter;
import com.qccr.rabbit.domain.SenderInter;
import com.qccr.rabbit.factory.SenderBuilder;
import com.qccr.rabbit.factory.ThreadPoolConsumerBuilder;


public class Test {

  public static void main(String[] args) {
    String exchange = "rabbit";
    String routingKey = "";
    String type = Constant.TOPIC;
    String queue = "rabbitQueue";
    boolean durable = true;
    boolean autoDelete = false;
    boolean exclusive = false;

    // 1. 设置链接
    Rabbit.setConnection("172.30.221.119", 5672, "guest", "guest");
    // 2. 创建rabbit对象
    Rabbit rabbit = Rabbit.start();
    // 3. 创建exchange
    rabbit.createExchange(exchange, routingKey, type, durable, autoDelete, rabbit.getConnection());
    // 4. 创建队列并绑定exchange
    rabbit.createQueueAndBindExchange(queue, durable, exclusive, autoDelete, rabbit.getConnection(),
        exchange, routingKey);
    // 3. 创建发送者
    SenderInter sender = SenderBuilder.buildMessageSender(exchange, routingKey, "", rabbit);
    sender.send("我是rabbit");
    // sender.send(new ConsumeFlag(true, "I am rabbit"));
    // 4. 创建线程池接收者
    MyProcess mp = new MyProcess();
    rabbit.threadPoolConsume(2, 1, queue, mp);
  }

}
