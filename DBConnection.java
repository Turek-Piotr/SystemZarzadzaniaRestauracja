import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection {

        private static final String URL = "jdbc:mysql://localhost:3306/restauracja";
        private static final String USER = "root";
        private static final String PASSWORD = "";
        private static Connection connection;

        // Statyczna metoda do uzyskiwania połączenia
        public static Connection getConnection() {
            if (connection == null) { // Połączenie będzie tworzone tylko raz
                try {
                    // Rejestracja sterownika JDBC
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    // Nawiązanie połączenia z bazą danych
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println("Połączono z bazą danych.");
                } catch (ClassNotFoundException e) {
                    System.err.println("Nie znaleziono sterownika JDBC: " + e.getMessage());
                } catch (SQLException e) {
                    System.err.println("Błąd podczas łączenia z bazą danych: " + e.getMessage());
                }
            }
            return connection;
        }

        // Statyczna metoda do zamykania połączenia
        public static void closeConnection() {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Połączenie z bazą danych zostało zamknięte.");
                } catch (SQLException e) {
                    System.err.println("Błąd podczas zamykania połączenia z bazą danych: " + e.getMessage());
                }
            }
        }
}
