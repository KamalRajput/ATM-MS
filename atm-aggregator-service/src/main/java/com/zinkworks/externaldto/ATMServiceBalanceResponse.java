package com.zinkworks.externaldto;

import lombok.Data;

@Data
public class ATMServiceBalanceResponse {
	public int fiftys;
	public int twentys;
	public int tens;
	public int five;
	public double balance;
}
