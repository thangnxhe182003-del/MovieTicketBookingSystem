<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="dal.DBContext" %>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Test Database Connection</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .success { color: green; }
        .error { color: red; }
        .info { color: blue; }
        pre { background: #f5f5f5; padding: 10px; border-radius: 5px; }
    </style>
</head>
<body>
    <h1>Database Connection Test</h1>
    
    <%
        try {
            DBContext dbContext = new DBContext();
            Connection conn = dbContext.connection;
            
            if (conn != null && !conn.isClosed()) {
                out.println("<p class='success'>✅ Kết nối database thành công!</p>");
                
                // Test query
                try {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM dbo.Customer");
                    if (rs.next()) {
                        out.println("<p class='info'>📊 Số lượng khách hàng: " + rs.getInt("count") + "</p>");
                    }
                    
                    // Test tables
                    DatabaseMetaData metaData = conn.getMetaData();
                    ResultSet tables = metaData.getTables(null, "dbo", "%", new String[]{"TABLE"});
                    
                    out.println("<h3>📋 Danh sách bảng trong database:</h3>");
                    out.println("<ul>");
                    while (tables.next()) {
                        String tableName = tables.getString("TABLE_NAME");
                        out.println("<li>" + tableName + "</li>");
                    }
                    out.println("</ul>");
                    
                } catch (SQLException e) {
                    out.println("<p class='error'>❌ Lỗi khi thực hiện query: " + e.getMessage() + "</p>");
                }
                
                conn.close();
            } else {
                out.println("<p class='error'>❌ Không thể kết nối database!</p>");
            }
        } catch (Exception e) {
            out.println("<p class='error'>❌ Lỗi: " + e.getMessage() + "</p>");
            out.println("<pre>" + e.toString() + "</pre>");
        }
    %>
    
    <h3>🔧 Thông tin kết nối:</h3>
    <ul>
        <li>Server: localhost:1433</li>
        <li>Database: MovieTicketBookingSystem</li>
        <li>Driver: com.microsoft.sqlserver.jdbc.SQLServerDriver</li>
    </ul>
</body>
</html>
