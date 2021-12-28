package com.zinkworks.externaldto;

import lombok.Data;

@Data
public class ATMServiceBalanceRequest {
	
	public int atmId;
    public Double amount;

}
