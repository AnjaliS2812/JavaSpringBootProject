package com.omnie.claim.discount.engine.service;

import com.omnie.claim.discount.engine.dto.PortBean;

public interface PortBeanService {
	
	public PortBean getPortDetail(String pBinNo, String pDateOfService, String pPcn);
	void storeValuesInCache(String binNo);
	PortBean searchValuefromCache(PortBean reqPortBean);
	String processRequest(String message);

}
