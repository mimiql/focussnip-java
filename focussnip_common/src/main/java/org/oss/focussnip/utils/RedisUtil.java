package org.oss.focussnip.utils;

import org.oss.focussnip.exception.BusinessErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil<K,V> {

    @Autowired
    private RedisTemplate redisTemplate;

    public void putHash(String hashK,K k, V v){
        HashOperations<String, K, V> hash = redisTemplate.opsForHash();
        hash.put(hashK,k,v);
    }

    public V getHash(String hashK,K k){
        HashOperations<String, K, V> hash = redisTemplate.opsForHash();
        return hash.get(hashK,k);
    }

    public Long delHash(String hashK,K k){
        HashOperations<String, K, V> hash = redisTemplate.opsForHash();
        return hash.delete(hashK,k);
    }

    public void putValue(String k, Integer v){
        ValueOperations<String, Integer> valueOps = redisTemplate.opsForValue();
        valueOps.set(k,v);
    }

    public Integer getValue(String k){
        ValueOperations<String, Integer> valueOps = redisTemplate.opsForValue();
        return valueOps.get(k);
    }

    public boolean delValue(String k){
        return Boolean.TRUE.equals(redisTemplate.delete(k));
    }

    // 乐观锁改数字
    public boolean optimismLock(String k,int change){
        redisTemplate.setEnableTransactionSupport(true);
        boolean flag = false;
        // 十次重试机会
        for(int i=0;i<10;i++){
            redisTemplate.watch(k);
            redisTemplate.multi();
            ValueOperations<String, Integer> valueOps = redisTemplate.opsForValue();
            int stock = valueOps.get(k);
            // 判断是否有库存
            if(stock<1){
                redisTemplate.unwatch();
                break;
            }
            valueOps.decrement(k,change);
            List exec = redisTemplate.exec();
            if(exec.size()!=0){
                flag = true;
                redisTemplate.unwatch();
                break;
            }
            redisTemplate.unwatch();
        }
        return flag;
    }

    public Long incr(String key) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return entityIdCounter.getAndIncrement();
    }

    public void putinList(String key, V... v){
        ListOperations<String,V> listOperations = redisTemplate.opsForList();
        listOperations.rightPushAll(key,v);
    }

    public List<V> getList(String key){
        ListOperations<String,V> listOperations = redisTemplate.opsForList();
        Long size = listOperations.size(key);
        if (null!=size){
            return listOperations.leftPop(key,size);
        }
        throw new NullPointerException();
    }
}
