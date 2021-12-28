package com.zinkworks.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zinkworks.constant.CardErrorKeys;
import com.zinkworks.dto.CardRequest;
import com.zinkworks.dto.CardResponse;
import com.zinkworks.entity.Card;
import com.zinkworks.service.CardService;

@RestController
@RequestMapping({ "/card" })
public class CardController {
	@Autowired
	public CardService service;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping({ "/" })
	public String serviceStatus() {
		return " Card Service is up and running";
	}

	@PostMapping({ "/accountDetails" })
	public ResponseEntity<CardResponse> getAccountNumberAndName(@RequestBody CardRequest request) {
		boolean valid = false;
		CardResponse response = new CardResponse();
		Card card = new Card();
		try {
		 card = service.fetchCardDetails(request.getCardId());
		}catch (Exception e) {
			response.setErrorCode(CardErrorKeys.CARD_NOT_ACTIVE.getErrorKey());
			response.setErrorMessage("Card is not active");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		if (!card.getActiveIndicator().booleanValue()) {
			response.setErrorCode(CardErrorKeys.CARD_NOT_ACTIVE.getErrorKey());
			response.setErrorMessage("Card is not active");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		Date dateOnCard = card.getExpiryDate();
		if (!dateOnCard.after(new Date())) {
			response.setErrorCode(CardErrorKeys.CARD_EXPIRED.getErrorKey());
			response.setErrorMessage("Card has expired");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		if (card.getPin() != request.getPin()) {
			response.setErrorCode(CardErrorKeys.INVALID_CARD_PIN.getErrorKey());
			response.setErrorMessage("Pin doesnt match");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		valid = ((Boolean) this.restTemplate.getForObject(
				"http://account-service:9091/account/" + card.getHashedAccountNumber(), Boolean.class, new Object[0]))
						.booleanValue();
		if (!valid) {
			response.setErrorCode(CardErrorKeys.ACCOUNT_INACTIVE.getErrorKey());
			response.setErrorMessage("Account is inactive");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.setCardHolderName(card.getCardHolderName());
		response.setHashedAccountNumber(card.getHashedAccountNumber());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
