/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import jakarta.activation.CommandMap;
import jakarta.activation.MailcapCommandMap;
import jakarta.activation.DataHandler;
import jakarta.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Utility class để xử lý gửi email (sử dụng JavaMail).
 *
 * @author thang
 */
public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "hoatqhe171842@fpt.edu.vn";  // Thay bằng Gmail của bạn
    private static final String EMAIL_PASSWORD = "vpnp dvfj khgp kuzu";     // Thay bằng App Password

    static {
        // Register Mailcap handlers to avoid UnsupportedDataTypeException for multipart/alternative
        try {
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);
        } catch (Exception ignored) { }
    }

    /**
     * Gửi email chứa OTP kích hoạt tài khoản (multipart/alternative UTF-8).
     */
    public static boolean sendOtpEmail(String toEmail, String username, String otp) throws UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.mime.charset", "UTF-8");
        props.put("mail.mime.allowutf8", "true");
        props.put("mail.smtp.allow8bitmime", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.trust", SMTP_HOST);

        Session session;
        session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });
        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME, "MovieNow", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(MimeUtility.encodeText("Xác thực OTP - MovieNow", "UTF-8", "B"));

            String bodyText = "Chào " + username + ",\n\n"
                    + "Cảm ơn bạn đã đăng ký tại MovieNow!\n"
                    + "Mã OTP kích hoạt tài khoản của bạn là: " + otp + "\n\n"
                    + "Mã OTP hết hạn sau 10 phút. Vui lòng không chia sẻ.\n\n"
                    + "Trân trọng,\nMovieNow Team";
            ByteArrayDataSource ds = new ByteArrayDataSource(bodyText.getBytes(java.nio.charset.StandardCharsets.UTF_8), "text/plain; charset=UTF-8");
            message.setDataHandler(new DataHandler(ds));
            message.setHeader("Content-Type", "text/plain; charset=UTF-8");
            message.setHeader("Content-Transfer-Encoding", "8bit");
            message.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(SMTP_HOST, EMAIL_USERNAME, EMAIL_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Email with OTP sent successfully to " + toEmail);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            Exception ne = e.getNextException();
            if (ne != null) ne.printStackTrace();
            System.out.println("Failed to send OTP email: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Gửi email chứa OTP để đặt lại mật khẩu.
     *
     * @param toEmail Email người nhận
     * @param username Tên đăng nhập để personalize
     * @param otp Mã OTP
     * @return true nếu gửi thành công, false nếu lỗi
     */
    public static boolean sendPasswordResetEmail(String toEmail, String username, String otp) throws UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session;
        session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });
        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME, "MovieNow", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(MimeUtility.encodeText("Đặt lại mật khẩu - MovieNow", "UTF-8", "B"));

            String bodyText = "Chào " + username + ",\n\n"
                    + "Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn tại MovieNow.\n"
                    + "Mã OTP để đặt lại mật khẩu của bạn là: " + otp + "\n\n"
                    + "Mã OTP hết hạn sau 10 phút. Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.\n\n"
                    + "Trân trọng,\nMovieNow Team";
            ByteArrayDataSource ds = new ByteArrayDataSource(bodyText.getBytes(java.nio.charset.StandardCharsets.UTF_8), "text/plain; charset=UTF-8");
            message.setDataHandler(new DataHandler(ds));
            message.setHeader("Content-Type", "text/plain; charset=UTF-8");
            message.setHeader("Content-Transfer-Encoding", "8bit");
            message.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(SMTP_HOST, EMAIL_USERNAME, EMAIL_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Password reset email sent successfully to " + toEmail);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            Exception ne = e.getNextException();
            if (ne != null) ne.printStackTrace();
            System.out.println("Failed to send password reset email: " + e.getMessage());
            return false;
        }
    }

    // Basic HTML escaping to avoid breaking HTML content
    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}