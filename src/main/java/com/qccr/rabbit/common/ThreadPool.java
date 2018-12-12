package com.qccr.rabbit.common;

import com.qccr.rabbit.handle.MessageProcessInter;
import org.springframework.amqp.rabbit.connection.Connection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
