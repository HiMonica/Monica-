package com.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SeckillDemoApplicationTests {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedisScript redisScript;

    @Test
    public void testLock01(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //占位，如果key不存在才可以设置成功
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1");
        //如果占位成功，进行正常操作
        if (isLock){
            valueOperations.set("name","xxxxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            //操作结束，删除锁
            redisTemplate.delete("k1");
        }else {
            System.out.println("有线程正在使用，请稍后重试");
        }
    }

    @Test
    public void testLock02(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Boolean isLock = valueOperations.setIfAbsent("k1", "v1", 5, TimeUnit.SECONDS);
        if (isLock){
            valueOperations.set("name","xxxxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            valueOperations.decrement("k1");
        }else {
            System.out.println("有线程正在使用，请稍后重试");
        }
    }

    @Test
    public void testLock03(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String value = UUID.randomUUID().toString();
        Boolean isLock = valueOperations.setIfAbsent("k1", value, 5, TimeUnit.SECONDS);
        if (isLock){
            valueOperations.set("name","xxxxx");
            String name = (String) valueOperations.get("name");
            System.out.println("name = " + name);
            System.out.println(valueOperations.get("k1"));
            Boolean result = (Boolean) redisTemplate.execute(redisScript, Collections.singletonList("k1"), value);
            System.out.println(result);
        }else {
            System.out.println("有线程在使用，请稍后");
        }
    }
}
