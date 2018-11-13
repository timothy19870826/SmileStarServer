package com.bigcat.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class SharedRedisClient extends RedisClient {

	private ShardedJedis jedis;
	private ShardedJedisPool jedisPool;

	protected SharedRedisClient() {
		super();
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(20);
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1001);
		config.setTestOnBorrow(true);
		
		List<JedisShardInfo> shareInfoList = new ArrayList<JedisShardInfo>();
		shareInfoList.add(new JedisShardInfo("127.0.0.1", 6379, "master"));
				
		jedisPool = new ShardedJedisPool(config, shareInfoList);
	}
	
	public Object getResource() {
		return jedisPool.getResource();
	}
	
	public String getString(String key) {
		jedis = jedisPool.getResource();
		return jedis.get(key);
	}
	
	public String setString(String key, String value) {
		jedis = jedisPool.getResource();
		return jedis.set(key, value);
	}
	
	public String setString(Integer time, String key, String value) {
		jedis = jedisPool.getResource();
		return jedis.set(key, value, "NX", "EX", time);
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
}
