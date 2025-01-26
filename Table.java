import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Table {
    private int number;
    private int seats;
    private String status;

    // Konstruktor klasy Table
    public Table(int number, int seats, String status) {
        this.number = number;
        this.seats = seats;
        this.status = status;
    }

    // Gettery i settery dla każdego z pól

    public int getNumber() {
        return number;
    }

    public int getSeats() {
        return seats;
    }

    public String getStatus() {
        return status;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Metoda do zapisania stolika do bazy danych
    public void saveToDatabase() {
        // Nawiązywanie połączenia z bazą danych
        Connection connection = DBConnection.getConnection();
        if (connection == null) {
            System.out.println("Brak połączenia z bazą danych.");
            return;
        }
        // Zapytanie SQL do dodania stolika do tabeli "stolik"
        String sql = "INSERT INTO stolik (id, liczba_miejsc, status) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Ustawianie parametrów zapytania SQL
            statement.setInt(1, this.number);
            statement.setInt(2, this.seats);
            statement.setString(3, this.status);
            // Wykonanie zapytania SQL
            statement.executeUpdate();
            System.out.println("Stolik został dodany do bazy danych.");
        } catch (SQLException e) {
            // Obsługa wyjątków związanych z SQL
            System.err.println("Błąd podczas dodawania stolika do bazy danych: " + e.getMessage());
        }
    }
}
