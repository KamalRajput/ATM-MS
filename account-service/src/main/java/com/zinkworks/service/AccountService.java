package com.zinkworks.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.zinkworks.dto.AccountWithdrawResult;
import com.zinkworks.entity.Account;
import com.zinkworks.exception.AccountNotFoundException;
import com.zinkworks.repository.AccountRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {

	@Autowired
	public AccountRepository repository;

	@Autowired
	public RestTemplate restTemplate;

	public Account findAccountbyHashedAccountNumber(String hashedAccountNumber) {
		return repository.findAccountByHashedAccountNumber(hashedAccountNumber);
	}

	public double getBalancebyhashedAccountNum(String hashedAccount) throws AccountNotFoundException {
		log.info("Fetching Balance by AccountNum");
		Account account = repository.findAccountByHashedAccountNumber(hashedAccount);
		if (account == null) {
			throw new AccountNotFoundException("Account doesnt Exist");
		}
		return account.getOpeningBalance();
	}

	public double getMaxAvailableBalance(String hashedAccount) throws AccountNotFoundException {
		log.info("Fetching maximum Available Balance for an account(balance+overdraft)");
		double maxBalance = 0.0D;
		Account account = repository.findAccountByHashedAccountNumber(hashedAccount);
		if (account == null) {
			throw new AccountNotFoundException("Account doesnt Exist");
		}
		maxBalance = account.getOpeningBalance() + account.getOverdraft();
		return maxBalance;
	}

	public AccountWithdrawResult withdrawAmount(String hashedAccount, double amountRequested) {
		log.info("Withdrawing amount");
		AccountWithdrawResult result = new AccountWithdrawResult();
		double remainingAmount = 0.0D;
		double openingBalance = 0.0D;
		double overdraft = 0.0D;
		Account account = repository.findAccountByHashedAccountNumber(hashedAccount);
		if (account == null)
			throw new RuntimeException("Account doesnt Exist");
		if (amountRequested <= account.getOpeningBalance()) {
			openingBalance = account.getOpeningBalance();
			overdraft = account.getOverdraft();
			remainingAmount = openingBalance - amountRequested;
			openingBalance = remainingAmount;
		} else if (amountRequested >= account.getOpeningBalance()) {
			openingBalance = account.getOpeningBalance();
			overdraft = account.getOverdraft();
			remainingAmount = openingBalance + overdraft - amountRequested;
			openingBalance = 0.0D;
			overdraft = remainingAmount;
		}
		Account updatedAccount = new Account(account.getAccountNumber(), account.getHashedAccountNumber(),
				openingBalance, overdraft, account.getActiveIndicator());

		boolean status= updateAccountBalance(updatedAccount);
		log.info("Account balance is updated");
		result.setAvailableBalance(openingBalance);
		result.setOverDraftBalance(overdraft);
		return result;
	}

	public boolean updateAccountBalance(Account accountDetails) {
		boolean status = false;
		Optional<Account> account = repository.findById(accountDetails.getAccountNumber());
		if (account.isPresent()) {
			Account accounttobeUpdated = account.get();

			accounttobeUpdated.setAccountNumber(accountDetails.getAccountNumber());
			accounttobeUpdated.setActiveIndicator(accountDetails.getActiveIndicator());
			accounttobeUpdated.setHashedAccountNumber(accountDetails.getHashedAccountNumber());
			accounttobeUpdated.setOpeningBalance(accountDetails.getOpeningBalance());
			accounttobeUpdated.setOverdraft(accountDetails.getOverdraft());
			repository.save(accounttobeUpdated);
			status = true;
		}
		return status;
	}
}