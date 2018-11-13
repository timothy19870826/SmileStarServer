package com.bigcat.redis;


import redis.clients.jedis.Jedis;

public abstract class RedisTable {
	
	protected Jedis redisRes;
	
	public RedisTable(Jedis redisRes) {
		this.redisRes = redisRes;
	}
	
	public abstract String getTableName();
}
