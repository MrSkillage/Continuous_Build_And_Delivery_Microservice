package com.tus.catalog.errors;

public enum ErrorMessages {
	INVALID_TITLE("Invalid Title Name - Title cannot contain symbols"),
	INVALID_AUTHOR("Invalid Author Name - Name cannot contain symbols or numbers"),
	INVALID_PRICE("Invalid Pricing - Price cannot contain symbols or letters");

	private String errorMessage;

	private ErrorMessages(String errMsg) {
		this.errorMessage = errMsg;
	}

	public String getMsg() {
		return this.errorMessage;
	}

}
