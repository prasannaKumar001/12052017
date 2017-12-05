<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Properties"%>
<%@page import="uds.opentext.dm.OTUtility"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html dir="rtl" lang="ar">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<script type="text/javascript">
		
	function validateForm()
	{
		if (!document.getElementById("recipient").value) {
            alert("Please choose a file!");
            return false;    
        }	
	}	
	
	</script>
<title>Send an e-mail</title>
</head>
<body>
    <form onsubmit="return validateForm()" action="EmailSendingServlet" method="post">
        <table border="0" width="35%" align="center">
            <caption><h2>Send New E-mail</h2></caption>
            <tr>
                <td width="50%">Recipient address </td>
                <td><input type="text" id='recipient' name="recipient" size="50"/></td>
            </tr>
            <tr>
                <td>Subject </td>
                <td><input type="text" id='subject' name="subject" size="50"/></td>
            </tr>
            <% 
           		/* Properties prop = new Properties();
            	List<Integer> dataids=new ArrayList<Integer>();
				prop.load(getServletContext().getResourceAsStream("/WEB-INF/config.properties"));
				String USERNAME=prop.getProperty("UDS.USERNAME");
				String PASSWORD=prop.getProperty("UDS.PASSWORD");
            	String params=request.getParameter("dataids");
            	String [] idparams=params.split(",");
            	for(String s : idparams)
            	{
            		int dataid=Integer.valueOf(s);
            		
            		dataids.add(dataid);
            	}
            	String authToken=OTUtility.getAuthToken(USERNAME, PASSWORD);
            	OTUtility.downloadAllMailAttachments(authToken, dataids); */
            	String filePath="C:\\temp\\mail\\"; 
            	
            	File f=new File(filePath);
            
            	File[] fileList=f.listFiles();          	
            %>
            <c:set var="n" value="<%=fileList%>"/>
            
             <tr>
                <td>Attachment </td>
                <td>
                <c:forEach items="${n}" var="element">
                	<a href="${element.getAbsolutePath()}" id="${element.getName()}" onclick="">${element.getName()}</a>
            	</c:forEach>
            	</td>
            </tr>
            
            <tr>
                <td>Content </td>
                <td><textarea rows="10" cols="44"  name="content" style="overflow:hidden"></textarea> </td>
            </tr>
            <tr>
                <td colspan="2" align="center"><input type="submit" value="Send"/></td>
            </tr>
        </table>
         
    </form>
</body>
</html>