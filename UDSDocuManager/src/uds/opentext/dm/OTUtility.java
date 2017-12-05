package uds.opentext.dm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.MTOMFeature;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.opentext.ecm.api.OTAuthentication;
import com.opentext.livelink.service.core.Authentication;
import com.opentext.livelink.service.core.Authentication_Service;
import com.opentext.livelink.service.core.BooleanValue;
import com.opentext.livelink.service.core.ContentService;
import com.opentext.livelink.service.core.ContentService_Service;
import com.opentext.livelink.service.core.DataValue;
import com.opentext.livelink.service.core.DateValue;
import com.opentext.livelink.service.core.FileAtts;
import com.opentext.livelink.service.core.IntegerValue;
import com.opentext.livelink.service.core.StringValue;
import com.opentext.livelink.service.docman.AttributeGroup;
import com.opentext.livelink.service.docman.DocumentManagement;
import com.opentext.livelink.service.docman.DocumentManagement_Service;
import com.opentext.livelink.service.docman.GetMetadataLanguages;
import com.opentext.livelink.service.docman.Metadata;
import com.opentext.livelink.service.docman.MoveOptions;
import com.opentext.livelink.service.docman.Node;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.Headers;
import com.sun.xml.internal.ws.developer.JAXWSProperties;
import com.sun.xml.internal.ws.developer.StreamingDataHandler;
import com.sun.xml.internal.ws.developer.WSBindingProvider;

import uds.opentext.dm.beans.Document;
import uds.opentext.dm.beans.Message;
import uds.opentext.dm.beans.Request;

public class OTUtility {
	
	//public static final String USERNAME="otadmin@otds.admin";
	//public static final String PASSWORD="SEC@crmp!2017";
	public static final String ECM_API_NAMESPACE = "urn:api.ecm.opentext.com"; // The namespace of the OTAuthentication object
	public static final String CORE_NAMESPACE = "urn:Core.service.livelink.opentext.com";
	static final Logger LOGGER = Logger.getLogger(OTUtility.class);
	
	//Authentication TOken
	public static String getAuthToken(String USERNAME,String PASSWORD)
	{
		
		
		LOGGER.info("Auth Request for USER: "+USERNAME);            		
		Authentication_Service authService = null;
		Authentication authClient = null;

		// Store the authentication token
		String authToken = null;

		// Call the AuthenticateUser() method to get an authentication token
		try
		{
			// Create the Authentication service client
			authService = new Authentication_Service();
			authClient = authService.getBasicHttpBindingAuthentication();
			
			
			System.out.print("Authenticating User...");
			authToken = authClient.authenticateUser(USERNAME, PASSWORD);
			//System.out.println("Expiry date  "+authClient.getSessionExpirationDate());
			System.out.println("SUCCESS!\n");
			LOGGER.info("Auth Token: "+authToken);
		}
		catch (SOAPFaultException e)
		{
			LOGGER.error(e.getMessage());
			LOGGER.error(e.getFault().getFaultCode());
			LOGGER.info(e.getMessage());
			//System.out.println("FAILED!\n");
			//System.out.println(e.getFault().getFaultCode() + " : " + e.getMessage());
			return authToken;
			
		}
		
		return authToken;
		
	}
	
	
	//Document Management Client 
	public static DocumentManagement getDocumentManagement(String authToken)
	{
		
		DocumentManagement_Service docManService = null;
		DocumentManagement docManClient = null;
		OTAuthentication otAuth = null;
		SOAPHeader header =null;
		SOAPHeaderElement otAuthElement =null;
		SOAPElement authTokenElement = null;
		String contextID = null;	// Store the context ID for the download
		
		// We need to manually set the SOAP header to include the authentication token
		try
		{
			// Create the DocumentManagement service client
			docManService = new DocumentManagement_Service();
			docManClient = docManService.getBasicHttpBindingDocumentManagement();
			
			// Create the OTAuthentication object and set the authentication token
			otAuth = new OTAuthentication();
			otAuth.setAuthenticationToken(authToken);
			
			// Create a SOAP header
			header = MessageFactory.newInstance().createMessage().getSOAPPart().getEnvelope().getHeader();

			// Add the OTAuthentication SOAP header element
			otAuthElement = header.addHeaderElement(new QName(ECM_API_NAMESPACE, "OTAuthentication"));

			// Add the AuthenticationToken SOAP element
			authTokenElement = otAuthElement.addChildElement(new QName(ECM_API_NAMESPACE, "AuthenticationToken"));
			authTokenElement.addTextNode(otAuth.getAuthenticationToken());
			
			
			// Set the SOAP header on the docManClient
			((WSBindingProvider) docManClient).setOutboundHeaders(Headers.create(otAuthElement));
			
			
		}
		catch (SOAPException  e)
		{
			LOGGER.error(e.getMessage());
			System.out.println("Failed to set authentication SOAP header!\n");
			System.out.println(e.getMessage());	
		}
		catch (SOAPFaultException e)
		{
			LOGGER.error(e.getMessage());
			System.out.println("FAILED!\n");
			System.out.println(e.getFault().getFaultCode() + " : " + e.getMessage());
		}
		
		return docManClient;
	}
	
	
	//returns Document Context
	public static Map<String,String> getContext(int dataID,String authToken)
	{
		DocumentManagement docManClient=null;
		Map<String,String> docContext=new HashMap<String,String>();
		String contextID=null;
		String docuName=null;
		try 
		{
			docManClient=OTUtility.getDocumentManagement(authToken);
			System.out.print("Generating context ID...");
			contextID = docManClient.getVersionContentsContext(dataID, 0);
			docuName = docManClient.getNode(dataID).getName();
			System.out.println("Document Name: "+docManClient.getNode(dataID).getName());
			System.out.println("Generated Context ID : "+contextID);
			if(contextID!=null && docuName!=null)
			{
				docContext.put("contextID", contextID);
				docContext.put("docuName", docuName);
			}
		}
		catch(Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		return docContext;
	}
	
	
	//returns Content Service client
	public static ContentService getContentService(String authToken,String contextID)
	{
		OTAuthentication otAuth = null;
		ContentService_Service contentService = null;
		ContentService contentServiceClient = null;
		SOAPHeader header = null;
		SOAPHeaderElement otAuthElement = null;
		SOAPElement authTokenElement = null;
		List<Header> headers = null;
		// We need to manually set the SOAP headers to include the authentication token and context ID
		try
		{
			otAuth = new OTAuthentication();
			otAuth.setAuthenticationToken(authToken);
			
			// Create the ContentService client
			// NOTE: ContentService is the only service that requires MTOM support
			contentService = new ContentService_Service();
			contentServiceClient = contentService.getBasicHttpBindingContentService(new MTOMFeature());
			
			// Create a SOAP header
			header = MessageFactory.newInstance().createMessage().getSOAPPart().getEnvelope().getHeader();

			// Add the OTAuthentication SOAP header element
			otAuthElement = header.addHeaderElement(new QName(ECM_API_NAMESPACE, "OTAuthentication"));

			// Add the AuthenticationToken
			authTokenElement = otAuthElement.addChildElement(new QName(ECM_API_NAMESPACE, "AuthenticationToken"));
			authTokenElement.addTextNode(otAuth.getAuthenticationToken());
			
			
			// Add the ContextID SOAP header element
			SOAPHeaderElement contextIDElement = header.addHeaderElement(new QName(CORE_NAMESPACE, "contextID"));
			contextIDElement.addTextNode(contextID);

			// Set the headers on the binding provider
			headers = new ArrayList<Header>();
			headers.add(Headers.create(otAuthElement));
			headers.add(Headers.create(contextIDElement));

			((WSBindingProvider) contentServiceClient).setOutboundHeaders(headers);
		}
		catch (SOAPException e)
		{
			LOGGER.error(e.getMessage());
			System.out.println("Failed to set SOAP headers!\n");
			System.out.println(e.getMessage());
			
		}
		catch (SOAPFaultException e)
		{
			LOGGER.error(e.getMessage());
			System.out.println("FAILED!\n");
			System.out.println(e.getFault().getFaultCode() + " : " + e.getMessage());
			
		}
			return contentServiceClient;
		
	}
	
	
	//returns Document stream 
	public static StreamingDataHandler downloadDoc(String authToken,String contextID)
	{
		// Create a StreamingDataHandler to download the file with
		StreamingDataHandler downloadStream = null;
		ContentService contentServiceClient=null;
		try 
		{
			contentServiceClient=OTUtility.getContentService(authToken, contextID);
			//System.out.println("Downloading file...");
			downloadStream = (StreamingDataHandler) contentServiceClient.downloadContent(contextID);
			String contenttype=downloadStream.getContentType();
			String fileName=downloadStream.getName();
			//System.out.println(contenttype+"::::"+fileName);
			//File file = new File(FILE_PATH);
			//downloadStream.moveTo(file);
			//System.out.println(contenttype +" ............"+ fileName);
			//System.out.println("Downloaded " + file.length() + " bytes to " + FILE_PATH + ".\n");
			
			
			      
		}
		
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			System.out.println("Failed to download file!\n");
			System.out.println(e.getMessage());
		}
		return downloadStream;
	}
	
	//returns all documents related to request number
	public static List<Document> getChildren(String authToken,long parentID)
	{
		DocumentManagement docManClient=null;
		List<Node> nodes=null;
		List<Document> documents=new ArrayList<Document>();
		Document document=null;
		try
		{
			//authToken=OTUtility.getAuthToken();
			docManClient=OTUtility.getDocumentManagement(authToken);
			nodes=docManClient.listNodes(parentID, false);
			
			
			if(nodes.size()>0)
			{
				for(Node node:nodes)
				{
					document=new Document();
					document.setName(node.getName());
					document.setDataID(node.getID());
					document.setCreatedDate(node.getCreateDate());
					document.setComment(node.getComment());
					List<AttributeGroup> attributegroup=node.getMetadata().getAttributeGroups();
					boolean flag1=false;
					for(AttributeGroup attr:attributegroup)
					{
						if(attr.getDisplayName().equals("Connection Request Document"))
						{
							BooleanValue flag=(BooleanValue)attr.getValues().get(5);
							flag1=flag.getValues().get(0);
							document.setExclude(flag1);
						}
						
					}
					
					documents.add(document);
				}
			}
			//docManClient.createFolder(parentID,"Sucker","",null);			
		}
		catch(Exception e)
		{
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return documents;
			
	}
	

	
	//upload document to content server
	public static Message uploaddocument(String authtoken,String filePath,int parentID,int categoryTemplateID,Map<String,String> metadata)
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
		Message msg=new Message();
		if (!file.exists())
		{
			LOGGER.error("File does not exist at : " + filePath);
			System.out.println("ERROR!\n");
			System.out.println("File does not exist at : " + filePath);
			//return;
		}
		
		try
		{
			otAuth = new OTAuthentication();
			otAuth.setAuthenticationToken(authtoken);
			
			System.out.print("Generating context ID...");
			contextID = docManClient.createDocumentContext(PARENT_ID, file.getName(), null, false, null);
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
			SOAPHeaderElement otAuthElement = header.addHeaderElement(new QName(ECM_API_NAMESPACE, "OTAuthentication"));

			// Add the AuthenticationToken
			SOAPElement authTokenElement = otAuthElement.addChildElement(new QName(ECM_API_NAMESPACE, "AuthenticationToken"));
			authTokenElement.addTextNode(otAuth.getAuthenticationToken());

			// Add the ContextID SOAP header element
			SOAPHeaderElement contextIDElement = header.addHeaderElement(new QName(CORE_NAMESPACE, "contextID"));
			contextIDElement.addTextNode(contextID);

			// Add the FileAtts SOAP header element
			SOAPHeaderElement fileAttsElement = header.addHeaderElement(new QName(CORE_NAMESPACE, "fileAtts"));

			// Add the CreatedDate element
			SOAPElement createdDateElement = fileAttsElement.addChildElement(new QName(CORE_NAMESPACE, "CreatedDate"));
			createdDateElement.addTextNode(fileAtts.getCreatedDate().toString());

			// Add the ModifiedDate element
			SOAPElement modifiedDateElement = fileAttsElement.addChildElement(new QName(CORE_NAMESPACE, "ModifiedDate"));
			modifiedDateElement.addTextNode(fileAtts.getModifiedDate().toString());

			// Add the FileSize element
			SOAPElement fileSizeElement = fileAttsElement.addChildElement(new QName(CORE_NAMESPACE, "FileSize"));
			fileSizeElement.addTextNode(fileAtts.getFileSize().toString());

			// Add the FileName element
			SOAPElement fileNameElement = fileAttsElement.addChildElement(new QName(CORE_NAMESPACE, "FileName"));
			fileNameElement.addTextNode(fileAtts.getFileName());

			// Set the headers on the binding provider
			List<Header> headers = new ArrayList<Header>();
			headers.add(Headers.create(otAuthElement));
			headers.add(Headers.create(contextIDElement));
			headers.add(Headers.create(fileAttsElement));

			((WSBindingProvider) contentServiceClient).setOutboundHeaders(headers);
			
			//System.out.print("Uploading document...");
			String objectID = contentServiceClient.uploadContent(new DataHandler(new FileDataSource(file)));
			
			//System.out.println("SUCCESS!\n");
			//System.out.println("New document uploaded with ID = " + objectID);
			
			int dataid=Integer.valueOf(objectID);
			//updateUploadedDocument(authtoken, dataid, categoryTemplateID, documentType, subscriberNumber);
			OTUtility.addCategory(authtoken,dataid,categoryTemplateID,metadata);
			
			msg.setStatusCode(200);
			return msg;
			
		}
		catch (SOAPFaultException | IOException | DatatypeConfigurationException | SOAPException e)
		{
			System.out.println("FAILED!\n");
			System.out.println( " : " + e.getMessage());
			LOGGER.error(e.getMessage());
			msg.setStatusCode(404);
			msg.setMessageforFailure(e.getMessage());
			return msg;
		}
		
	}
	
	//copy from one request no to another req no.
	public static String copyDocument(String authToken,int parentid,int docID,String newName)
	{
		DocumentManagement docManClient=null;
		
		try
		{
			//authToken=OTUtility.getAuthToken();
			docManClient=OTUtility.getDocumentManagement(authToken);
			Node n=docManClient.copyNode(docID, parentid, newName, null);
			//System.out.println(n.getID());
		}
		catch(Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		return null;
		
	}
	
	//Move Document from one req no to another.
	public static String moveDocument(String authToken,int dataID, int parentID)
	{
		DocumentManagement docManClient=null;
		
		try
		{
			//System.out.println("dataid "+dataID);
			//System.out.println("parentID "+parentID);
			//authToken=OTUtility.getAuthToken();

			docManClient=OTUtility.getDocumentManagement(authToken);
			MoveOptions moveoptions=new MoveOptions();
			docManClient.moveNode(dataID,parentID,  null,null);
			
		}
		catch(Exception e)
		{
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return "Moved";
	}
	

	//updating category after uploading document
	public  static void updateUploadedDocument(String authToken, int dataid,int categoryTemplateID,String documentType,String subscriberNumber)
	{
		DocumentManagement fDocMan=null;
		Node node;
		
		try
		{
			fDocMan=OTUtility.getDocumentManagement(authToken);
			AttributeGroup attrgroup = fDocMan.getCategoryTemplate(categoryTemplateID);
			String toBeUpdateCatName =attrgroup.getDisplayName();
			List <AttributeGroup> nodeattrgroups = new ArrayList<AttributeGroup>();
			node=fDocMan.getNode(dataid);
			Node processingNode = node;
			Metadata updateMetadata =processingNode.getMetadata();
			String dataType;
			   
			if (updateMetadata!= null )
			{
				nodeattrgroups = updateMetadata.getAttributeGroups();	
			    if (nodeattrgroups.size()>0)
			    {
				    for (AttributeGroup atg:nodeattrgroups)
				    {
				    	String currCategoryName =atg.getDisplayName();
				    	if ((currCategoryName.compareToIgnoreCase(toBeUpdateCatName))==0)
				    	{
				    		//Update Existing Category Values
				    		StringValue str=(StringValue) atg.getValues().get(1);
				    		str.getValues().clear();
			    			str.getValues().add(subscriberNumber);
			    			
			    			StringValue str1=(StringValue) atg.getValues().get(7);
				    		str1.getValues().clear();
			    			str1.getValues().add(documentType);
				    		
				        }
				    }
			    }
			    processingNode.setMetadata(updateMetadata);
				fDocMan.updateNode(processingNode); 
			}
			else
			{
				System.out.println("Currently no Category is Attached to this Document ");
			}
			   
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	//adding category to the document while uploading document. 
	public static void addCategory(String authToken,int dataid,int categoryTemplateID,Map<String,String> categorySet)
	{
		DocumentManagement docManClient=null;
		List<DataValue> dataValues=null;
		AttributeGroup categoryTemplate = null;
		Metadata metadata=new Metadata();
		Node n;
		try
		{
			docManClient=OTUtility.getDocumentManagement(authToken);
			categoryTemplate = docManClient.getCategoryTemplate(categoryTemplateID);
			dataValues = categoryTemplate.getValues();
			n=docManClient.getNode(dataid);
			
			//Request Number
			StringValue RequestNumber=(StringValue)dataValues.get(0);
			RequestNumber.getValues().clear();
			Node requestNode=docManClient.getNode(Integer.valueOf(categorySet.get("RequestNumber")));
			RequestNumber.getValues().add(requestNode.getName());
				
			//Subscription Number
			StringValue SubscriptionNumber=(StringValue)dataValues.get(1);
			SubscriptionNumber.getValues().clear();
			SubscriptionNumber.getValues().add(categorySet.get("SubscriptionNumber"));
			
			//Uploaded Date			
			DateValue UploadedDate=(DateValue)dataValues.get(2);
			UploadedDate.getValues().clear();
			UploadedDate.getValues().add(n.getCreateDate());
			
			//Document Type
			StringValue DocumentType=(StringValue)dataValues.get(3);
			DocumentType.getValues().clear();
			DocumentType.getValues().add(categorySet.get("DocumentType"));
			
			//Uploaded By
			StringValue UploadedBy=(StringValue)dataValues.get(4);
			UploadedBy.getValues().clear();
			UploadedBy.getValues().add(n.getCreatedBy().toString());
			
			//Excluded
			BooleanValue Excluded=(BooleanValue)dataValues.get(5);
			Excluded.getValues().clear();
			Excluded.getValues().add(false);
			
			
			metadata.getAttributeGroups().add(categoryTemplate);
			n.setMetadata(metadata);
			docManClient.updateNode(n);
		}
		catch (Exception e) 
		{
			LOGGER.error(e.getMessage());
		}
	}
	
	//Update existing category for exclude option
	public static void updateCategory(String authToken,int dataid,int categoryTemplateID)
	{
		DocumentManagement fDocMan=null;
		Node node;
		
		try
		{
			//authToken=OTUtility.getAuthToken();
			fDocMan=OTUtility.getDocumentManagement(authToken);
			AttributeGroup attrgroup = fDocMan.getCategoryTemplate(categoryTemplateID);
			String toBeUpdateCatName =attrgroup.getDisplayName();
			List <AttributeGroup> nodeattrgroups = new ArrayList<AttributeGroup>();
			node=fDocMan.getNode(dataid);
			Node processingNode = node;
			Metadata updateMetadata =processingNode.getMetadata();
			//String currAttributeName ="";
			String dataType;
			   
			if (updateMetadata!= null )
			{
				nodeattrgroups = updateMetadata.getAttributeGroups();	
			    if (nodeattrgroups.size()>0)
			    {
				    for (AttributeGroup atg:nodeattrgroups)
				    {
				    	String currCategoryName =atg.getDisplayName();
				    	if ((currCategoryName.compareToIgnoreCase(toBeUpdateCatName))==0)
				    	{
				    		//Update Existing Category Values
				    		BooleanValue str=(BooleanValue) atg.getValues().get(5);
				    		str.getValues().clear();
			    			str.getValues().add(true);
				    		
				        }
				    }
			    }
			    processingNode.setMetadata(updateMetadata);
				fDocMan.updateNode(processingNode); 
			}
			else
			{
				System.out.println("Currently no Category is Attached to this Document ");
			}
			   
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	//Update existing category for exclude option
	public static void updateExcludeCategory(String authToken,int dataid,int categoryTemplateID)
		{
			DocumentManagement fDocMan=null;
			Node node;
			
			try
			{
				fDocMan=OTUtility.getDocumentManagement(authToken);
				AttributeGroup attrgroup = fDocMan.getCategoryTemplate(categoryTemplateID);
				String toBeUpdateCatName =attrgroup.getDisplayName();
				List <AttributeGroup> nodeattrgroups = new ArrayList<AttributeGroup>();
				node=fDocMan.getNode(dataid);
				Node processingNode = node;
				Metadata updateMetadata =processingNode.getMetadata();
				String dataType;
				   
				if (updateMetadata!= null )
				{
					nodeattrgroups = updateMetadata.getAttributeGroups();	
				    if (nodeattrgroups.size()>0)
				    {
					    for (AttributeGroup atg:nodeattrgroups)
					    {
					    	String currCategoryName =atg.getDisplayName();
					    	//if(currCategoryName.equals("Connection Request Document"))
					    	{
					    		if ((currCategoryName.compareToIgnoreCase(toBeUpdateCatName))==0)
					    		{
					    			//Update Existing Category Values
					    			BooleanValue str=(BooleanValue) atg.getValues().get(5);
					    			str.getValues().clear();
					    			str.getValues().add(false);
					    		}
					    		
					        }
					    }
				    }
				    processingNode.setMetadata(updateMetadata);
					fDocMan.updateNode(processingNode); 
				}
				else
				{
					System.out.println("Currently no Category is Attached to this Document ");
				}
				   
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		

	//Returns Request object which contain iframe info
	public static Request getNode(String authToken,int dataID)
	{
		DocumentManagement docManClient=null;
		Node node = null;
		Map<String,String> retVal=new HashedMap<String,String>();
		Request request=new Request();
		try
		{
			docManClient=OTUtility.getDocumentManagement(authToken);
			node=docManClient.getNode(dataID);
			
			request.setCreatedDate(node.getCreateDate().toGregorianCalendar().getTime());
			Metadata updateMetadata =node.getMetadata();
			List <AttributeGroup> nodeattrgroups = new ArrayList<AttributeGroup>();
			List<DataValue> values=new ArrayList<DataValue>();
			
			if (updateMetadata!= null )
			{
				nodeattrgroups = updateMetadata.getAttributeGroups();	
				if (nodeattrgroups.size()>0)
				{
					for (AttributeGroup atg:nodeattrgroups)
					{
						if(atg!=null)
						{
							
							values=atg.getValues();
							for(DataValue data:values)
							{
								
								if(data.getDescription().equals("Request Number"))
								{
									StringValue str = (StringValue) data;
				    				List<String> dataValues=str.getValues();
				    				for(String s:dataValues)
				    				{	
				    					request.setRequestNumber(s);
				    					
				    				}
								}
								if(data.getDescription().equals("Subscription Number"))
								{
									StringValue str = (StringValue) data;
				    				List<String> dataValues=str.getValues();
				    				for(String s:dataValues)
				    				{	
				    					request.setSubscriptionNumber(s);
				    					
				    				}	
								}
								if(data.getDescription().equals("Subscriber Name"))
								{
									StringValue str = (StringValue) data;
				    				List<String> dataValues=str.getValues();
				    				for(String s:dataValues)
				    				{	
				    					request.setCustomerName(s);
				    					
				    				}	
								}
								
								if(data.getDescription().equals("Request Type"))
								{
									StringValue str = (StringValue) data;
				    				List<String> dataValues=str.getValues();
				    				for(String s:dataValues)
				    				{	
				    					request.setRequestType(s);
				    					
				    				}	
								}
								if(data.getDescription().equals("Office"))
								{
									StringValue str = (StringValue) data;
				    				List<String> dataValues=str.getValues();
				    				for(String s:dataValues)
				    				{	
				    					request.setOffice(s);
				    					
				    				}		
								}
								if(data.getDescription().equals("Request Status"))
								{
									StringValue str = (StringValue) data;
				    				List<String> dataValues=str.getValues();
				    				for(String s:dataValues)
				    				{	
				    					request.setRequestStatus(s);		
				    				}			
								}
								
							}	
						}
					}
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return request;
	}
	
	
	//Email functionality to attach all selected documets
	public static void downloadAllMailAttachments(String authToken,List<Integer> dataids)
	{
		// Create a StreamingDataHandler to download the file with
				StreamingDataHandler downloadStream = null;
				ContentService contentServiceClient=null;
				String FILE_PATH="C:\\temp\\mail\\";
				Map<String,String> docContext=new HashMap<String,String>();
				try 
				{
					for(int i:dataids)
					{
						docContext=OTUtility.getContext(i, authToken);
						contentServiceClient=OTUtility.getContentService(authToken, docContext.get("contextID"));
						//System.out.println("Downloading file...");
						downloadStream = (StreamingDataHandler) contentServiceClient.downloadContent(docContext.get("contextID"));
						//String contenttype=downloadStream.getContentType();
						String fileName=docContext.get("docuName");
						//System.out.println(contenttype+"::::"+fileName);
						File file = new File(FILE_PATH);
						if(file.isDirectory())
						{
							File fle=new File(FILE_PATH+fileName);
							downloadStream.moveTo(fle);
							
						}
					
					}
					      
				}
				
				catch (Exception e)
				{
					LOGGER.error(e.getMessage());
					System.out.println("Failed to download file!\n");
					System.out.println(e.getMessage());
				}
				
	}
	
	
	/*//delete Document 
	public static String deleteDocument(String authToken,int dataID)
	{
		DocumentManagement docManClient=null;
		
		try
		{
			//authToken=OTUtility.getAuthToken();

			docManClient=OTUtility.getDocumentManagement(authToken);
			docManClient.deleteNode(dataID);
		}
		catch(Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		return "deleted";
	}
	*/
	
	
	
	
	
	/*//listing excluded nodes
	public static List<Node> excludedNodes(String authToken,int dataID)
	{
		DocumentManagement fDocMan=null;
		Node node;
		
		List<Node> excludedNodes=new ArrayList<Node>();
		try
		{
			
			fDocMan=OTUtility.getDocumentManagement(authToken);
			List <AttributeGroup> nodeattrgroups = new ArrayList<AttributeGroup>();
			List<Node> children=fDocMan.listNodes(dataID, false);
			
			for(Node nod:children)
			{
				
				Metadata updateMetadata =nod.getMetadata();
				//String currAttributeName ="";
				String dataType;
			   
				if (updateMetadata!= null )
				{
					nodeattrgroups = updateMetadata.getAttributeGroups();	
					if (nodeattrgroups.size()>0)
					{
						for (AttributeGroup atg:nodeattrgroups)
						{
							if(atg!=null)
							{
								//Update Existing Category Values
								BooleanValue str=(BooleanValue) atg.getValues().get(8);
								List<Boolean> flag=str.getValues();
								if(flag.size()!=0)
								{
									for(Boolean b:flag)
									{
										if(b)
										{
											excludedNodes.add(nod);
										}
									}
								}
							}
							
				    			
						}
					}
		
		
				}
			}
	
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return excludedNodes;
	}*/
	
	
	/*public static boolean getNodeExcluded(Node node)
	{
		List<AttributeGroup> attributegroup=node.getMetadata().getAttributeGroups();
		boolean flag1=false;
		for(AttributeGroup attr:attributegroup)
		{
			if(attr.getDisplayName().equals("Connection Request Document"))
			{
				BooleanValue flag=(BooleanValue)attr.getValues().get(5);
				flag1=flag.getValues().get(0);
				return flag1;
			}
		}
		return flag1;
		
	}*/
	
}
