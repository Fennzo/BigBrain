package com.bigbrain.v1.models;

import java.util.List;


public class Addresses {

	public enum StateCodes {
		AL,AK,AZ,AR,CA,CO,CT,DE,FL,GA,HI,ID,IL,IN,IA,KS,KY,LA,ME,MD,MA,MI,MN,MS,MO,MT,NE,NV,NH,NJ,NM,NY,NC,ND,OH,OK,OR,PA,RI,SC,SD,TN,TX,UT,VT,VA,WA,WV,WI,WY
	}
	private int addressIDPK;

	private int userIDFK;

	private String addressLine1;

	private String addressLine2;

	private String city;

	private String stateCode;

	private String zipCode;
	private List<Requests> requests;

	public List<Requests> getRequests() {
		return requests;
	}

	public Addresses(){

	}

	public Addresses(int userIDFK){
		this.userIDFK = userIDFK;
	}
	public Addresses(int userIDFK, String addressLine1, String addressLine2, String city, String stateCode, String zipCode) {
		this.userIDFK = userIDFK;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.stateCode = stateCode;
		this.zipCode = zipCode;
	}

	public void setAddressIDPK(int addressIDPK) {
		this.addressIDPK = addressIDPK;
	}

	public void addRequests(Requests request) {
		requests.add(request);
	}

	public int getAddressIDPK() {
		return addressIDPK;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public String toString() {
		return "Address [addressIDPK=" + addressIDPK + ", userIDFK=" + userIDFK + ", addressLine1=" + addressLine1
				+ ", addressLine2=" + addressLine2 + ", city=" + city + ", stateCode=" + stateCode + ", zipCode="
				+ zipCode + "]";
	}

}
