package com.qccr.rabbit.domain;

import com.qccr.rabbit.cache.MessageCache;
import com.qccr.rabbit.common.SuccessFlag;
import com.qccr.rabbit.factory.RabbitTemplateBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

public class Sender implements SenderInter {

  private static RabbitTemplate rabbitTemplate;

  private MessageCache messageCache;

  public Sender(String exchange, String routingKey, String queue, ConnectionFactory autoFactory) {
    messageCache = new MessageCache();
    messageCache.setSender(this);
    rabbitTemplate = RabbitTemplateBuilder.createRabbitTemplate(exchange, routingKey, queue,
            autoFactory, messageCache);
  }

  @Override
  public SuccessFlag send(Object message) {
    try {
      String id = messageCache.generateId();
      messageCache.add(id, message);
      rabbitTemplate.correlationConvertAndSend(message, new CorrelationData(id));
      return new SuccessFlag(true, null,"");
    } catch (Exception e) {
      e.printStackTrace();
      return new SuccessFlag(false,null,e.getMessage());
    }
  }


}
