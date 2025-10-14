import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlserver://KIDMARS\\SQLEXPRESS;databaseName=HospitalDB;encrypt=false;trustServerCertificate=true;";
    private static final String USER = "hospital_user";
    private static final String PASSWORD = "Hospital@123";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connected to SQL Server successfully.");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to SQL Server:");
            e.printStackTrace();
            return null;
        }
    }

    // Quick test entry point
    public static void main(String[] args) {
        connect();
    }
}
