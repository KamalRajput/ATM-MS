package com.zinkworks.dto;

import lombok.Data;

@Data
public class ATMUpdateRequest {
	public int atmId;
	public DenominationCount denomination;
}
