package com.omnie.claim.discount.engine.config;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.omnie.claim.discount.engine.dto.PortBean;

public class CachedFactory {
	
	private static CachedFactory cachedFactory;
	
	private ConcurrentHashMap<String, Map<String, PortBean>> cachedMap =new  ConcurrentHashMap<String, Map<String, PortBean>>();
	
	private CachedFactory() {
	}

	public static CachedFactory getFactory() {
		if(cachedFactory==null) {
			synchronized (CachedFactory.class) {
				cachedFactory=new CachedFactory();
			}
		}
		return cachedFactory;
	}
	
	public ConcurrentHashMap<String, Map<String, PortBean>> getCachedMap() {
		return cachedMap;
	}

	public String generateKey(String ...keys) {
		return Arrays.asList(keys).stream().collect(Collectors.joining("~"));
	}

	public void putValueInCachedMap(String outerMapKey, Map<String, PortBean> map) {
		getCachedMap().put(outerMapKey, map);
	}

	public Map<String, PortBean> getValueFromCachedMap(String outerMapKey) {
		return getCachedMap().get(outerMapKey);
	}
}
