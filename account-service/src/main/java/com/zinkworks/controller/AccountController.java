package com.zinkworks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zinkworks.dto.AccountBalanceRequest;
import com.zinkworks.dto.AccountBalanceResult;
import com.zinkworks.dto.AccountBalanceRevertRequest;
import com.zinkworks.dto.AccountWithdrawRequest;
import com.zinkworks.dto.AccountWithdrawResult;
import com.zinkworks.entity.Account;
import com.zinkworks.exception.AccountNotFoundException;
import com.zinkworks.exception.InsufficientAccountBalanceException;
import com.zinkworks.repository.AccountRepository;
import com.zinkworks.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping({ "/account" })
@Slf4j
public class AccountController {

	@Autowired
	public AccountService accountService;

	@Autowired
	public AccountRepository accountRepository;

	@Autowired
	public RestTemplate restTemplate;

	@GetMapping
	public String getStatus() {
		return "Account Service is up and running";
	}

	@GetMapping("/{hashedAccountNum}")
	public Boolean isAccountActive(@PathVariable("hashedAccountNum") String hashedAccountNumber) {
		log.info("Checking if account is active or not");
		boolean valid = false;
		Account account = accountService.findAccountbyHashedAccountNumber(hashedAccountNumber);
		if (account.getActiveIndicator().booleanValue())
			valid = true;
		return Boolean.valueOf(valid);
	}

	@PostMapping("/balanceEnquiry")
	public ResponseEntity<AccountBalanceResult> getUserBalanceDetails(@RequestBody AccountBalanceRequest request) {
		log.info("Inside getUserBalanceDetails method of AccountController");
		boolean valid = false;
		double maximumWithdrawableAmount = 0.0D;
		double balance = 0.0D;
		AccountBalanceResult result = new AccountBalanceResult();
		try {
			maximumWithdrawableAmount = accountService.getMaxAvailableBalance(request.getHashedAccountNumber());
			balance = accountService.getBalancebyhashedAccountNum(request.getHashedAccountNumber());
		} catch (AccountNotFoundException e) {
			result.setErrorCode("302");
			result.setErrorMessage("Account Not Found");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		
		result.setAvailableBalance(balance);
		result.setMaxWithdrawableBalance(maximumWithdrawableAmount);
		result.setErrorCode(result.getErrorCode());
		result.setErrorMessage(result.getErrorMessage());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	
	@PutMapping("/revertBalance")
	public ResponseEntity<Boolean> revertAccountBalance(@RequestBody AccountBalanceRevertRequest request) {
		boolean status = false;
		Account account = accountService.findAccountbyHashedAccountNumber(request.getHashedAccountNumber());
		if (account != null) {
			status = true;
			account.setOpeningBalance(request.getAmount());
		}
		try {
		accountRepository.save(account);
		}
		catch (Exception e) {
			status=false;
		}
		
		return new ResponseEntity<>(status,HttpStatus.OK);
	}

	@PostMapping("/withdrawMoney")
	public ResponseEntity<AccountWithdrawResult> withdrawMoney(@RequestBody AccountWithdrawRequest request)
			throws InsufficientAccountBalanceException {
		AccountWithdrawResult result = new AccountWithdrawResult();
			try {
				if (request.getAmount() > accountService.getMaxAvailableBalance(request.getHashedAccountNumber())) {
					result.setErrorCode("401");
					result.setErrorMessage("Insufficient balance");
					return new ResponseEntity<>(result, HttpStatus.OK);
				}
			} catch (AccountNotFoundException e) {
				result.setErrorCode("302");
				result.setErrorMessage("Account not found");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			result = accountService.withdrawAmount(request.getHashedAccountNumber(),
				request.getAmount());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
