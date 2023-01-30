package com.bigbrain.v1.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import org.apache.coyote.Request;

import java.util.List;


public class Address {

	private String addressIDPK;

	private int userIDFK;

	private String addressLine1;

	private String addressLine2;

	private String city;

	private String stateCode;

	private int zipCode;
	private List<Requests> requests;

	public List<Requests> getRequests() {
		return requests;
	}

	public void addRequests(Requests request) {
		requests.add(request);
	}

	public String getAddressIDPK() {
		return addressIDPK;
	}

	public void setAddressIDPK(String addressIDPK) {
		this.addressIDPK = addressIDPK;
	}

	public int getUserIDFK() {
		return userIDFK;
	}

	public void setUserIDFK(int userIDFK) {
		this.userIDFK = userIDFK;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		return "Address [addressIDPK=" + addressIDPK + ", userIDFK=" + userIDFK + ", addressLine1=" + addressLine1
				+ ", addressLine2=" + addressLine2 + ", city=" + city + ", stateCode=" + stateCode + ", zipCode="
				+ zipCode + "]";
	}

}
