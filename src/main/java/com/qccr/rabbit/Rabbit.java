package com.qccr.rabbit;

import com.qccr.rabbit.factory.ConnectionFactoryBuilder;
import com.qccr.rabbit.factory.ExchangeAndQueueBuilder;
import com.qccr.rabbit.factory.ThreadPoolConsumerBuilder;
import com.qccr.rabbit.handle.MessageProcessInter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class Rabbit {
  @Autowired
  private ConnectionFactory connectionFactory;

  private static Connection connection;

  private static ApplicationContext ctx = getInstance();

  private Rabbit() {
    log.info("实例初始化时调用:");
  }

  public ConnectionFactory getConnectionFactory() {
    return connectionFactory;
  }

  public Connection getConnection() {
    return connection;
  }

  @PostConstruct
  public void init() {
    log.info("[*]创建连接...");
    // TODO: PK 做连接中断判断,或者在使用的地方判断链接是否断开.
    connection = connectionFactory.createConnection();
  }

  public static void setConnection(String host, int port, String username, String password) {
    log.info("[*]设置初始连接...");
    ConnectionFactoryBuilder.connectionFactoryBuilder(ctx, host, port, username, password);
  }

  public void createExchange(String exchange, String routingKey, String type, boolean durable,
      boolean autoDelete, Connection connection) {
      ExchangeAndQueueBuilder.createExchange(exchange, routingKey, type, durable, autoDelete,
        connection);
  }

  public void createQueueAndBindExchange(String queue, boolean durable, boolean exclusive,
      boolean autoDelete, Connection connection, String exchange, String routingKey) {
      ExchangeAndQueueBuilder.createQueueAndBindExchange(queue, durable, exclusive, autoDelete,
        connection, exchange, routingKey);
  }

  public <T> void threadPoolConsume(int threadCount, int interval, String queue,
      MessageProcessInter messageProcess) {
      ThreadPoolConsumerBuilder.getInstance(threadCount, interval, queue, connection, messageProcess)
        .threadPoolConsume();
  }

  // 创建rabbitMQ连接
  public static Rabbit start() {
    System.out.println("Rabbit.start");
    log.info("Rabbit.start");
    return ctx.getBean(Rabbit.class);
  }

  public static ApplicationContext getInstance(){
    if (ctx == null) {
      ctx =new ClassPathXmlApplicationContext("applicationContext.xml");
    }
    return ctx;
  }
}
