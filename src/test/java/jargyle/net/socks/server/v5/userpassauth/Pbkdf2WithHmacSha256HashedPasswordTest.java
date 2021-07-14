package jargyle.net.socks.server.v5.userpassauth;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Pbkdf2WithHmacSha256HashedPasswordTest {

	@Test
	public void testNewInstanceCharArrayByteArray01() {
		char[] password = "opensesame".toCharArray();
		Pbkdf2WithHmacSha256HashedPassword hashedPassword1 = 
				Pbkdf2WithHmacSha256HashedPassword.newInstance(password);
		Pbkdf2WithHmacSha256HashedPassword hashedPassword2 = 
				Pbkdf2WithHmacSha256HashedPassword.newInstance(
						password, hashedPassword1.getSalt());
		assertEquals(hashedPassword1, hashedPassword2);
	}

	@Test
	public void testNewInstanceCharArrayByteArray02() {
		char[] password = "mission:impossible".toCharArray();
		Pbkdf2WithHmacSha256HashedPassword hashedPassword1 = 
				Pbkdf2WithHmacSha256HashedPassword.newInstance(password);
		Pbkdf2WithHmacSha256HashedPassword hashedPassword2 = 
				Pbkdf2WithHmacSha256HashedPassword.newInstance(
						password, hashedPassword1.getSalt());
		assertEquals(hashedPassword1, hashedPassword2);
	}

	@Test
	public void testNewInstanceCharArrayByteArray03() {
		char[] password = "safeDriversSave40%".toCharArray();
		Pbkdf2WithHmacSha256HashedPassword hashedPassword1 = 
				Pbkdf2WithHmacSha256HashedPassword.newInstance(password);
		Pbkdf2WithHmacSha256HashedPassword hashedPassword2 = 
				Pbkdf2WithHmacSha256HashedPassword.newInstance(
						password, hashedPassword1.getSalt());
		assertEquals(hashedPassword1, hashedPassword2);
	}

}
