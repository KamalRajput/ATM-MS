package com.zinkworks.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {

	@Id
	private int cardId;

	private int cardNumber;

	private String cardHolderName;

	private String hashedAccountNumber;

	private int pin;

	private Date expiryDate;

	private int cvv;

	private Boolean activeIndicator;

}
