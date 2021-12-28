package com.zinkworks.constant;

public enum CardErrorKeys {

	CARD_NOT_ACTIVE("900"), CARD_EXPIRED("901"), INVALID_CARD_PIN("902"), ACCOUNT_INACTIVE("303");

	private String errorKey;

	private CardErrorKeys(String errorKey) {
		this.errorKey = errorKey;
	}

	public String getErrorKey() {
		return errorKey;
	}

}
