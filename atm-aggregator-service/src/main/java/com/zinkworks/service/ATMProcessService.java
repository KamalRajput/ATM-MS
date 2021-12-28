package com.zinkworks.service;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zinkworks.dto.AccountWithdrawableDetails;
import com.zinkworks.dto.DenominationCount;
import com.zinkworks.dto.ValidateCardResponse;
import com.zinkworks.exception.AccountNotFoundException;
import com.zinkworks.exception.InsufficientAccountBalanceException;
import com.zinkworks.exception.InsufficientDenominationException;
import com.zinkworks.exception.InvalidDenominationException;
import com.zinkworks.externaldto.ATMCardRequest;
import com.zinkworks.externaldto.ATMCardResponse;
import com.zinkworks.externaldto.ATMServiceBalanceRequest;
import com.zinkworks.externaldto.ATMServiceBalanceResponse;
import com.zinkworks.externaldto.AccountCheckBalanceRequest;
import com.zinkworks.externaldto.AccountCheckBalanceResponse;
import com.zinkworks.externaldto.AccountWithdrawBalanceRequest;
import com.zinkworks.externaldto.AccountWithdrawBalanceResponse;
import com.zinkworks.externaldto.AdjustDenominationRequest;
import com.zinkworks.externaldto.AdjustDenominationResponse;
import com.zinkworks.externaldto.RevertBalanceRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ATMProcessService {

	@Autowired
	public RestTemplate restTemplate;

	public AccountCheckBalanceResponse checkAccountBalance(String hashedAccountNumber) {
		log.info("Inside checkAccountBalance method of ATMProcessService");
		AccountCheckBalanceRequest request = new AccountCheckBalanceRequest();
		request.setHashedAccountNumber(hashedAccountNumber);
		ResponseEntity<AccountCheckBalanceResponse> accountCheckBalanceResponseResponseEntity = restTemplate
				.postForEntity("http://account-service:9091/account/balanceEnquiry", request,
						AccountCheckBalanceResponse.class, new HashMap<>());
		assert accountCheckBalanceResponseResponseEntity.getStatusCode() == HttpStatus.OK;
		AccountCheckBalanceResponse checkBalanceResponse = accountCheckBalanceResponseResponseEntity.getBody();
		assert checkBalanceResponse != null;
		return checkBalanceResponse;
	}

	public DenominationCount validateReturnDenomination(int atmId, Double amount)
			throws InsufficientDenominationException, InvalidDenominationException {
		log.info("Inside validateReturnDenomination method of ATMProcessService");
		DenominationCount count = new DenominationCount();
		ATMServiceBalanceRequest request = new ATMServiceBalanceRequest();
		request.setAtmId(atmId);
		request.setAmount(amount);
		ResponseEntity<ATMServiceBalanceResponse> atmServiceBalanceResponseResponseEntity = restTemplate.postForEntity(
				"http://atm-info-service:9092/atmInfo/denomination", request, ATMServiceBalanceResponse.class,
				new HashMap<>());

		assert atmServiceBalanceResponseResponseEntity.getStatusCode() == HttpStatus.OK;
		ATMServiceBalanceResponse checkATMBalanceResponse = atmServiceBalanceResponseResponseEntity.getBody();
		assert checkATMBalanceResponse != null;

		// count denomination to be used.
		int availableFiftyQty = checkATMBalanceResponse.getFiftys();
		int availableTwentyQty = checkATMBalanceResponse.getTwentys();
		int availableTensQty = checkATMBalanceResponse.getTens();
		int availableFiveQty = checkATMBalanceResponse.getFive();
		double atmBalance = checkATMBalanceResponse.getBalance();

		int fiftiesTobeUsed = 0;
		int twentiesTobeUsed = 0;
		int tensTobeUsed = 0;
		int fivesTobeUsed = 0;

		while (amount >= 0 && !(amount > atmBalance)) {
			if((amount %5!=0) && (amount%10!=0) && (amount%20!=0) && (amount%50!=0)){
				throw new InvalidDenominationException("Invalid Denomination Exception");
			}
			else if (amount >= 50 && (availableFiftyQty > 0)) {
				amount -= 50;
				fiftiesTobeUsed += 1;
				availableFiftyQty -= 1;
			} else if (amount >= 20 && (availableTwentyQty > 0)) {
				amount -= 20;
				twentiesTobeUsed += 1;
				availableTwentyQty -= 1;
			} else if (amount >= 10 && (availableTensQty > 0)) {
				amount -= 10;
				tensTobeUsed += 1;
				availableTensQty -= 1;
			} else if (amount >= 5 && (availableFiveQty > 0)) {
				amount -= 5;
				fivesTobeUsed += 1;
				availableFiveQty -= 1;
			}
			else if (amount == 0) {
				break;
			} else {
				throw new InsufficientDenominationException("Not sufficient funds to fulfill the request");
			}
		}
		count.setFiftys(fiftiesTobeUsed);
		count.setTwentys(twentiesTobeUsed);
		count.setTens(tensTobeUsed);
		count.setFive(fivesTobeUsed);
		return count;
	}

	public AccountWithdrawableDetails withdrawBalance(String hashedAccountNumber, Double amount, int id)
			throws InsufficientAccountBalanceException, AccountNotFoundException {
		AccountWithdrawableDetails details = new AccountWithdrawableDetails();
		AccountWithdrawBalanceRequest request = new AccountWithdrawBalanceRequest();
		request.setAtmId(id);
		request.setHashedAccountNumber(hashedAccountNumber);
		request.setAmount(amount);

		ResponseEntity<AccountWithdrawBalanceResponse> accountWithdrawResponseResponseEntity = restTemplate
				.postForEntity("http://account-service:9091/account/withdrawMoney", request,
						AccountWithdrawBalanceResponse.class, new HashMap<>());

		assert accountWithdrawResponseResponseEntity.getStatusCode() == HttpStatus.OK;
		AccountWithdrawBalanceResponse withdrawResponse = accountWithdrawResponseResponseEntity.getBody();
		assert withdrawResponse != null;
		if (withdrawResponse.errorCode != null) {
			details.setErrorCode(withdrawResponse.getErrorCode());
			details.setErrorMessage(withdrawResponse.getErrorMessage());
			return details;
		}

		details.setAvailableBalance(withdrawResponse.getAvailableBalance());
		details.setOverDraftBalance(withdrawResponse.getOverDraftBalance());
		return details;
	}

	public boolean revertWithdrawBalance(String hashedAccountNumber, Double amount) {
		RevertBalanceRequest req = new RevertBalanceRequest();
		req.setAmount(amount);
		req.setHashedAccountNumber(hashedAccountNumber);
		boolean status = false;
		HttpEntity<RevertBalanceRequest> HTTP_ENTITY = new HttpEntity<RevertBalanceRequest>(req);
		try {
			ResponseEntity<Boolean> aa= restTemplate.exchange("http://account-service:9091/account/revertBalance",
					HttpMethod.PUT,HTTP_ENTITY,Boolean.class);
			status = true;
		} catch (Exception e) {
		}
		return status;
	}

	public AdjustDenominationResponse adjustDenomination(int atmId, DenominationCount denomination) throws IOException {
		
		AdjustDenominationRequest request = new AdjustDenominationRequest();
		request.setAtmId(atmId);
		request.setDenomination(denomination);
		ResponseEntity<AdjustDenominationResponse> adjustDenominationResponseEntity = restTemplate.postForEntity(
				"http://atm-info-service:9092/atmInfo/adjustDenomination", request, AdjustDenominationResponse.class,
				new HashMap<>());
		AdjustDenominationResponse adjustDenominationResponse = adjustDenominationResponseEntity.getBody();
		return adjustDenominationResponse;

	}

	public ValidateCardResponse validateCard(int cardId, int pin) {
		ATMCardRequest request = new ATMCardRequest();
		request.setCardId(cardId);
		request.setPin(pin);
		ValidateCardResponse cardResponse = new ValidateCardResponse();
		ResponseEntity<ValidateCardResponse> cardResponseEntity = restTemplate.postForEntity(
				"http://card-service:9090/card/accountDetails", request, ValidateCardResponse.class,
				new HashMap<>());
		
		assert cardResponseEntity.getStatusCode() == HttpStatus.OK;
		cardResponse = cardResponseEntity.getBody();
		assert cardResponse !=null;
		return cardResponse;
	}
}
