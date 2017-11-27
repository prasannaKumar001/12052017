package uds.opentext.dm.beans;

import java.util.Date;

public class Document {
	
	private String name;
	private Date createdDate;
	private int dataID;
	private String comment;
	private boolean execlude;
	
	
	public Document() 
	{
		super();		
	}
	
	public Document(String name, Date createdDate, int dataID, String comment,boolean execlude) 
	{
		super();
		this.name = name;
		this.createdDate = createdDate;
		this.dataID = dataID;
		this.comment = comment;
		this.execlude = execlude;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public int getDataID() {
		return dataID;
	}
	
	public void setDataID(int dataID) {
		this.dataID = dataID;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public boolean isExeclude() {
		return execlude;
	}

	public void setExeclude(boolean execlude) {
		this.execlude = execlude;
	}

	

}
