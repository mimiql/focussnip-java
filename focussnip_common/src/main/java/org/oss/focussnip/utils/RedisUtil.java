package org.oss.focussnip.utils;

import org.oss.focussnip.exception.BusinessErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class RedisUtil<String, V> {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    void init() {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
    }

    public void putHash(String hashK, String k, V v) {
        HashOperations<String, String, V> hash = redisTemplate.opsForHash();
        hash.put(hashK, k, v);
    }

    public V getHash(String hashK, String k) {
        HashOperations<String, String, V> hash = redisTemplate.opsForHash();
        return hash.get(hashK, k);
    }

    public Long delHash(String hashK, String k) {
        HashOperations<String, String, V> hash = redisTemplate.opsForHash();
        return hash.delete(hashK, k);
    }

    public void putValue(String k, Integer v) {
        ValueOperations<String, Integer> valueOps = redisTemplate.opsForValue();
        valueOps.set(k, v);
    }

    public Integer getValue(String k) {
        ValueOperations<String, Integer> valueOps = redisTemplate.opsForValue();
        return valueOps.get(k);
    }

    public boolean delValue(String k) {
        return Boolean.TRUE.equals(redisTemplate.delete(k));
    }

    // 乐观锁改数字
    public boolean optimismLock(String k, int change) {
        redisTemplate.setEnableTransactionSupport(true);
        boolean flag = false;
        // 十次重试机会
        RedisAtomicLong entityIdCounter = new RedisAtomicLong((java.lang.String) k, redisTemplate.getConnectionFactory());
        Long num = entityIdCounter.getAndDecrement();
        if(num>0){
            return true;
        }
        this.incr(k);
        throw new BusinessErrorException("111111","库存不足");
    }

    public Long incr(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong((java.lang.String) key, redisTemplate.getConnectionFactory());
        return entityIdCounter.getAndIncrement();
    }

    public List<V> getList(String key) {
        ListOperations<String, V> listOperations = redisTemplate.opsForList();
        Long size = listOperations.size(key);
        if (null != size) {
            return listOperations.leftPop(key, size);
        }
        throw new NullPointerException();
    }
}
