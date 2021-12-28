package com.zinkworks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zinkworks.dto.ATMInfoRequest;
import com.zinkworks.dto.ATMInfoResponse;
import com.zinkworks.dto.ATMUpdateRequest;
import com.zinkworks.dto.ATMUpdateResponse;
import com.zinkworks.dto.DenominationCount;
import com.zinkworks.entity.ATMInfo;
import com.zinkworks.repository.ATMInfoRepository;
import com.zinkworks.service.ATMInfoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping({ "/atmInfo" })
@Slf4j
public class ATMInfoController {

	@Autowired
	public ATMInfoService service;

	@Autowired
	public ATMInfoRepository repository;

	@GetMapping({ "/" })
	public String serviceStatus() {
		return " ATM Info Service is up and running";
	}

	@PostMapping("/denomination")
	public ResponseEntity<ATMInfoResponse> getDenominationDetailsWithAmount(@RequestBody ATMInfoRequest request) {
		log.info("fetching Denomination Details in ATMInfo Contoller");
		ATMInfo atmInfo = service.getDenominationCount(request.getAtmId());
		ATMInfoResponse response = new ATMInfoResponse();
		response.setFiftys(atmInfo.getFiftyEuroQty());
		response.setTwentys(atmInfo.getTwentyEuroQty());
		response.setTens(atmInfo.getTenEuroQty());
		response.setFive(atmInfo.getFiveEuroQty());
		response.setBalance(atmInfo.getAtmBalance());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping({ "/balCheck/{amountRequested}/{id}" })
	public boolean validateBalanceInATM(@PathVariable("amountRequested") double amountRequested,
			@PathVariable("id") int atmId) {
		log.info("Validating if enough balance is available in ATM to withdraw");
		boolean enoughBalanceAvailable = false;
		double remainingBalanceinATM = this.service.getBalancefromATM(atmId);
		if (amountRequested <= remainingBalanceinATM) {
			enoughBalanceAvailable = true;
			return enoughBalanceAvailable;
		}
		return enoughBalanceAvailable;
	}

	@PostMapping("/adjustDenomination")
	public ResponseEntity<ATMUpdateResponse> adjustDenominations(@RequestBody ATMUpdateRequest request) {
		log.info("Updating  Denomination Details in ATMInfo Contoller");
		ATMInfo atmInfo = service.getDenominationCount(request.getAtmId());
		DenominationCount denoCount = request.getDenomination();
		ATMUpdateResponse response = new ATMUpdateResponse();

		double fiftyValue = denoCount.getFiftys() * 50;
		double twentyValue = denoCount.getTwentys() * 20;
		double tensValue = denoCount.getTens() * 10;
		double fiveValue = denoCount.getFive() * 5;

		atmInfo.setFiftyEuroQty((atmInfo.getFiftyEuroQty()-denoCount.getFiftys()));
		atmInfo.setTwentyEuroQty(atmInfo.getTenEuroQty()-denoCount.getTwentys());

		atmInfo.setTenEuroQty(atmInfo.getTenEuroQty()-denoCount.getTens());

		atmInfo.setFiveEuroQty(atmInfo.getFiveEuroQty()-denoCount.getFive());

		atmInfo.setAtmBalance(atmInfo.getAtmBalance() - (fiftyValue + twentyValue + tensValue + fiveValue));

		try {
			repository.save(atmInfo);
		} catch (Exception e) {
			response.setUpdate(false);
		}
		response.setUpdate(true);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
