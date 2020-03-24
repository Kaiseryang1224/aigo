package com.ctbcbank.aigo.dataAccess.mqRedis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

  @Autowired
  private RedisTemplate redisTemplate;

  /**
   * jdkSerializeable used by redisTemplate serialization, stores binary bytecode,
   *   so custom serialization class is convenient for debugging redis
   *
   * @param redisConnectionFactory
   * @return
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

    // Use Jackson2JsonRedisSerializer to serialize and deserialize the value of redis, same as redis package
    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());

    redisTemplate.setEnableTransactionSupport(true);

    redisTemplate.setConnectionFactory(redisConnectionFactory);

    return redisTemplate;
  }

  @Bean
  MessageListenerAdapter messageListener() {
    return new MessageListenerAdapter(new RedisMessageSubscriber());
  }

  @Bean
  RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                          MessageListenerAdapter listenerAdapter) {

    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(listenerAdapter, topic());

    return container;
  }

  @Bean
  MessagePublisher redisPublisher() {
    return new RedisMessagePublisher(redisTemplate, topic());
  }

  @Bean
  ChannelTopic topic() {
    return new ChannelTopic("messageQueue");
  }
}
