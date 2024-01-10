package com.omnie.claimrouter.service;

import static com.omnie.switchrouter.constants.Constants.CONFIG_PARSER_VALIDATION;
import static com.omnie.switchrouter.constants.Constants.CONFIG_PARSER_VARIFICATION;
import static com.omnie.switchrouter.constants.Constants.CONFIG_REDIRECT_ENABLE;
import static com.omnie.switchrouter.constants.Constants.CONFIG_TCP_REDIRECT_HOST;
import static com.omnie.switchrouter.constants.Constants.CONFIG_TCP_REDIRECT_PORT;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.omnie.claimrouter.config.CachedFactory;
import com.omnie.claimrouter.config.NioTcpClientFactory;
import com.omnie.claimrouter.dto.PortBean;
import com.omnie.claimrouter.repository.PortBeanRepository;
import com.omnie.claimrouter.stream.service.ClaimBillingService;
import com.omnie.claimrouter.stream.service.ClaimBillingServiceImpl;
import com.omnie.claimrouter.stream.service.ClaimRebillService;
import com.omnie.claimrouter.stream.service.ClaimRebillServiceImpl;
import com.omnie.claimrouter.stream.service.ClaimReversalService;
import com.omnie.claimrouter.stream.service.ClaimReversalServiceImpl;
import com.omnie.switchrouter.config.ConfigurationFactory;
import com.omnie.switchrouter.stream.bean.HeaderModel;
import com.omnie.switchrouter.stream.bean.TransactionModel;
import com.omnie.switchrouter.stream.io.cofig.SegmentConfig;
import com.omnie.switchrouter.stream.io.reader.RequestSegmentReader;
import com.omnie.switchrouter.stream.io.reader.SegmentReader;
import com.omnie.switchrouter.stream.io.validators.RequestSegementValidator;
import com.omnie.switchrouter.stream.io.varifiers.RequestSegementVarifer;
import com.omnie.switchrouter.stream.io.writer.ResponseSegmentWriter;
import com.omnie.switchrouter.stream.io.writer.SegmentWriter;
import com.omnie.switchrouter.unit.CommanUtil;

public class JavaCodePortBeanServiceImpl implements PortBeanService{
	
	private final static Logger LOGGER=LoggerFactory.getLogger(JavaCodePortBeanServiceImpl.class);
	
	private PortBeanRepository portBeanRepository=new PortBeanRepository();
	
	private ClaimBillingService claimBillingService=new ClaimBillingServiceImpl();
	
	private ClaimReversalService claimReversalService=new ClaimReversalServiceImpl();
	
	private ClaimRebillService claimRebilService=new ClaimRebillServiceImpl();
	
	@Override
	public PortBean getPortDetail(String pBinNo, String pDateOfService, String pPcn) {
		return portBeanRepository.getPortDetail(pBinNo, pDateOfService, pPcn);
	}

	@Override
	public String processRequest(String message) {
		MDC.put("transactionid", UUID.randomUUID().toString());
		LOGGER.info("Start process request");
		Boolean redirect = ConfigurationFactory.getFactory().findAsBol(CONFIG_REDIRECT_ENABLE);
		Boolean validation = ConfigurationFactory.getFactory().findAsBol(CONFIG_PARSER_VALIDATION);
		Boolean varification = ConfigurationFactory.getFactory().findAsBol(CONFIG_PARSER_VARIFICATION);
		LOGGER.info("Process request redirect : {}, validation : {}, varification : {}", redirect, validation, varification);
		String reponse=null;
		if ( redirect) {
			reponse = redirectProcess(message, validation, varification, reponse);
		} else {
			reponse = normalProcess(message, validation, varification);
		}
		LOGGER.info("End process request");
		MDC.put("transactionid", "");
		return reponse;
	}

	private String normalProcess(String message, Boolean validation, Boolean varification) {
		LOGGER.info("Process request move to normal");
		String reponse = null;
		RequestSegementValidator validator =new RequestSegementValidator();
		SegmentConfig configRequest=new SegmentConfig();
		configRequest.setValidation(validation);
		configRequest.setVarification(varification);
		configRequest.setHeaderMaxmumSize(56);
		SegmentReader reader =new RequestSegmentReader(configRequest);
		TransactionModel request = reader.parse(message);
		TransactionModel response= new TransactionModel();
		if(validator.validate(message, request,  response)) {
			if(varification) {
				LOGGER.info("Process varification request normal start");
				RequestSegementVarifer varifer =new RequestSegementVarifer();
				if(varifer.varify(request, response)) {
					LOGGER.info("Process varification pass request  normal start");
					reponse = send(request, response);
					LOGGER.info("Process varification pass request normal end");
				} else {
					LOGGER.info("Process varification fail request  normal start");
					SegmentWriter writer =new ResponseSegmentWriter();
					reponse= writer.parse(response);
					LOGGER.info("Process varification fail request normal end");
				}
				LOGGER.info("Process varification request normal end");
			}
		} else {
			LOGGER.info("Process varification fail request  normal start");
			SegmentWriter writer =new ResponseSegmentWriter();
			reponse= writer.parse(response);
			LOGGER.info("Process varification fail request normal end");
		}
		return reponse;
	}


	private String send(TransactionModel requestSegment, TransactionModel responseSegment) {
		String reponse;
		HeaderModel header = requestSegment.getHeader();
		if(header.getTransaction_code().toValue().equalsIgnoreCase("b1")) {
			responseSegment=claimBillingService.doProcess(requestSegment);
		}
		if(header.getTransaction_code().toValue().equalsIgnoreCase("b2")) {
			responseSegment=claimReversalService.doProcess(requestSegment);
		}
		if(header.getTransaction_code().toValue().equalsIgnoreCase("b3")) {
			responseSegment=claimRebilService.doProcess(requestSegment);
		}
		SegmentWriter writer =new ResponseSegmentWriter();
		reponse= writer.parse(responseSegment);
		return reponse;
	}

	private String redirectProcess(String message, Boolean validation, Boolean varification, String reponse) {
		LOGGER.info("Process request move to redirect");
		String host=ConfigurationFactory.getFactory().findAsString(CONFIG_TCP_REDIRECT_HOST);
		Integer port=ConfigurationFactory.getFactory().findAsInt(CONFIG_TCP_REDIRECT_PORT);
		LOGGER.info("Process request redirect host : {}, port : {}", host, port);
		if(validation) {
			reponse = redirectValidationProcess(message, validation, varification, reponse, host, port);
		} else {
			LOGGER.info("Process request redirect start");
			reponse= NioTcpClientFactory.getFactory().send(host, port, message);
			LOGGER.info("Process request redirect end");
		}
		return reponse;
	}

	private String redirectValidationProcess(String message, Boolean validation, Boolean varification, String reponse,
			String host, Integer port) {
		LOGGER.info("Process validation request redirect start");
		RequestSegementValidator validator =new RequestSegementValidator();
		SegmentConfig configRequest=new SegmentConfig();
		configRequest.setValidation(validation);
		configRequest.setVarification(varification);
		configRequest.setHeaderMaxmumSize(56);
		SegmentReader reader =new RequestSegmentReader(configRequest);
		TransactionModel request = reader.parse(message);
		TransactionModel response= new TransactionModel();
		if(validator.validate(message, request,  response)) {
			if(varification) {
				LOGGER.info("Process varification request redirect start");
				RequestSegementVarifer varifer =new RequestSegementVarifer();
				if(varifer.varify(request, response)) {
					LOGGER.info("Process varification pass request  redirect start");
					reponse= NioTcpClientFactory.getFactory().send(host, port, message);
					LOGGER.info("Process varification pass request redirect end");
				} else {
					LOGGER.info("Process varification fail request  redirect start");
					SegmentWriter writer =new ResponseSegmentWriter();
					reponse= writer.parse(response);
					LOGGER.info("Process varification fail request redirect end");
				}
				LOGGER.info("Process varification request redirect end");
			}
		} else {
			LOGGER.info("Process validation fail request  redirect start");
			SegmentWriter writer =new ResponseSegmentWriter();
			reponse= writer.parse(response);
			LOGGER.info("Process validation fail request redirect end");
		}
		LOGGER.info("Process validation request redirect end");
		return reponse;
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
