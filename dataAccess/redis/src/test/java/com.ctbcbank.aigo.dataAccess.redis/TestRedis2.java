package com.ctbcbank.aigo.dataAccess.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestRedis2 {

  @Autowired
  private StringRedisTemplate template;

  @Test
  public void test() {
    ValueOperations<String, String> ops = this.template.opsForValue();
    String key = "RedisApplication";
    if (!this.template.hasKey(key)) {
      ops.set(key, "aigo.demo");
    }
    log.info("找到key=" + key + ", 值=" + ops.get(key));
  }
}
