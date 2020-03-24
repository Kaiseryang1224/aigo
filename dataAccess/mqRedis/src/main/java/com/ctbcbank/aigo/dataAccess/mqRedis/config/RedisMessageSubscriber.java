package com.ctbcbank.aigo.dataAccess.mqRedis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Message subscriber
 */
@Service
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

  public static List<String> messageList = new ArrayList<>();

  public void onMessage(Message message, byte[] pattern) {
    messageList.add(message.toString());
    log.info("Subscriber 接收到了消息==>{}", message.toString());
  }
}
