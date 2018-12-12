package com.qccr.rabbit.factory;

import com.qccr.rabbit.Rabbit;
import com.qccr.rabbit.domain.Sender;
import com.qccr.rabbit.domain.SenderInter;

public class SenderBuilder {
  public static SenderInter buildMessageSender(final String exchange, final String routingKey,
                                               final String queue, final Rabbit rabbit) {
    // 1. 构造rabbitMQTemplate模块
    return new Sender(exchange, routingKey, queue, rabbit.getConnectionFactory());
  }
}
