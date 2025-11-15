package dal;

import model.Ticket;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TicketDAO extends DBContext {
    
    /**
     * Tạo ticket mới (MaOrder = NULL)
     */
    public boolean addTicket(Ticket ticket) {
        String sql = "INSERT INTO dbo.Ticket (MaOrder, MaSuatChieu, MaGhe, DonGia, TrangThai, CreatedAt) " +
                    "VALUES (NULL, ?, ?, ?, ?, SYSUTCDATETIME())";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ticket.getMaSuatChieu());
            ps.setInt(2, ticket.getMaGhe());
            ps.setBigDecimal(3, ticket.getGiaVe());
            ps.setString(4, ticket.getTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Tạo ticket mới gắn với MaOrder
     */
    public boolean addTicketWithOrder(int maOrder, Ticket ticket) {
        String sql = "INSERT INTO dbo.Ticket (MaOrder, MaSuatChieu, MaGhe, DonGia, TrangThai, CreatedAt) " +
                    "VALUES (?, ?, ?, ?, ?, SYSUTCDATETIME())";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maOrder);
            ps.setInt(2, ticket.getMaSuatChieu());
            ps.setInt(3, ticket.getMaGhe());
            ps.setBigDecimal(4, ticket.getGiaVe());
            ps.setString(5, ticket.getTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Tạo ticket hàng loạt cho một suất chiếu
     */
    public boolean bulkCreateTicketsForShowtime(int maSuatChieu, List<Ticket> tickets) {
        String sql = "INSERT INTO dbo.Ticket (MaOrder, MaSuatChieu, MaGhe, DonGia, TrangThai, CreatedAt) " +
                    "VALUES (NULL, ?, ?, ?, ?, SYSUTCDATETIME())";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Ticket ticket : tickets) {
                ps.setInt(1, ticket.getMaSuatChieu());
                ps.setInt(2, ticket.getMaGhe());
                ps.setBigDecimal(3, ticket.getGiaVe());
                ps.setString(4, ticket.getTrangThai());
                ps.addBatch();
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Lấy danh sách ticket theo suất chiếu
     */
    public List<Ticket> getTicketsByShowtime(int maSuatChieu) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Ticket WHERE MaSuatChieu = ? ORDER BY MaGhe";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRowToTicket(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    
    /**
     * Lấy danh sách ghế đã thanh toán (đã khóa) theo suất chiếu
     */
    public List<Integer> getPaidSeatIdsByShowtime(int maSuatChieu) {
        List<Integer> seatIds = new ArrayList<>();
        String sql = "SELECT MaGhe FROM dbo.Ticket WHERE MaSuatChieu = ? AND TrangThai = N'Đã thanh toán'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seatIds.add(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatIds;
    }
    
    /**
     * Lấy danh sách ticket đã đặt theo suất chiếu
     */
    public List<Ticket> getBookedTicketsByShowtime(int maSuatChieu) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Ticket WHERE MaSuatChieu = ? AND TrangThai IN ('Đã đặt', 'Hold') ORDER BY MaGhe";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(mapRowToTicket(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }
    
    /**
     * Cập nhật trạng thái ticket
     */
    public boolean updateTicketStatus(int maVe, String trangThai) {
        String sql = "UPDATE dbo.Ticket SET TrangThai = ?, CreatedAt = CreatedAt WHERE MaVe = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maVe);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa tất cả ticket của một suất chiếu
     */
    public boolean deleteTicketsByShowtime(int maSuatChieu) {
        String sql = "DELETE FROM dbo.Ticket WHERE MaSuatChieu = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Kiểm tra xem ticket đã tồn tại chưa
     */
    public boolean ticketExists(int maSuatChieu, int maGhe) {
        String sql = "SELECT COUNT(*) FROM dbo.Ticket WHERE MaSuatChieu = ? AND MaGhe = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            ps.setInt(2, maGhe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map ResultSet to Ticket object
     */
    private Ticket mapRowToTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setMaVe(rs.getInt("MaVe"));
        ticket.setMaSuatChieu(rs.getInt("MaSuatChieu"));
        ticket.setMaGhe(rs.getInt("MaGhe"));
        ticket.setGiaVe(rs.getBigDecimal("DonGia"));
        ticket.setTrangThai(rs.getString("TrangThai"));
        ticket.setGhiChu(null);
        return ticket;
    }
    
    public boolean hasBookedTickets(int maSuatChieu) {
        String sql = "SELECT COUNT(*) FROM dbo.Ticket WHERE MaSuatChieu = ? AND TrangThai = 'Đã đặt'";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Ticket> getTicketsByOrder(int maOrder) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Ticket WHERE MaOrder = ? ORDER BY MaGhe";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maOrder);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ticket t = new Ticket();
                    t.setMaVe(rs.getInt("MaVe"));
                    t.setMaSuatChieu(rs.getInt("MaSuatChieu"));
                    t.setMaGhe(rs.getInt("MaGhe"));
                    t.setGiaVe(rs.getBigDecimal("DonGia"));
                    t.setTrangThai(rs.getString("TrangThai"));
                    tickets.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public boolean updateTicketsStatusByOrder(int maOrder, String trangThai) {
        String sql = "UPDATE dbo.Ticket SET TrangThai = ? WHERE MaOrder = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, maOrder);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class AdminTicketRow {
        private int maOrder;
        private int maSuatChieu;
        private int soVe;
        private String trangThai;
        private java.math.BigDecimal tongTienVe; // giữ lại nếu cần
        private java.math.BigDecimal orderTotal;
        private String movieTitle;
        private String showDate;
        private String startTime;
        private String endTime;
        private String cinemaName;
        private String roomName;
        private int comboCount;
        private java.math.BigDecimal comboTotal;

        public int getMaOrder() { return maOrder; }
        public int getMaSuatChieu() { return maSuatChieu; }
        public int getSoVe() { return soVe; }
        public String getTrangThai() { return trangThai; }
        public java.math.BigDecimal getTongTienVe() { return tongTienVe; }
        public java.math.BigDecimal getOrderTotal() { return orderTotal; }
        public String getMovieTitle() { return movieTitle; }
        public String getShowDate() { return showDate; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public String getCinemaName() { return cinemaName; }
        public String getRoomName() { return roomName; }
        public int getComboCount() { return comboCount; }
        public java.math.BigDecimal getComboTotal() { return comboTotal; }

        public void setOrderTotal(java.math.BigDecimal v) { this.orderTotal = v; }
        public void setMovieTitle(String v) { this.movieTitle = v; }
        public void setShowDate(String v) { this.showDate = v; }
        public void setStartTime(String v) { this.startTime = v; }
        public void setEndTime(String v) { this.endTime = v; }
        public void setCinemaName(String v) { this.cinemaName = v; }
        public void setRoomName(String v) { this.roomName = v; }
        public void setComboCount(int v) { this.comboCount = v; }
        public void setComboTotal(java.math.BigDecimal v) { this.comboTotal = v; }
    }

    public List<AdminTicketRow> getAdminTicketSummary(String maOrderFilter, String statusFilter) {
        List<AdminTicketRow> rows = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT t.MaOrder, MIN(t.MaSuatChieu) AS MaSuatChieu, COUNT(*) AS SoVe, ")
          .append("MIN(t.TrangThai) AS TrangThai, SUM(t.DonGia) AS TongTienVe ")
          .append("FROM dbo.Ticket t WHERE 1=1 ");
        if (maOrderFilter != null && !maOrderFilter.trim().isEmpty()) {
            sb.append("AND t.MaOrder = ? ");
        }
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            sb.append("AND t.TrangThai = ? ");
        }
        sb.append("GROUP BY t.MaOrder ORDER BY t.MaOrder DESC");

        try (PreparedStatement ps = connection.prepareStatement(sb.toString())) {
            int idx = 1;
            if (maOrderFilter != null && !maOrderFilter.trim().isEmpty()) {
                ps.setInt(idx++, Integer.parseInt(maOrderFilter.trim()));
            }
            if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                ps.setString(idx++, statusFilter);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AdminTicketRow r = new AdminTicketRow();
                    r.maOrder = rs.getInt("MaOrder");
                    r.maSuatChieu = rs.getInt("MaSuatChieu");
                    r.soVe = rs.getInt("SoVe");
                    r.trangThai = rs.getString("TrangThai");
                    r.tongTienVe = rs.getBigDecimal("TongTienVe");
                    rows.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }

    public static class GroupedTicketsRow {
        private java.time.LocalDate ngayChieu;
        private String ngayChieuStr;
        private String tenPhim;
        private int maSuatChieu;
        private String gioBatDau;
        private String gioKetThuc;
        private java.time.LocalDateTime gioBatDauDateTime;
        private int soVe;
        private String trangThai;
        private int maOrder;

        public LocalDate getNgayChieu() { return ngayChieu; }
        public String getNgayChieuStr() { return ngayChieuStr; }
        public String getTenPhim() { return tenPhim; }
        public int getMaSuatChieu() { return maSuatChieu; }
        public String getGioBatDau() { return gioBatDau; }
        public String getGioKetThuc() { return gioKetThuc; }
        public java.time.LocalDateTime getGioBatDauDateTime() { return gioBatDauDateTime; }
        public int getSoVe() { return soVe; }
        public String getTrangThai() { return trangThai; }
        public int getMaOrder() { return maOrder; }
    }

    public List<GroupedTicketsRow> getGroupedTickets(String statusFilter, boolean onlyFromToday, boolean onlySuccessByDefault, 
                                                     Integer maPhimFilter, Integer maRapFilter, String ngayFilter) {
        List<GroupedTicketsRow> rows = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT s.NgayChieu, m.TenPhim, t.MaSuatChieu, ")
          .append("FORMAT(s.GioBatDau, 'HH:mm') AS GioBD, FORMAT(s.GioKetThuc, 'HH:mm') AS GioKT, ")
          .append("CONVERT(VARCHAR(23), s.GioBatDau, 120) AS GioBatDauDT, ")
          .append("COUNT(*) AS SoVe, MIN(t.TrangThai) AS TrangThai, t.MaOrder ")
          .append("FROM dbo.Ticket t ")
          .append("JOIN dbo.Showtime s ON s.MaSuatChieu = t.MaSuatChieu ")
          .append("JOIN dbo.Movie m ON m.MaPhim = s.MaPhim ")
          .append("JOIN dbo.Room r ON r.MaPhong = s.MaPhong ")
          .append("WHERE 1=1 ");
        if (onlyFromToday) {
            sb.append("AND CAST(s.NgayChieu AS DATE) >= CAST(GETDATE() AS DATE) ");
        }
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            sb.append("AND t.TrangThai = ? ");
        } else if (onlySuccessByDefault) {
            sb.append("AND t.TrangThai IN (N'Đã thanh toán', N'Đã sử dụng') ");
        }
        if (maPhimFilter != null) {
            sb.append("AND s.MaPhim = ? ");
        }
        if (maRapFilter != null) {
            sb.append("AND r.MaRap = ? ");
        }
        if (ngayFilter != null && !ngayFilter.trim().isEmpty()) {
            sb.append("AND CAST(s.NgayChieu AS DATE) = ? ");
        }
        sb.append("GROUP BY s.NgayChieu, m.TenPhim, t.MaSuatChieu, FORMAT(s.GioBatDau, 'HH:mm'), FORMAT(s.GioKetThuc, 'HH:mm'), CONVERT(VARCHAR(23), s.GioBatDau, 120), t.MaOrder ")
          .append("ORDER BY s.NgayChieu ASC, m.TenPhim ASC, GioBD ASC");

        String sql = sb.toString();
        System.out.println("[TicketDAO.getGroupedTickets] SQL=" + sql + ", statusFilter=" + statusFilter + ", onlyFromToday=" + onlyFromToday + ", onlySuccessByDefault=" + onlySuccessByDefault);
        
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int idx = 1;
            if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                ps.setString(idx++, statusFilter);
            }
            if (maPhimFilter != null) {
                ps.setInt(idx++, maPhimFilter);
            }
            if (maRapFilter != null) {
                ps.setInt(idx++, maRapFilter);
            }
            if (ngayFilter != null && !ngayFilter.trim().isEmpty()) {
                ps.setString(idx++, ngayFilter);
            }
            try (ResultSet rs = ps.executeQuery()) {
                int cnt = 0;
                while (rs.next()) {
                    GroupedTicketsRow r = new GroupedTicketsRow();
                    r.ngayChieu = rs.getDate("NgayChieu").toLocalDate();
                    r.ngayChieuStr = r.ngayChieu != null ? r.ngayChieu.format(df) : "";
                    r.tenPhim = rs.getString("TenPhim");
                    r.maSuatChieu = rs.getInt("MaSuatChieu");
                    r.gioBatDau = rs.getString("GioBD");
                    r.gioKetThuc = rs.getString("GioKT");
                    // Parse GioBatDau từ string thành LocalDateTime
                    String gioBatDauStr = rs.getString("GioBatDauDT");
                    if (gioBatDauStr != null && !gioBatDauStr.trim().isEmpty()) {
                        try {
                            gioBatDauStr = gioBatDauStr.trim();
                            if (gioBatDauStr.length() > 19) {
                                gioBatDauStr = gioBatDauStr.substring(0, 19);
                            }
                            gioBatDauStr = gioBatDauStr.replace(" ", "T");
                            r.gioBatDauDateTime = LocalDateTime.parse(gioBatDauStr);
                        } catch (Exception e) {
                            System.err.println("[TicketDAO.getGroupedTickets] Error parsing GioBatDau: " + gioBatDauStr + " - " + e.getMessage());
                            r.gioBatDauDateTime = null;
                        }
                    }
                    r.soVe = rs.getInt("SoVe");
                    r.trangThai = rs.getString("TrangThai");
                    r.maOrder = rs.getInt("MaOrder");
                    rows.add(r);
                    cnt++;
                }
                System.out.println("[TicketDAO.getGroupedTickets] rows=" + cnt);
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO.getGroupedTickets] ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return rows;
    }

    public List<Integer> getUsedSeatIdsByShowtime(int maSuatChieu) {
        List<Integer> seatIds = new ArrayList<>();
        String sql = "SELECT MaGhe FROM dbo.Ticket WHERE MaSuatChieu = ? AND TrangThai = N'Đã sử dụng'";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, maSuatChieu);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    seatIds.add(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seatIds;
    }
}
