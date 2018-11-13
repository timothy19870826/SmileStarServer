package com.bigcat.redis;

import java.util.List;
import java.util.Map;

public abstract class RedisClient {

	
	public RedisClient() {
		
	}
	
	
	public abstract Object getResource();

	public abstract String getString(String key);
	
	public abstract String setString(String key, String value);
	
	public abstract String setString(Integer time, String key, String value);
	
	public abstract long push2List(Boolean isRPush, String name, String... value);
	
	public abstract List<String> getList(String name, long start, long end);
	
	public abstract void push2Map(String key, Map<String, String> hash);
	
	public abstract void delMapVal(String mapName, String... mapKey);
	
	public abstract void setMapVal(String mapName, String mapKey, String mapVal);
	
	public abstract String getMapVal(String mapName, String mapKey);
	
	public abstract boolean isMapValExist(String mapName, String mapKey);
}
