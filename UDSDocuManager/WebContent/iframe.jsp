
<%@page import="uds.opentext.dm.beans.Request"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Properties"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.opentext.livelink.service.docman.Node"%>
<%@page import="uds.opentext.dm.OTUtility"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html >
<html dir="rtl" lang="ar">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="x-ua-compatible" content="IE=edge">
		
	<script type="text/javascript" src="script/jquery-3.2.1.min.js"></script>
  	<script type="text/javascript" src="script/jquery-3.2.1.js"></script>
  	<script type="text/javascript" src="script/jquery-ui.js"></script>
	 <LINK REL="stylesheet" TYPE="text/css" HREF="style/screen.css?v=1344.1360">
	 <link rel="stylesheet" TYPE="text/css"  href="style/jquery-ui.css">
	 <link rel="stylesheet" type="text/css" href="style/datatables.min.css"/>
	 <script type="text/javascript" src="script/datatables.min.js"></script>
	 <script type="text/javascript" src="script/ot.js"></script>
	 
    <link type="text/css" rel="stylesheet" href="style/waitMe.css">
     <script src="script/waitMe.js"></script>
	<style type="text/css">
		#loader{
			position: fixed;
			align-content: center;
			align-items: center;
			right: 28%;
			left: 40%;
			border-radius: 20px;
			padding: 25px;
			
		}	
	</style>
	<script type="text/javascript">
		 $(document).ready(function(){
			$("#iframe").on('load',function(){
				$("#loader").hide();
			});			
		}); 
	</script>
	<!--  background-image: url('style/images/loadingcir.gif'); -->
<title>OpenText</title>
</head>
<body>
	<div id="header-inner">
		<h1 id="logo">
			<img alt="" src="headerbar_content_server.png">
		</h1>
	</div>
		<%
				Properties prop = new Properties();
				prop.load(getServletContext().getResourceAsStream("/WEB-INF/config.properties"));
				String USERNAME=prop.getProperty("UDS.USERNAME");
				String PASSWORD=prop.getProperty("UDS.PASSWORD");
				String dataIDString=request.getParameter("RequestNumber");
				System.out.println(dataIDString);
				int RequestNum=0;
				if(dataIDString!=null && !dataIDString.trim().equals(""))
				{
					RequestNum=Integer.valueOf(dataIDString);
					System.out.println("Request Numbr "+RequestNum);
				}
				System.out.print("USERNAME: "+USERNAME);
				
				//	String parentID=request.getParameter("parentID");
			
					Request rqst=null;
					String authToken=null;
				try
				{
					authToken=OTUtility.getAuthToken(USERNAME, PASSWORD);
				
					
					if(authToken!=null || authToken.trim().equals(""))
					{
						if(RequestNum!=0)
						{
							rqst=OTUtility.getNode(authToken, RequestNum);
						}
					}
				}
				catch(Exception e)
				{
					out.print("<script>alert('Error occurred while loading this page')</script>");
					
				}	
			%>
	
	<div style="min-height: 117px; ">
		<table  width="100%" border="0" class="level1table"  align="Right">
			<tbody >
				<tr>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; ">Request ID</td>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; "><%= rqst.getRequestNumber()%></td>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; ">Request Type</td>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; "><%= rqst.getRequestType()%></td>
				</tr>
				<tr>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; ">Creation Date</td>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; "><%= rqst.getCreatedDate() %></td>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; ">Office</td>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; "><%= rqst.getOffice()%></td>
				</tr>
				<tr>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; ">Customer Name</td>	
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; "><%=rqst.getCustomerName() %></td>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; ">Req Status</td>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; "><%= rqst.getRequestStatus() %></td>	
				</tr>
				<tr>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; ">Customer ID</td>
					<td align="right" class="lavel1td browseRow2" style="background-color:#FFFFFF; "></td>	
					<td></td>
					<td></td>					
				</tr>
				
			</tbody>
		</table>
	</div>
	
		<div id="myframe">
			<img id="loader" src="style/images/loadingcir.gif" >
		 	<iframe id="iframe" width="100%" height="450" scrolling="no" src="ListFiles.jsp?RequestNumber=<%= dataIDString %>" ></iframe>
		</div>
	<div id="dialog" class="model" style="display: none">
			<div id="dwtcontrolContainer"></div>
			<br/>
			Document Typ	   : <input type="text" value="docType" onclick="" />
			<br/>
			<br>
			Subscription Number: <input type="text" value="subNumber" onclick="" />
			<br/>
			<br>
			<input type="button" value="Upload" onclick="upload()" />
   			<input type="button" value="Exit" onclick="exit();" />
  				<!--  <input type="button" value="Save" onclick="SaveWithFileDialog();" />-->
  				<br />
   			<!-- <label><input type="radio" value="jpg" name="ImageType" id="imgTypejpeg" />JPEG</label>
  				<label><input type="radio" value="tif" name="ImageType" id="imgTypetiff" />TIFF</label>
  				<label><input type="radio" value="pdf" name="ImageType" id="imgTypepdf" checked="checked" />PDF</label> -->
		</div>	
	<div id="uploadDialog" style="display: none">	
		<%-- <div id="header-inner">
			<h1 id="logo">
				<img alt="" src="headerbar_content_server.png">
			</h1>
		</div>
		
		<div style="padding-top: 20px">
			
				<form  onsubmit="return validateForm();" action="UploadServlet?RequestNumber=<%= dataIDString%>" enctype="multipart/form-data" method="post">
					<table>
					<tbody>
						<tr>
							<td><label class="uploadfieldtype">Select file to upload</label></td>
							<td>:</td>
							<td><input type="file" name="dataFile" class="inputfile" id="fileChooser"/></td>
						</tr>
						<tr>
							<td><label class="uploadfieldtype">Document Type</label></td>
							<td>:</td>
							<td><input type="text" name="docType" id="docType"/></td>
						</tr>
						<tr>
							<td><label class="uploadfieldtype">Subscription Number</label></td>
							<td>:</td>
							<td><input type="text" name="subNum" id="subNum"/></td>
						</tr>
						<tr>
							<td><input type="submit" value="Upload" id="upload" onclick=""/></td>
						</tr>
						</tbody>
					</table>
				</form>	
			 	
		</div> --%>
	</div>	
	
	
	<p class="copyright">
				OpenText Content Server version 16. Copyright © 1995 - 2017 Open Text All Rights Reserved    
			</p>
			
			
</body>
</html>