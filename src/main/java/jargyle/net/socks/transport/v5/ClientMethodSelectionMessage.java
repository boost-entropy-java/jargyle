package jargyle.net.socks.transport.v5;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jargyle.util.UnsignedByte;

public final class ClientMethodSelectionMessage {
	
	private static final class Params {
		private Version version;
		private Methods methods;
		private byte[] byteArray;
	}
	
	public static ClientMethodSelectionMessage newInstance(final byte[] b) {
		ClientMethodSelectionMessage cmsm;
		try {
			cmsm = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return cmsm;
	}
	
	public static ClientMethodSelectionMessage newInstance(
			final Methods methods) {
		List<Method> methodsList = methods.toList();
		if (methodsList.size() > UnsignedByte.MAX_INT_VALUE) {
			throw new IllegalArgumentException(String.format(
					"number of methods must be no more than %s",
					UnsignedByte.MAX_INT_VALUE));
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V5;
		out.write(UnsignedByte.newInstance(version.byteValue()).intValue());
		out.write(methodsList.size());
		for (Method method : methodsList) {
			out.write(UnsignedByte.newInstance(method.byteValue()).intValue());
		}
		Params params = new Params();
		params.version = version;
		params.methods = methods;
		params.byteArray = out.toByteArray();
		return new ClientMethodSelectionMessage(params);
	}
	
	public static ClientMethodSelectionMessage newInstanceFrom(
			final InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version ver = Version.valueOfByteFrom(in);
		out.write(UnsignedByte.newInstance(ver.byteValue()).intValue());
		UnsignedByte methodCount = UnsignedByte.newInstanceFrom(in);
		out.write(methodCount.intValue());
		byte[] bytes = new byte[methodCount.intValue()];
		if (methodCount.intValue() > 0) {
			int bytesRead = in.read(bytes);
			if (bytesRead != methodCount.intValue()) {
				throw new IOException(String.format(
						"expected number of methods is %s. "
						+ "actual number of methods is %s", 
						methodCount.intValue(), bytesRead));
			}
			bytes = Arrays.copyOf(bytes, bytesRead);			
		}
		List<Method> meths = new ArrayList<Method>();
		for (byte by : bytes) {
			Method meth = null;
			try {
				meth = Method.valueOfByte(by);
			} catch (IllegalArgumentException e) {
				throw new IOException(e);
			}
			meths.add(meth);
			out.write(UnsignedByte.newInstance(by).intValue());
		}
		Params params = new Params();
		params.version = ver;
		params.methods = Methods.newInstance(meths);
		params.byteArray = out.toByteArray();
		return new ClientMethodSelectionMessage(params);
	}
	
	private final Version version;
	private final Methods methods;
	private final byte[] byteArray;
	
	private ClientMethodSelectionMessage(final Params params) {
		this.version = params.version;
		this.methods = params.methods;
		this.byteArray = params.byteArray;
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
		ClientMethodSelectionMessage other = (ClientMethodSelectionMessage) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		return true;
	}

	public Methods getMethods() {
		return this.methods;
	}
	
	public Version getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.byteArray);
		return result;
	}

	public byte[] toByteArray() {
		return Arrays.copyOf(this.byteArray, this.byteArray.length);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [version=")
			.append(this.version)
			.append(", methods=")
			.append(this.methods)
			.append("]");
		return builder.toString();
	}
	
}
