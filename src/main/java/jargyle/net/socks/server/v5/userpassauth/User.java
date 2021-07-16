package jargyle.net.socks.server.v5.userpassauth;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import jargyle.internal.net.socks.transport.v5.userpassauth.UsernamePasswordRequest;

@XmlJavaTypeAdapter(User.UserXmlAdapter.class)
final class User {

	@XmlAccessorType(XmlAccessType.NONE)
	@XmlType(name = "user", propOrder = { })
	static class UserXml {
		@XmlElement(name = "hashedPassword", required = true)
		protected HashedPassword hashedPassword;
		@XmlElement(name = "name", required = true)
		protected String name;
	}
	
	static final class UserXmlAdapter extends XmlAdapter<UserXml, User> {

		@Override
		public UserXml marshal(final User v) throws Exception {
			UserXml userXml = new UserXml();
			userXml.name = v.name;
			userXml.hashedPassword = v.hashedPassword;
			return userXml;
		}

		@Override
		public User unmarshal(final UserXml v) throws Exception {
			return new User(v.name, v.hashedPassword);
		}
		
	}
	
	public static final int MAX_NAME_LENGTH = 
			UsernamePasswordRequest.MAX_UNAME_LENGTH;
	public static final int MAX_PASSWORD_LENGTH = 
			UsernamePasswordRequest.MAX_PASSWD_LENGTH;
	
	public static User newInstance(final String s) {
		String[] sElements = s.split(":");
		if (sElements.length != 2) {
			throw new IllegalArgumentException(
					"user must be in the following format: NAME:PASSWORD");
		}
		String userName = null;
		try {
			userName = URLDecoder.decode(sElements[0], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		String userPassword = null;
		try {
			userPassword = URLDecoder.decode(sElements[1], "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
		return newInstance(userName, userPassword.toCharArray());
	}
	
	public static User newInstance(final String name, final char[] password) {
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(password, "password must not be null");
		validateName(name);
		validatePassword(password);
		return new User(name, HashedPassword.newInstance(password));
	}
	
	public static void validateName(final String name) {
		UsernamePasswordRequest.validateUsername(name);
	}
	
	public static void validatePassword(final char[] password) {
		UsernamePasswordRequest.validatePassword(password);
	}
	
	private final String name;
	private final HashedPassword hashedPassword;
	
	private User(final String n, final HashedPassword hashedPssword) {
		this.name = n;
		this.hashedPassword = hashedPssword;
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
		User other = (User) obj;
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	public HashedPassword getHashedPassword() {
		return this.hashedPassword;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.name == null) ? 
				0 : this.name.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [hashedPassword=")
			.append(this.hashedPassword)
			.append(", name=")
			.append(this.name)
			.append("]");
		return builder.toString();
	}
}
