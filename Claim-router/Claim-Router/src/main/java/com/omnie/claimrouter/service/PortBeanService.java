package com.omnie.claimrouter.service;

import com.omnie.claimrouter.dto.PortBean;

public interface PortBeanService {
	
	public PortBean getPortDetail(String pBinNo, String pDateOfService, String pPcn);
	void storeValuesInCache(String binNo);
	PortBean searchValuefromCache(PortBean reqPortBean);
	String processRequest(String message);

}
