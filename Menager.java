import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Menager extends Pracownik {
    private String dzial;

    // Konstruktor
    public Menager(String imie, String nazwisko, double pensja, String dzial) {
        super(imie, nazwisko, pensja); // Wywołanie konstruktora klasy nadrzędnej (Pracownik)
        this.dzial = dzial; // Inicjalizacja pola dzial
    }

    // Getter do odczytu działu menadżera
    public String getDzial() {
        return dzial;
    }

    // Setter do zmiany działu menadżera
    public void setDzial(String dzial) {
        this.dzial = dzial;
    }

    // Metoda wyświetlająca informacje o menadżerze
    @Override
    public void wyswietlInformacje() {
        super.wyswietlInformacje();
        System.out.println("Dział: " + dzial);
    }

    // Statyczna metoda do dodania nowego menadżera
    public static Menager dodajMenagera(Scanner scanner) {
        System.out.println("\nDodawanie menadżera:");


        System.out.print("Imię: ");
        String imie = scanner.next();

        System.out.print("Nazwisko: ");
        String nazwisko = scanner.next();

        System.out.print("Pensja: ");
        double pensja = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Dział: ");
        String dzial = scanner.nextLine();

        // Tworzenie obiektu menadżera
        Menager menager = new Menager(imie, nazwisko, pensja, dzial);

        // Dodanie menadżera do bazy danych
        Connection connection = DBConnection.getConnection(); // Uzyskanie połączenia z bazą danych
        String sql = "INSERT INTO managerzy (imie, nazwisko, pensja, dzial) VALUES (?, ?, ?, ?)"; // Zapytanie SQL

        // Przechwycenie wyjątku w przypadku błędu podczas wykonywania zapytania SQL
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Ustawianie wartości parametrów w zapytaniu SQL
            statement.setString(1, menager.getImie());
            statement.setString(2, menager.getNazwisko());
            statement.setDouble(3, menager.getPensja());
            statement.setString(4, menager.getDzial());

            // Wykonanie zapytania
            statement.executeUpdate();
            System.out.println("Menadżer został dodany do bazy danych: " + imie + " " + nazwisko);
        } catch (SQLException e) {
            // Obsługa błędów związanych z bazą danych
            System.err.println("Błąd podczas dodawania menadżera do bazy danych: " + e.getMessage());
        }

        // Zwracamy obiekt menadżera, który został stworzony
        return menager;
    }
}
