package com.github.jh3nd3rs0n.jargyle.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.jh3nd3rs0n.jargyle.IoHelper;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.concurrent.ExecutorHelper;

public final class EchoServer {

	private static final class Listener implements Runnable {
		
		private final ServerSocket serverSocket;
	
		public Listener(final ServerSocket serverSock) {
			this.serverSocket = serverSock;
		}
	
		public void run() {
			ExecutorService executor = ExecutorHelper.newExecutor();
			while (true) {
				try {
					Socket clientSocket = this.serverSocket.accept();
					executor.execute(new Worker(clientSocket));
				} catch (IOException e) {
					if (e instanceof SocketException 
							|| ThrowableHelper.getRecentCause(
									e, SocketException.class) != null) {
						break;
					}
					e.printStackTrace();
					break;
				}
			}
			executor.shutdownNow();
		}
	}

	public static enum State {
		
		STARTED,
		
		STOPPED;
		
	}
	
	private static final class Worker implements Runnable {
		
		private final Socket clientSocket;
	
		public Worker(final Socket clientSock) {
			this.clientSocket = clientSock;
		}
	
		public void run() {
			try {
				InputStream in = this.clientSocket.getInputStream();
				OutputStream out = this.clientSocket.getOutputStream();
				byte[] bytes = IoHelper.readFrom(in);
				String string = new String(bytes);
				IoHelper.writeThenFlush(string.getBytes(), out);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					this.clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static final int PORT = 1084;
	
	private int actualPort;
	private ExecutorService executor;
	private final int expectedPort;	
	private final NetObjectFactory netObjectFactory;
	private ServerSocket serverSocket;
	private State state;

	public EchoServer() {
		this(new DefaultNetObjectFactory(), PORT);
	}
	
	public EchoServer(final NetObjectFactory netObjFactory, final int prt) {
		this.actualPort = -1;
		this.executor = null;
		this.expectedPort = prt;
		this.netObjectFactory = netObjFactory;
		this.serverSocket = null;
		this.state = State.STOPPED;
	}

	public int getPort() {
		return this.actualPort;
	}
	
	public State getState() {
		return this.state;
	}
	
	public void start() throws IOException {
		this.serverSocket = this.netObjectFactory.newServerSocket(
				this.expectedPort);
		this.actualPort = this.serverSocket.getLocalPort();
		this.executor = Executors.newSingleThreadExecutor();
		this.executor.execute(new Listener(this.serverSocket));
		this.state = State.STARTED;
	}

	public void stop() throws IOException {
		this.serverSocket.close();
		this.serverSocket = null;
		this.actualPort = -1;
		this.executor.shutdownNow();
		this.executor = null;
		this.state = State.STOPPED;
	}
	
}