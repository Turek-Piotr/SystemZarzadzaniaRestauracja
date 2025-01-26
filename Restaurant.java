import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Restaurant {
    Scanner scanner;
    private final List<Table> tables; // Lista stolików w restauracji

    // Konstruktor klasy Restaurant
    Restaurant() {
        this.scanner = new Scanner(System.in); // Inicjalizacja skanera
        this.tables = new ArrayList(); // Inicjalizacja listy stolików
    }

    // Metoda dodająca stolik do listy stolików
    public void addTable(Table table) {
        this.tables.add(table);
    }

    // Metoda zwracająca listę stolików
    public List<Table> getTables() {
        return this.tables;
    }

    // Metoda do wyświetlania wszystkich stolików z bazy danych
    public void displayAllTables() {
        // Nawiązywanie połączenia z bazą danych
        Connection connection = DBConnection.getConnection();

        // Sprawdzenie, czy połączenie z bazą danych zostało nawiązane
        if (connection == null) {
            System.out.println("Brak połączenia z bazą danych.");
            return;
        }

        String sql = "SELECT * FROM stolik"; // Zapytanie SQL do pobrania danych o stolikach

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Sprawdzanie, czy zapytanie zwróciło jakiekolwiek dane
            if (!resultSet.isBeforeFirst()) {
                System.out.println("Brak stolików w systemie.");
            } else {
                System.out.println("Lista stolików:");
                int tableNumber = 1;

                while (resultSet.next()) {
                    int number = resultSet.getInt("id");
                    int seats = resultSet.getInt("liczba_miejsc");
                    String status = resultSet.getString("status");

                    System.out.println("\nStolik " + tableNumber + ":");
                    System.out.println("Numer stolika: " + number);
                    System.out.println("Liczba miejsc: " + seats);
                    System.out.println("Status stolika: " + status);
                    System.out.println("------------------");

                    tableNumber++;
                }
            }
        } catch (SQLException e) {
            // Obsługa błędów SQL
            System.err.println("Błąd podczas pobierania stolików z bazy danych: " + e.getMessage());
        }
    }

    // Metoda do zmiany parametrów stolika (liczba miejsc, status)
    public void changeTableParameters(int tableNumber, int newSeats, String newStatus) {
        // Nawiązywanie połączenia z bazą danych
        Connection connection = DBConnection.getConnection();

        // Sprawdzenie, czy połączenie z bazą danych zostało nawiązane
        if (connection == null) {
            System.out.println("Brak połączenia z bazą danych.");
            return;
        }

        String sql = "UPDATE stolik SET liczba_miejsc = ?, status = ? WHERE id = ?"; // Zapytanie SQL do aktualizacji parametrów stolika

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Przypisanie nowych wartości do zapytania SQL
            statement.setInt(1, newSeats);
            statement.setString(2, newStatus);
            statement.setInt(3, tableNumber);

            // Wykonanie zapytania SQL
            int rowsUpdated = statement.executeUpdate();


            if (rowsUpdated > 0) {
                System.out.println("Parametry stolika o numerze " + tableNumber + " zostały zmienione.");
            } else {
                System.out.println("Nie znaleziono stolika o podanym numerze.");
            }
        } catch (SQLException e) {
            // Obsługa błędów SQL
            System.err.println("Błąd podczas aktualizacji parametrów stolika: " + e.getMessage());
        }
    }
}
