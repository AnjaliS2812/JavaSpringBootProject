package com.omnie.claim.discount.engine.constants;

public class Constants {
	

	public static final String STX = "";
	public static final String ETX = "";
	public static final String EMPTY_STR = "";
	public static final String COMMA = ",";
	public static final String C1 = "C1";
	public static final String _D0 = "D0";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String CB = "CB";
	public static final String H_HMMSS_SSS = "HHmmssSSS";
	public static final String TP = "TP";
	public static final String CA = "CA";
	public static final String D2 = "D2";
	public static final String AM01 = "AM01";
	public static final String YYYY_M_MDD = "yyyyMMdd";
	
	public static final String SQL_GET_PORT_DETAILS = "{call Rates.sp_GetPortDetails(?,?,?)}";
	public static final String SQL_GET_PORT_DETAILS_FOR_CACHE = "{call Rates.sp_GetPortDetailsforCache(?)}";
	public static final String SQL_CLAIM_PARSER = "{call [NCPDP].[ParseB1RequestStream_v2](?,?,?,?)}";
	
	public static final String CONFIG_TCP_SERVER_HOST= "config.tcp.server.host";
	public static final String CONFIG_TCP_SERVER_PORT= "config.tcp.server.port";
	public static final String CONFIG_TCP_SERVER_LOG_FILTER= "config.tcp.server.log.filter";
	
	public static final String CONFIG_TCP_FORWORD_REQUEST_ENABLE= "config.tcp.forword.request.enable";
	public static final String CONFIG_TCP_FORWORD_REQUEST_HOST= "config.tcp.forword.request.host";
	public static final String CONFIG_TCP_FORWORD_REQUEST_port= "config.tcp.forword.request.port";
}
