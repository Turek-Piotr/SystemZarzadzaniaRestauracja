import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;

public class Main {
    // Lista przechowująca pracowników
    private static final List<Pracownik> pracownicy = new ArrayList<>();

    public static void main(String[] args) {
        // Obiekty do interakcji z użytkownikiem i bazą danych
        Scanner scanner = new Scanner(System.in);
        Restaurant restaurant = new Restaurant();
        Connection connection = DBConnection.getConnection(); // Połączenie z bazą danych

        int choice;
        do {
            // Menu restauracji
            System.out.println("\nMenu restauracji:");
            System.out.println("0. Wyjdź");
            System.out.println("1. Dodaj stolik");
            System.out.println("2. Zarezerwuj stolik");
            System.out.println("3. Wyświetl wszystkie stoliki");
            System.out.println("4. Zmień parametry stolika");
            System.out.println("5. Dodaj pracownika");
            System.out.println("6. Dodaj menadżera");
            System.out.println("7. Wyświetl wszystkich pracowników");
            System.out.print("Wybierz opcję: ");
            choice = scanner.nextInt(); // Odczytuje wybór użytkownika

            // Obsługuje wybór użytkownika
            switch (choice) {
                case 0:
                    // Wyjście z programu
                    System.out.println("Do widzenia!");
                    DBConnection.closeConnection(); // Zamykanie połączenia z bazą danych
                    break;
                case 1:
                    // Dodawanie nowego stolika
                    System.out.print("Podaj numer stolika: ");
                    int number = scanner.nextInt();
                    System.out.print("Podaj liczbę miejsc: ");
                    int seats = scanner.nextInt();
                    System.out.print("Podaj status stolika: ");
                    scanner.nextLine();
                    String status = scanner.nextLine();
                    // Tworzy obiekt stolika i zapisujemy go do bazy danych
                    Table newTable = new Table(number, seats, status);
                    restaurant.addTable(newTable); // Dodaje stolik do restauracji
                    newTable.saveToDatabase(); // Zapisuje stolik do bazy danych
                    break;
                case 2:
                    // Rezerwowanie stolika
                    ReservationService reservationService = new ReservationService(scanner, restaurant);
                    reservationService.reserveTableById(); // Rezerwacja stolika przez użytkownika
                    break;
                case 3:
                    // Wyświetlanie wszystkich stolików
                    restaurant.displayAllTables(); // Wyświetlanie wszystkich stolików w restauracji
                    break;
                case 4:
                    // Zmiana parametrów stolika
                    System.out.print("Podaj numer stolika, którego parametry chcesz zmienić: ");
                    int tableNumberToChange = scanner.nextInt();
                    System.out.print("Podaj nową liczbę miejsc: ");
                    int newSeats = scanner.nextInt();
                    System.out.print("Podaj nowy status stolika: ");
                    scanner.nextLine();
                    String newStatus = scanner.nextLine();
                    // Zmienia parametry wybranego stolika
                    restaurant.changeTableParameters(tableNumberToChange, newSeats, newStatus);
                    break;
                case 5:
                    // Dodawanie nowego pracownika
                    Pracownik nowyPracownik = Pracownik.dodajPracownika(scanner); // Tworzy nowego pracownika
                    pracownicy.add(nowyPracownik); // Dodaje go do listy pracowników
                    break;
                case 6:
                    // Dodawanie nowego menadżera
                    Menager nowyMenager = Menager.dodajMenagera(scanner); // Tworzy nowego menadżera
                    pracownicy.add(nowyMenager); // Dodaje go do listy pracowników
                    break;
                case 7:
                    // Wyświetlanie wszystkich pracowników
                    Pracownik.wyswietlPracownikowIMenagerow(); // Wyświetla listę pracowników i menadżerów
                    break;
                default:
                    // Obsługuje niepoprawny wybór
                    System.out.println("Niepoprawny wybór. Spróbuj ponownie.");
            }
        } while (choice != 0); // Program działa, dopóki użytkownik nie wybierze opcji "0"

        scanner.close(); // Zamyka skaner po zakończeniu działania programu
    }
}
