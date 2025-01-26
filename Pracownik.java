import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.ResultSet;

public class Pracownik {
    private String imie;
    private String nazwisko;
    private double pensja;

    // Konstruktor klasy Pracownik
    public Pracownik(String imie, String nazwisko, double pensja) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pensja = pensja;
    }

    // Settery i gettery imie, nazwisko i pensja

    public void setImie(String imie) {
        this.imie = imie;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public void setPensja(double pensja) {
        this.pensja = pensja;
    }

    public String getImie() {
        return imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public double getPensja() {
        return pensja;
    }

    // Metoda wyświetlająca informacje o pracowniku
    public void wyswietlInformacje() {
        System.out.println("Imię: " + imie + ", Nazwisko: " + nazwisko + ", Pensja: " + pensja);
    }

    // Metoda statyczna dodająca pracownika do bazy danych
    public static void dodajPracownikaDoBD(Pracownik pracownik) {
        // Połączenie z bazą danych
        Connection connection = DBConnection.getConnection();
        String sql = "INSERT INTO pracownicy (imie, nazwisko, pensja) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Przypisanie wartości do zapytania SQL
            statement.setString(1, pracownik.getImie());
            statement.setString(2, pracownik.getNazwisko());
            statement.setDouble(3, pracownik.getPensja());

            // Wykonanie zapytania do bazy danych
            statement.executeUpdate();
            System.out.println("Pracownik został dodany do bazy danych.");
        } catch (SQLException e) {
            // Obsługa błędów podczas dodawania do bazy
            System.err.println("Błąd podczas dodawania pracownika do bazy danych: " + e.getMessage());
        }
    }

    // Metoda statyczna dodająca nowego pracownika
    public static Pracownik dodajPracownika(Scanner scanner) {
        System.out.println("\nDodawanie pracownika:");

        System.out.print("Imię: ");
        String imie = scanner.next();

        System.out.print("Nazwisko: ");
        String nazwisko = scanner.next();

        System.out.print("Pensja: ");
        double pensja = scanner.nextDouble();
        scanner.nextLine();

        // Tworzenie nowego obiektu Pracownik
        Pracownik pracownik = new Pracownik(imie, nazwisko, pensja);
        dodajPracownikaDoBD(pracownik);
        return pracownik;
    }

    // Metoda statyczna wyświetlająca wszystkich pracowników oraz menadżerów
    public static void wyswietlPracownikowIMenagerow() {
        // Połączenie z bazą danych
        Connection connection = DBConnection.getConnection();

        // Sprawdzenie, czy połączenie jest prawidłowe
        if (connection == null) {
            System.out.println("Brak połączenia z bazą danych.");
            return;
        }

        // Zapytanie SQL do pobrania danych pracowników
        String sqlPracownicy = "SELECT * FROM pracownicy";
        // Zapytanie SQL do pobrania danych menadżerów
        String sqlManagerzy = "SELECT * FROM managerzy";

        try {
            // Pobieranie i wyświetlanie pracowników
            try (PreparedStatement statementPracownicy = connection.prepareStatement(sqlPracownicy);
                 ResultSet resultSetPracownicy = statementPracownicy.executeQuery()) {

                System.out.println("\nLista pracowników:");
                boolean hasPracownicy = false; // Flaga sprawdzająca, czy są pracownicy

                while (resultSetPracownicy.next()) {
                    hasPracownicy = true;
                    String imie = resultSetPracownicy.getString("imie");
                    String nazwisko = resultSetPracownicy.getString("nazwisko");
                    double pensja = resultSetPracownicy.getDouble("pensja");

                    System.out.println("Imię: " + imie + ", Nazwisko: " + nazwisko + ", Pensja: " + pensja);
                    System.out.println("------------------");
                }

                if (!hasPracownicy) {
                    System.out.println("Brak pracowników w systemie.");
                }
            }

            // Pobieranie i wyświetlanie menadżerów
            try (PreparedStatement statementManagerzy = connection.prepareStatement(sqlManagerzy);
                 ResultSet resultSetManagerzy = statementManagerzy.executeQuery()) {

                System.out.println("\nLista menadżerów:");
                boolean hasManagerzy = false; // Flaga sprawdzająca, czy są menadżerowie

                while (resultSetManagerzy.next()) {
                    hasManagerzy = true;
                    String imie = resultSetManagerzy.getString("imie");
                    String nazwisko = resultSetManagerzy.getString("nazwisko");
                    double pensja = resultSetManagerzy.getDouble("pensja");
                    String dzial = resultSetManagerzy.getString("dzial");

                    System.out.println("Imię: " + imie + ", Nazwisko: " + nazwisko + ", Pensja: " + pensja + ", Dział: " + dzial);
                    System.out.println("------------------");
                }

                if (!hasManagerzy) {
                    System.out.println("Brak menadżerów w systemie.");
                }
            }
        } catch (SQLException e) {
            // Obsługa błędów przy pobieraniu danych z bazy
            System.err.println("Błąd podczas pobierania danych z bazy danych: " + e.getMessage());
        }
    }
}
