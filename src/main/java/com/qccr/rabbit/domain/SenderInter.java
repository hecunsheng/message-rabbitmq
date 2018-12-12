package com.qccr.rabbit.domain;

import com.qccr.rabbit.common.SuccessFlag;

public interface SenderInter {
  // 对象发送
  SuccessFlag send(Object message);
}
