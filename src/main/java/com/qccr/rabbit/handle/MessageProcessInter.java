package com.qccr.rabbit.handle;

import com.qccr.rabbit.common.SuccessFlag;
import org.springframework.amqp.core.Message;

public interface MessageProcessInter {
  SuccessFlag process(Message message);
}
