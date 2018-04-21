package bookshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetConnection {
    private static final String url = "jdbc:mysql://localhost:3306/bookshop?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "RAPtor1234";

    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
