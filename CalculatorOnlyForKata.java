import jdk.jfr.ContentType;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CalculatorOnlyForKata {

    public static String[] romanNumbers = {"I", "IV", "V", "IX", "X","XL", "L","XC", "C"}; // индекс римских и арабских чисел должен совпадать
    public static int[] arabicNumbers = {1, 4, 5, 9, 10, 40, 50, 90, 100};
    public static boolean romanCalc;

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        input = input.toUpperCase();
        input = input.trim();


        String output = mathCalculation(input);

        System.out.println(output);
    }

    static String mathCalculation(String input) throws Exception {
        int outputInt = 0;
        String outputStr;

        boolean onlyOneOperation = false; // исключение если кол-во математических знаков меньше 1
        int value0;
        int value1;

        romanCalc = false;
        for (int i = 0; i < romanNumbers.length; i++) {
            if (String.valueOf(input.charAt(0)).equals(romanNumbers[i])) {
                romanCalc = true;    //совпадение первого символа ввода с любым римским символом
            }
        }

        // убираем пробелы
        String[] mathExpression = input.split(" ");
        String inputWithoutSpace = "";

        for (String mathComponent : mathExpression) {
            inputWithoutSpace += mathComponent;
        }

        //считаем
        if (inputWithoutSpace.contains("+")) {
            String[] values = inputWithoutSpace.split("\\+");

            if (romanCalc) {
                value0 = toArabic(values[0]);
                value1 = toArabic(values[1]);
            } else {
                value0 = Integer.parseInt(values[0]);
                value1 = Integer.parseInt(values[1]);
            }

            outputInt = value0 + value1;

            if ((value0 < 1) || (value0 > 10)) throw new Exception("выход за пределы ввода [1..10]");
            if ((value1 < 1) || (value1 > 10)) throw new Exception("выход за пределы ввода [1..10]");
            if (values.length > 2) throw new Exception("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");

            onlyOneOperation = true;
        }
        if (inputWithoutSpace.contains("-")) {
            String[] values = inputWithoutSpace.split("-");

            if (romanCalc) {
                value0 = toArabic(values[0]);
                value1 = toArabic(values[1]);
            } else {
                value0 = Integer.parseInt(values[0]);
                value1 = Integer.parseInt(values[1]);
            }

            outputInt = value0 - value1;

            if ((value0 < 1) || (value0 > 10)) throw new Exception("выход за пределы ввода [1..10]");
            if ((value1 < 1) || (value1 > 10)) throw new Exception("выход за пределы ввода [1..10]");
            if (values.length > 2) throw new Exception("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");

            onlyOneOperation = true;
        }
        if (inputWithoutSpace.contains("*")) {
            String[] values = inputWithoutSpace.split("\\*");

            if (romanCalc) {
                value0 = toArabic(values[0]);
                value1 = toArabic(values[1]);
            } else {
                value0 = Integer.parseInt(values[0]);
                value1 = Integer.parseInt(values[1]);
            }

            outputInt = value0 * value1;

            if ((value0 < 1) || (value0 > 10)) throw new Exception("выход за пределы ввода [1..10]");
            if ((value1 < 1) || (value1 > 10)) throw new Exception("выход за пределы ввода [1..10]");
            if (values.length > 2) throw new Exception("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");

            onlyOneOperation = true;
        }
        if (inputWithoutSpace.contains("/")) {
            String[] values = inputWithoutSpace.split("/");

            if (romanCalc) {
                value0 = toArabic(values[0]);
                value1 = toArabic(values[1]);
            } else {
                value0 = Integer.parseInt(values[0]);
                value1 = Integer.parseInt(values[1]);
            }

            outputInt = (value0 - (value0 % value1)) / value1; // чтобы точно выходило, сперва уберу остаток

            if ((value0 < 1) || (value0 > 10)) throw new Exception("выход за пределы ввода [1..10]");
            if ((value1 < 1) || (value1 > 10)) throw new Exception("выход за пределы ввода [1..10]");
            if (values.length > 2) throw new Exception("формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");

            onlyOneOperation = true;
        }


        //исключаем
        if (onlyOneOperation) {
            outputStr = String.valueOf(outputInt);
        } else {
            throw new Exception("строка не является математической операцией");
        }
        if (romanCalc) {
            if (outputInt <= 0) throw new Exception("в римской системе могут быть только положительные числа");
            outputStr = toRoman(outputInt);
        }

        //и наконец, выводим
        return outputStr;
    }


    static int toArabic(String romanInput) throws Exception {
        int output = 0;

        String reversedInput = new StringBuilder(romanInput).reverse().toString();
        for (int j = 0; j < reversedInput.length(); j++) {
            //исключаем
            for (int i = 0; i < 9; i++) {
                if (reversedInput.charAt(j) == Integer.toString(i).charAt(0)) throw new Exception("используются одновременно разные системы счисления");
            }
            //двухзнаковые числа
            for (int i = romanNumbers.length - 2; i > -1; i -= 2) {
                if (romanInput.contains(romanNumbers[i])) {
                    romanInput = romanInput.replaceFirst(romanNumbers[i], "");
                    output += arabicNumbers[i];
                }
            }
            // однознаковые числа
            for (int i = romanNumbers.length - 1; i > -1; i -= 2) {
                if (romanInput.contains(romanNumbers[i])) {
                    romanInput = romanInput.replaceFirst(romanNumbers[i], "");
                    output += arabicNumbers[i];
                }
            }
        }

        return output;
    }


    static String toRoman(int arabicInput) {
        String output = "";

        int kef = 1;

        while (arabicInput > 0) {
            for (int i = romanNumbers.length-1; i > -1; i--) {
                if ((arabicInput - arabicNumbers[i]) >= 0) {
                    arabicInput -= arabicNumbers[i];
                    output += romanNumbers[i];
                    break;
                }
            }
        }

        return output;
    }
}
