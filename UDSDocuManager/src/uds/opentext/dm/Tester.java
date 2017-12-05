package uds.opentext.dm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPFaultException;

import com.opentext.ecm.api.OTAuthentication;
import com.opentext.livelink.service.core.Attachment;
import com.opentext.livelink.service.core.ContentService;
import com.opentext.livelink.service.core.ContentService_Service;
import com.opentext.livelink.service.core.FileAtts;
import com.opentext.livelink.service.docman.DocumentManagement;
import com.opentext.livelink.service.docman.Node;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.Headers;
import com.sun.xml.internal.ws.developer.JAXWSProperties;
import com.sun.xml.internal.ws.developer.WSBindingProvider;

import uds.opentext.dm.beans.Request;

public class Tester {
	
	public static void uploaddocument(String authtoken,String filePath,int parentID,int categoryTemplateID)
	{
		//String authtoken=OTUtility.getAuthToken();
		OTAuthentication otAuth = null;
		DocumentManagement docManClient=OTUtility.getDocumentManagement(authtoken);
		String contextID = null;
		int PARENT_ID=parentID;
		ContentService_Service contentService = null;
		ContentService contentServiceClient = null;
		File file = new File(filePath);
		BasicFileAttributes fileAttributes;
		FileAtts fileAtts = new FileAtts();

		if (!file.exists())
		{
			//LOGGER.error("File does not exist at : " + filePath);
			System.out.println("ERROR!\n");
			System.out.println("File does not exist at : " + filePath);
			return;
		}
		
		try
		{
			otAuth = new OTAuthentication();
			otAuth.setAuthenticationToken(authtoken);
			
			System.out.print("Generating context ID...");
	//		docManClient.addVersionContext(id, metadata)
			contextID = docManClient.createDocumentContext(PARENT_ID, file.getName(), null, true, null);
			//System.out.println("SUCCESS!\n");
			
			contentService = new ContentService_Service();
			contentServiceClient = contentService.getBasicHttpBindingContentService(new MTOMFeature());
			

			// The number of bytes to write in each chunk
			final int CHUNK_SIZE = 10240;
			
			// Enable streaming and use chunked transfer encoding to send the request body to support large files
			((BindingProvider) contentServiceClient).getRequestContext().put(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE, CHUNK_SIZE);
			fileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			
			fileAtts.setCreatedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(fileAttributes.creationTime().toString()));
			fileAtts.setFileName(file.getName());
			fileAtts.setFileSize(file.length());
			fileAtts.setModifiedDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(fileAttributes.lastModifiedTime().toString()));

			// Create a SOAP header
			SOAPHeader header = MessageFactory.newInstance().createMessage().getSOAPPart().getEnvelope().getHeader();

			// Add the OTAuthentication SOAP header element
			SOAPHeaderElement otAuthElement = header.addHeaderElement(new QName(OTUtility.ECM_API_NAMESPACE, "OTAuthentication"));

			// Add the AuthenticationToken
			SOAPElement authTokenElement = otAuthElement.addChildElement(new QName(OTUtility.ECM_API_NAMESPACE, "AuthenticationToken"));
			authTokenElement.addTextNode(otAuth.getAuthenticationToken());

			// Add the ContextID SOAP header element
			SOAPHeaderElement contextIDElement = header.addHeaderElement(new QName(OTUtility.CORE_NAMESPACE, "contextID"));
			contextIDElement.addTextNode(contextID);

			// Add the FileAtts SOAP header element
			SOAPHeaderElement fileAttsElement = header.addHeaderElement(new QName(OTUtility.CORE_NAMESPACE, "fileAtts"));

			// Add the CreatedDate element
			SOAPElement createdDateElement = fileAttsElement.addChildElement(new QName(OTUtility.CORE_NAMESPACE, "CreatedDate"));
			createdDateElement.addTextNode(fileAtts.getCreatedDate().toString());

			// Add the ModifiedDate element
			SOAPElement modifiedDateElement = fileAttsElement.addChildElement(new QName(OTUtility.CORE_NAMESPACE, "ModifiedDate"));
			modifiedDateElement.addTextNode(fileAtts.getModifiedDate().toString());

			// Add the FileSize element
			SOAPElement fileSizeElement = fileAttsElement.addChildElement(new QName(OTUtility.CORE_NAMESPACE, "FileSize"));
			fileSizeElement.addTextNode(fileAtts.getFileSize().toString());

			// Add the FileName element
			SOAPElement fileNameElement = fileAttsElement.addChildElement(new QName(OTUtility.CORE_NAMESPACE, "FileName"));
			fileNameElement.addTextNode(fileAtts.getFileName());

			// Set the headers on the binding provider
			List<Header> headers = new ArrayList<Header>();
			headers.add(Headers.create(otAuthElement));
			headers.add(Headers.create(contextIDElement));
			headers.add(Headers.create(fileAttsElement));

			((WSBindingProvider) contentServiceClient).setOutboundHeaders(headers);
			
			//System.out.print("Uploading document...");
			//docManClient.get
			String objectID = contentServiceClient.uploadContent(new DataHandler(new FileDataSource(file)));
		
			//System.out.println("SUCCESS!\n");
			//System.out.println("New document uploaded with ID = " + objectID);
			
			int dataid=Integer.valueOf(objectID);
			
			
			Map <String,String> metadata=new HashMap<>();
			metadata.put("RequestNumber", "1725600086");
			metadata.put("SubscriptionNumber", "3345667724");
			metadata.put("DocumentType", "ZINSTLNCOS");
			OTUtility.addCategory(authtoken,dataid,categoryTemplateID,metadata);
			
		}
		catch (SOAPFaultException | IOException | DatatypeConfigurationException | SOAPException e)
		{
			System.out.println("FAILED!\n");
			System.out.println( " : " + e.getMessage());
			return;
		}
		
	}

		public static void main(String[] args) {
			
			String authtoken=OTUtility.getAuthToken("Otadmin@otds.admin", "SEC@crmp!2017");
			uploaddocument(authtoken, "D:\\UDS\\MOM(UDS Integration).xlsx",53784, 56424);
			
		}
}