package jargyle.common.net.socks5.usernamepasswordauth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

import jargyle.common.util.UnsignedByte;

public final class UsernamePasswordRequest {

	private static final class Params {
		private Version version;
		private String username;
		private char[] password;
		private byte[] byteArray;
	}
	
	public static final int MAX_UNAME_LENGTH = 255;
	public static final int MAX_PASSWD_LENGTH = 255;
	
	private static byte[] getValidatedPasswordBytes(final char[] password) {
		ByteArrayOutputStream byteArrayOutputStream = 
				new ByteArrayOutputStream();
		Writer writer = new OutputStreamWriter(byteArrayOutputStream);
		for (char ch : password) {
			try {
				writer.write(ch);
			} catch (IOException e) {
				throw new AssertionError(e);
			}
		}
		try {
			writer.flush();
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		byte[] passwordBytes = byteArrayOutputStream.toByteArray();
		if (passwordBytes.length > MAX_PASSWD_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"password must be no more than %s byte(s)",
					MAX_PASSWD_LENGTH));
		}
		return passwordBytes;
	}
	
	private static byte[] getValidatedUsernameBytes(final String username) {
		byte[] usernameBytes = username.getBytes();
		if (usernameBytes.length > MAX_UNAME_LENGTH) {
			throw new IllegalArgumentException(String.format(
					"username must be no more than %s byte(s)",
					MAX_UNAME_LENGTH));
		}
		return usernameBytes;
	}
	
	public static UsernamePasswordRequest newInstance(final byte[] b) {
		UsernamePasswordRequest usernamePasswordRequest;
		try {
			usernamePasswordRequest = newInstanceFrom(new ByteArrayInputStream(b));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
		return usernamePasswordRequest;
	}
	
	public static UsernamePasswordRequest newInstance(
			final String username,
			final char[] password) {
		char[] passwd = Arrays.copyOf(password, password.length);
		byte[] usernameBytes = getValidatedUsernameBytes(username);
		byte[] passwdBytes = getValidatedPasswordBytes(passwd);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Version version = Version.V1;
		out.write(version.byteValue());
		out.write((byte) usernameBytes.length);
		try {
			out.write(usernameBytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		out.write((byte) passwdBytes.length);
		try {
			out.write(passwdBytes);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
		Params params = new Params();
		params.version = version;
		params.username = username;
		params.password = passwd;
		params.byteArray = out.toByteArray();
		return new UsernamePasswordRequest(params);
	}
	
	public static UsernamePasswordRequest newInstanceFrom(
			final InputStream in) throws IOException {
		int b = -1;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		b = in.read();
		Version ver = null;
		try {
			ver = Version.valueOf(
					(byte) UnsignedByte.newInstance(b).intValue()); 
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(b);
		b = in.read();
		int ulen;
		try {
			ulen = UnsignedByte.newInstance(b).intValue();
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(b);
		byte[] bytes = new byte[ulen];
		int bytesRead = in.read(bytes);
		if (bytesRead != ulen) {
			throw new IOException(String.format(
					"expected username length is %s byte(s). "
					+ "actual username length is %s byte(s)", 
					ulen, bytesRead));
		}
		bytes = Arrays.copyOf(bytes, bytesRead);
		String uname = new String(bytes);
		out.write(bytes);
		b = in.read();
		int plen;
		try {
			plen = UnsignedByte.newInstance(b).intValue();
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
		out.write(b);
		bytes = new byte[plen];
		bytesRead = in.read(bytes);
		if (bytesRead != plen) {
			throw new IOException(String.format(
					"expected password length is %s byte(s). "
					+ "actual password length is %s byte(s)", 
					ulen, bytesRead));
		}
		bytes = Arrays.copyOf(bytes, bytesRead);
		Reader reader = new InputStreamReader(new ByteArrayInputStream(
				bytes));
		int passwdLength = 0;
		char[] passwd = new char[passwdLength];
		int ch = -1;
		do {
			try {
				ch = reader.read();
			} catch (IOException e) {
				throw new AssertionError(e);
			}
			if (ch != -1) {
				passwd = Arrays.copyOf(passwd, ++passwdLength);
				passwd[passwdLength - 1] = (char) ch;
			}
		} while (ch != -1);
		out.write(bytes);
		Params params = new Params();
		params.version = ver;
		params.username = uname;
		params.password = passwd;
		params.byteArray = out.toByteArray();
		return new UsernamePasswordRequest(params);
	}
	
	public static void validatePassword(final char[] password) {
		getValidatedPasswordBytes(password);
	}
	
	public static void validateUsername(final String username) {
		getValidatedUsernameBytes(username);
	}
	
	private final Version version;
	private final String username;
	private final char[] password;
	private final byte[] byteArray;
	
	private UsernamePasswordRequest(final Params params) {
		this.version = params.version;
		this.username = params.username;
		this.password = params.password;
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
		if (!(obj instanceof UsernamePasswordRequest)) {
			return false;
		}
		UsernamePasswordRequest other = (UsernamePasswordRequest) obj;
		if (!Arrays.equals(this.byteArray, other.byteArray)) {
			return false;
		}
		if (!Arrays.equals(this.password, other.password)) {
			return false;
		}
		if (this.username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!this.username.equals(other.username)) {
			return false;
		}
		if (this.version != other.version) {
			return false;
		}
		return true;
	}

	public char[] getPassword() {
		return Arrays.copyOf(this.password, this.password.length);
	}

	public String getUsername() {
		return this.username;
	}
	
	public Version getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(this.byteArray);
		result = prime * result + Arrays.hashCode(this.password);
		result = prime * result + ((this.username == null) ? 0 : this.username.hashCode());
		result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
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
			.append(", username=")
			.append(this.username)
			.append(", password=")
			.append(Arrays.toString(this.password))
			.append("]");
		return builder.toString();
	}
	
}
