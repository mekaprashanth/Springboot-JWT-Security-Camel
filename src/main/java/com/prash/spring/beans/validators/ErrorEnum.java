/**
 * 
 */
package com.prash.spring.beans.validators;

/**
 * @author Prashanth_Meka
 *
 */
public enum ErrorEnum {
	
	FIELDEMPTYFIRSTNAME(101, "Firstname should not be empty"),
	FIELDEMPTYLASTNAME(102, "Lastname should not be empty"),
	USERUNDERAGE(103, "User underaged for 20k salary");
	
	private ErrorEnum(int errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	int errorCode;
	String errorMessage;
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getErrorMessage(String argument) {
		return String.format(errorMessage, argument);
	}
}
