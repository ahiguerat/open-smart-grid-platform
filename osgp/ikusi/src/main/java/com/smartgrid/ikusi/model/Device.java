package com.smartgrid.ikusi.model;

public class Device  {

    private String deviceIdentification;
    private String containerCity;
    private String containerPostalCode;
    private String containerStreet;
    private String containerNumber;
    private String region;
    private String transitionOrganisationIdentification;
    private boolean activated;
    private boolean hasSchedule;
    private String activatedText;
    private String hasScheduleText;
    
	public String getDeviceIdentification() {
		return deviceIdentification;
	}
	public void setDeviceIdentification(String deviceIdentification) {
		this.deviceIdentification = deviceIdentification;
	}
	public String getContainerCity() {
		return containerCity;
	}
	public void setContainerCity(String containerCity) {
		this.containerCity = containerCity;
	}
	public String getContainerPostalCode() {
		return containerPostalCode;
	}
	public void setContainerPostalCode(String containerPostalCode) {
		this.containerPostalCode = containerPostalCode;
	}
	public String getContainerStreet() {
		return containerStreet;
	}
	public void setContainerStreet(String containerStreet) {
		this.containerStreet = containerStreet;
	}
	public String getContainerNumber() {
		return containerNumber;
	}
	public void setContainerNumber(String containerNumber) {
		this.containerNumber = containerNumber;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getTransitionOrganisationIdentification() {
		return transitionOrganisationIdentification;
	}
	public void setTransitionOrganisationIdentification(String transitionOrganisationIdentification) {
		this.transitionOrganisationIdentification = transitionOrganisationIdentification;
	}
	public boolean isActivated() {
		return activated;
	}
	public void setActivated(boolean activated) {
		this.activated = activated;
	}
	public boolean isHasSchedule() {
		return hasSchedule;
	}
	public void setHasSchedule(boolean hasSchedule) {
		this.hasSchedule = hasSchedule;
	}
	public String getActivatedText() {
		return activatedText;
	}
	public void setActivatedText(String activatedText) {
		this.activatedText = activatedText;
	}
	public String getHasScheduleText() {
		return hasScheduleText;
	}
	public void setHasScheduleText(String hasScheduleText) {
		this.hasScheduleText = hasScheduleText;
	}

    
}