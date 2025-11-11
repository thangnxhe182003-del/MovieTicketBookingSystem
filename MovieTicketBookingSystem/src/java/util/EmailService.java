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
        // Register Mailcap handlers to avoid UnsupportedDataTypeException
        try {
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("multipart/alternative;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
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
    
    /**
     * Gửi email tóm tắt đơn hàng cho khách hàng sau khi thanh toán thành công
     *
     * @param toEmail Email người nhận
     * @param customerName Tên khách hàng
     * @param maOrder Mã đơn hàng
     * @return true nếu gửi thành công, false nếu lỗi
     */
    public static boolean sendOrderSummaryEmail(String toEmail, String customerName, int maOrder) throws UnsupportedEncodingException {
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
            // Lấy thông tin đơn hàng từ database
            dal.OrderDAO orderDAO = new dal.OrderDAO();
            model.Order order = orderDAO.getOrderById(maOrder);
            if (order == null) {
                System.out.println("[EmailService] Order not found: " + maOrder);
                return false;
            }
            
            dal.TicketDAO ticketDAO = new dal.TicketDAO();
            dal.OrderFoodDetailDAO ofdDAO = new dal.OrderFoodDetailDAO();
            dal.ShowtimeDAO showtimeDAO = new dal.ShowtimeDAO();
            dal.MovieDAO movieDAO = new dal.MovieDAO();
            dal.RoomDAO roomDAO = new dal.RoomDAO();
            dal.ProductDAO productDAO = new dal.ProductDAO();
            dal.SeatDAO seatDAO = new dal.SeatDAO();
            dal.DiscountDAO discountDAO = new dal.DiscountDAO();
            
            java.util.List<model.Ticket> tickets = ticketDAO.getTicketsByOrder(maOrder);
            java.util.List<model.OrderFoodDetail> orderFoodDetails = ofdDAO.getByOrder(maOrder);
            
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME, "MovieNow", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(MimeUtility.encodeText("Xác nhận đơn hàng #" + order.getOrderCode() + " - MovieNow", "UTF-8", "B"));

            // Calculate totals
            java.math.BigDecimal ticketTotal = java.math.BigDecimal.ZERO;
            java.math.BigDecimal comboTotal = java.math.BigDecimal.ZERO;
            
            for (model.Ticket ticket : tickets) {
                if (ticket.getGiaVe() != null) {
                    ticketTotal = ticketTotal.add(ticket.getGiaVe());
                }
            }
            
            for (model.OrderFoodDetail ofd : orderFoodDetails) {
                // Calculate combo total: if ThanhTien is null or 0, calculate from DonGia * SoLuong
                java.math.BigDecimal itemTotal = ofd.getThanhTien();
                if (itemTotal == null || itemTotal.compareTo(java.math.BigDecimal.ZERO) == 0) {
                    if (ofd.getDonGia() != null && ofd.getSoLuong() > 0) {
                        itemTotal = ofd.getDonGia().multiply(new java.math.BigDecimal(ofd.getSoLuong()));
                    } else {
                        itemTotal = java.math.BigDecimal.ZERO;
                    }
                }
                comboTotal = comboTotal.add(itemTotal);
            }
            
            // Get discount information
            model.Discount discount = null;
            java.math.BigDecimal discountAmount = java.math.BigDecimal.ZERO;
            if (order.getMaGiamGia() != null) {
                discount = discountDAO.getDiscountById(order.getMaGiamGia());
                if (discount != null) {
                    java.math.BigDecimal grandTotal = ticketTotal.add(comboTotal);
                    String hinhThucGiam = discount.getHinhThucGiam();
                    boolean isPhanTram = "Phần trăm".equals(hinhThucGiam) || "PhanTram".equals(hinhThucGiam);
                    boolean isTienMat = "Tiền mặt".equals(hinhThucGiam) || "TienMat".equals(hinhThucGiam);
                    
                    if (isPhanTram) {
                        if ("Vé".equals(discount.getLoaiGiamGia())) {
                            discountAmount = ticketTotal.multiply(discount.getGiaTriGiam()).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                        } else if ("Đồ ăn".equals(discount.getLoaiGiamGia())) {
                            discountAmount = comboTotal.multiply(discount.getGiaTriGiam()).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                        } else if ("Toàn đơn".equals(discount.getLoaiGiamGia())) {
                            discountAmount = grandTotal.multiply(discount.getGiaTriGiam()).divide(new java.math.BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
                        }
                        
                        if (discount.getGiaTriToiDa() != null && discountAmount.compareTo(discount.getGiaTriToiDa()) > 0) {
                            discountAmount = discount.getGiaTriToiDa();
                        }
                    } else if (isTienMat) {
                        if ("Vé".equals(discount.getLoaiGiamGia())) {
                            int soLuongVe = tickets.size();
                            java.math.BigDecimal discountPerTicket = discount.getGiaTriGiam();
                            java.math.BigDecimal totalDiscountForTickets = discountPerTicket.multiply(new java.math.BigDecimal(soLuongVe));
                            discountAmount = totalDiscountForTickets.compareTo(ticketTotal) < 0 ? totalDiscountForTickets : ticketTotal;
                        } else if ("Đồ ăn".equals(discount.getLoaiGiamGia())) {
                            discountAmount = discount.getGiaTriGiam().compareTo(comboTotal) < 0 ? discount.getGiaTriGiam() : comboTotal;
                        } else if ("Toàn đơn".equals(discount.getLoaiGiamGia())) {
                            discountAmount = discount.getGiaTriGiam().compareTo(grandTotal) < 0 ? discount.getGiaTriGiam() : grandTotal;
                        }
                    }
                }
            }
            
            // Build simplified HTML email body
            StringBuilder htmlBody = new StringBuilder();
            htmlBody.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            htmlBody.append("<style>");
            htmlBody.append("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; color: #333; background: #f5f5f5; margin: 0; padding: 20px; }");
            htmlBody.append(".email-container { max-width: 600px; margin: 0 auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }");
            htmlBody.append(".email-header { background: linear-gradient(135deg, #e50914 0%, #c90812 100%); color: white; padding: 25px 20px; text-align: center; }");
            htmlBody.append(".email-header h1 { margin: 0; font-size: 22px; font-weight: 700; }");
            htmlBody.append(".email-content { padding: 25px 20px; }");
            htmlBody.append(".greeting { font-size: 16px; margin-bottom: 15px; color: #333; }");
            htmlBody.append(".summary-box { background: #f8f9fa; border: 2px solid #e0e0e0; border-radius: 8px; padding: 20px; margin: 20px 0; }");
            htmlBody.append(".summary-title { font-size: 18px; font-weight: 700; color: #e50914; margin-bottom: 15px; text-align: center; }");
            htmlBody.append(".summary-row { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #e0e0e0; }");
            htmlBody.append(".summary-row:last-child { border-bottom: none; border-top: 2px solid #e50914; margin-top: 10px; padding-top: 12px; font-size: 18px; font-weight: 800; }");
            htmlBody.append(".summary-label { color: #666; }");
            htmlBody.append(".summary-value { font-weight: 700; color: #333; }");
            htmlBody.append(".summary-row:last-child .summary-value { color: #e50914; font-size: 20px; }");
            htmlBody.append(".info-section { background: #f0f7ff; padding: 15px; border-radius: 8px; margin: 15px 0; }");
            htmlBody.append(".info-section p { margin: 5px 0; }");
            htmlBody.append(".info-section strong { color: #1976d2; }");
            htmlBody.append(".note-section { background: #fff3cd; padding: 15px; border-radius: 8px; margin: 20px 0; font-size: 14px; color: #856404; }");
            htmlBody.append(".email-footer { background: #f8f9fa; padding: 20px; text-align: center; color: #666; font-size: 14px; }");
            htmlBody.append("</style></head><body>");
            htmlBody.append("<div class='email-container'>");
            htmlBody.append("<div class='email-header'><h1>🎬 MovieNow</h1></div>");
            htmlBody.append("<div class='email-content'>");
            htmlBody.append("<div class='greeting'>Chào <strong>").append(escape(customerName != null ? customerName : "Quý khách")).append("</strong>,</div>");
            htmlBody.append("<p style='font-size: 16px; margin-bottom: 20px;'>Cảm ơn bạn đã đặt vé tại MovieNow! Đơn hàng của bạn đã được thanh toán thành công.</p>");
            
            // Showtime Information (simplified)
            if (!tickets.isEmpty()) {
                model.Ticket firstTicket = tickets.get(0);
                model.Showtime showtime = showtimeDAO.getShowtimeById(firstTicket.getMaSuatChieu());
                if (showtime != null) {
                    model.Movie movie = movieDAO.getMovieById(showtime.getMaPhim());
                    model.Room room = roomDAO.getRoomById(showtime.getMaPhong());
                    
                    htmlBody.append("<div class='info-section'>");
                    if (movie != null) {
                        htmlBody.append("<p><strong>Phim:</strong> ").append(escape(movie.getTenPhim())).append("</p>");
                    }
                    if (room != null) {
                        htmlBody.append("<p><strong>Rạp:</strong> ").append(escape(room.getTenRap())).append(" - ").append(escape(room.getTenPhong())).append("</p>");
                    }
                    htmlBody.append("<p><strong>Ngày chiếu:</strong> ").append(showtime.getNgayChieu() != null ? 
                        showtime.getNgayChieu().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "").append("</p>");
                    htmlBody.append("<p><strong>Giờ chiếu:</strong> ").append(showtime.getGioBatDau() != null ? 
                        showtime.getGioBatDau().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) : "").append(" - ")
                        .append(showtime.getGioKetThuc() != null ? 
                        showtime.getGioKetThuc().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")) : "").append("</p>");
                    
                    // Seat list
                    htmlBody.append("<p><strong>Ghế:</strong> ");
                    java.util.List<String> seatNames = new java.util.ArrayList<>();
                    for (model.Ticket ticket : tickets) {
                        model.Seat seat = seatDAO.getSeatById(ticket.getMaGhe());
                        String seatName = seat != null ? (seat.getHangGhe() + String.valueOf(seat.getSoGhe())) : ("Ghế #" + ticket.getMaGhe());
                        seatNames.add(seatName);
                    }
                    htmlBody.append(String.join(", ", seatNames)).append("</p>");
                    htmlBody.append("</div>");
                }
            }
            
            // Combo Information (simplified)
            if (!orderFoodDetails.isEmpty()) {
                htmlBody.append("<div class='info-section'>");
                htmlBody.append("<p><strong>Combo:</strong> ");
                java.util.List<String> comboItems = new java.util.ArrayList<>();
                for (model.OrderFoodDetail ofd : orderFoodDetails) {
                    model.Product product = productDAO.getProductById(ofd.getMaSP());
                    String productName = product != null ? product.getTenSP() : ("Sản phẩm #" + ofd.getMaSP());
                    java.math.BigDecimal itemTotal = ofd.getThanhTien();
                    if (itemTotal == null || itemTotal.compareTo(java.math.BigDecimal.ZERO) == 0) {
                        if (ofd.getDonGia() != null && ofd.getSoLuong() > 0) {
                            itemTotal = ofd.getDonGia().multiply(new java.math.BigDecimal(ofd.getSoLuong()));
                        } else {
                            itemTotal = java.math.BigDecimal.ZERO;
                        }
                    }
                    comboItems.add(productName + " × " + ofd.getSoLuong() + " (₫" + itemTotal.toPlainString() + ")");
                }
                htmlBody.append(String.join(", ", comboItems)).append("</p>");
                htmlBody.append("</div>");
            }
            
            // Summary Section (simplified)
            htmlBody.append("<div class='summary-box'>");
            htmlBody.append("<div class='summary-title'>💰 TÓM TẮT ĐƠN HÀNG</div>");
            htmlBody.append("<div class='summary-row'><span class='summary-label'>Mã đơn hàng:</span><span class='summary-value'>").append(escape(order.getOrderCode())).append("</span></div>");
            htmlBody.append("<div class='summary-row'><span class='summary-label'>Tổng tiền vé:</span><span class='summary-value'>₫").append(ticketTotal.toPlainString()).append("</span></div>");
            htmlBody.append("<div class='summary-row'><span class='summary-label'>Tổng tiền combo:</span><span class='summary-value'>₫").append(comboTotal.toPlainString()).append("</span></div>");
            if (discount != null && discountAmount.compareTo(java.math.BigDecimal.ZERO) > 0) {
                htmlBody.append("<div class='summary-row'><span class='summary-label' style='color: #4caf50;'>Giảm giá (").append(escape(discount.getMaCode())).append("):</span><span class='summary-value' style='color: #4caf50;'>-₫").append(discountAmount.toPlainString()).append("</span></div>");
            }
            htmlBody.append("<div class='summary-row'><span class='summary-label'>Tổng thanh toán:</span><span class='summary-value'>₫").append(order.getTongTien() != null ? order.getTongTien().toPlainString() : "0").append("</span></div>");
            htmlBody.append("</div>");
            
            // Note Section
            htmlBody.append("<div class='note-section'>");
            htmlBody.append("<strong>📌 Lưu ý:</strong> Vui lòng đến rạp trước giờ chiếu 15 phút để làm thủ tục check-in. Vé đã đặt không hoàn, không hủy, không đổi suất chiếu.");
            htmlBody.append("</div>");
            
            htmlBody.append("</div>"); // email-content
            htmlBody.append("<div class='email-footer'>");
            htmlBody.append("<p style='margin: 0; color: #e50914; font-weight: 700;'>MovieNow Team</p>");
            htmlBody.append("</div>");
            htmlBody.append("</div>"); // email-container
            htmlBody.append("</body></html>");
            
            // Set HTML content directly (simpler approach, avoids multipart/alternative issues)
            ByteArrayDataSource htmlDataSource = new ByteArrayDataSource(
                htmlBody.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8), 
                "text/html; charset=UTF-8"
            );
            message.setDataHandler(new DataHandler(htmlDataSource));
            message.setHeader("Content-Type", "text/html; charset=UTF-8");
            message.setHeader("Content-Transfer-Encoding", "8bit");
            message.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(SMTP_HOST, EMAIL_USERNAME, EMAIL_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Order summary email sent successfully to " + toEmail);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            Exception ne = e.getNextException();
            if (ne != null) ne.printStackTrace();
            System.out.println("Failed to send order summary email: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send order summary email: " + e.getMessage());
            return false;
        }
    }
}