package controller;

import dal.StatisticsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin-dashboard"})
public class AdminDashboardServlet extends HttpServlet {
    
    private StatisticsDAO statisticsDAO;
    
    @Override
    public void init() throws ServletException {
        statisticsDAO = new StatisticsDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("getStatistics".equals(action)) {
            // API endpoint for getting statistics
            handleGetStatistics(request, response);
            return;
        }
        
        // Default: show dashboard page
        showDashboard(request, response);
    }
    
    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("[AdminDashboardServlet] showDashboard() - Loading dashboard data...");
        
        // Get today's statistics
        System.out.println("[AdminDashboardServlet] Fetching today's statistics...");
        Map<String, Object> todayStats = statisticsDAO.getTodayStatistics();
        System.out.println("[AdminDashboardServlet] Today stats: " + todayStats);
        request.setAttribute("todayStats", todayStats);
        
        // Get this week's statistics
        System.out.println("[AdminDashboardServlet] Fetching this week's statistics...");
        Map<String, Object> weekStats = statisticsDAO.getThisWeekStatistics();
        System.out.println("[AdminDashboardServlet] Week stats: " + weekStats);
        request.setAttribute("weekStats", weekStats);
        
        // Get this month's statistics
        System.out.println("[AdminDashboardServlet] Fetching this month's statistics...");
        Map<String, Object> monthStats = statisticsDAO.getThisMonthStatistics();
        System.out.println("[AdminDashboardServlet] Month stats: " + monthStats);
        request.setAttribute("monthStats", monthStats);
        
        // Get this quarter's statistics
        System.out.println("[AdminDashboardServlet] Fetching this quarter's statistics...");
        Map<String, Object> quarterStats = statisticsDAO.getThisQuarterStatistics();
        System.out.println("[AdminDashboardServlet] Quarter stats: " + quarterStats);
        request.setAttribute("quarterStats", quarterStats);
        
        // Get default chart data (last 7 days - order count by day)
        System.out.println("[AdminDashboardServlet] Fetching daily order count data (7 days)...");
        List<Map<String, Object>> chartData = statisticsDAO.getDailyOrderCount(7);
        System.out.println("[AdminDashboardServlet] Chart data size: " + (chartData != null ? chartData.size() : 0));
        request.setAttribute("chartData", chartData);
        request.setAttribute("chartType", "daily");
        
        System.out.println("[AdminDashboardServlet] Forwarding to dashboard.jsp...");
        request.getRequestDispatcher("Views/admin/dashboard.jsp").forward(request, response);
    }
    
    private void handleGetStatistics(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String period = request.getParameter("period"); // daily, weekly, monthly, quarterly
        String days = request.getParameter("days");
        String months = request.getParameter("months");
        String quarters = request.getParameter("quarters");
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> chartData = null;
        Map<String, Object> summaryStats = null;
        
        System.out.println("[AdminDashboardServlet] handleGetStatistics() - period: " + period + 
                         ", days: " + days + ", months: " + months + ", quarters: " + quarters);
        
        try {
            if ("dateRange".equals(period)) {
                // Stats by date range
                String startDateStr = request.getParameter("startDate");
                String endDateStr = request.getParameter("endDate");
                
                if (startDateStr != null && endDateStr != null) {
                    LocalDate startDate = LocalDate.parse(startDateStr);
                    LocalDate endDate = LocalDate.parse(endDateStr);
                    System.out.println("[AdminDashboardServlet] Processing dateRange from " + startDate + " to " + endDate);
                    summaryStats = statisticsDAO.getRevenueStatistics(startDate, endDate);
                    chartData = null; // No chart for date range
                }
                
            } else if ("daily".equals(period)) {
                // Chart: order count by day (always 7 days)
                int daysCount = 7;
                System.out.println("[AdminDashboardServlet] Processing daily period - always 7 days");
                chartData = statisticsDAO.getDailyOrderCount(daysCount);
                
                // Summary: revenue stats for the period
                LocalDate endDate = LocalDate.now();
                LocalDate startDate = endDate.minusDays(6);
                System.out.println("[AdminDashboardServlet] Calculating summary from " + startDate + " to " + endDate);
                summaryStats = statisticsDAO.getRevenueStatistics(startDate, endDate);
                
            } else if ("weekly".equals(period)) {
                // Chart: order count by week (always 7 weeks)
                int daysCount = 49; // 7 weeks
                System.out.println("[AdminDashboardServlet] Processing weekly period - always 7 weeks");
                chartData = statisticsDAO.getWeeklyOrderCount(daysCount);
                
                // Summary: revenue stats for the period
                LocalDate endDate = LocalDate.now();
                LocalDate startDate = endDate.minusDays(48);
                System.out.println("[AdminDashboardServlet] Calculating summary from " + startDate + " to " + endDate);
                summaryStats = statisticsDAO.getRevenueStatistics(startDate, endDate);
                
            } else if ("monthly".equals(period)) {
                // Chart: order count by month (always 7 months)
                int monthsCount = 7;
                System.out.println("[AdminDashboardServlet] Processing monthly period - always 7 months");
                chartData = statisticsDAO.getMonthlyOrderCount(monthsCount);
                
                // Calculate summary for the period
                LocalDate endDate = LocalDate.now();
                LocalDate startDate = endDate.minusMonths(6).withDayOfMonth(1);
                System.out.println("[AdminDashboardServlet] Calculating summary from " + startDate + " to " + endDate);
                summaryStats = statisticsDAO.getRevenueStatistics(startDate, endDate);
            }
            
            System.out.println("[AdminDashboardServlet] Chart data size: " + (chartData != null ? chartData.size() : 0));
            System.out.println("[AdminDashboardServlet] Summary stats: " + summaryStats);
            
            result.put("success", true);
            result.put("chartData", chartData);
            result.put("summaryStats", summaryStats);
            result.put("period", period);
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        // Convert to JSON manually (simple approach)
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"success\": ").append(result.get("success")).append(",\n");
        
        if (result.containsKey("chartData") && result.get("chartData") != null) {
            json.append("  \"chartData\": [\n");
            List<Map<String, Object>> chartDataList = (List<Map<String, Object>>) result.get("chartData");
            for (int i = 0; i < chartDataList.size(); i++) {
                Map<String, Object> item = chartDataList.get(i);
                json.append("    {\n");
                boolean hasField = false;
                if (item.containsKey("date")) {
                    json.append("      \"date\": \"").append(item.get("date")).append("\"");
                    hasField = true;
                }
                if (item.containsKey("week")) {
                    if (hasField) json.append(",\n");
                    json.append("      \"week\": \"").append(item.get("week")).append("\"");
                    hasField = true;
                }
                if (item.containsKey("month")) {
                    if (hasField) json.append(",\n");
                    json.append("      \"month\": \"").append(item.get("month")).append("\",\n");
                    json.append("      \"orderCount\": ").append(item.get("orderCount"));
                    hasField = true;
                }
                if (item.containsKey("quarter")) {
                    if (hasField) json.append(",\n");
                    json.append("      \"quarter\": \"").append(item.get("quarter")).append("\"");
                    hasField = true;
                }
                if (item.containsKey("orderCount")) {
                    if (hasField) json.append(",\n");
                    json.append("      \"orderCount\": ").append(item.get("orderCount"));
                    hasField = true;
                }
                if (item.containsKey("ticketRevenue")) {
                    if (hasField) json.append(",\n");
                    BigDecimal ticketRev = (BigDecimal) item.get("ticketRevenue");
                    json.append("      \"ticketRevenue\": ").append(ticketRev != null ? ticketRev : "0");
                    hasField = true;
                }
                if (item.containsKey("comboRevenue")) {
                    if (hasField) json.append(",\n");
                    BigDecimal comboRev = (BigDecimal) item.get("comboRevenue");
                    json.append("      \"comboRevenue\": ").append(comboRev != null ? comboRev : "0");
                    hasField = true;
                }
                if (item.containsKey("totalRevenue")) {
                    if (hasField) json.append(",\n");
                    BigDecimal totalRev = (BigDecimal) item.get("totalRevenue");
                    json.append("      \"totalRevenue\": ").append(totalRev != null ? totalRev : "0");
                }
                json.append("\n    }");
                if (i < chartDataList.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ],\n");
        } else {
            json.append("  \"chartData\": null,\n");
        }
        
        if (result.containsKey("summaryStats")) {
            Map<String, Object> summary = (Map<String, Object>) result.get("summaryStats");
            json.append("  \"summaryStats\": {\n");
            json.append("    \"ticketRevenue\": ").append(summary.get("ticketRevenue")).append(",\n");
            json.append("    \"comboRevenue\": ").append(summary.get("comboRevenue")).append(",\n");
            json.append("    \"totalRevenue\": ").append(summary.get("totalRevenue")).append(",\n");
            json.append("    \"orderCount\": ").append(summary.get("orderCount")).append("\n");
            json.append("  },\n");
        }
        
        json.append("  \"period\": \"").append(result.get("period")).append("\"\n");
        json.append("}");
        
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
