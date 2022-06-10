package org.oss.focussnip.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
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

    public V getHash(String hashK,K k,V forType){
        HashOperations<String, K, V> hash = redisTemplate.opsForHash();
        return hash.get(hashK,k);
    }

    public Long delHash(String hashK,K k){
        HashOperations<String, K, V> hash = redisTemplate.opsForHash();
        return hash.delete(hashK,k);
    }

    public void putValue(String k, V v){
        ValueOperations<String, V> valueOps = redisTemplate.opsForValue();
        valueOps.set(k,v);
    }

    public V getValue(String k,V forType){
        ValueOperations<String, V> valueOps = redisTemplate.opsForValue();
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
            if(stock>0){
                valueOps.decrement(k,1);
            }
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

    public Long incr(String key, long liveTime) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        long increment = entityIdCounter.getAndIncrement();

        if ( increment == 0 && liveTime > 0) {//初始设置过期时间
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }
        return increment;
    }
}
