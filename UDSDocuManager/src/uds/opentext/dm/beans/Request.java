package uds.opentext.dm.beans;

import java.util.Date;

public class Request {
	
	private String requestNumber;
	private String subscriptionNumber;
	private Date createdDate;
	private String customerName;
	private String requestType;
	private String office;
	private String requestStatus;
	private String customerID;
	
	public Request() {
		super();
		// TODO Auto-generated constructor stub
	}
	
		
	public Request(String requestNumber, String subscriptionNumber, Date createdDate, String customerName,
			String requestType, String office, String requestStatus, String customerID) {
		super();
		this.requestNumber = requestNumber;
		this.subscriptionNumber = subscriptionNumber;
		this.createdDate = createdDate;
		this.customerName = customerName;
		this.requestType = requestType;
		this.office = office;
		this.requestStatus = requestStatus;
		this.customerID = customerID;
	}


	public String getRequestNumber() {
		return requestNumber;
	}
	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}
	public String getSubscriptionNumber() {
		return subscriptionNumber;
	}
	public void setSubscriptionNumber(String subscriptionNumber) {
		this.subscriptionNumber = subscriptionNumber;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getOffice() {
		return office;
	}
	public void setOffice(String office) {
		this.office = office;
	}
	public String getRequestStatus() {
		return requestStatus;
	}
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	
	
	
	

}
