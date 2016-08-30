package biba.bicicleta.publica.badajoz.utils;

import java.util.Locale;

public class Common {

    public static String toLowerCase(String str) {
        String[] words = str.split("\\s");
        String out = "";

        for (int i = 0; i < words.length - 1; i++) {
            out = out + toLowerCaseWord(words[i]) + " ";
        }
        out = out + toLowerCaseWord(words[words.length - 1]);
        return out;
    }

    private static String toLowerCaseWord(String str) {

        if (str.length() == 0) return "";

        if (str.length() == 1) return str.toUpperCase(Locale.ENGLISH);

        if (!Character.isLetter(str.charAt(0))) {
            return str.substring(0, 1) + str.substring(1, 2).toUpperCase(Locale.ENGLISH) + str.substring(2).toLowerCase(Locale.ENGLISH);
        } else {
            return str.substring(0, 1).toUpperCase(Locale.ENGLISH) + str.substring(1).toLowerCase(Locale.ENGLISH);
        }
    }

}
