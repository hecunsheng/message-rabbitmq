package com.qccr.rabbit.factory;

import com.qccr.rabbit.cache.MessageCache;
import com.qccr.rabbit.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;

@Slf4j
public class RabbitTemplateBuilder {
  public static RabbitTemplate createRabbitTemplate(final String exchange, final String routingKey,
                                                    final String queue, final ConnectionFactory connectionFactory, final MessageCache messageCache) {
    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMandatory(true);
    rabbitTemplate.setExchange(exchange);
    rabbitTemplate.setRoutingKey(routingKey);
    // 设置消息序列化方法,传对象消息需要使用.
    // rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

    // 设置回调,消息发送到rabbitMQ的时候,会调用此回调.
    rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String err) {
            if (!ack) {
                log.info("发送失败: " + err + correlationData.toString());
            } else {
                messageCache.del(correlationData.getId());
            }
        }
    });
    // 如果没有exchange和队列接收此消息,则会回调
    rabbitTemplate
        .setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String tmpExchange, String tmpRoutingKey) {
                try {
                    Thread.sleep(Constant.ONE_SECOND);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 发送失败
                log.info("发送失败: " + replyCode + ":" + replyText);
                // Send a message to a default exchange with a specific routing key.
                // 此处并没什么用,只是说在没有队列和exchange的情况下,往一个默认的exchange发送.
                rabbitTemplate.send(message);
            }
        });
    return rabbitTemplate;
  }
}
