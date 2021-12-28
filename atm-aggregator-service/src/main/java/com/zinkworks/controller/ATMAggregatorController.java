package com.zinkworks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zinkworks.constants.ATMErrorKeys;
import com.zinkworks.dto.ATMCheckBalanceRequest;
import com.zinkworks.dto.ATMCheckBalanceResponse;
import com.zinkworks.dto.ATMWithdrawBalanceRequest;
import com.zinkworks.dto.ATMWithdrawBalanceResponse;
import com.zinkworks.dto.AccountWithdrawableDetails;
import com.zinkworks.dto.DenominationCount;
import com.zinkworks.dto.ValidateCardRequest;
import com.zinkworks.dto.ValidateCardResponse;
import com.zinkworks.exception.DenominationUpdateFailureException;
import com.zinkworks.exception.InsufficientDenominationException;
import com.zinkworks.exception.InvalidDenominationException;
import com.zinkworks.externaldto.AccountCheckBalanceResponse;
import com.zinkworks.externaldto.AdjustDenominationResponse;
import com.zinkworks.service.ATMProcessService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping({ "/atm" })
@Slf4j
public class ATMAggregatorController {

	@Autowired
	public ATMProcessService service;

	@GetMapping({ "/" })
	public String serviceStatus() {
		return " ATM Aggregator Service is up and running";
	}

	@PostMapping("/checkBalance")
	public ATMCheckBalanceResponse checkBalance(@RequestBody ATMCheckBalanceRequest atmCheckBalanceRequest) {
		log.info("Entering checkBalance workflow");
		ATMCheckBalanceResponse atmCheckBalanceResponse = new ATMCheckBalanceResponse();
		AccountCheckBalanceResponse AccountCheckBalanceResponse = service
				.checkAccountBalance(atmCheckBalanceRequest.getHashedAccountNumber());
		atmCheckBalanceResponse.setAvailableBalance(AccountCheckBalanceResponse.getAvailableBalance());
		atmCheckBalanceResponse.setMaxWithdrawableBalance(AccountCheckBalanceResponse.getMaxWithdrawableBalance());
		atmCheckBalanceResponse.setErrorCode(AccountCheckBalanceResponse.getErrorCode());
		atmCheckBalanceResponse.setErrorMessage(AccountCheckBalanceResponse.getErrorMessage());
		return atmCheckBalanceResponse;
	}

	@PostMapping({ "/withdraw" })
	public ATMWithdrawBalanceResponse withdrawBalance(
			@RequestBody ATMWithdrawBalanceRequest atmWithdrawBalanceRequest) {
		log.info("Entering withdrawBalance workflow");
		ATMWithdrawBalanceResponse atmWithdrawBalanceResponse = new ATMWithdrawBalanceResponse();
		DenominationCount denomination;

		try {
			denomination = service.validateReturnDenomination(atmWithdrawBalanceRequest.getAtmId(),
					atmWithdrawBalanceRequest.getAmount());
		} 
		catch (InvalidDenominationException exception) {
			atmWithdrawBalanceResponse.setErrorCode(ATMErrorKeys.INVALID_DENOMINATION_EXCEPTION.getErrorKey());
			atmWithdrawBalanceResponse.setErrorMessage("Invalid Amount Entered, please enter amount in correct denomination..");
			return atmWithdrawBalanceResponse;
		}
		catch (InsufficientDenominationException exception) {
			atmWithdrawBalanceResponse.setErrorCode(ATMErrorKeys.ATM_INSUFFICIENT_BALANCE.getErrorKey());
			atmWithdrawBalanceResponse.setErrorMessage("Requested Balance is not available with ATM");
			return atmWithdrawBalanceResponse;
		}
		AccountWithdrawableDetails accountWithdrawableDetails;
		try {
			accountWithdrawableDetails = service.withdrawBalance(atmWithdrawBalanceRequest.getHashedAccountNumber(),
					atmWithdrawBalanceRequest.getAmount(), atmWithdrawBalanceRequest.getAtmId());
			if(accountWithdrawableDetails.getErrorCode()!=null && accountWithdrawableDetails.getErrorCode().equalsIgnoreCase("401")) {
				atmWithdrawBalanceResponse.setErrorCode(ATMErrorKeys.CUSTOMER_INSUFFICIENT_BALANCE.getErrorKey());
				atmWithdrawBalanceResponse.setErrorMessage("Requested Balance is not available with Account");
				return atmWithdrawBalanceResponse;
			}else if(accountWithdrawableDetails.getErrorCode()!=null && accountWithdrawableDetails.getErrorCode().equalsIgnoreCase("302")){
				atmWithdrawBalanceResponse.setErrorCode(ATMErrorKeys.ACCOUNT_NOT_FOUND.getErrorKey());
				atmWithdrawBalanceResponse.setErrorMessage("Account Not found");
				denomination=null;
				return atmWithdrawBalanceResponse;
			}
		} catch (Exception exception) {
			atmWithdrawBalanceResponse.setErrorCode(ATMErrorKeys.SERVER_DOWN.getErrorKey());
			atmWithdrawBalanceResponse.setErrorMessage("SERVER_DOWN");
			return atmWithdrawBalanceResponse;
		}
		try {
			AdjustDenominationResponse response =service.adjustDenomination(atmWithdrawBalanceRequest.getAtmId(), denomination);
			if(!response.isUpdate()) {
				throw new DenominationUpdateFailureException("Denomination update Failed");
			}
		} catch (Exception exception) {
			boolean statusUpdate = service.revertWithdrawBalance(
					atmWithdrawBalanceRequest.getHashedAccountNumber(), atmWithdrawBalanceRequest.getAmount());
					if(!statusUpdate) {
							log.info("sending  email notification to bank.Please contact bank for manual processing/update");
					}
			atmWithdrawBalanceResponse.setErrorCode(ATMErrorKeys.ATM_OUT_OF_SERVICE.getErrorKey());
			atmWithdrawBalanceResponse.setErrorMessage("ATM Is Down");
			return atmWithdrawBalanceResponse;
		}
		atmWithdrawBalanceResponse.setDenominationCount(denomination);
		atmWithdrawBalanceResponse.setAccountWithdrawableDetails(accountWithdrawableDetails);
		return atmWithdrawBalanceResponse;
	}
	
	@PostMapping("/authenticate")
	public ValidateCardResponse validateCard(@RequestBody ValidateCardRequest request) {
		log.info("Entering Validate Card workflow");
		ValidateCardResponse cardResponse = new ValidateCardResponse();
		cardResponse =service.validateCard(request.getCardId(),request.getPin());
		return cardResponse;
	}
}
