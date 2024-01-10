package com.omnie.claimrouter;

import java.io.IOException;

import com.omnie.claimrouter.config.NioTcpServerFactory;
import com.omnie.claimrouter.constants.Constants;
import com.omnie.switchrouter.config.ConfigurationFactory;

public class ClaimRouterApplication {

	public static void main(String[] args) throws IOException {
		ConfigurationFactory configurationFactory = ConfigurationFactory.getFactory().loadFromArgs(args);
		System.out.println(configurationFactory.getConfigurationCache());
		String host = configurationFactory.findAsString(Constants.CONFIG_TCP_SERVER_HOST);
		int port=configurationFactory.findAsInt(Constants.CONFIG_TCP_SERVER_PORT);
		NioTcpServerFactory.getFactory().registorMain(host, port);
	}

}
