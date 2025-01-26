import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class ReservationService {

    private final Scanner scanner;
    private final Restaurant restaurant;

    public ReservationService(Scanner scanner, Restaurant restaurant) {
        this.scanner = scanner;
        this.restaurant = restaurant;
    }

    // Metoda do rezerwacji stolika na podstawie jego numeru
    public void reserveTableById() {
        // Nawiązywanie połączenia z bazą danych
        Connection connection = DBConnection.getConnection();

        if (connection == null) {
            System.out.println("Brak połączenia z bazą danych.");
            return;
        }

        String sql = "SELECT * FROM stolik"; // Zapytanie do pobrania stolików

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (!resultSet.isBeforeFirst()) { // Sprawdza, czy są dostępne stoliki
                System.out.println("Brak stolików w systemie.");
                return;
            }

            // Wyświetl dostępne stoliki
            System.out.println("Dostępne stoliki:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int seats = resultSet.getInt("liczba_miejsc");
                String status = resultSet.getString("status");

                System.out.println("Stolik " + id + ":");
                System.out.println("Numer stolika: " + id);
                System.out.println("Liczba miejsc: " + seats);
                System.out.println("Status: " + status);
                System.out.println("------------------------");
            }

            System.out.print("Podaj numer stolika do rezerwacji: ");
            int tableId = scanner.nextInt();

            // Zmiana statusu na Zarezerwowany
            String updateSql = "UPDATE stolik SET status = ? WHERE id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                updateStatement.setString(1, "Zarezerwowany");
                updateStatement.setInt(2, tableId);

                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Stolik o numerze " + tableId + " został zarezerwowany.");
                } else {
                    System.out.println("Nie znaleziono stolika o podanym numerze.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas rezerwacji stolika: " + e.getMessage());
        }
    }
}
