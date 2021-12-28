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
public class Account {
	
	@Id
	private int accountNumber;

	private String hashedAccountNumber;

	private double openingBalance;

	private double overdraft;

	private Boolean activeIndicator;

}
