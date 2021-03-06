package uds.opentext.dm;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    static final Logger LOGGER = Logger.getLogger(UploadServlet.class); 

    private static final String DATA_DIRECTORY = "data";
    private static final int MAX_MEMORY_SIZE = 1024 * 1024 * 1024;
    private static final int MAX_REQUEST_SIZE = 1024 * 1024*1024;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	Properties prop = new Properties();
		prop.load(getServletContext().getResourceAsStream("/WEB-INF/config.properties"));
		String USERNAME=prop.getProperty("UDS.USERNAME");
		String PASSWORD=prop.getProperty("UDS.PASSWORD");
		
		String categoryID=prop.getProperty("UDS.CategoryTemplateID");
		int categoryTemplateID = 0;
		if(categoryID!=null && !categoryID.equals(""))
		{
			categoryTemplateID=Integer.valueOf(categoryID);
		}
		else
		{
			LOGGER.error("Category Template ID is missing or not valid");
			
		}
		
			String authToken=OTUtility.getAuthToken(USERNAME, PASSWORD);
	    	
	        // Check that we have a file upload request
	        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	
	        if (!isMultipart) {
	            return;
	        }
	
	        // Create a factory for disk-based file items
	        DiskFileItemFactory factory = new DiskFileItemFactory();
	
	        // Sets the size threshold beyond which files are written directly to
	        // disk.
	        factory.setSizeThreshold(MAX_MEMORY_SIZE);
	
	        // Sets the directory used to temporarily store files that are larger
	        // than the configured size threshold. We use temporary directory for
	        // java
	        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
	
	        // constructs the folder where uploaded file will be stored
	        String uploadFolder = getServletContext().getRealPath("")
	                + File.separator + DATA_DIRECTORY;
	        File f=new File(uploadFolder);
	        if(!f.exists())
	        {
	        	System.out.println("creating directory");
	        	f.mkdirs();
	        }
	        // Create a new file upload handler
	        ServletFileUpload upload = new ServletFileUpload(factory);
	
	        // Set overall request size constraint
	        upload.setSizeMax(MAX_REQUEST_SIZE);
	
	        try {
	            // Parse the request
	        	String filePath=null;
	            List items = upload.parseRequest(request);
	            Iterator iter = items.iterator();
	            while (iter.hasNext()) {
	                FileItem item = (FileItem) iter.next();
	
	                if (!item.isFormField()) {
	                    String fileName = new File(item.getName()).getName();
	                    filePath = uploadFolder + File.separator + fileName;
	                    //System.out.println(fileName);
	                    File uploadedFile = new File(filePath);
	                    System.out.println(filePath);
	                    // saves the file to upload directory
	                    item.write(uploadedFile);
	                }
	            }
	
	            // displays done.jsp page after upload finished
	            OTUtility.uploaddocument(authToken,filePath,85457,categoryTemplateID);
	            getServletContext().getRequestDispatcher("/done.jsp").forward(
	                    request, response);
	      
	        } catch (FileUploadException ex) {
	            throw new ServletException(ex);
	        } catch (Exception ex) {
	            throw new ServletException(ex);
	        }
	
	    }

}
