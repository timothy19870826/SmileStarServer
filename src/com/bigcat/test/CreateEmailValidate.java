package com.bigcat.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bigcat.data.http.HttpResponseMsg;
import com.bigcat.redis.SingleRedisClient;

import net.sf.json.JSONObject;
import redis.clients.jedis.Jedis;

/**
 * Servlet implementation class CreateEmailValidate
 */
@WebServlet("/CreateEmailValidate")
public class CreateEmailValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateEmailValidate() {
        super();
        // TODO Auto-generated constructor stub
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
		HttpResponseMsg msg = new HttpResponseMsg();
		String email = request.getParameter("email");

		/*
		UserValidateInfoInRedis uInfoInRedis = UserValidateInfoInRedis.read(email);
		if (uInfoInRedis == null) {
			Date date = new Date();
			Random random = new Random(date.getTime());
			UserValidateInfo uInfo = new UserValidateInfo();
			uInfo.setEmailCode(String.format("%6d", random.nextInt(1000000)));			
			uInfo.setLastOpTime(SimpleDateFormat.getInstance().format(date));
			uInfoInRedis = UserValidateInfoInRedis.write(email, uInfo, 60 * 5);
		}
		*/

		Date now = new Date();
		Jedis jedis = SingleRedisClient.getInstance().getResource();
		try {
			RedisUserValidateInfoTab redisUserValidateInfoTab = new RedisUserValidateInfoTab(jedis);
			UserValidateInfo userValidateInfo = redisUserValidateInfoTab.getValidateInfo(email);
			if (userValidateInfo == null) {
				Random random = new Random(now.getTime());
				userValidateInfo = new UserValidateInfo();
				userValidateInfo.setEmailCode(String.format("%6d", random.nextInt(1000000)));		
				userValidateInfo.setCreateTime(SimpleDateFormat.getInstance().format(now));			
				userValidateInfo.setLastOpTime(SimpleDateFormat.getInstance().format(now));
				redisUserValidateInfoTab.setValidateInfo(email, userValidateInfo);
				sendEmail(email, userValidateInfo.getEmailCode());
			}
			else {		
				Date lastOpDate = SimpleDateFormat.getInstance().parse(userValidateInfo.getLastOpTime());
				if (lastOpDate.getTime() <= 0 || lastOpDate.getTime() - now.getTime() < 60 * 1000) {
					msg.setCode(1);
					msg.setData(null);
					msg.setMessage("Validate Code In CD");
					JSONObject jsonObject = JSONObject.fromObject(msg);
					response.getWriter().append(jsonObject.toString());
				}				
				else {
					sendEmail(email, userValidateInfo.getEmailCode());
					userValidateInfo.setLastOpTime(SimpleDateFormat.getInstance().format(now));
					redisUserValidateInfoTab.setValidateInfo(email, userValidateInfo);
				}
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			msg.setCode(1);
			msg.setData(null);
			msg.setMessage(e.getMessage());
			JSONObject jsonObject = JSONObject.fromObject(msg);
			response.getWriter().append(jsonObject.toString());
			return ;
		}finally {
			jedis.close();
		}
	}
	
	private void sendEmail(String email, String code) throws MessagingException {

		Properties properties = System.getProperties();
		properties.setProperty("mail.host", "smtp.aliyun.com");
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.auth", "true");
		
		Session session = Session.getDefaultInstance(properties);
		session.setDebug(true);
		
		Transport transport = session.getTransport();
		transport.connect("smtp.aliyun.com", "bigbiglazycat@aliyun.com", "aliyun1513");			
		MimeMessage emailMsg = new MimeMessage(session);
		emailMsg.setFrom(new InternetAddress("bigbiglazycat@aliyun.com"));
		emailMsg.setRecipient(RecipientType.TO, new InternetAddress(email));
		emailMsg.setSubject("验证");
		emailMsg.setText(code);
		emailMsg.setSentDate(new Date());
		transport.sendMessage(emailMsg, emailMsg.getAllRecipients());
		transport.close(); 
	}
}
