package com.zinkworks.constants;

public enum ATMErrorKeys {

		ATM_INSUFFICIENT_BALANCE("101"),
		ATM_OUT_OF_SERVICE("102"),
		INVALID_DENOMINATION_EXCEPTION("103"),
		BAD_REQUEST("201"),
		AUTHENTICATION_ERROR("202"),
		BAD_REQUEST_INVALID_AMOUNT("203"),
		CUSTOMER_INSUFFICIENT_BALANCE("301"),
		ACCOUNT_NOT_FOUND("302"),
		SERVER_DOWN("501");

	private String errorKey;

	private ATMErrorKeys(String errorKey) {
		this.errorKey = errorKey;
	}

	public String getErrorKey() {
		return errorKey;
	}

}
