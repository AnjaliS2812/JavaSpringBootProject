package com.omnie.claimrouter.config;

import javax.sql.DataSource;


import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.omnie.switchrouter.config.ConfigurationFactory;

public class DataSourceFactory {
	
	
	private static final String CONFIG_DATASOURCE_PASSWORD = "config.datasource.password";

	private static final String CONFIG_DATASOURCE_USERNAME = "config.datasource.username";

	private static final String CONFIG_DATASOURCE_URL = "config.datasource.url";

	private static DataSourceFactory cachedFactory;
	
	private DataSource dataSource;
	
	private DataSourceFactory() {
	}

	public static DataSourceFactory getFactory() {
		if(cachedFactory==null) {
			synchronized (DataSourceFactory.class) {
				cachedFactory=new DataSourceFactory();
			}
		}
		return cachedFactory;
	}
	
	public DataSource getDataSource() {
		if(dataSource!=null) {
			return dataSource;
		}
		ConfigurationFactory configurationFactory = ConfigurationFactory.getFactory();
    	SQLServerDataSource dataSource =new SQLServerDataSource();
    	dataSource.setURL(configurationFactory.findAsString(CONFIG_DATASOURCE_URL));
    	//dataSource.set("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    	dataSource.setUser(configurationFactory.findAsString(CONFIG_DATASOURCE_USERNAME));
    	dataSource.setPassword(configurationFactory.findAsString(CONFIG_DATASOURCE_PASSWORD));
		return dataSource;
    }
}
