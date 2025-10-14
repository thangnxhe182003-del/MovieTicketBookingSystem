/*
 * Click nbfs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Utility class để xử lý gửi email (sử dụng JavaMail).
 *
 * @author thang
 */
public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String EMAIL_USERNAME = "he182003nguyenxuanthang@gmail.com";  // Thay bằng Gmail của bạn
    private static final String EMAIL_PASSWORD = "dnmb cgkx anvo aybv";     // Thay bằng App Password

    /**
     * Gửi email chứa OTP kích hoạt tài khoản.
     *
     * @param toEmail Email người nhận
     * @param username Tên đăng nhập để personalize
     * @param otp Mã OTP
     * @return true nếu gửi thành công, false nếu lỗi
     */
    public static boolean sendOtpEmail(String toEmail, String username, String otp) {
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

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Xác thực OTP - MovieNow");

            String body = "Chào " + username + ",\n\n"
                    + "Cảm ơn bạn đã đăng ký tại MovieNow!\n"
                    + "Mã OTP kích hoạt tài khoản của bạn là: " + otp + "\n\n"
                    + "Mã OTP hết hạn sau 10 phút. Vui lòng không chia sẻ.\n\n"
                    + "Trân trọng,\nMovieNow Team";

            message.setText(body);
            Transport.send(message);
            System.out.println("Email with OTP sent successfully to " + toEmail);
            return true;
        } catch (MessagingException e) {
            System.out.println("Failed to send OTP email: " + e.getMessage());
            return false;
        }
    }
//    // Thêm vào cuối class
//public static void main(String[] args) {
//    String testEmail = "test@example.com";  // Thay bằng email test của bạn
//    String testUsername = "TestUser";
//    String testOtp = "123456";
//    boolean result = sendOtpEmail(testEmail, testUsername, testOtp);
//    System.out.println("Test send OTP: " + (result ? "Success" : "Failed"));
//}
}
