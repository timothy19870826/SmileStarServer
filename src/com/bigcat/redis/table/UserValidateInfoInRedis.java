package com.bigcat.redis.table;

import java.util.HashMap;

import com.bigcat.redis.SingleRedisClient;
import com.bigcat.test.UserValidateInfo;

import redis.clients.jedis.Jedis;

public class UserValidateInfoInRedis {
	
	public static UserValidateInfoInRedis read(String key) {
		Jedis jedis = SingleRedisClient.getInstance().getResource();
		if (jedis.exists(key)) {
			return new UserValidateInfoInRedis(jedis, key);
		}
		else {
			return null;
		}
	}
	
	public static UserValidateInfoInRedis write(String key, UserValidateInfo val, int expireInSeconds) {
		Jedis jedis = SingleRedisClient.getInstance().getResource();
		return new UserValidateInfoInRedis(jedis, key, val, expireInSeconds);
	}
	
	public static void delete(String key) {
		Jedis jedis = SingleRedisClient.getInstance().getResource();
		jedis.del(key);
		jedis.close();
	}

	private String userKey;
	private Jedis jedis;
	
	protected UserValidateInfoInRedis(Jedis jedis, String key) {
		this.jedis = jedis;
		this.userKey = key;
	}
	
	protected UserValidateInfoInRedis(Jedis jedis, String key, UserValidateInfo val, int expireInSeconds) {
		this.jedis = jedis;
		this.userKey = key;
		HashMap<String, String> hashMap = new HashMap<>();
		hashMap.put(UserValidateInfo.getEmailCodeField(), val.getEmailCode());
		hashMap.put(UserValidateInfo.getLastOpTimeField(), val.getLastOpTime());
		jedis.del(key);
		jedis.hmset(key, hashMap);
		if (expireInSeconds > 0) {
			jedis.expire(key, expireInSeconds);
		}
	}
	
	public String getEmailCode() {
		return this.jedis.hget(userKey, UserValidateInfo.getEmailCodeField());
	}
	
	public void setEmailCode(String emailCode) {
		this.jedis.hset(userKey, UserValidateInfo.getEmailCodeField(), emailCode);
	}
	
	public String getLastOpTime() {
		return this.jedis.hget(userKey, UserValidateInfo.getLastOpTimeField());
	}
	
	public void setLastReadTime(String lastOpTime) {
		this.jedis.hset(userKey, UserValidateInfo.getLastOpTimeField(), lastOpTime);
	}
	
	public void close() {
		this.jedis.close();
	}
}
