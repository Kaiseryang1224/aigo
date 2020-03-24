package com.ctbcbank.aigo.dataAccess.mqRedis.config;

public interface MessagePublisher {
  void publish(String message);
}
