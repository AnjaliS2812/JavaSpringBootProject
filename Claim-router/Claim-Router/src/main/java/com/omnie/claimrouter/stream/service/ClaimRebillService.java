package com.omnie.claimrouter.stream.service;

import com.omnie.switchrouter.stream.bean.TransactionModel;

public interface ClaimRebillService {

	TransactionModel doProcess(TransactionModel rootSegment);
	
}
