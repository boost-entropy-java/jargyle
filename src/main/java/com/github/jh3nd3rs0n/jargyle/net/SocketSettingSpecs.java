package com.github.jh3nd3rs0n.jargyle.net;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

final class SocketSettingSpecs {

	private final Map<String, SocketSettingSpec<Object>> socketSettingSpecMap;
	
	public SocketSettingSpecs() {
		this.socketSettingSpecMap = 
				new HashMap<String, SocketSettingSpec<Object>>();
	}
	
	public <T> SocketSettingSpec<T> add(final SocketSettingSpec<T> value) {
		@SuppressWarnings("unchecked")
		SocketSettingSpec<Object> val = (SocketSettingSpec<Object>) value;
		this.socketSettingSpecMap.put(val.toString(), val);
		return value;
	}
	
	public boolean containsKey(final String s) {
		return this.socketSettingSpecMap.containsKey(s);
	}
	
	public SocketSettingSpec<Object> get(final String s) {
		return this.socketSettingSpecMap.get(s);
	}
	
	public Map<String, SocketSettingSpec<Object>> toMap() {
		return Collections.unmodifiableMap(this.socketSettingSpecMap);
	}
	
}
