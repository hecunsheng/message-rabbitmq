package com.qccr.rabbit;

import com.qccr.rabbit.common.SuccessFlag;
import com.qccr.rabbit.handle.MessageProcessInter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import java.io.UnsupportedEncodingException;
@Slf4j
public class MyProcess implements MessageProcessInter {

  @Override
  public SuccessFlag process(Message message) {
    MessageConverter messageConverter = new Jackson2JsonMessageConverter();
    // ConsumeFlag cf = (ConsumeFlag) messageConverter.fromMessage(message);
    // System.out.println(cf.getError());
    String retrunMsg = "";
    try {
      retrunMsg = new String(message.getBody(), "UTF-8");
      System.out.println(retrunMsg);
      log.info(retrunMsg);
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return new SuccessFlag(true,retrunMsg, "");
  }

}
