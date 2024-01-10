package com.omnie.claimrouter.config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import org.apache.mina.api.IoFuture;
import org.apache.mina.api.IoSession;
import org.apache.mina.codec.delimited.serialization.JavaNativeMessageEncoder;
import org.apache.mina.transport.nio.NioTcpClient;

public class NioTcpClientFactory {

	private static NioTcpClientFactory cachedFactory;

	private NioTcpClientFactory() {
	}

	public static NioTcpClientFactory getFactory() {
		if (cachedFactory == null) {
			synchronized (NioTcpClientFactory.class) {
				cachedFactory = new NioTcpClientFactory();
			}
		}
		return cachedFactory;
	}
	
	private NioTcpClient nioTcpClient(TcpClientHandler clientHandler) {
		NioTcpClient nioTcpClient=new NioTcpClient();
		nioTcpClient.setIoHandler(clientHandler);
		return nioTcpClient;
	}
	
	public String send(String tcpClientHost, int tcpClientPort, String msg) {
		TcpClientHandler clientHandler=new TcpClientHandler();
		NioTcpClient nioTcpClient = nioTcpClient(clientHandler);
		try {
			IoFuture<IoSession> future = nioTcpClient.connect(new InetSocketAddress(tcpClientHost, tcpClientPort));
			IoSession session = future.get();
			JavaNativeMessageEncoder<String> in = new JavaNativeMessageEncoder<String>();
			ByteBuffer encode = in.encode(msg);
			session.write(encode);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		try {
			return clientHandler.getThreadLocal().take().toString();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				nioTcpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
    }
	
}
