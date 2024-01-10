package com.omnie.claim.discount.engine.stream.service;

import com.omnie.switchrouter.stream.bean.TransactionModel;

public interface ClaimBillingService {

	TransactionModel doProcess(TransactionModel rootSegment);
}
