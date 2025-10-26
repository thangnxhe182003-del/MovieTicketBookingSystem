package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {

    public Connection connection;

    public DBContext() {
        try {
            String username = "sa";
            String password = "123";
            String url = "jdbc:sqlserver://localhost:1433;databaseName=BanVePhim";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) throws Exception {
        DBContext dbContext = new DBContext();

        try {
            Connection connection = dbContext.connection;
            if (connection != null) {
                System.out.println("Connected to database successfully.");
                // Do other operations if needed
                connection.close(); // Close the connection when done
            } else {
                System.out.println("Failed to connect to database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
