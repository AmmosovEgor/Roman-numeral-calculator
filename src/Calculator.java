import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        input = input.toUpperCase();

        String strOutput = "";

        String[] romanNumbers = {"I", "V", "X", "L", "C", "D", "M"};
        boolean romanCalc = false;
        for (int i = 0; i < romanNumbers.length; i++) {
            if (String.valueOf(input.charAt(0)).equals(romanNumbers[i])) {
                romanCalc = true;
            }
        }

        String[] mathExpression = input.split(" ");
        String inputWithoutSpace = "";

        for (String mathComponent : mathExpression) {
            inputWithoutSpace += mathComponent;
        }



        int[] numbers;
        int intOutput = 0;
        int haveOperaton = 0;
        for (int i = 0; i < inputWithoutSpace.length(); i++)
            switch (inputWithoutSpace.charAt(i)) {
                case '+' -> {
                    numbers = mathCheck(inputWithoutSpace, i, "\\+", romanCalc);
                    intOutput = numbers[0] + numbers[1];
                    haveOperaton += 1;
                }
                case '-' -> {
                    numbers = mathCheck(inputWithoutSpace, i, "-", romanCalc);
                    intOutput = numbers[0] - numbers[1];
                    haveOperaton += 1;
                }
                case '*' -> {
                    numbers = mathCheck(inputWithoutSpace, i, "\\*", romanCalc);
                    intOutput = numbers[0] * numbers[1];
                    haveOperaton += 1;
                }
                case '/' -> {
                    numbers = mathCheck(inputWithoutSpace, i, "/", romanCalc);
                    intOutput = (numbers[0] - (numbers[0] % numbers[1])) / numbers[1];
                    haveOperaton += 1;
                }
            }
        if (haveOperaton == 0) strOutput = "throws Exception //т.к. строка не является математической операцией";
        else if (haveOperaton > 1) strOutput = "throws Exception //т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)";

        if (strOutput == "") {
            if (romanCalc == true) {
                if (intOutput < 0) {
                    strOutput = "throws Exception //т.к. в римской системе нет отрицательных чисел";
                } else
                    strOutput = arabicToRoman(intOutput);
            } else {
                strOutput = String.valueOf(intOutput);
            }
        }

        System.out.println(strOutput);
    }

    static int[] mathCheck(String inputString, int index, String arithmeticOperation, boolean romanCalc) {
        String[] strNumbers = inputString.split(arithmeticOperation);

        boolean ret = false;
        if (strNumbers.length != 2) {
            ret = true;
        }

        int numbers[] = new int[2];
        for (int i = 0; i<2 ;i++) {
            if (romanCalc == true) {
                strNumbers[i] = String.valueOf(romanToArabic(strNumbers[i]));
                if (strNumbers[i] == "-1") {
                    int lose[] = {-1, -1};
                }
            }

            if (ret == true) numbers[i] = 0;
            else numbers[i] = Integer.parseInt(strNumbers[i]);
        }
        return numbers;
    }

    static int romanToArabic(String romanInput) {
        String[] romanNumbers = {"I", "V", "X", "L", "C", "D", "M"}; // индекс римских и арабских чисел должен совпадать
        int[] arabicNumbers = {1, 5 ,10, 50, 100, 500, 1000};
        int output = 0;
        int numberBefore =  2147483647;
        for (int i = 0; i < romanInput.length(); i++) {
            char ch = romanInput.charAt(i);
            boolean correctFormat = false;

            for (int j = 0; j < romanNumbers.length; j++) {
                if (ch == romanNumbers[j].charAt(0)) {
                    correctFormat = true;
                    if (numberBefore<arabicNumbers[j]) {
                        output = output + arabicNumbers[j] - (numberBefore * 2);
                    } else{
                        output += arabicNumbers[j];
                        numberBefore = arabicNumbers[j];
                    }

                }
            }
            if (!correctFormat){
                System.out.println("throws Exception //т.к. используются одновременно разные системы счисления");
                return -1;
            }
        }

        return output;
    }

    static String arabicToRoman(int arabicInput) {
        String[] romanNumbers = {"I", "V", "X", "L", "C", "D", "M"}; // индекс римских и арабских чисел должен совпадать
        int[] arabicNumbers = {1, 5, 10, 50, 100, 500, 1000};

        StringBuilder output = new StringBuilder("");
        String specialNumber = "";

        while (arabicInput > 0) {
            for (int i = romanNumbers.length - 1; i > -1; i--) {

                if (specialNumber.equals("")) {
                    for (int j = 0; j < romanNumbers.length - 1; j += 2) {
                        if (arabicInput % (5 * arabicNumbers[j]) == (4 * arabicNumbers[j])) {
                            arabicInput += arabicNumbers[j];
                            specialNumber = romanNumbers[j];
                            break;
                        }
                    }
                }

                if (arabicInput - arabicNumbers[i] >= 0) {
                    arabicInput -= arabicNumbers[i];
                    output.append(romanNumbers[i]);

                    break;
                }
            }
        }
        if (!specialNumber.equals("")) {
            output.insert(output.length()-1, specialNumber);
        }
        return output.toString();
    }
}



