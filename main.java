import java.util.Scanner;
 class SimpleCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Pobieranie dwóch liczb od użytkownika
        System.out.print("Wprowadź pierwszą liczbę: ");
        double num1 = scanner.nextDouble();

        System.out.print("Wprowadź drugą liczbę: ");
        double num2 = scanner.nextDouble();

        // Wybór operacji
        System.out.println("Wybierz operację: +, -, *, /");
        char operation = scanner.next().charAt(0);

        double result = 0;

        // Wykonywanie operacji w zależności od wyboru użytkownika
        switch (operation) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    System.out.println("Błąd: Dzielenie przez zero!");
                    scanner.close();
                    return; // Zakończ program w przypadku dzielenia przez zero
                }
                break;
            default:
                System.out.println("Nieprawidłowa operacja!");
                scanner.close();
                return; // Zakończ program w przypadku nieprawidłowej operacji
        }

        // Wyświetlenie wyniku
        System.out.println("Wynik: " + result);

        // Zamknięcie skanera
        scanner.close();
    }
}
