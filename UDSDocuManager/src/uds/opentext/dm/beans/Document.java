package uds.opentext.dm.beans;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

public class Document {
	
	private String name;
	private XMLGregorianCalendar createdDate;
	private long dataID;
	private String comment;
	private boolean exclude;
	
	
	public Document() 
	{
		super();		
	}
	
	public Document(String name, XMLGregorianCalendar createdDate, int dataID, String comment,boolean execlude) 
	{
		super();
		this.name = name;
		this.createdDate = createdDate;
		this.dataID = dataID;
		this.comment = comment;
		this.exclude = execlude;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public XMLGregorianCalendar getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(XMLGregorianCalendar xmlGregorianCalendar) {
		this.createdDate = xmlGregorianCalendar;
	}
	
	public long getDataID() {
		return dataID;
	}
	
	public void setDataID(long l) {
		this.dataID = l;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public boolean isExclude() {
		return exclude;
	}

	public void setExclude(boolean execlude) {
		this.exclude = execlude;
	}
	
	

}
