package com.bigcat.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class SingleRedisClient {
	
	private static SingleRedisClient sInstance = null;
	public static SingleRedisClient getInstance() {
		if (sInstance == null) {
			sInstance = new SingleRedisClient();
		}
		return sInstance;
	}

	//private Jedis jedis;
	private JedisPool jedisPool;
	
	protected SingleRedisClient() {
		super();
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(20);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1001);
		config.setTestOnBorrow(true);
		jedisPool = new JedisPool(config, "127.0.0.1", 6379);
	}
	
	public Jedis getResource() {
		return jedisPool.getResource();
	}
	
	/*
	public String setString(String key, String value) {
		jedis = jedisPool.getResource();
		return jedis.set(key, value);
	}
	
	public String setString(Integer time, String key, String value) {
		jedis = jedisPool.getResource();jedis.close();
		return jedis.set(key, value, "NX", "EX", time);
	}
	
	public String getString(String key) {
		jedis = jedisPool.getResource();
		return jedis.get(key);
	}
	
	public long push2List(Boolean isRPush, String name, String... value) {
		jedis = jedisPool.getResource();
		if (isRPush) {
			return jedis.rpush(name, value);
		}
		else {
			return jedis.lpush(name, value);
		}
	}
	
	public List<String> getList(String name, long start, long end) {
		jedis = jedisPool.getResource();
		return jedis.lrange(name, start, end);
	}
	
	public void push2Map(String key, Map<String, String> hash) {
		jedis = jedisPool.getResource();
		jedis.hmset(key, hash);
	}
	
	public void delMapVal(String mapName, String... mapKey) {
		jedis = jedisPool.getResource();
		jedis.hdel(mapName, mapKey);
	}
	
	public void setMapVal(String mapName, String mapKey, String mapVal) {
		jedis = jedisPool.getResource();
		jedis.hset(mapName, mapKey, mapVal);
	}
	
	public String getMapVal(String mapName, String mapKey) {
		jedis = jedisPool.getResource();
		return jedis.hget(mapName, mapKey);
	}
	
	public boolean isMapValExist(String mapName, String mapKey) {
		jedis = jedisPool.getResource();
		return jedis.hexists(mapName, mapKey);
	}
	*/
}
