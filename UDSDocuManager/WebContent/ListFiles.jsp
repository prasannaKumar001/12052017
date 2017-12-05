
<%@page import="uds.opentext.dm.beans.Document"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Properties"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.opentext.livelink.service.docman.Node"%>
<%@page import="uds.opentext.dm.OTUtility"%>
<%@page import="java.util.List"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% int x=1;  %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html dir="rtl" lang="ar">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="x-ua-compatible" content="IE=edge">

	<script type="text/javascript" src="script/jquery-3.2.1.min.js"></script>
  	<script type="text/javascript" src="script/jquery-3.2.1.js"></script>
  	<script type="text/javascript" src="script/jquery-ui.js"></script>
	 <LINK REL="stylesheet" TYPE="text/css" HREF="style/screen.css?v=1344.1360">
	 <link rel="stylesheet" TYPE="text/css"  href="style/jquery-ui.css">
	 <link rel="stylesheet" type="text/css" href="style/datatables.min.css"/>
	 <script type="text/javascript" src="script/datatables.min.js"></script>
	 <script type="text/javascript" src="script/ot.js"></script>
	 <script type="text/javascript" src="Resources/dynamsoft.webtwain.initiate.js"></script>
    <script type="text/javascript" src="Resources/dynamsoft.webtwain.config.js"></script>
    <link type="text/css" rel="stylesheet" href="style/waitMe.css">
     <script src="script/waitMe.js"></script>
    
	<script type="text/javascript">
	
		if (typeof(UserAgentInfo) != 'undefined' && !window.addEventListener) 
		{ 
	   		 UserAgentInfo.strBrowser=1; 
		} 
	
		Dynamsoft.WebTwainEnv.AutoLoad = false; // The property is set to true by default.
		function Dynamsoft_OnReady() {
	    DWObject = Dynamsoft.WebTwainEnv.GetWebTwain('dwtcontrolContainer'); // Get the Dynamic Web TWAIN object that is embeded in the div with id 'dwtcontrolContainer'
	    if (DWObject) {
	        DWObject.RegisterEvent('OnPostAllTransfers'); //SaveWithFileDialog
	    }
		}
	
	function AcquireImage() {
		var DWObject;
		var console = window['console']?window['console']:{'log':function(){}};
		//Dynamsoft.WebTwainEnv.RegisterEvent('OnWebTwainReady', Dynamsoft_OnReady); // Register OnWebTwainReady event. This event fires as soon as Dynamic Web TWAIN is initialized and ready to be used
		LoadControl();	
		
	    if (DWObject) {
	        var bSelected = DWObject.SelectSource();
			
			if(bSelected){
				var OnAcquireImageSuccess, OnAcquireImageFailure;
				OnAcquireImageSuccess = OnAcquireImageFailure= function (){
					DWObject.CloseSource();
				};
				
				DWObject.OpenSource();
				DWObject.IfDisableSourceAfterAcquire = true;	// Scanner source will be disabled/closed automatically after the scan. 
				DWObject.AcquireImage(OnAcquireImageSuccess, OnAcquireImageFailure);
			}
	    }
	}

	//Callback functions for async APIs
	function OnSuccess() {
	    console.log('successful');
	}

	function OnFailure(errorCode, errorString) {
	    alert(errorString);
	}

	function LoadImage() {
	    if (DWObject) {
	        DWObject.IfShowFileDialog = true; // Open the system's file dialog to load image
	        DWObject.LoadImageEx("", EnumDWT_ImageType.IT_ALL, OnSuccess, OnFailure); // Load images in all supported formats (.bmp, .jpg, .tif, .png, .pdf). OnSuccess or OnFailure will be called after the operation
	    }
	}

	
	
	function upload()
	{
		//DWObject.IfShowFileDialog = false;
		//DWObject.SaveAsPDF("C:\DynamicWebTWAIN.pdf", 0);
		var strHTTPServer = location.hostname;
		DWObject.HTTPPort = location.port == "" ? 80 : location.port;
		var CurrentPathName = unescape(location.pathname);
		var CurrentPath = CurrentPathName.substring(0, CurrentPathName.lastIndexOf("/") + 1);
		var strActionPage = CurrentPath + "ScanUpload";
		var uploadfilename = "TestI.pdf";
		DWObject.HTTPUploadAllThroughPostAsPDF(
				strHTTPServer,
				strActionPage,
				uploadfilename,
				OnSuccess, OnFailure
				);
		exit();
	}
	
	function exit()
	{
		$("#dialog").dialog("close");
	}
	
	function UnloadControl() {
		  //Unload the control manually
		  Dynamsoft.WebTwainEnv.Unload();
		}
	function LoadControl() {
		  //Load the control manually
		  Dynamsoft.WebTwainEnv.Load(); 
		}
	
	
	</script>
	<style type="text/css">
		
 			.browseRow1 { background-color: #FFFFFF;  } 
 			
	</style>
	
	<style type="text/css">
		.model{
			display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    padding-top: 100px; /* Location of the box */
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
		}
	
	
		.tabs {
			float:right!important;
			}
		.updatedBrowse .browseListHeader TD {
   			height: 40px;
			}
			
		table.dataTable thead > tr > th {
    		 padding-left: 30px !important;
   			 padding-right: initial !important;
		}

	    table.dataTable thead .sorting,
	   	table.dataTable thead .sorting_asc, 
	    table.dataTable thead .sorting_desc, 
	    table.dataTable thead .sorting_asc_disabled, 
	    table.dataTable thead .sorting_desc_disabled{
	    	 background-position: center;    
	     }
	     
		table.dataTable tbody th, table.dataTable tbody td{
	     	padding-right: 0px;
	     }
		table.dataTable thead .sorting_asc{
			background-image: none;
		}
			
		.options {
				background: transparent;
				padding: 5px 35px 5px 5px;
				font-size: 12px;
				-webkit-appearance: none;
				-moz-appearance: none;
				appearance: none;
			}
				
		.dataTables_filter{
			margin-right: 1.6em;	
			display:inline-table;
					
			}
				
		 .dataTables_length{
			display:inline-block;
			}
			
			body{
				min-height: 600px;
				max-height: 600px;
			}
			
			table.dataTable tbody th, table.dataTable tbody td{
				padding: 0px 3px;
			}	
			
			.options{
				text-align: left;
			}	
			
			.dataTables_wrapper .dataTables_filter{
				padding: 5px;
			}
	</style>
<title>OpenText Document List</title>
</head>
<body id="container">
	
	<%
				Properties prop = new Properties();
				prop.load(getServletContext().getResourceAsStream("/WEB-INF/config.properties"));
				String USERNAME=prop.getProperty("UDS.USERNAME");
				String PASSWORD=prop.getProperty("UDS.PASSWORD");
				
				String dataIDString=request.getParameter("RequestNumber");
				int RequestNum=0;
				if(dataIDString!=null && !dataIDString.trim().equals(""))
				{
					RequestNum=Integer.valueOf(dataIDString);
				}
				
				System.out.print("USERNAME: "+USERNAME);
				
				//	String parentID=request.getParameter("parentID");
			
				//need to write logic to get parent id from DB
				
					List<Document> documents=new ArrayList<Document>();
					
					String authToken=null;
				try
				{
					authToken=OTUtility.getAuthToken(USERNAME, PASSWORD);
				
					
					if(authToken!=null || authToken.trim().equals(""))
					{
						
						if(RequestNum!=0)
						{
							documents=OTUtility.getChildren(authToken,RequestNum);//parent ID
							//nodes2=OTUtility.excludedNodes(authToken,85457);
						}
					}
				}
				catch(Exception e)
				{
					out.print("<script>alert('Error occurred while loading this page')</script>");
					
				}
					
				//System.out.println("Path    "+System.getProperty("java.io.tmpdir"));
				
			%>	
			
			
	
		
		<div style="margin-top:-10 px;">
	
			
			<c:set var="n" value="<%=documents%>"/>
			
			<div id="tabs"  >
  				
				<ul>
					<li class="tabs"><a href="#tabs-1">Documents</a></li>
					<li class="tabs"><a href="#tabs-2">Excluded</a></li>
					<li class="tabs"><a href="#tabs-3">Comments</a></li>
					<div class="options">
  						<select id='selectlist' class="" onchange="downloadAllFiles();" onfocus="this.selectedIndex = -1;">
  							<option id="#">--Select--</option>
						  	<option id="Scan">Scan</option>
						  	<option id="Display">Display</option>
						  	<option id="Upload">Upload</option>
						  	<option id="Copy">Copy</option>
						  	<option id="Move">Move</option>
						  	<option id="Exclude">Exclude</option>
						  	<option value="Retrieve" id="Retrieve">Retrieve</option>
						 	<!--<option id="Delete">Delete</option>-->
						  	<option id="Send">Send</option>
						</select>
  					</div>
				</ul>
				<div id="tabs-1">
				
				 <form name=form1 method=post>
	   				<table id="CScontent" width="100%" border="0" cellspacing="1" cellpadding="0" class="browseTable updatedBrowse">
						<thead>
							<tr class="browseListHeader">
								<td class="browseListHeaderName" style="background-image: none">
							 		<input type="checkbox" name="top_checkbox" value="checkbox" onClick="toggle(this);">
							 	</td>
							 	<td class="browseListHeaderName" >Documents</td>
							 	<td class="browseListHeaderName">Created Date</td>
							 	<td class="browseListHeaderName">Comments</td>
							 	<td class="browseListHeaderName">Data ID</td>
							 	<td class="browseListHeaderName"></td>
						    </tr>
						</thead>
						<c:forEach items="${n}" var="element">
						<c:choose>
							<c:when test='${element.isExclude()}'>
									<tr class="browseRow2" style="background-color: red !important;">
										<td  class="browseRow2 ">
											<input   type="checkbox" name="foo" id="ckb" value="${element.getDataID()}">
										</td>
										<td align="left"  class="browseRow2">
							    			<a class="browseItemNameContainer"><c:out value="${element.getName()}"/></a>
							    			<div id="z85229" class="functionMenuDiv"></div>
								    	</td>
								    	<td align="left"  class="browseRow2"><c:out value="${element.getCreatedDate()}"/></td>
										<td align="left"  class="browseRow2"><c:out value="${element.getComment()}"/></td>
										<td align="left"  class="browseRow2"><c:out value="${element.getDataID()}"/></td>
										<td align="left" class="browseRow2">
							    			<a href="#" id="${element.getDataID()}" onclick="downlaodFile(this.id,'download');" title="Download">Download</a>
							    			<div id="z85229" class="functionMenuDiv"></div>
								    	</td>
								    </tr>
						    </c:when>
						   	<c:otherwise>
							<tr class="browseRow2">
								<td  class="browseRow2 ">
									<input   type="checkbox" name="foo" id="ckb" value="${element.getDataID()}">
								</td>
								<td align="left"  class="browseRow2">
					    			<a class="browseItemNameContainer"><c:out value="${element.getName()}"/></a>
					    			<div id="z85229" class="functionMenuDiv"></div>
						    	</td>
						    	<td align="left"  class="browseRow2"><c:out value="${element.getCreatedDate()}"/></td>
						    	<td align="left"  class="browseRow2"><c:out value="${element.getComment()}"/></td>
						    	<td align="left"  class="browseRow2"><c:out value="${element.getDataID()}"/></td>
								<td align="left" class="browseRow2">
					    			<a href="#" id="${element.getDataID()}" onclick="downlaodFile(this.id,'download');" title="Download">Download</a>
					    			<div id="z85229" class="functionMenuDiv"></div>
						    	</td>
						    </tr>
						     </c:otherwise>
						   </c:choose>
						</c:forEach>
	 				</table>
	 			</form>
	  		</div>
  			<div id="tabs-2">
  				
    			<form name=form1 method=post>
	   				<table id="Exclude" width="100%" border="0" cellspacing="1" cellpadding="0" class="browseTable updatedBrowse">
						<thead>
							<tr class="browseListHeader">
								<td class="browseListHeaderCheck" style="background-image: none";>
							 		<input type="checkbox" name="top_checkbox" value="checkbox" onClick="toggle(this);">
							 	</td>
							 	<td class="browseListHeaderName" >Documents</td>
							 	<td class="browseListHeaderName">Created Date</td>
							 	<td class="browseListHeaderName">Comments</td>
							 	<td class="browseListHeaderName">Data ID</td>
							 	<td class="browseListHeaderCheck"></td>
						    </tr>
						</thead>
						<c:forEach items="${n}" var="element">
							<c:if test='${element.isExclude()}'>
							<tr class="browseRow2" style="color: red">
								<td  class="browseRow2 ">
									<input   type="checkbox" name="foo" id="ckb" value="${element.getDataID()}">
								</td>
								<td align="left"  class="browseRow2">
					    			<a class="browseItemNameContainer"><c:out value="${element.getName()}"/></a>
					    			<div id="z85229" class="functionMenuDiv"></div>
						    	</td>
						    	<td align="left"  class="browseRow2"><c:out value="${element.getCreatedDate()}"/></td>	
						    	<td align="left"  class="browseRow2"><c:out value="${element.getComment()}"/></td>
						    	<td align="left"  class="browseRow2"><c:out value="${element.getDataID()}"/></td>
								<td align="left" class="browseRow2">
					    			<a href="#" id="${element.getDataID()}" onclick="downlaodFile(this.id,'download');" title="Download">Download</a>
					    			<div id="z85229" class="functionMenuDiv"></div>
						    	</td>
						    </tr>
						    </c:if>
						</c:forEach>
	 				</table>
	 			</form>
  			</div>
  			<div id="tabs-3">
    			<p></p>
    		</div>
		</div>	
		</div>
	
</body>
</html>
