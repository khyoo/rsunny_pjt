package com.regroup.rsunny.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.regroup.rsunny.common.model.ResultDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailUtil {
	
	public static ResultDTO sendMail(String toEmail, String subject, String content, String sndEmail) {
		return sendMail(toEmail, subject, content, sndEmail, null, null, null);
	}
	
	public static ResultDTO sendMail(String toEmail, String subject, String content, String sndEmail, String attachName, File attachFile, String hiddenReceiver) {

		// Sender's email ID needs to be mentioned
		String from = (StringUtils.isEmpty(sndEmail))? "restate1020@naver.com" : sndEmail;
		
		// Recipient's email ID needs to be mentioned.
		String to = toEmail;

		// Assuming you are sending email from localhost
		String host = "127.0.0.1";	//139.150.74.35

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.ssl.enable", "false");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("rsunny", "rsunny!09");
            }
        });

		// Get the default Session object.
		//Session session = Session.getDefaultInstance(properties);

		try {
			log.info("Mail send start...{}", toEmail);
			//log.info(content);
			
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// 발신자.
			InternetAddress fromAddress = new InternetAddress(from, "알써니");
			message.setFrom(fromAddress);

			// 수신자.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			//숨은참조.
			if( !StringUtils.isEmpty(hiddenReceiver) ) {
				log.debug("BCC add...{}", hiddenReceiver);
				message.addRecipient(Message.RecipientType.BCC, new InternetAddress(hiddenReceiver));
			}

			// 메일 제목.
			message.setSubject(subject, "utf-8");

			//메일 내용.
			if(attachFile != null && attachFile.exists()) {		//첨부가 존재하면...
		        Multipart multipart = new MimeMultipart();
		         
		        BodyPart messageBodyPart = new MimeBodyPart();
		        messageBodyPart.setContent(content, "text/html; charset=UTF-8");
		        multipart.addBodyPart(messageBodyPart);

		        // 첨부 파일.
		        messageBodyPart = new MimeBodyPart();
		        DataSource source = new FileDataSource(attachFile.getAbsolutePath());
		        messageBodyPart.setDataHandler(new DataHandler(source));
		        // 헤더에 파일이름을 세팅할때 base64형태로 반드시.
		        //messageBodyPart.setFileName(attachName);
		        messageBodyPart.setFileName(MimeUtility.encodeText(attachName, "euc-kr","B"));
		        multipart.addBodyPart(messageBodyPart);
		         
		        message.setContent(multipart);
			}
			else {	//첨부가 없으면.
				message.setContent(content, "text/html; charset=UTF-8");
			}

			// Send message
			Transport.send(message);
			
			log.info("Mail send sucessfully...receiver={}...bcc={}", toEmail, hiddenReceiver);
			
			return ResultDTO.of(0, "전송되었습니다.");
			
		} catch (MessagingException mex) {
			log.error("[ERROR-MessagingException] Mail send failed...{}\n{}", toEmail, mex);
			return ResultDTO.of(-1, String.format("전송에 실패하였습니다.\n%s", mex.getMessage()));
		} catch (Exception ex) {
			log.error("[ERROR-Exception] Mail send failed...{}\n{}", toEmail, ex);
			return ResultDTO.of(-1, String.format("전송에 실패하였습니다.\n%s", ex.getMessage()));
		}
	}
	
	public static String getContent(String mailType) {
		InputStream is = null;
		String content = "";
		
		try {
			String filePath = String.format("/static/mailform/%s.html", mailType);
			is = MailUtil.class.getResourceAsStream(filePath);
			content = IOUtils.toString(is, StandardCharsets.UTF_8.name());
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try { if(is != null) is.close(); } catch(Exception e) {};
		}
		
		return content;
	}

	public static String getBaseLinkURL() {
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			
			URL url = new URL(request.getScheme(), 
						        request.getServerName(), 
						        request.getServerPort(), 
						        request.getContextPath());
			return url.toString();
		}
		catch(Exception e) {
			log.error("[ERROR] {}", e.getMessage());
			return "";
		}
	}

}
