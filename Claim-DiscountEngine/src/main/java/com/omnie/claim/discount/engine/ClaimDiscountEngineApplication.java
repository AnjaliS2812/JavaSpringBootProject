package com.omnie.claim.discount.engine;

import java.io.IOException;

import com.omnie.claim.discount.engine.config.NioTcpServerFactory;
import com.omnie.claim.discount.engine.constants.Constants;
import com.omnie.switchrouter.config.ConfigurationFactory;

public class ClaimDiscountEngineApplication {

	public static void main(String[] args) throws IOException {
		ConfigurationFactory configurationFactory = ConfigurationFactory.getFactory().loadFromArgs(args);
		String host = configurationFactory.findAsString(Constants.CONFIG_TCP_SERVER_HOST);
		int port=configurationFactory.findAsInt(Constants.CONFIG_TCP_SERVER_PORT);
		NioTcpServerFactory.getFactory().registorMain(host, port);
		
	}

}
