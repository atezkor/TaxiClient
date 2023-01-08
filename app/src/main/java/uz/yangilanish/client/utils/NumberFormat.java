package uz.yangilanish.client.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class NumberFormat {

    // phone format
    public static String maskedPhone(String number) {
        String phoneNumber = number.replaceAll("[(-)- +]", "");

        String result = "";
        char[] numbers = phoneNumber.toCharArray();
        for (int i = 0; i < numbers.length; i++) {
            if (i == 0)
                result += "(";
            if (i == 2)
                result += ") ";
            if (i == 5)
                result += "-";
            if (i == 7)
                result += "-";

            result = result.concat(String.valueOf(numbers[i]));
        }

        return result;
    }

    public static String unmasked(String number) {
        return number.replaceAll("[(-)- +]", "");
    }

    /* Sms code */
    public static String unmaskedCode(String code) {
        return code.replaceAll("[\u2022 ]", "");
    }

    public static String maskedCode(String code) {
        String unmasked = unmaskedCode(code);

        switch (unmasked.length()) {
            case 0:
                return unmasked.concat("\u2022 \u2022 \u2022 \u2022");
            case 1:
                return unmasked.concat(" \u2022 \u2022 \u2022");
            case 2:
                return unmasked.concat(" \u2022 \u2022");
            case 3:
                return unmasked.concat(" \u2022");
            default:
                return unmasked;
        }
    }

    public static short getSmsCode(String code) {
        return Short.parseShort(code);
    }

    public static String price(int price) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("###,###", decimalFormatSymbols);

        return decimalFormat.format(price);
    }

    public static String additionalPayment(int roundingPrice) {
        return "(+" + roundingPrice + ")";
    }
}
