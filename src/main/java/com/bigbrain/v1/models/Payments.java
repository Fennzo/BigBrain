package com.bigbrain.v1.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;

import java.util.Date;


public class Payments {

	private int paymentIDPK;
	private int billIDFK;

	private String creditCardNumber;

	private int CVV;

	private int expirationMonth;

	private int expirationYear;
	private long amountPaid;
	private Date paymentDate;
	private int userIdFk;

	public Payments(){

	}

	public Payments(int billIDFK, int userIdFk){
		this.billIDFK = billIDFK;
		this.userIdFk = userIdFk;
	}
	public Payments(int billIDFK, String creditCardNumber, int CVV, int expirationMonth, int expirationYear, long amountPaid, int userIdFk) {
		this.billIDFK = billIDFK;
		this.creditCardNumber = creditCardNumber;
		this.CVV = CVV;
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
		this.amountPaid = amountPaid;
		this.userIdFk = userIdFk;
	}

	public int getPaymentIDPK() {
		return paymentIDPK;
	}

	public void setPaymentIDPK(int paymentIDPK) {
		this.paymentIDPK = paymentIDPK;
	}

	public int getBillIDFK() {
		return billIDFK;
	}

	public void setBillIDFK(int billIDFK) {
		this.billIDFK = billIDFK;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public int getCVV() {
		return CVV;
	}

	public void setCVV(int CVV) {
		this.CVV = CVV;
	}

	public int getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(int expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public int getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(int expirationYear) {
		this.expirationYear = expirationYear;
	}

	public long getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(long amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public int getUserIdFk() {
		return userIdFk;
	}

	public void setUserIdFk(int userIdFk) {
		this.userIdFk = userIdFk;
	}

	@Override
	public String toString() {
		return "Payments [paymentIDPK=" + paymentIDPK + ", creditCardNumber=" + creditCardNumber + ", CVV=" + CVV
				+ ", expirationMonth=" + expirationMonth + ", expirationYear=" + expirationYear + "]";
	}
	
	
}
