package com.zinkworks.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zinkworks.entity.Card;
import com.zinkworks.exception.ResourceNotFoundException;
import com.zinkworks.repository.CardRepository;

@Service
public class CardService {

	@Autowired
	public CardRepository repository;

	public Card fetchCardDetails(int cardId) {
		Optional<Card> card = this.repository.findById(Integer.valueOf(cardId));
		if (!card.isPresent())
			throw new ResourceNotFoundException("Card doesnt Exist");
		return card.get();
	}
}
