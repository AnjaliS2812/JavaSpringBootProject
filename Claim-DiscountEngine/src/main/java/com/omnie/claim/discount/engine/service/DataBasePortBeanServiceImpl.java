package com.omnie.claim.discount.engine.service;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.omnie.claim.discount.engine.config.CachedFactory;
import com.omnie.claim.discount.engine.constants.CommanUtil;
import com.omnie.claim.discount.engine.dto.PortBean;
import com.omnie.claim.discount.engine.repository.ClaimRepository;
import com.omnie.claim.discount.engine.repository.PortBeanRepository;

public class DataBasePortBeanServiceImpl implements PortBeanService{
	
	private PortBeanRepository portBeanRepository=new PortBeanRepository();
	
	private ClaimRepository claimRepository=new ClaimRepository();
	
	@Override
	public PortBean getPortDetail(String pBinNo, String pDateOfService, String pPcn) {
		return portBeanRepository.getPortDetail(pBinNo, pDateOfService, pPcn);
	}

	@Override
	public String processRequest(String message) {
		claimRepository.parserClaim("Ram Kishor", 1, "Transaction Header Segment", message);
		/*PortBean portBean = new PortBean();
		message = message + Constants.ETX;
		if (CommanUtil.isWrappedRequest((String) message)) {
			message = Constants.STX + ((String) message).substring(40);
		}
		
		portBean.setBin(CommanUtil.getBinNumberFromMessage((String) message));
		portBean.setDateOfService(CommanUtil.getDateOfServicFromMessage((String) message));
		portBean.setPcn(CommanUtil.getProcessorControlNumberFromMessage((String) message));
		portBean.setGroupNum(CommanUtil.getGroupNumber((String) message));
		portBean = this.searchValuefromCache(portBean);
		if (portBean == null) {
			return message;
		}*/
		return message+"-Sachin Kohil" /* + Constants.STX + portBean.getHost()+":"+portBean.getPort()+Constants.ETX */;
	}
	
	@Override
	public synchronized void storeValuesInCache(String binNo) {
		CachedFactory factory = CachedFactory.getFactory();
		factory.getCachedMap().clear();
		List<PortBean> portBeansList = portBeanRepository.getPortDetailsforCache(binNo);
		if (portBeansList != null) {
			for (PortBean bean : portBeansList) {
				String outerMapKey = factory.generateKey(bean.getStartDate(), bean.getTerminationDate());
				Map<String, PortBean> map = CachedFactory.getFactory().getValueFromCachedMap(outerMapKey);
				String innerMapKey = factory.generateKey(bean.getGroupNum(), bean.getPcn(), bean.getBin());
				if (null != map) {
					map.put(innerMapKey, bean);
				} else {
					map = new Hashtable<String, PortBean>();
					map.put(innerMapKey, bean);
				}
				factory.putValueInCachedMap(outerMapKey,map);
			}
		}
	}
	
	@Override
	public PortBean searchValuefromCache(PortBean reqPortBean) {
		CachedFactory factory = CachedFactory.getFactory();
		PortBean portBean = null;
		Map<String, Map<String, PortBean>> map = factory.getCachedMap();
		if (map.isEmpty()) {
			storeValuesInCache(reqPortBean.getBin());
		}
		String checkdate = CommanUtil.dateFormate(reqPortBean.getDateOfService(), "yyyyMMdd", "yyyy-MM-dd");
		for (Entry<String, Map<String, PortBean>> entry : map.entrySet()) {
			String key = entry.getKey();
			String date1 = key.substring(0, key.lastIndexOf("~"));
			String date2 = key.substring(key.lastIndexOf("~") + 1, key.length());
			Date dateStart = CommanUtil.stringToDate(date1);
			Date dateEnd = CommanUtil.stringToDate(date2);
			Date dateCheck = CommanUtil.stringToDate(checkdate);
			boolean result = CommanUtil.between(dateCheck, dateStart, dateEnd);
			if (result) {
				portBean = entry.getValue().get(reqPortBean.getGroupNum().trim() + "~"+ reqPortBean.getPcn().trim() + "~"+ reqPortBean.getBin().trim());
				if(portBean == null){
					portBean = entry.getValue().get("" + "~"+ reqPortBean.getPcn().trim() + "~"+ reqPortBean.getBin().trim());
				}
				if(portBean == null){
					portBean = entry.getValue().get("" + "~"+ "" + "~"+ reqPortBean.getBin().trim());
				}
				if (portBean != null) {
					if(!portBean.isPortFlag()){
						portBean.setPortFlag(true);
						portBean.setPort(portBean.getPort1());
					}else{
						portBean.setPortFlag(false);
						portBean.setPort(portBean.getPort2());
					}
					break;

				}
			}
		}
		return portBean;
	}

}
