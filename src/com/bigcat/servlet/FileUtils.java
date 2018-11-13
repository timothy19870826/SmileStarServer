package com.bigcat.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.bigcat.data.http.HttpResponseMsg;
import com.bigcat.data.http.UploadFileResponse;
import com.bigcat.utils.UrlUtils;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class FileUtils
 */
@WebServlet("/FileUtils")
public class FileUtils extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String actionDownload = "download";
	private static final String actionUpload = "upload";

	private String savePath = "";
    private String userFilePath = "";  
    private String tempPath = "";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FileUtils() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void init() throws ServletException {
    	// TODO Auto-generated method stub
    	super.init();
    	tempPath = getServletContext().getRealPath("WEB-INF\\temp");
    	File temp = new File(tempPath);
    	if (temp.exists() == false) {
    		temp.mkdirs();
    	}
    	savePath = getServletContext().getInitParameter("userFilePath");
    	userFilePath = getServletContext().getRealPath(String.format("%s", savePath));
    	File save = new File(userFilePath);
    	if (save.exists() == false) {
    		save.mkdirs();
    	}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	throw new ServletException("GET method used with " +
   		     getClass( ).getName( )+": POST method required.");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String[] methods = UrlUtils.getMethodPath(request.getQueryString());
		if (methods == null) {
			HttpResponseMsg msg = new HttpResponseMsg();
			msg.setCode(1);
			msg.setData(null);
			msg.setMessage("method is null");
			JSONObject jsonObject = JSONObject.fromObject(msg);
			response.getWriter().append(jsonObject.toString());
			return;
		}
		switch (methods[1]) {
		case actionDownload:			
			
			break;
		case actionUpload:			
			uploadFile(request, response);
			break;

		default:
			break;
		}
	}
	
	private void uploadFile(HttpServletRequest request, HttpServletResponse response) {

		response.setContentType("text/html");
		DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
		diskFileItemFactory.setSizeThreshold(4*1024*1024);
		diskFileItemFactory.setRepository(new File(tempPath));
		
		ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
		servletFileUpload.setSizeMax(50*1024*1024);

		File file;
		int leng = 0;
		byte[] buff = new byte[256];
		FileItemIterator fileItemIterator = null;
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		UploadFileResponse uploadFileResponse = new UploadFileResponse(); 
		try {
			fileItemIterator = servletFileUpload.getItemIterator(request);
			String serverName = request.getServerName();
			int serverPort = request.getServerPort();
			String netFilePath = String.format("http://%s:%d%s/%s/", serverName, serverPort, request.getContextPath(),savePath);
			while (fileItemIterator.hasNext()) {
				FileItemStream fileItem = fileItemIterator.next();
				if (fileItem.isFormField()) {
					continue;
				}
	            // 获取上传文件的参数
	            String fileName = fileItem.getName();
	            // 写入文件
	            inputStream = fileItem.openStream();
	            if (inputStream == null) {
	            	continue;
	            }
	            if(fileName.lastIndexOf("\\") >= 0 ){
	               file = new File( userFilePath + "\\" +
	               fileName.substring( fileName.lastIndexOf("\\")+1)) ;
	            }else{
	               file = new File( userFilePath + "\\" + fileName) ;
	            }
	            leng = 0;
	            fileOutputStream = new FileOutputStream(file);
	            while ((leng = inputStream.read(buff)) > 0) {
	            	fileOutputStream.write(buff, 0, leng);
				}
	            fileOutputStream.close();
	            fileOutputStream = null;
	            inputStream.close();
	            inputStream = null;
	            uploadFileResponse.getSrcList().add(fileName);
	            uploadFileResponse.getSaveList().add(netFilePath + fileName);
			}		

			HttpResponseMsg msg = new HttpResponseMsg();
			msg.setCode(0);
			msg.setData(uploadFileResponse);
			JSONObject jsonObject = JSONObject.fromObject(msg);
			response.getWriter().append(jsonObject.toString());
			
		} catch (FileUploadException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fileOutputStream != null) {
	            try {
					fileOutputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            fileOutputStream = null;
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inputStream = null;
			}
		} 
	}

}
