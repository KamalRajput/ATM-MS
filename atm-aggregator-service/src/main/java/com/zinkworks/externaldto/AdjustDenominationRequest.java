package com.zinkworks.externaldto;

import com.zinkworks.dto.DenominationCount;

import lombok.Data;

@Data
public class AdjustDenominationRequest {
	
	public int atmId;
	public DenominationCount denomination;

}
