package com.zinkworks.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ATMInfo {

	@Id
	private int atmId;

	private int fiftyEuroQty;

	private int twentyEuroQty;

	private int tenEuroQty;

	private int fiveEuroQty;

	private double atmBalance;

}
