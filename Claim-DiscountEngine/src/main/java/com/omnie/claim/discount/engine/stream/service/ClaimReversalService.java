package com.omnie.claim.discount.engine.stream.service;

import com.omnie.switchrouter.stream.bean.TransactionModel;

public interface ClaimReversalService {

	TransactionModel doProcess(TransactionModel requestSegment);

}
