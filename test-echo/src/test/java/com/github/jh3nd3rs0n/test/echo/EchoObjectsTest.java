package com.github.jh3nd3rs0n.test.echo;

import com.github.jh3nd3rs0n.test.help.net.DatagramServer;
import com.github.jh3nd3rs0n.test.help.net.Server;
import com.github.jh3nd3rs0n.test.help.string.StringConstants;
import com.github.jh3nd3rs0n.test.help.thread.ThreadHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class EchoObjectsTest {

	private static DatagramServer echoDatagramServer;
	private static int echoDatagramServerPort;
	private static Server echoServer;
	private static int echoServerPort;

	@Rule
	public Timeout globalTimeout = Timeout.builder()
			.withTimeout(60, TimeUnit.SECONDS)
			.withLookingForStuckThread(true)
			.build();

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		echoDatagramServer = EchoDatagramServerHelper.newEchoDatagramServer(0);
		echoDatagramServer.start();
		echoDatagramServerPort = echoDatagramServer.getPort();
		echoServer = EchoServerHelper.newEchoServer(0);
		echoServer.start();
		echoServerPort = echoServer.getPort();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		if (echoDatagramServer != null
				&& !echoDatagramServer.getState().equals(DatagramServer.State.STOPPED)) {
			echoDatagramServer.stop();
		}
		if (echoServer != null
				&& !echoServer.getState().equals(Server.State.STOPPED)) {
			echoServer.stop();
		}		
		ThreadHelper.interruptibleSleepForThreeSeconds();
	}

	@Test
	public void testEchoDatagramClient01() throws IOException {
		String string = StringConstants.STRING_01;
		String returningString = new EchoDatagramClient().echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClient02() throws IOException {
		String string = StringConstants.STRING_02;
		String returningString = new EchoDatagramClient().echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClient03() throws IOException {
		String string = StringConstants.STRING_03;
		String returningString = new EchoDatagramClient().echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClient04() throws IOException {
		String string = StringConstants.STRING_04;
		String returningString = new EchoDatagramClient().echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoDatagramClient05() throws IOException {
		String string = StringConstants.STRING_05;
		String returningString = new EchoDatagramClient().echo(string, echoDatagramServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient01() throws IOException {
		String string = StringConstants.STRING_01;
		String returningString = new EchoClient().echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient02() throws IOException {
		String string = StringConstants.STRING_02;
		String returningString = new EchoClient().echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient03() throws IOException {
		String string = StringConstants.STRING_03;
		String returningString = new EchoClient().echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient04() throws IOException {
		String string = StringConstants.STRING_04;
		String returningString = new EchoClient().echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

	@Test
	public void testEchoClient05() throws IOException {
		String string = StringConstants.STRING_05;
		String returningString = new EchoClient().echo(string, echoServerPort);
		assertEquals(string, returningString);
	}

}
