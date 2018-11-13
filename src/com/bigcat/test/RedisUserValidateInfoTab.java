package com.bigcat.test;

import java.util.HashMap;

import com.bigcat.redis.RedisTable;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import redis.clients.jedis.Jedis;

public class RedisUserValidateInfoTab extends RedisTable{
	
	private final int Expire = 300;

	public RedisUserValidateInfoTab(Jedis redisRes) {
		super(redisRes);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "UserValidateInfo";
	}
	
	public UserValidateInfo getValidateInfo(String key) {
		if (redisRes.exists(getTableName()) && redisRes.hexists(getTableName(), key)) {
			String val = redisRes.hget(getTableName(), key);
			JSONObject jsonObject = JSONObject.fromObject(val);
			return (UserValidateInfo) JSONObject.toBean(jsonObject, UserValidateInfo.class);
		}
		else {
			return null;
		}
	}
	
	public void setValidateInfo(String key, UserValidateInfo val) {
		if (redisRes.exists(getTableName()) == false) {
			HashMap<String, String> hashMap = new HashMap<>();
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(UserValidateInfo.class);
			hashMap.put(key, JSONObject.fromObject(val, jsonConfig).toString());
			redisRes.hmset(getTableName(), hashMap);
		}
		else {
			redisRes.hset(getTableName(), key, JSONObject.fromObject(val).toString());
		}
		redisRes.expire(getTableName(), Expire);
	}

	public void delValidateInfo(String key) {
		if (redisRes.exists(getTableName())) {
			redisRes.del(getTableName());
		}
	}
}
