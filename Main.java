import java.util.Random;
import java.util.Scanner;

public class Main {

    // Настройки уровней сложности: минимум, максимум, попытки
    static final int[][] LEVELS = {
            {1, 50, 7},
            {1, 100, 7},
            {1, 200, 6}
    };

    static final String[] LEVEL_NAMES = {"Лёгкий", "Средний", "Сложный"};
    static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int games = 0;
        int wins = 0;
        boolean playAgain = true;

        printWelcome();

        // Главный игровой цикл
        while (playAgain) {
            int level = chooseLevel(scanner);
            boolean won = playRound(scanner, level);

            games++;

            if (won) {
                wins++;
            }

            playAgain = askPlayAgain(scanner);
        }

        printStats(games, wins);
    }

    // Приветствие и правила игры
    static void printWelcome() {
        System.out.println("=================================");
        System.out.println("        УГАДАЙ ЧИСЛО");
        System.out.println("=================================");
        System.out.println("Я загадываю число, а ты пытаешься его угадать.");
        System.out.println("После каждой попытки я скажу: больше или меньше.");
        System.out.println();
    }

    // Выбор уровня сложности
    static int chooseLevel(Scanner scanner) {
        System.out.println("Выбери уровень сложности:");

        for (int i = 0; i < LEVELS.length; i++) {
            System.out.println((i + 1) + " - " + LEVEL_NAMES[i]
                    + " (число от " + LEVELS[i][0] + " до " + LEVELS[i][1]
                    + ", попыток: " + LEVELS[i][2] + ")");
        }

        int choice = readInt(scanner, "Твой выбор (1-3): ", 1, 3);

        switch (choice) {
            case 1:
                System.out.println("Лёгкий уровень — отличное начало!");
                break;
            case 2:
                System.out.println("Средний уровень — будет интересно!");
                break;
            case 3:
                System.out.println("Сложный уровень — удачи, она понадобится!");
                break;
        }

        return choice - 1;
    }

    // Один раунд игры
    static boolean playRound(Scanner scanner, int level) {
        int min = LEVELS[level][0];
        int max = LEVELS[level][1];
        int maxAttempts = LEVELS[level][2];

        int secret = min + RANDOM.nextInt(max - min + 1);

        int[] history = new int[maxAttempts];
        int used = 0;
        boolean won = false;

        System.out.println("\nЯ загадал число от " + min + " до " + max + ". Начинаем!");

        while (used < maxAttempts) {
            int guess = readInt(scanner,
                    "Попытка " + (used + 1) + "/" + maxAttempts + ": ", min, max);

            // Проверяем, не вводилось ли это число раньше
            if (alreadyGuessed(history, used, guess)) {
                System.out.println("Ты уже называл это число. Попробуй другое!");
                continue;
            }

            history[used] = guess;
            used++;

            if (guess == secret) {
                won = true;
                break;
            } else if (guess < secret) {
                System.out.println("Загаданное число БОЛЬШЕ.");
            } else {
                System.out.println("Загаданное число МЕНЬШЕ.");
            }

            System.out.println("Осталось попыток: " + (maxAttempts - used));
        }

        printResult(won, secret, used, maxAttempts, history);

        return won;
    }

    // Проверка повторных попыток
    static boolean alreadyGuessed(int[] history, int used, int guess) {
        for (int i = 0; i < used; i++) {
            if (history[i] == guess) {
                return true;
            }
        }

        return false;
    }

    // Вывод результата игры
    static void printResult(boolean won, int secret, int used,
                            int maxAttempts, int[] history) {
        System.out.println();

        if (won) {
            double score = (double) (maxAttempts - used + 1) / maxAttempts * 100;

            System.out.println("ПОБЕДА! Ты угадал число " + secret + " за " + used + " попыт.");
            System.out.printf("Твоя оценка: %.0f очков из 100%n", score);
        } else {
            System.out.println("ПОРАЖЕНИЕ! Попытки закончились. Я загадал число " + secret + ".");
        }

        // Показываем все введённые числа
        System.out.print("Твои попытки: ");

        for (int i = 0; i < used; i++) {
            System.out.print(history[i] + (i < used - 1 ? ", " : ""));
        }

        System.out.println("\n");
    }

    // Предлагаем сыграть ещё раз
    static boolean askPlayAgain(Scanner scanner) {
        int answer = readInt(scanner, "Сыграем ещё? (1 - да, 2 - нет): ", 1, 2);

        System.out.println();

        return answer == 1;
    }

    // Общая статистика
    static void printStats(int games, int wins) {
        double percent = games > 0 ? (double) wins / games * 100 : 0;

        System.out.println("=================================");
        System.out.println("Игр сыграно: " + games);
        System.out.println("Побед: " + wins);
        System.out.printf("Процент побед: %.1f%%%n", percent);
        System.out.println("Спасибо за игру! До встречи!");
    }

    // Проверка введённого числа
    static int readInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);

            if (!scanner.hasNextLine()) {
                System.out.println("\nВвод завершён. Выход.");
                System.exit(0);
            }

            String line = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(line);

                if (value >= min && value <= max) {
                    return value;
                }

                System.out.println("Число должно быть от " + min + " до " + max + ".");

            } catch (NumberFormatException e) {
                System.out.println("Это не число! Введи целое число.");
            }
        }
    }
}

