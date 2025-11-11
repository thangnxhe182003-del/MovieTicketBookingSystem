package dal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for statistics and revenue reports
 */
public class StatisticsDAO extends DBContext {
    
    /**
     * Get revenue statistics by date range
     * @param startDate Start date (inclusive)
     * @param endDate End date (inclusive)
     * @return Map with ticketRevenue, comboRevenue, totalRevenue, orderCount
     */
    public Map<String, Object> getRevenueStatistics(LocalDate startDate, LocalDate endDate) {
        System.out.println("[StatisticsDAO] getRevenueStatistics() - startDate: " + startDate + ", endDate: " + endDate);
        Map<String, Object> stats = new HashMap<>();
        String sql = "SELECT " +
                    "    ISNULL(SUM(t.DonGia), 0) as TicketRevenue, " +
                    "    ISNULL(SUM(ofd.ThanhTien), 0) as ComboRevenue, " +
                    "    ISNULL(SUM(o.TongTien), 0) as TotalRevenue, " +
                    "    COUNT(DISTINCT o.MaOrder) as OrderCount " +
                    "FROM dbo.[Order] o " +
                    "LEFT JOIN dbo.Ticket t ON t.MaOrder = o.MaOrder " +
                    "LEFT JOIN dbo.OrderFoodDetail ofd ON ofd.MaOrder = o.MaOrder " +
                    "WHERE o.TrangThai = ? " +
                    "    AND CAST(o.PaidAt AS DATE) >= ? " +
                    "    AND CAST(o.PaidAt AS DATE) <= ?";
        
        System.out.println("[StatisticsDAO] SQL Query: " + sql);
        System.out.println("[StatisticsDAO] Parameters: trangThai=Đã thanh toán, startDate=" + startDate.toString() + ", endDate=" + endDate.toString());
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Đã thanh toán");
            ps.setString(2, startDate.toString());
            ps.setString(3, endDate.toString());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BigDecimal ticketRevenue = rs.getBigDecimal("TicketRevenue");
                    BigDecimal comboRevenue = rs.getBigDecimal("ComboRevenue");
                    BigDecimal totalRevenue = rs.getBigDecimal("TotalRevenue");
                    int orderCount = rs.getInt("OrderCount");
                    
                    System.out.println("[StatisticsDAO] Result from DB - TicketRevenue: " + ticketRevenue + 
                                     ", ComboRevenue: " + comboRevenue + 
                                     ", TotalRevenue: " + totalRevenue + 
                                     ", OrderCount: " + orderCount);
                    
                    stats.put("ticketRevenue", ticketRevenue);
                    stats.put("comboRevenue", comboRevenue);
                    stats.put("totalRevenue", totalRevenue);
                    stats.put("orderCount", orderCount);
                } else {
                    System.out.println("[StatisticsDAO] No data found in result set, returning zeros");
                    stats.put("ticketRevenue", BigDecimal.ZERO);
                    stats.put("comboRevenue", BigDecimal.ZERO);
                    stats.put("totalRevenue", BigDecimal.ZERO);
                    stats.put("orderCount", 0);
                }
            }
        } catch (SQLException e) {
            System.out.println("[StatisticsDAO] SQLException in getRevenueStatistics: " + e.getMessage());
            e.printStackTrace();
            stats.put("ticketRevenue", BigDecimal.ZERO);
            stats.put("comboRevenue", BigDecimal.ZERO);
            stats.put("totalRevenue", BigDecimal.ZERO);
            stats.put("orderCount", 0);
        }
        
        System.out.println("[StatisticsDAO] Returning stats: " + stats);
        return stats;
    }
    
    /**
     * Get daily order count for chart (last N days) - always returns 7 days with 0 for missing dates
     * @param days Number of days to look back (should be 7)
     * @return List of maps with date, orderCount (always 7 items)
     */
    public List<Map<String, Object>> getDailyOrderCount(int days) {
        System.out.println("[StatisticsDAO] getDailyOrderCount() - days: " + days);
        List<Map<String, Object>> data = new ArrayList<>();
        String sql = "SELECT " +
                    "    CAST(o.PaidAt AS DATE) as Date, " +
                    "    COUNT(DISTINCT o.MaOrder) as OrderCount " +
                    "FROM dbo.[Order] o " +
                    "WHERE o.TrangThai = ? " +
                    "    AND o.PaidAt >= DATEADD(DAY, -?, GETDATE()) " +
                    "GROUP BY CAST(o.PaidAt AS DATE) " +
                    "ORDER BY Date ASC";
        
        System.out.println("[StatisticsDAO] SQL Query: " + sql);
        System.out.println("[StatisticsDAO] Parameters: trangThai=Đã thanh toán, days=" + days);
        
        // Create a map to store results by date
        Map<String, Integer> orderCountMap = new HashMap<>();
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Đã thanh toán");
            ps.setInt(2, days);
            
            try (ResultSet rs = ps.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    rowCount++;
                    java.sql.Date dateValue = rs.getDate("Date");
                    int orderCount = rs.getInt("OrderCount");
                    
                    if (dateValue != null) {
                        String dateStr = dateValue.toLocalDate().toString();
                        orderCountMap.put(dateStr, orderCount);
                        System.out.println("[StatisticsDAO] Row " + rowCount + " - Date: " + dateStr + 
                                         ", OrderCount: " + orderCount);
                    }
                }
                System.out.println("[StatisticsDAO] Total rows returned: " + rowCount);
            }
        } catch (SQLException e) {
            System.out.println("[StatisticsDAO] SQLException in getDailyOrderCount: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Generate all 7 days, filling missing dates with 0
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String dateStr = date.toString();
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", dateStr);
            dayData.put("orderCount", orderCountMap.getOrDefault(dateStr, 0));
            data.add(dayData);
        }
        
        System.out.println("[StatisticsDAO] Returning " + data.size() + " daily order count records (filled to 7 days)");
        return data;
    }
    
    /**
     * Get daily revenue for chart (last N days)
     * @param days Number of days
     * @return List of maps with date, ticketRevenue, comboRevenue, totalRevenue
     */
    public List<Map<String, Object>> getDailyRevenue(int days) {
        System.out.println("[StatisticsDAO] getDailyRevenue() - days: " + days);
        List<Map<String, Object>> data = new ArrayList<>();
        String sql = "SELECT " +
                    "    CAST(o.PaidAt AS DATE) as Date, " +
                    "    ISNULL(SUM(t.DonGia), 0) as TicketRevenue, " +
                    "    ISNULL(SUM(ofd.ThanhTien), 0) as ComboRevenue, " +
                    "    ISNULL(SUM(o.TongTien), 0) as TotalRevenue " +
                    "FROM dbo.[Order] o " +
                    "LEFT JOIN dbo.Ticket t ON t.MaOrder = o.MaOrder " +
                    "LEFT JOIN dbo.OrderFoodDetail ofd ON ofd.MaOrder = o.MaOrder " +
                    "WHERE o.TrangThai = ? " +
                    "    AND o.PaidAt >= DATEADD(DAY, -?, GETDATE()) " +
                    "GROUP BY CAST(o.PaidAt AS DATE) " +
                    "ORDER BY Date ASC";
        
        System.out.println("[StatisticsDAO] SQL Query: " + sql);
        System.out.println("[StatisticsDAO] Parameters: trangThai=Đã thanh toán, days=" + days);
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Đã thanh toán");
            ps.setInt(2, days);
            
            try (ResultSet rs = ps.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    rowCount++;
                    Map<String, Object> dayData = new HashMap<>();
                    java.sql.Date dateValue = rs.getDate("Date");
                    BigDecimal ticketRevenue = rs.getBigDecimal("TicketRevenue");
                    BigDecimal comboRevenue = rs.getBigDecimal("ComboRevenue");
                    BigDecimal totalRevenue = rs.getBigDecimal("TotalRevenue");
                    
                    if (dateValue != null) {
                        dayData.put("date", dateValue.toLocalDate().toString());
                    } else {
                        dayData.put("date", "");
                    }
                    dayData.put("ticketRevenue", ticketRevenue);
                    dayData.put("comboRevenue", comboRevenue);
                    dayData.put("totalRevenue", totalRevenue);
                    data.add(dayData);
                    
                    System.out.println("[StatisticsDAO] Row " + rowCount + " - Date: " + dayData.get("date") + 
                                     ", TicketRevenue: " + ticketRevenue + 
                                     ", ComboRevenue: " + comboRevenue + 
                                     ", TotalRevenue: " + totalRevenue);
                }
                System.out.println("[StatisticsDAO] Total rows returned: " + rowCount);
            }
        } catch (SQLException e) {
            System.out.println("[StatisticsDAO] SQLException in getDailyRevenue: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("[StatisticsDAO] Returning " + data.size() + " daily revenue records");
        return data;
    }
    
    /**
     * Get weekly revenue for chart (group by week)
     * @param days Number of days to look back
     * @return List of maps with week, orderCount
     */
    public List<Map<String, Object>> getWeeklyOrderCount(int days) {
        System.out.println("[StatisticsDAO] getWeeklyOrderCount() - days: " + days);
        List<Map<String, Object>> data = new ArrayList<>();
        String sql = "SELECT " +
                    "    DATEPART(YEAR, o.PaidAt) as Year, " +
                    "    DATEPART(WEEK, o.PaidAt) as Week, " +
                    "    MIN(CAST(o.PaidAt AS DATE)) as WeekStart, " +
                    "    COUNT(DISTINCT o.MaOrder) as OrderCount " +
                    "FROM dbo.[Order] o " +
                    "WHERE o.TrangThai = ? " +
                    "    AND o.PaidAt >= DATEADD(DAY, -?, GETDATE()) " +
                    "GROUP BY DATEPART(YEAR, o.PaidAt), DATEPART(WEEK, o.PaidAt) " +
                    "ORDER BY Year ASC, Week ASC";
        
        System.out.println("[StatisticsDAO] SQL Query: " + sql);
        System.out.println("[StatisticsDAO] Parameters: trangThai=Đã thanh toán, days=" + days);
        
        // Create a map to store results by week key (Year-Week)
        Map<String, Integer> orderCountMap = new HashMap<>();
        Map<String, String> weekLabelMap = new HashMap<>();
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Đã thanh toán");
            ps.setInt(2, days);
            
            try (ResultSet rs = ps.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    rowCount++;
                    int year = rs.getInt("Year");
                    int week = rs.getInt("Week");
                    java.sql.Date weekStart = rs.getDate("WeekStart");
                    int orderCount = rs.getInt("OrderCount");
                    
                    String weekKey = year + "-" + week;
                    String weekLabel = "Tuần " + week + "/" + year;
                    if (weekStart != null) {
                        weekLabel = "Tuần " + week + "/" + year + " (" + weekStart.toLocalDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM")) + ")";
                    }
                    
                    orderCountMap.put(weekKey, orderCount);
                    weekLabelMap.put(weekKey, weekLabel);
                    
                    System.out.println("[StatisticsDAO] Row " + rowCount + " - Week: " + weekLabel + 
                                     ", OrderCount: " + orderCount);
                }
                System.out.println("[StatisticsDAO] Total rows returned: " + rowCount);
            }
        } catch (SQLException e) {
            System.out.println("[StatisticsDAO] SQLException in getWeeklyOrderCount: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Generate last 7 weeks, filling missing weeks with 0
        LocalDate today = LocalDate.now();
        java.time.temporal.WeekFields weekFields = java.time.temporal.WeekFields.ISO;
        
        for (int i = 6; i >= 0; i--) {
            LocalDate weekDate = today.minusWeeks(i);
            int year = weekDate.get(weekFields.weekBasedYear());
            int week = weekDate.get(weekFields.weekOfWeekBasedYear());
            String weekKey = year + "-" + week;
            
            String weekLabel = weekLabelMap.get(weekKey);
            if (weekLabel == null) {
                // Generate label for missing week
                LocalDate weekStart = weekDate.with(weekFields.dayOfWeek(), 1);
                weekLabel = "Tuần " + week + "/" + year + " (" + weekStart.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM")) + ")";
            }
            
            Map<String, Object> weekData = new HashMap<>();
            weekData.put("week", weekLabel);
            weekData.put("orderCount", orderCountMap.getOrDefault(weekKey, 0));
            data.add(weekData);
        }
        
        System.out.println("[StatisticsDAO] Returning " + data.size() + " weekly order count records (filled to 7 weeks)");
        return data;
    }
    
    /**
     * Get monthly order count for chart - always returns 7 months with 0 for missing months
     * @param months Number of months to look back (should be 7)
     * @return List of maps with month, orderCount (always 7 items)
     */
    public List<Map<String, Object>> getMonthlyOrderCount(int months) {
        System.out.println("[StatisticsDAO] getMonthlyOrderCount() - months: " + months);
        List<Map<String, Object>> data = new ArrayList<>();
        String sql = "SELECT " +
                    "    FORMAT(o.PaidAt, 'yyyy-MM') as Month, " +
                    "    COUNT(DISTINCT o.MaOrder) as OrderCount " +
                    "FROM dbo.[Order] o " +
                    "WHERE o.TrangThai = ? " +
                    "    AND o.PaidAt >= DATEADD(MONTH, -?, GETDATE()) " +
                    "GROUP BY FORMAT(o.PaidAt, 'yyyy-MM') " +
                    "ORDER BY Month ASC";
        
        System.out.println("[StatisticsDAO] SQL Query: " + sql);
        System.out.println("[StatisticsDAO] Parameters: trangThai=Đã thanh toán, months=" + months);
        
        // Create a map to store results by month
        Map<String, Integer> orderCountMap = new HashMap<>();
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Đã thanh toán");
            ps.setInt(2, months);
            
            try (ResultSet rs = ps.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    rowCount++;
                    String month = rs.getString("Month");
                    int orderCount = rs.getInt("OrderCount");
                    orderCountMap.put(month, orderCount);
                    
                    System.out.println("[StatisticsDAO] Row " + rowCount + " - Month: " + month + 
                                     ", OrderCount: " + orderCount);
                }
                System.out.println("[StatisticsDAO] Total rows returned: " + rowCount);
            }
        } catch (SQLException e) {
            System.out.println("[StatisticsDAO] SQLException in getMonthlyOrderCount: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Generate last 7 months, filling missing months with 0
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate monthDate = today.minusMonths(i);
            String month = monthDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"));
            
            Map<String, Object> monthData = new HashMap<>();
            monthData.put("month", month);
            monthData.put("orderCount", orderCountMap.getOrDefault(month, 0));
            data.add(monthData);
        }
        
        System.out.println("[StatisticsDAO] Returning " + data.size() + " monthly order count records (filled to 7 months)");
        return data;
    }
    
    /**
     * Get monthly revenue for chart
     * @param months Number of months
     * @return List of maps with month, ticketRevenue, comboRevenue, totalRevenue
     */
    public List<Map<String, Object>> getMonthlyRevenue(int months) {
        System.out.println("[StatisticsDAO] getMonthlyRevenue() - months: " + months);
        List<Map<String, Object>> data = new ArrayList<>();
        String sql = "SELECT " +
                    "    FORMAT(o.PaidAt, 'yyyy-MM') as Month, " +
                    "    ISNULL(SUM(t.DonGia), 0) as TicketRevenue, " +
                    "    ISNULL(SUM(ofd.ThanhTien), 0) as ComboRevenue, " +
                    "    ISNULL(SUM(o.TongTien), 0) as TotalRevenue " +
                    "FROM dbo.[Order] o " +
                    "LEFT JOIN dbo.Ticket t ON t.MaOrder = o.MaOrder " +
                    "LEFT JOIN dbo.OrderFoodDetail ofd ON ofd.MaOrder = o.MaOrder " +
                    "WHERE o.TrangThai = ? " +
                    "    AND o.PaidAt >= DATEADD(MONTH, -?, GETDATE()) " +
                    "GROUP BY FORMAT(o.PaidAt, 'yyyy-MM') " +
                    "ORDER BY Month ASC";
        
        System.out.println("[StatisticsDAO] SQL Query: " + sql);
        System.out.println("[StatisticsDAO] Parameters: trangThai=Đã thanh toán, months=" + months);
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Đã thanh toán");
            ps.setInt(2, months);
            
            try (ResultSet rs = ps.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    rowCount++;
                    Map<String, Object> monthData = new HashMap<>();
                    String month = rs.getString("Month");
                    BigDecimal ticketRevenue = rs.getBigDecimal("TicketRevenue");
                    BigDecimal comboRevenue = rs.getBigDecimal("ComboRevenue");
                    BigDecimal totalRevenue = rs.getBigDecimal("TotalRevenue");
                    
                    monthData.put("month", month);
                    monthData.put("ticketRevenue", ticketRevenue);
                    monthData.put("comboRevenue", comboRevenue);
                    monthData.put("totalRevenue", totalRevenue);
                    data.add(monthData);
                    
                    System.out.println("[StatisticsDAO] Row " + rowCount + " - Month: " + month + 
                                     ", TicketRevenue: " + ticketRevenue + 
                                     ", ComboRevenue: " + comboRevenue + 
                                     ", TotalRevenue: " + totalRevenue);
                }
                System.out.println("[StatisticsDAO] Total rows returned: " + rowCount);
            }
        } catch (SQLException e) {
            System.out.println("[StatisticsDAO] SQLException in getMonthlyRevenue: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("[StatisticsDAO] Returning " + data.size() + " monthly revenue records");
        return data;
    }
    
    /**
     * Get quarterly revenue for chart
     * @param quarters Number of quarters
     * @return List of maps with quarter, ticketRevenue, comboRevenue, totalRevenue
     */
    public List<Map<String, Object>> getQuarterlyRevenue(int quarters) {
        System.out.println("[StatisticsDAO] getQuarterlyRevenue() - quarters: " + quarters);
        List<Map<String, Object>> data = new ArrayList<>();
        String sql = "SELECT " +
                    "    CONCAT(YEAR(o.PaidAt), '-Q', DATEPART(QUARTER, o.PaidAt)) as Quarter, " +
                    "    ISNULL(SUM(t.DonGia), 0) as TicketRevenue, " +
                    "    ISNULL(SUM(ofd.ThanhTien), 0) as ComboRevenue, " +
                    "    ISNULL(SUM(o.TongTien), 0) as TotalRevenue " +
                    "FROM dbo.[Order] o " +
                    "LEFT JOIN dbo.Ticket t ON t.MaOrder = o.MaOrder " +
                    "LEFT JOIN dbo.OrderFoodDetail ofd ON ofd.MaOrder = o.MaOrder " +
                    "WHERE o.TrangThai = ? " +
                    "    AND o.PaidAt >= DATEADD(QUARTER, -?, GETDATE()) " +
                    "GROUP BY YEAR(o.PaidAt), DATEPART(QUARTER, o.PaidAt) " +
                    "ORDER BY YEAR(o.PaidAt), DATEPART(QUARTER, o.PaidAt) ASC";
        
        System.out.println("[StatisticsDAO] SQL Query: " + sql);
        System.out.println("[StatisticsDAO] Parameters: trangThai=Đã thanh toán, quarters=" + quarters);
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Đã thanh toán");
            ps.setInt(2, quarters);
            
            try (ResultSet rs = ps.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    rowCount++;
                    Map<String, Object> quarterData = new HashMap<>();
                    String quarter = rs.getString("Quarter");
                    BigDecimal ticketRevenue = rs.getBigDecimal("TicketRevenue");
                    BigDecimal comboRevenue = rs.getBigDecimal("ComboRevenue");
                    BigDecimal totalRevenue = rs.getBigDecimal("TotalRevenue");
                    
                    quarterData.put("quarter", quarter);
                    quarterData.put("ticketRevenue", ticketRevenue);
                    quarterData.put("comboRevenue", comboRevenue);
                    quarterData.put("totalRevenue", totalRevenue);
                    data.add(quarterData);
                    
                    System.out.println("[StatisticsDAO] Row " + rowCount + " - Quarter: " + quarter + 
                                     ", TicketRevenue: " + ticketRevenue + 
                                     ", ComboRevenue: " + comboRevenue + 
                                     ", TotalRevenue: " + totalRevenue);
                }
                System.out.println("[StatisticsDAO] Total rows returned: " + rowCount);
            }
        } catch (SQLException e) {
            System.out.println("[StatisticsDAO] SQLException in getQuarterlyRevenue: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("[StatisticsDAO] Returning " + data.size() + " quarterly revenue records");
        return data;
    }
    
    /**
     * Get today's statistics
     */
    public Map<String, Object> getTodayStatistics() {
        LocalDate today = LocalDate.now();
        System.out.println("[StatisticsDAO] getTodayStatistics() - today: " + today);
        return getRevenueStatistics(today, today);
    }
    
    /**
     * Get this week's statistics (Monday to Sunday)
     */
    public Map<String, Object> getThisWeekStatistics() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);
        System.out.println("[StatisticsDAO] getThisWeekStatistics() - startOfWeek: " + startOfWeek + ", endOfWeek: " + endOfWeek);
        return getRevenueStatistics(startOfWeek, endOfWeek);
    }
    
    /**
     * Get this month's statistics
     */
    public Map<String, Object> getThisMonthStatistics() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        System.out.println("[StatisticsDAO] getThisMonthStatistics() - startOfMonth: " + startOfMonth + ", endOfMonth: " + endOfMonth);
        return getRevenueStatistics(startOfMonth, endOfMonth);
    }
    
    /**
     * Get this quarter's statistics
     */
    public Map<String, Object> getThisQuarterStatistics() {
        LocalDate today = LocalDate.now();
        int quarter = (today.getMonthValue() - 1) / 3;
        LocalDate startOfQuarter = today.withMonth(quarter * 3 + 1).withDayOfMonth(1);
        LocalDate endOfQuarter = startOfQuarter.plusMonths(2).withDayOfMonth(startOfQuarter.plusMonths(2).lengthOfMonth());
        System.out.println("[StatisticsDAO] getThisQuarterStatistics() - startOfQuarter: " + startOfQuarter + ", endOfQuarter: " + endOfQuarter + ", quarter: " + (quarter + 1));
        return getRevenueStatistics(startOfQuarter, endOfQuarter);
    }
}

