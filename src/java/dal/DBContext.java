package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    private static final String SERVER_NAME = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE_NAME = "banvephim6";
    private static final String USER_ID = "sa";
    private static final String PASSWORD = "123456"; // Thay bằng mật khẩu thực tế

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT + 
                        ";databaseName=" + DATABASE_NAME + 
                        ";encrypt=true;trustServerCertificate=true";
            Connection conn = DriverManager.getConnection(url, USER_ID, PASSWORD);
            System.out.println("Kết nối SQL Server thành công: " + url);
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("Lỗi: Driver JDBC không tìm thấy - " + e.getMessage());
            throw new SQLException("Driver JDBC không khả dụng", e);
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối SQL Server: " + e.getMessage());
            throw e;
        }
    }
}