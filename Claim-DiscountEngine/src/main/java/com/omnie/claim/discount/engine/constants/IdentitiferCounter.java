package com.omnie.claim.discount.engine.constants;

import java.util.LinkedHashMap;
import java.util.Map;

public enum IdentitiferCounter {
	//TRANSACTION HEADER SEGMENT 
	BIN_NUMBER("BIN Number", 0, 6, "bin"),
    VERSION_RELEASE_NUMBER("Version Release Number", 6, 8, "releaseNumber"),
	TRANSACTION_CODE("Transaction Code", 8, 10, "transactionCode"),
	PROCESSOR_CONTROL_NUMBER("Processor Control Number", 10, 20, "processorControlNumber"),
	TRANSACTION_COUNT("Transaction Count", 20, 21, "transactionCount"),
	SERVICE_PROVIDER_ID_QUALIFIER("Service Provider ID Qualifier", 21, 23, "serviceProviderIDQualifier"),
	SERVICE_PROVIDER_ID("Service Provider ID", 23, 38, "serviceProviderID"),
	DATE_OF_SERVICE("Date of Service", 38, 46, "dateOfService"),
	Software_Vendor_Certification_ID("Software Vendor/Certification ID", 46, 56, "svcd");

	String name;
	int start;
	int end;
	String key;

	private IdentitiferCounter(String name, int start, int end, String key) {
		this.name = name;
		this.start = start;
		this.end = end;
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public String getKey() {
		return key;
	}

	public static Map<String, Object> finalMap(String field) {
		 Map<String, Object> returnMap=new LinkedHashMap<String, Object>();
		for(IdentitiferCounter counter :  values()) {
			returnMap.put(counter.getKey(), field.substring(counter.getStart(), counter.getEnd()));
		}
		return returnMap;
	}
}