package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    private static final String SERVER_NAME = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE_NAME = "BanVePhim";
    private static final String USER_ID = "sa";
    private static final String PASSWORD = "123456"; // Đảm bảo khớp với password cài đặt

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        String url = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT + 
                     ";databaseName=" + DATABASE_NAME + 
                     ";encrypt=true;trustServerCertificate=true";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(url, USER_ID, PASSWORD);
            if (conn == null) {
                throw new SQLException("Connection is null - check configuration");
            }
            System.out.println("Connection successful to " + DATABASE_NAME + " at " + new java.util.Date());
            return conn;
        } catch (SQLException e) {
            System.err.println("SQL Connection Error: " + e.getMessage() + " (Check server name, port, user, password, or firewall)");
            throw e;
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver Missing: " + e.getMessage() + " (Add mssql-jdbc.jar to WEB-INF/lib)");
            throw e;
        }
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        DBContext db = new DBContext();
        try {
            Connection conn = db.getConnection();
            if (conn != null) {
                System.out.println("Kết nối thành công!");
                db.closeConnection(conn);
            }
        } catch (Exception e) {
            System.err.println("Kết nối thất bại: " + e.getMessage());
            e.printStackTrace();
        }
    }
}