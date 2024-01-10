package com.omnie.claim.discount.engine.config;

import java.nio.ByteBuffer;

import org.apache.mina.api.IdleStatus;
import org.apache.mina.api.IoHandler;
import org.apache.mina.api.IoService;
import org.apache.mina.api.IoSession;
import org.apache.mina.codec.IoBuffer;
import org.apache.mina.codec.delimited.serialization.JavaNativeMessageDecoder;
import org.apache.mina.codec.delimited.serialization.JavaNativeMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.omnie.claim.discount.engine.service.JavaCodePortBeanServiceImpl;
import com.omnie.claim.discount.engine.service.PortBeanService;
import com.omnie.switchrouter.config.ConfigurationFactory;

public class TcpServerHandler implements IoHandler {

	private static final Logger LOG = LoggerFactory.getLogger(TcpServerHandler.class);
	
	private PortBeanService portBeanService =new JavaCodePortBeanServiceImpl();

	@Override
	public void sessionOpened(IoSession session) {
		LOG.info("server session opened {" + session + "}");
	}

	@Override
	public void sessionClosed(IoSession session) {
		LOG.info("IP:" + session.getRemoteAddress().toString() + " close");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {

	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		String serverName = ConfigurationFactory.getFactory().findAsString("config.tcp.server.name");
		if (message instanceof ByteBuffer) {
			try {

				JavaNativeMessageDecoder<String> decoder = new JavaNativeMessageDecoder<String>();
				IoBuffer ioBuff = IoBuffer.wrap((ByteBuffer) message);
				String messageString=decoder.decode(ioBuff);
				String messageParser = portBeanService.processRequest(messageString);
				// encode
				JavaNativeMessageEncoder<String> in = new JavaNativeMessageEncoder<String>();
				ByteBuffer encode = in.encode(serverName+": "+messageParser);
				session.write(encode);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			JavaNativeMessageEncoder<String> in = new JavaNativeMessageEncoder<String>();
			ByteBuffer encode = in.encode("Invalid request !!");
			session.write(encode);
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) {
	}

	@Override
	public void serviceActivated(IoService service) {

	}

	@Override
	public void serviceInactivated(IoService service) {

	}

	@Override
	public void exceptionCaught(IoSession session, Exception cause) {

	}


}