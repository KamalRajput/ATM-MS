package com.zinkworks.externaldto;

import lombok.Data;

@Data
public class ATMCardResponse {
	public String cardHolderName;
	public String hashedAccountNumber;
	public String errorCode;
	public String errorMessage;
}
