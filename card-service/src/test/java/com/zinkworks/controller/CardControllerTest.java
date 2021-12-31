package com.zinkworks.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zinkworks.dto.CardRequest;
import com.zinkworks.dto.CardResponse;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CardControllerTest {
	
	@Autowired
	public CardController controller;
	
	@Test
	public void cardControllerTest() {
		CardRequest request = new CardRequest();
		request.setCardId(101010);
		request.setPin(1234);
		ResponseEntity<CardResponse> accountNumberAndName = controller.getAccountNumberAndName(request);
		System.out.println(accountNumberAndName);
	}

}
