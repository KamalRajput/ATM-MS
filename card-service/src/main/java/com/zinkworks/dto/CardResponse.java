package com.zinkworks.dto;

import lombok.Data;

@Data
public class CardResponse {

	public String cardHolderName;
	public String hashedAccountNumber;
	public String errorCode;
	public String errorMessage;
}
