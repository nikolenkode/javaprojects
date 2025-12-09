package prisoners;

import container.MyLinkedList;
import java.time.LocalDate;
import java.util.*;
import java.io.File;

/**
 * Главный класс приложения для управления списком заключенных.
 * Предоставляет консольное меню для выполнения операций с данными.
 * 
 * @version 1.0
 */
public class Main {
    /** Путь к текстовому файлу с данными */
    private static final String TXT_PATH = "c:\\dz\\KHPI\\java\\Prisoner5\\src\\prisoners.txt";
    /** Путь к бинарному файлу с сериализованными данными */
    private static final String DAT_PATH = "c:\\dz\\KHPI\\java\\Prisoner5\\src\\prisoners.dat";

    /**
     * Главный метод приложения.
     * Запускает интерактивное меню для управления списком заключенных.
     * 
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MyLinkedList<Prisoner> list = new MyLinkedList<>();

        list.add(new Prisoner("Ivan Petrenko", LocalDate.of(1990, 5, 10), 180, "green",
                Arrays.asList("scar on left cheek"), LocalDate.of(2020, 1, 10), LocalDate.of(2025, 1, 10)));

        list.add(new Prisoner("Olena Koval", LocalDate.of(1992, 8, 20), 165, "gray",
                Arrays.asList("tattoo on arm"), LocalDate.of(2021, 3, 15), null));

        while (true) {
            clearConsole();

            System.out.println("--- MENU ---");
            System.out.println("1 - Show prisoners");
            System.out.println("2 - Add prisoner");
            System.out.println("3 - Remove prisoner");
            System.out.println("4 - Search prisoners");
            System.out.println("5 - Clear ALL prisoners");
            System.out.println("6 - Load from file");
            System.out.println("7 - Save to file");
            System.out.println("x - Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    clearConsole();
                    if (list.size() == 0) {
                        System.out.println("No prisoners in list.");
                    } else {
                        System.out.println("Prisoners:");
                        int idx = 1;
                        for (Prisoner p : list) {
                            System.out.println("=== Prisoner #" + idx + " ===");
                            System.out.println(p);
                            idx++;
                        }
                    }
                    System.out.print("Press Enter to return to menu...");
                    scanner.nextLine();
                    break;

                case "2":
                    clearConsole();
                    list.add(inputPrisoner(scanner));
                    System.out.println("Prisoner added!");
                    System.out.print("Press Enter to return to menu...");
                    scanner.nextLine();
                    break;

                case "3":
                    clearConsole();
                    if (list.size() == 0) {
                        System.out.println("No prisoners to remove.");
                        System.out.print("Press Enter to return to menu...");
                        scanner.nextLine();
                        break;
                    } else {
                        System.out.println("Current prisoners:");
                        int idx = 1;
                        for (Prisoner p : list) {
                            System.out.println("[" + idx + "] " + p.getFullName());
                            idx++;
                        }

                        System.out.print("Enter prisoner number to remove: ");
                        try {
                            int index = Integer.parseInt(scanner.nextLine()) - 1;
                            if (index >= 0 && index < list.size()) {
                                Prisoner prisonerToRemove = null;
                                int cur = 0;
                                for (Prisoner p : list) {
                                    if (cur == index) {
                                        prisonerToRemove = p;
                                        break;
                                    }
                                    cur++;
                                }

                                if (prisonerToRemove != null) {
                                    if (list.remove(index)) {
                                        System.out
                                                .println("Prisoner '" + prisonerToRemove.getFullName() + "' removed!");
                                    } else {
                                        System.out.println("Error removing prisoner.");
                                    }
                                } else {
                                    System.out.println("Error: prisoner not found at index.");
                                }
                            } else {
                                System.out.println("Invalid prisoner number.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    }
                    System.out.print("Press Enter to return to menu...");
                    scanner.nextLine();
                    break;

                case "4":
                    showSearchMenu(list, scanner);
                    break;

                case "5":
                    clearConsole();
                    System.out.print("Are you sure you want to clear ALL prisoners? (y/n): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("y")) {
                        list.clear();
                        System.out.println("All prisoners cleared!");
                    } else {
                        System.out.println("Operation cancelled.");
                    }
                    System.out.print("Press Enter to return to menu...");
                    scanner.nextLine();
                    break;

                case "6":
                    clearConsole();
                    try {
                        File file = new File(DAT_PATH);
                        if (file.exists()) {
                            MyLinkedList<Prisoner> loadedList = MyLinkedList.loadFromFileSerialized(DAT_PATH);
                            int addedCount = 0;
                            for (Prisoner loadedPrisoner : loadedListToIterable(loadedList)) {
                                boolean exists = false;
                                for (Prisoner existing : list) {
                                    if (existing.getFullName().equals(loadedPrisoner.getFullName()) &&
                                            existing.getBirthDate().equals(loadedPrisoner.getBirthDate())) {
                                        exists = true;
                                        break;
                                    }
                                }
                                if (!exists) {
                                    list.add(loadedPrisoner);
                                    addedCount++;
                                }
                            }
                            System.out.println("Added " + addedCount + " new prisoners from file");
                            System.out.println("Total prisoners now: " + list.size());
                        } else {
                            System.out.println("File not found. Save prisoners first to create the file.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error loading from file: " + e.getMessage());
                    }
                    System.out.print("Press Enter to return to menu...");
                    scanner.nextLine();
                    break;

                case "7":
                    clearConsole();
                    try {
                        list.saveToFileSerialized(DAT_PATH);
                        list.saveToFileText(TXT_PATH);
                        System.out.println("Saved " + list.size() + " prisoners to both files:");
                        System.out.println("- " + DAT_PATH + " (objects)");
                        System.out.println("- " + TXT_PATH + " (text)");
                    } catch (Exception e) {
                        System.out.println("Error saving files: " + e.getMessage());
                    }
                    System.out.print("Press Enter to return to menu...");
                    scanner.nextLine();
                    break;

                case "x":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
            }
        }
    }

    /**
     * Преобразует MyLinkedList в Iterable для совместимости.
     * 
     * @param loaded список для преобразования
     * @return итерируемый объект
     */
    private static Iterable<Prisoner> loadedListToIterable(MyLinkedList<Prisoner> loaded) {
        return loaded;
    }

    /**
     * Отображает меню поиска заключенных по различным критериям.
     * 
     * @param list    список заключенных
     * @param scanner объект Scanner для ввода данных
     */
    private static void showSearchMenu(MyLinkedList<Prisoner> list, Scanner scanner) {
        while (true) {
            clearConsole();
            System.out.println("--- SEARCH MENU ---");
            System.out.println("1 - Search by exact name");
            System.out.println("2 - Search by exact eye color");
            System.out.println("3 - Search by birth year");
            System.out.println("4 - Search by height");
            System.out.println("5 - Search by feature");
            System.out.println("6 - Search by status");
            System.out.println("0 - Back to main menu");
            System.out.print("Choice: ");

            String searchChoice = scanner.nextLine();

            switch (searchChoice) {
                case "1":
                    clearConsole();
                    System.out.print("Enter exact name to search: ");
                    String searchName = scanner.nextLine();
                    boolean found = false;
                    for (Prisoner p : list) {
                        if (p.getFullName().equals(searchName)) {
                            System.out.println("=== FOUND PRISONER ===");
                            System.out.println(p);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Prisoner with exact name '" + searchName + "' not found.");
                    }
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    break;

                case "2":
                    clearConsole();
                    System.out.print("Enter exact eye color: ");
                    String eyeColor = scanner.nextLine();
                    System.out.println("Prisoners with eye color '" + eyeColor + "':");
                    boolean foundEye = false;
                    for (Prisoner p : list) {
                        if (p.getEyeColor().equalsIgnoreCase(eyeColor)) {
                            System.out.println("=== Prisoner ===");
                            System.out.println(p);
                            foundEye = true;
                        }
                    }
                    if (!foundEye) {
                        System.out.println("No prisoners with eye color '" + eyeColor + "' found.");
                    }
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    break;

                case "3":
                    clearConsole();
                    System.out.print("Enter birth year: ");
                    try {
                        int year = Integer.parseInt(scanner.nextLine());
                        System.out.println("Prisoners born in " + year + ":");
                        boolean foundYear = false;
                        for (Prisoner p : list) {
                            if (p.getBirthDate().getYear() == year) {
                                System.out.println("=== Prisoner ===");
                                System.out.println(p);
                                foundYear = true;
                            }
                        }
                        if (!foundYear) {
                            System.out.println("No prisoners born in " + year + " found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid year.");
                    }
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    break;

                case "4":
                    clearConsole();
                    System.out.print("Enter exact height: ");
                    try {
                        int height = Integer.parseInt(scanner.nextLine());
                        System.out.println("Prisoners with height " + height + "cm:");
                        boolean foundHeight = false;
                        for (Prisoner p : list) {
                            if (p.getHeight() == height) {
                                System.out.println("=== Prisoner ===");
                                System.out.println(p);
                                foundHeight = true;
                            }
                        }
                        if (!foundHeight) {
                            System.out.println("No prisoners with height " + height + "cm found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid height.");
                    }
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    break;

                case "5":
                    clearConsole();
                    System.out.print("Enter exact feature: ");
                    String feature = scanner.nextLine();
                    System.out.println("Prisoners with feature '" + feature + "':");
                    boolean foundFeature = false;
                    for (Prisoner p : list) {
                        if (p.getFeatures().contains(feature)) {
                            System.out.println("=== Prisoner ===");
                            System.out.println(p);
                            foundFeature = true;
                        }
                    }
                    if (!foundFeature) {
                        System.out.println("No prisoners with feature '" + feature + "' found.");
                    }
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    break;

                case "6":
                    clearConsole();
                    System.out.println("Search by status:");
                    System.out.println("1 - Currently imprisoned");
                    System.out.println("2 - Released");
                    System.out.print("Choice: ");
                    String statusChoice = scanner.nextLine();

                    if (statusChoice.equals("1")) {
                        System.out.println("Currently imprisoned prisoners:");
                        boolean foundImprisoned = false;
                        for (Prisoner p : list) {
                            if (p.getDateOut() == null) {
                                System.out.println("=== Prisoner ===");
                                System.out.println(p);
                                foundImprisoned = true;
                            }
                        }
                        if (!foundImprisoned) {
                            System.out.println("No currently imprisoned prisoners.");
                        }
                    } else if (statusChoice.equals("2")) {
                        System.out.println("Released prisoners:");
                        boolean foundReleased = false;
                        for (Prisoner p : list) {
                            if (p.getDateOut() != null) {
                                System.out.println("=== Prisoner ===");
                                System.out.println(p);
                                foundReleased = true;
                            }
                        }
                        if (!foundReleased) {
                            System.out.println("No released prisoners.");
                        }
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Invalid choice.");
                    System.out.print("Press Enter to continue...");
                    scanner.nextLine();
            }
        }
    }

    /**
     * Очищает консоль в зависимости от операционной системы.
     * Работает на Windows (cmd) и Unix-подобных системах.
     */
    private static void clearConsole() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    /**
     * Считывает данные заключенного с консоли.
     * 
     * @param scanner объект Scanner для ввода данных
     * @return новый объект Prisoner с введенными данными
     */
    private static Prisoner inputPrisoner(Scanner scanner) {
        System.out.print("Full Name: ");
        String name = scanner.nextLine();

        System.out.print("Birth Date (yyyy-mm-dd): ");
        LocalDate birth = LocalDate.parse(scanner.nextLine());

        System.out.print("Height: ");
        int height = Integer.parseInt(scanner.nextLine());

        System.out.print("Eye Color: ");
        String eyes = scanner.nextLine();

        List<String> features = new ArrayList<>();
        System.out.println("Features (empty to finish):");
        while (true) {
            String feature = scanner.nextLine();
            if (feature.isEmpty())
                break;
            features.add(feature);
        }

        System.out.print("Imprisonment Date (yyyy-mm-dd): ");
        LocalDate dateIn = LocalDate.parse(scanner.nextLine());

        System.out.print("Release Date (yyyy-mm-dd or empty): ");
        String release = scanner.nextLine();
        LocalDate dateOut = release.isEmpty() ? null : LocalDate.parse(release);

        return new Prisoner(name, birth, height, eyes, features, dateIn, dateOut);
    }
}