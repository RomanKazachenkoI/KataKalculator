package org.example;

import java.util.Map;
import java.util.Scanner;

public class Calculator {
    private static String[] parse(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Некорректное выражение");
        }
        String[] parts = input.split("[+*-/]");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Некорректное выражение");
        }
        String operator = input.replaceAll("[^+*-/]", "");
        return new String[]{parts[0], operator, parts[1]};
    }

    private static int toArabic(String s) {
        int value = 0;
        int num = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            switch (s.charAt(i)) {
                case 'I':
                    num = 1;
                    break;
                case 'V':
                    num = 5;
                    break;
                case 'X':
                    num = 10;
                    break;

                default:
                    throw new IllegalArgumentException("Неверное римское число");
            }
            if (4 * num < value) {
                value -= num;
            } else {
                value += num;
            }
        }
        return value;
    }

    private static String toRomanian(int i) {
        Map<Integer, String> map = Map.of(
                1, "I",
                4, "IV",
                5, "V",
                9, "IX",
                10, "X",
                40, "XL",
                50, "L",
                90, "XC",
                100, "C"
        );
        StringBuilder romanian = new StringBuilder();
        int multiplier = 1;
        while (i > 0) {
            int digit = i % 10;
            String value = map.get(digit * multiplier);
            if (value == null) {
                if (digit >= 5) {
                    value = map.get(5 * multiplier) + map.get(multiplier).repeat(digit - 5);
                } else {
                    value = map.get(multiplier).repeat(digit);
                }
            }
            romanian.insert(0, value);
            i = i / 10;
            multiplier *= 10;
        }
        return romanian.toString();
    }

    private static boolean checkToDiapazon(int value1, int value2) {
        return value1 >= 0 && value1 <= 10 && value2 >= 0 && value2 <= 10;
    }

    private static String calc(String input) {
        String[] parts = parse(input);
        String operator = parts[1];
        int res = 0;
        boolean isRoman = false;
        int value1;
        int value2;
        if (isRomanNumeral(parts[0]) && isRomanNumeral(parts[2])) {
            value1 = toArabic(parts[0]);
            value2 = toArabic(parts[2]);
            isRoman = true;
        } else {
            value1 = Integer.parseInt(parts[0]);
            value2 = Integer.parseInt(parts[2]);
        }
        switch (operator) {
            case "+":
                res = value1 + value2;
                break;
            case "-":
                res = value1 - value2;
                break;
            case "*":
                res = value1 * value2;
                break;
            case "/":
                if (value2 == 0) {
                    throw new ArithmeticException("Деление на ноль");
                }
                res = value1 / value2;
                break;
            default:
                throw new IllegalArgumentException("Некорректный оператор");
        }

        if (isRoman) {
            if (res < 0) {
                throw new IllegalArgumentException("Значение отрицательное");
            }
            return toRomanian(res);
        }
        return String.valueOf(res);
    }

    private static boolean isRomanNumeral(String str) {
        return str.matches("[IVXLCDM]+");
    }

    public static void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите выражение типа (Число_пробел_оператор_пробел_число) или q для выхода");
            String input = scanner.nextLine();
            if ("q".equalsIgnoreCase(input)) {
                break;
            }
            try {
                String result = calc(input);
                System.out.println("Результат: " + result);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: некорректное число");
            } catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

}
