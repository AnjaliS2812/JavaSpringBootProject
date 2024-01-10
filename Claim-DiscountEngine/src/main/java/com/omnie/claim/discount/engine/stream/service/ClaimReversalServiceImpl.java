package com.omnie.claim.discount.engine.stream.service;

import java.util.List;

import com.omnie.claim.discount.engine.config.NioTcpClientFactory;
import com.omnie.switchrouter.stream.bean.Group;
import com.omnie.switchrouter.stream.bean.HeaderRequestModel;
import com.omnie.switchrouter.stream.bean.HeaderResponseModel;
import com.omnie.switchrouter.stream.bean.ModelObject;
import com.omnie.switchrouter.stream.bean.TransactionModel;
import com.omnie.switchrouter.stream.bean.group.CouponSegment;
import com.omnie.switchrouter.stream.bean.response.ResponseClaimSegment;
import com.omnie.switchrouter.stream.bean.response.ResponseMessageSegment;
import com.omnie.switchrouter.stream.bean.response.ResponsePricingSegment;
import com.omnie.switchrouter.stream.bean.response.ResponseStatusSegment;
import com.omnie.switchrouter.stream.io.cofig.SegmentConfig;
import com.omnie.switchrouter.stream.io.reader.RequestSegmentReader;
import com.omnie.switchrouter.stream.io.reader.SegmentReader;
import com.omnie.switchrouter.stream.io.writer.RequestSegmentWriter;
import com.omnie.switchrouter.stream.io.writer.SegmentWriter;

public class ClaimReversalServiceImpl implements ClaimReversalService {

	private static final String RESPONSE_MESSAGE = "ResponseMessage";

	private static final String RESPONSE_PRICING = "ResponsePricing";

	private static final String RESPONSE_CLAIM = "ResponseClaim";

	private static final String RESPONSE_STATUS = "ResponseStatus";

	boolean isClaimEditing =true;
	
	boolean isClaimForword =false;
	
	String host="192.168.0.0";
	
	int port= 1901;


	@Override
	public TransactionModel doProcess(TransactionModel requestSegment) {
		if(isClaimEditing) {
			CouponSegment segment = getCouponSegment();
			List<Group> groups = requestSegment.getGroups();
			for(Group group : groups) {
				group.addSegment("Coupon",segment);
			}
		}
		
		if(isClaimForword) {
			return getClaimForworded(requestSegment, host, port);
		} else {
			return getGenerateResponse(requestSegment);
		}
	}

	private TransactionModel getClaimForworded(TransactionModel requestSegment, String host, int port) {
		SegmentConfig config=new SegmentConfig();
		SegmentWriter writer =new RequestSegmentWriter(config);
		String finalString = writer.parse(requestSegment);
		String response = NioTcpClientFactory.getFactory().send(host, port, finalString);
		SegmentReader reader =new RequestSegmentReader();
		return reader.parse(response);
	}

	private TransactionModel getGenerateResponse(TransactionModel requestSegment) {
		HeaderRequestModel requestHeader = (HeaderRequestModel) requestSegment.getHeader();
		
		TransactionModel responseSegment =new TransactionModel();
		responseSegment.setHeader(getHeaderResponse(requestHeader));
		
		ResponseMessageSegment responseMessage = getResponseMessageSegment();
		responseSegment.getSegments().put(RESPONSE_MESSAGE, responseMessage);
		
		List<Group>  responseGroups = responseSegment.getGroups();
		if(responseGroups.isEmpty()) {
			responseGroups.add(new Group());
		}
		for(Group group:  responseGroups) {
			group.getSegments().put(RESPONSE_STATUS, getResponseStatusSegment());
			group.getSegments().put(RESPONSE_CLAIM, getResponseClaimSegment());
			group.getSegments().put(RESPONSE_PRICING, getResponsePricingSegment());
		}
		return responseSegment;
	}

	private ModelObject getResponsePricingSegment() {
		ResponsePricingSegment responsePricingSegment=new ResponsePricingSegment(); 
		responsePricingSegment.setSegment_identification(ModelObject.buildField("23", null));
		return responsePricingSegment;
	}

	private ModelObject getResponseClaimSegment() {
		ResponseClaimSegment responseClaimSegment=new ResponseClaimSegment(); 
		responseClaimSegment.setSegment_identification(ModelObject.buildField("22", null));
		return responseClaimSegment;
	}

	private HeaderResponseModel getHeaderResponse(HeaderRequestModel requestHeader) {
		HeaderResponseModel header=new HeaderResponseModel();
		header.setService_provider_id(requestHeader.getService_provider_id());
		header.setService_provider_id_qualifier(requestHeader.getService_provider_id_qualifier());
		header.setVersion(requestHeader.getVersion());
		header.setTransaction_code(requestHeader.getTransaction_code());
		header.setDate_of_service(requestHeader.getDate_of_service());
		header.setTransaction_count(ModelObject.buildField("1", null));
		header.setHeader_response_status(ModelObject.buildField("pass", null));
		return header;
	}

	private CouponSegment getCouponSegment() {
		CouponSegment segment=new CouponSegment(); 
		segment.setSegment_identification(ModelObject.buildField("09", null));
		segment.setCoupon_type(ModelObject.buildField("Omnie", null));
		segment.setCoupon_value_amount(ModelObject.buildField("2012", null));
		segment.setCoupon_number(ModelObject.buildField("12011", null));
		return segment;
	}

	private ResponseMessageSegment getResponseMessageSegment() {
		ResponseMessageSegment responseMessage=new ResponseMessageSegment();
		responseMessage.setSegment_identification(ModelObject.buildField("20", null));
		responseMessage.setMessage(ModelObject.buildField("OK", null));
		return responseMessage;
	}

	private ResponseStatusSegment getResponseStatusSegment() {
		ResponseStatusSegment responseStatus=new ResponseStatusSegment();
		responseStatus.setSegment_identification(ModelObject.buildField("21", null));
		responseStatus.setAdditional_message_information(ModelObject.buildField("Status Ok", null));
		return responseStatus;
	}
}
