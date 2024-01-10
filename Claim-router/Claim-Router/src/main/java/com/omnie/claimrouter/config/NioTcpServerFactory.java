package com.omnie.claimrouter.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.nio.NioTcpServer;

import com.omnie.claimrouter.constants.Constants;
import com.omnie.switchrouter.config.ConfigurationFactory;

public class NioTcpServerFactory {

	private static NioTcpServerFactory cachedFactory;

	private NioTcpServerFactory() {
	}

	public static NioTcpServerFactory getFactory() {
		if (cachedFactory == null) {
			synchronized (NioTcpServerFactory.class) {
				cachedFactory = new NioTcpServerFactory();
			}
		}
		return cachedFactory;
	}
	
	public void registorMain(String host, int port){
		ConfigurationFactory configurationFactory = ConfigurationFactory.getFactory();
		String loggingFilter =configurationFactory.findAsString(Constants.CONFIG_TCP_SERVER_LOG_FILTER);
		TcpServerHandler tcpServerHandler=new TcpServerHandler();
		SocketAddress socketAddress=new InetSocketAddress(host, port);
		NioTcpServer tcpServer=new NioTcpServer();
		tcpServer.setFilters(new LoggingFilter(loggingFilter));
		tcpServer.bind(socketAddress);
		tcpServer.setIoHandler(tcpServerHandler);
		try (BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(System.in))){
			bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		tcpServer.getManagedSessions().clear();
		tcpServer.unbind();
	}
	
	
}
