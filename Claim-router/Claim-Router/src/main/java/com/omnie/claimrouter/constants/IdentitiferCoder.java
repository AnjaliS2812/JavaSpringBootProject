package com.omnie.claimrouter.constants;

public enum IdentitiferCoder {

	//INSURANCE SEGMENT
	SEGMENT_IDENTIFICATION("INSURANCE SEGMENT", "Segment Identification","AM","segmentId"),
	CARDHOLDER_ID("INSURANCE SEGMENT", "Cardholder ID","C2","cardholderID"),
	CARDHOLDER_FIRST_NAME("INSURANCE SEGMENT", "Cardholder First Name","CC","cardholderFirstName"),
	CARDHOLDER_LAST_NAME("INSURANCE SEGMENT", "Cardholder Last Name","CD","cardholderLastName"),
	HOME_PLAN("INSURANCE SEGMENT", "Home Plan","CE","homePlan"),
	PLAN_ID("INSURANCE SEGMENT", "Plan ID","FO","planID"),
	ELIGIBILITY_CLARIFICATION_CODE("INSURANCE SEGMENT", "Eligibility Clarification Code","C9","eligibilityClarificationCode"),
	GROUP_ID("INSURANCE SEGMENT", "Group ID","C1","groupID"),
	PERSON_CODE("INSURANCE SEGMENT", "Person Code","C3","personCode"),
	PATIENT_RELATIONSHIP_CODE("INSURANCE SEGMENT", "Patient Relationship Code","C6","Patient Relationship Code"),
	OTHER_PAYER_BIN_NUMBER("INSURANCE SEGMENT", "Other Payer BIN Number","MG","Other Payer BIN Number"),
	OTHER_PAYER_PROCESSOR_CONTROL_NUMBER("INSURANCE SEGMENT", "Other Payer Processor Control Number","MH","Other Payer Processor Control Number"),
	OTHER_PAYER_CARDHOLDER_ID("INSURANCE SEGMENT", "Other Payer Cardholder ID","NU","Other Payer Cardholder ID"),
	OTHER_PAYER_GROUP_ID("INSURANCE SEGMENT", "Other Payer Group ID","MJ","Other Payer Group ID"),
	MEDIGAP_ID("INSURANCE SEGMENT", "Medigap ID","2A","Medigap ID"),
	MEDICAID_INDICATOR("INSURANCE SEGMENT", "Medicaid Indicator","2B","Medicaid Indicator"),
	PROVIDER_ACCEPT_ASSIGNMENT_INDICATOR("INSURANCE SEGMENT", "Provider Accept Assignment Indicator","2D","Provider Accept Assignment Indicator"),
	CMS_PART_D_DEFINED_QUALIFIED_FACILITY("INSURANCE SEGMENT", "CMS Part D Defined Qualified Facility","G2","CMS Part D Defined Qualified Facility"),
	MEDICAID_ID_NUMBER("INSURANCE SEGMENT", "Medicaid ID Number","N5","Medicaid ID Number"),
	MEDICAID_AGENCY_NUMBER("INSURANCE SEGMENT", "Medicaid Agency Number","N6","Medicaid Agency Number"),
	
	//PATIENT SEGMENT
	SEGMENT_IDENTIFICATIONS("PATIENT SEGMENT","Segment Identification","AM","segmentIdentification"),
	PATIENT_ID_QUALIFIER("PATIENT SEGMENT","Patient ID Qualifier","CX","patientIDQualifier"),
	PATIENT_ID("PATIENT SEGMENT","Patient ID ","CY","patientID"),
	DATE_OF_BIRTH("PATIENT SEGMENT","Date of Birth","C4","dateofBirth"),
	PATIENT_GENDER_CODE("PATIENT SEGMENT","Patient Gender Code","C5","patientGenderCode"),
	PATIENT_FIRST_NAME("PATIENT SEGMENT","Patient First Name","CA","patientFirstName"),
	PATIENT_LAST_NAME("PATIENT SEGMENT","Patient Last Name","CB","patientLastName"),
	PATIENT_STREET_ADDRESS("PATIENT SEGMENT","Patient Street Address","CM","patientStreetAddress"),
	PATIENT_CITY("PATIENT SEGMENT","Patient City ","CN","patientCity"),
	PATIENT_STATE_OR_PROVINCE("PATIENT SEGMENT","Patient State or Province","CO","patientStateorProvince"),
	PATIENT_ZIP_POSTAL_CODE("PATIENT SEGMENT","Patient Zip/Postal Code","CP","patientZipPostalCode"),
	PATIENT_PHONE_NUMBER("PATIENT SEGMENT","Patient Phone number","CQ","patientPhonenumber"),
	PLACE_OF_SERVICE("PATIENT SEGMENT","Place of Service","C7","placeofService"),
	EMPLOYER_ID("PATIENT SEGMENT","Employer ID ","CZ","employerID"),
	SMOKER_NON_SMOKER_CODE("PATIENT SEGMENT","Smoker/Non-smoker Code","1C","smokerNonsmokerCode"),
	PREGNANCY_INDICATOR("PATIENT SEGMENT","Pregnancy Indicator","2C","pregnancyIndicator"),
	PATIENT_EMAIL_ADDRESS("PATIENT SEGMENT","Patient E-Mail Address","HN","patientEmailAddress"),
	PATIENT_RESIDENCE("PATIENT SEGMENT","Patient Residence","4X","patientResidence");

	String groupKey,name, code, key;

	IdentitiferCoder(String groupKey,String name, String code, String key) {
		this.groupKey=groupKey;
		this.name=name;
		this.code=code;
		this.key=key;
	}

	public String getGroupKey() {
		return groupKey;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getKey() {
		return key;
	}
	
	public static IdentitiferCoder findStartWith(String message) {
		for(IdentitiferCoder identitifer:  values()) {
			if(message.startsWith(identitifer.getCode())) {
				return identitifer;
			}
		}
		return null;
	}
	
}
