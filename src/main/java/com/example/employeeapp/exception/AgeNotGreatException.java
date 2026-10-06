package com.example.employeeapp.exception;

public class AgeNotGreatException extends Throwable{
	String message ="";
	public AgeNotGreatException(String mesage) {
		this.message=message;
	}
	

}
