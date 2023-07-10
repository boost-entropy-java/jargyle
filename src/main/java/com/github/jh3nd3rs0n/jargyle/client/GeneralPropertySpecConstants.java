package com.github.jh3nd3rs0n.jargyle.client;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.HostPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.PortRangesPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.PositiveIntegerPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.SocketSettingsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.lang.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;

public final class GeneralPropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	public static final PropertySpec<Host> CLIENT_BIND_HOST = 
			PROPERTY_SPECS.addThenGet(new HostPropertySpec(
					"socksClient.clientBindHost",
					Host.getAllZerosInet4Instance()));
	
	public static final PropertySpec<PortRanges> CLIENT_BIND_PORT_RANGES =
			PROPERTY_SPECS.addThenGet(new PortRangesPropertySpec(
					"socksClient.clientBindPortRanges",
					PortRanges.getDefault()));
	
	public static final PropertySpec<PositiveInteger> CLIENT_CONNECT_TIMEOUT = 
			PROPERTY_SPECS.addThenGet(new PositiveIntegerPropertySpec(
					"socksClient.clientConnectTimeout",
					PositiveInteger.newInstance(60000))); // 1 minute
	
	public static final PropertySpec<SocketSettings> CLIENT_SOCKET_SETTINGS = 
			PROPERTY_SPECS.addThenGet(new SocketSettingsPropertySpec(
					"socksClient.clientSocketSettings",
					SocketSettings.newInstance()));
	
	public static List<PropertySpec<Object>> values() {
		return PROPERTY_SPECS.toList();
	}
	
	public static Map<String, PropertySpec<Object>> valuesMap() {
		return PROPERTY_SPECS.toMap();
	}
	
	private GeneralPropertySpecConstants() { }
}
