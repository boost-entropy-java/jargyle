package jargyle.net.socks.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Properties {

	public static Properties newInstance(
			final List<Property<? extends Object>> properties) {
		Map<PropertySpec<Object>, Property<Object>> props = 
				new HashMap<PropertySpec<Object>, Property<Object>>();
		for (Property<? extends Object> property : properties) {
			@SuppressWarnings("unchecked")
			Property<Object> prop = (Property<Object>) property;
			PropertySpec<Object> propSpec = prop.getPropertySpec();
			props.put(propSpec, prop);
		}
		return new Properties(props);
	}
	
	@SafeVarargs
	public static Properties newInstance(
			final Property<? extends Object>... properties) {
		return newInstance(Arrays.asList(properties));
	}
	
	private final Map<PropertySpec<Object>, Property<Object>> properties;
	
	private Properties(
			final Map<PropertySpec<Object>, Property<Object>> props) {
		this.properties = new HashMap<PropertySpec<Object>, Property<Object>>(
				props);
	}
	
	private Properties(final Properties other) {
		this.properties = new HashMap<PropertySpec<Object>, Property<Object>>(
				other.properties);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Properties other = (Properties) obj;
		if (this.properties == null) { 
			if (other.properties != null) {
				return false;
			}
		} else if (!this.properties.equals(other.properties)) {
			return false;
		}
		return true;
	}
	
	public <V> V getValue(final PropertySpec<V> propertySpec) {
		V value = null;
		Property<Object> property = this.properties.get(propertySpec);
		if (property != null) {
			value = propertySpec.getValueType().cast(property.getValue());
		}
		if (value == null) {
			Property<V> defaultProperty = propertySpec.getDefaultProperty();
			value = defaultProperty.getValue();
		}
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.properties == null) ? 
				0 : this.properties.hashCode());
		return result;
	}

	public Map<PropertySpec<Object>, Property<Object>> toMap() {
		return Collections.unmodifiableMap(this.properties);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [properties=")
			.append(this.properties)
			.append("]");
		return builder.toString();
	}
	
}
