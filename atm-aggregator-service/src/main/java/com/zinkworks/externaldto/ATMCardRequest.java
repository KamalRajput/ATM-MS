package com.zinkworks.externaldto;

import lombok.Data;

@Data
public class ATMCardRequest {
	int cardId;
	int pin;
}