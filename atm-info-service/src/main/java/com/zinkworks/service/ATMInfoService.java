package com.zinkworks.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zinkworks.entity.ATMInfo;
import com.zinkworks.exception.ResourceNotFoundException;
import com.zinkworks.repository.ATMInfoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ATMInfoService {

	@Autowired
	public ATMInfoRepository repository;

	public ATMInfo getDenominationCount(int atmId) {
		log.info("fetching Denomination count in ATMInfo Service");
		Optional<ATMInfo> atm = this.repository.findById(Integer.valueOf(atmId));
		if (!atm.isPresent())
			throw new ResourceNotFoundException("Invalid ATM Id");
		return atm.get();
	}

	public double getBalancefromATM(int atmId) {
		log.info("fetching remaining Balance in ATMInfo Service");
		Optional<ATMInfo> atm = this.repository.findById(Integer.valueOf(atmId));
		if (!atm.isPresent())
			throw new ResourceNotFoundException("Invalid ATM Id");
		return ((ATMInfo) atm.get()).getAtmBalance();
	}
}
