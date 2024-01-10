package com.omnie.claim.discount.engine.config;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.mina.api.IdleStatus;
import org.apache.mina.api.IoHandler;
import org.apache.mina.api.IoService;
import org.apache.mina.api.IoSession;
import org.apache.mina.codec.IoBuffer;
import org.apache.mina.codec.delimited.serialization.JavaNativeMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpClientHandler implements IoHandler {

	static final private Logger LOG = LoggerFactory.getLogger(TcpClientHandler.class);
	
	static BlockingDeque<Object> threadLocal = new  LinkedBlockingDeque<Object>();;
	
	public BlockingDeque<Object> getThreadLocal() {
		if(threadLocal==null) {
			 threadLocal = new  LinkedBlockingDeque<Object>();
		}
		return threadLocal;
	}

	@Override
	public void sessionOpened(IoSession session) {
		LOG.info("session opened {" + session + "}");
	}

	@Override
	public void sessionClosed(IoSession session) {
		LOG.info("client :" + session.getRemoteAddress().toString() + " close connection");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		if (message instanceof ByteBuffer) {
			JavaNativeMessageDecoder<String> decoder = new JavaNativeMessageDecoder<String>();
			IoBuffer ioBuff = IoBuffer.wrap((ByteBuffer) message);
			String response = decoder.decode(ioBuff);
			getThreadLocal().add(response);
		} else {
			getThreadLocal().add("Invalid type : "+message);
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) {
	}

	@Override
	public void serviceActivated(IoService service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void serviceInactivated(IoService service) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exceptionCaught(IoSession session, Exception cause) {
		// TODO Auto-generated method stub

	}

}