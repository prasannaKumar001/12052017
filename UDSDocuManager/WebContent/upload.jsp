	 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html dir="rtl" lang="ar">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="x-ua-compatible" content="IE=edge">
<link type="text/css" rel="stylesheet" href="style/waitMe.css">
<script type="text/javascript" src="script/jquery-3.2.1.js"></script>
 	 <script type="text/javascript" src="script/jquery-3.2.1.min.js"></script>
 	  <script type="text/javascript" src="script/jquery-ui.js"></script>
 	 <script src="script/waitMe.js"></script> 
	     
<script type="text/javascript">
		var current_effect = 'bounce'; // 

		function validateForm()
		{
			if (!document.getElementById("fileChooser").value) 
			{
        		alert("Please choose a file!");
        		return false;    
    		}
			if((document.getElementById('docType').value).trim()=="")
			{
	 			alert("Please Enter Document Type..!");
        		return false;
			}
			if((document.getElementById('subNum').value).trim()=="")
			{
	 			alert("Please Enter Subscription Number..!");
        		return false;
			}
			spin();
		}

		function spin()
		{
			run_waitMe(current_effect);
		}
		
	
	



		function run_waitMe(effect)
		{
			$('#container').waitMe({
			effect: 'roundBounce',
			text: '',
			g: 'rgba(255,255,255,0.7)',
			color: '#000',
			maxSize: '',
			waitTime: -1,
			source: '',
			textPos: 'vertical',
			ontSize: '',
			onClose: function() {}});
		}


</script>	

	<style type="text/css">
		#logo {
   	 			background: #d62727;
    			padding: 14px 0 14px 10px;
    			margin: 0;
    			background-color: #e9e9e9;
    		}
    		
    	.ui-dialog .ui-dialog-content{
    		overflow: hidden;
    	}
</style>

<title>File Upload</title>
</head>
<body  id="container">
	<%
		String dataIDString=request.getParameter("RequestNumber");
	%>
	<div id="uploadDialog" >	
		<div id="header-inner">
			<h1 id="logo">
				<img alt="" src="headerbar_content_server.png">
			</h1>
		</div>
	
		<div style="padding-top: 20px">
			
				<form  onsubmit="return validateForm();" action="UploadServlet?RequestNumber=<%= dataIDString%>" enctype="multipart/form-data" method="post">
					<table>
					<tbody>
						<tr>
							<td><label class="uploadfieldtype">Document</label></td>
							<td>:</td>
							<td><input type="file" name="dataFile" class="inputfile" id="fileChooser"/></td>
						</tr>
						<tr>
							<td><label class="uploadfieldtype">Document Type</label></td>
							<td>:</td>
							<td>
								<div class="options">
  									<select name="docType">
  										<option id="#">--Select--</option>
						  				<option value="Service Request">Service Request</option>
						  				<option value="Proof of Identity">Proof of Identity</option>
						  				<option value="Proprietary Proof">Proprietary Proof</option>
						  				<option value="Lease">Lease</option>
						  				<option value="Building Permit with Crocker General Location">Building Permit with Crocker General Location</option>
						  				<option value="Agency">Agency</option>
						  				<option value="Joint Undertakings">Joint Undertakings</option>
						  				<option value="Documents received from the Municipality">Documents received from the Municipality</option>
						  				<option value="Documents issued to the Municipality">Documents issued to the Municipality</option>
						  				<option value="Documents of the Ministry of Electricity">Documents of the Ministry of Electricity</option>
						  				<option value="Civil Defense Documents">Civil Defense Documents</option>
						  				<option value="Direct deposit form - costs">Direct deposit form - costs</option>
						  				<option value="Direct Deposit Form - Insurance">Direct Deposit Form - Insurance</option>
						  				<option value="Internal communications">Internal communications</option>
						  				<option value="Sketchbooks">Sketchbooks</option>
						  				<option value="Sample Site Preview Report">Sample Site Preview Report</option>
						  				<option value="Model of the combination of meters in a consumer statement">Model of the combination of meters in a consumer statement</option>
						  				<option value="Letter / Government Link Form">Letter / Government Link Form</option>
						  				<option value="External communications">External communications</option>
						  				<option value="Preliminary Project Model">Preliminary Project Model</option>
						  				<option value="Stream record for government utilities">Stream record for government utilities</option>
						  				<option value="Technical data on loads / units">Technical data on loads / units</option>
						  				<option value="Model Conventions">Model Conventions</option>
						  				<option value="Thermal insulation installation document">Thermal insulation installation document</option>
						  				<option value="Bill">Bill</option>
						  				<option value="Coordination certificate">Coordination certificate</option>
						  				<option value="Financial settlement model">Financial settlement model</option>
						  				<option value="Picture Station Location">Picture Station Location</option>
						  				<option value="Image of the location of the counter">Image of the location of the counter</option>
						  				<option value="Picture of the building">Picture of the building</option>
						  				<option value="Certificate of Hygiene">Certificate of Hygiene</option>
						  				<option value="Low voltage crochet">Low voltage crochet</option>
						  				<option value="Low voltage / azaleate">Low voltage / azaleate</option>
						  				<option value="The model cannot be connected">The model cannot be connected</option>
						  				<option value="Model voltage difference">Model voltage difference</option>
						  				<option value="Model of the ministry to exempt from the location of the distribution station">Model of the ministry to exempt from the location of the distribution station</option>
						  				<option value="Low voltage assay">Low voltage assay</option>
						  				<option value="Letters and reports">Letters and reports</option>
						  				<option value="Installation of a distribution station">Installation of a distribution station</option>
						  				<option value="Distribution station change accreditation form">Distribution station change accreditation form</option>
						  				<option value="The costing model is greater than SR 50,000">The costing model is greater than SR 50,000</option>
						  				<option value="Load load model greater than 2000 KVA">Load load model greater than 2000 KVA</option>
						  				<option value="Average Sketch">Average Sketch</option>
						  				<option value="Sketch Medium Voltage / Azbelt">Sketch Medium Voltage / Azbelt</option>
						  				<option value="Medium voltage assay">Medium voltage assay</option>
						  				<option value="An initial sketch of a distribution station site">An initial sketch of a distribution station site</option>
						  				<option value="Receipt of a distribution station location">Receipt of a distribution station location</option>
						  				<option value="Sketch and base station distribution specification">Sketch and base station distribution specification</option>
						  				<option value="Model approval extension of low voltage cable more than 250 m">Model approval extension of low voltage cable more than 250 m</option>
						  				<option value="Sample Assaying Form">Sample Assaying Form</option>
						  				<option value="Low voltage colored crochet">Low voltage colored crochet</option>
						  				<option value="Medium colored crochet effort">Medium colored crochet effort</option>
						  				<option value="Implementation model CA3 - F1">Implementation model CA3 - F1</option>
						  				<option value="Minutes of receiving the beneficiary">Minutes of receiving the beneficiary</option>
						  				<option value="Plan by execution / Azbelt">Plan by execution / Azbelt</option>
						  				<option value="Initial completion certificate form">Initial completion certificate form</option>
						  				<option value="Data checking work - connections">Data checking work - connections</option>
						  				<option value="Minutes of the receipt and safety of the receipt">Minutes of the receipt and safety of the receipt</option>
						  				<option value="Receipt of a new distribution station">Receipt of a new distribution station</option>
						  				<option value="Municipality permit for the contractor">Municipality permit for the contractor</option>
						  				<option value="Soil cohesion testing certificate">Soil cohesion testing certificate</option>
						  				<option value="Implementation Notes">Implementation Notes</option>
						  				<option value="Contractor's undertakings with the competent authorities">Contractor's undertakings with the competent authorities</option>
						  				<option value="Special chart documents">Special chart documents</option>
						  				<option value="Then return material">Then return material</option>
						  				<option value="Permission to exchange materials">Permission to exchange materials</option>
						  				<option value="Order extension work - d">Order extension work - d</option>
						  				<option value="Statement of quantities">Statement of quantities</option>
						  				<option value="Schedule of Facilities Works">Schedule of Facilities Works</option>
						  				<option value="Modification of the implementation plan">Modification of the implementation plan</option>
						  				<option value="Data port work order">Data port work order</option>
						  				<option value="Implementation data">Implementation data</option>
						  				<option value="Data Articles / Reaction">Data Articles / Reaction</option>
						  				<option value="190 Procedure Form">190 Procedure Form</option>
						  				<option value="200 Sample Procedure">200 Sample Procedure</option>
						  				<option value="Contractor Claims (Extract)">Contractor Claims (Extract)</option>
						  				<option value="Statement of implementation of meters">Statement of implementation of meters</option>
						  				<option value="Biat quantities of materials and work">Biat quantities of materials and work</option>
						  				<option value="General documents">General documents</option>
						  				<option value="Insurance Bond - Refund">Spherical</option>
						  				<option value="Work order">Work order</option>
						  				<option value="Implementation model CA3 - F1">Implementation model CA3 - F1</option>
						  				<option value="Financial Settlement Form">Financial Settlement Form</option>
						  				<option value="ZINSTLNCON">Connection Agreement</option>
						  				<option value="ZINSTLNCOS">Consumption Agreement</option>
						  				<option value="ZINSTLNMAS">Main and Sub Agreement</option>
									</select>
								</div>
							</td>
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
			 	
		</div>
	</div>	
	
	 
</body>
</html>