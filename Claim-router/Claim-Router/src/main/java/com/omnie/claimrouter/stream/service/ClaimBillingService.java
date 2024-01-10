package com.omnie.claimrouter.stream.service;

import com.omnie.switchrouter.stream.bean.TransactionModel;

public interface ClaimBillingService {

	TransactionModel doProcess(TransactionModel rootSegment);
}
