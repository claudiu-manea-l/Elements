package com.codez.mainlibrary.utilities;

import android.text.InputType;
import android.text.TextUtils;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Claudiu on 02/07/2015.
 */
public class TextChecker {

    /**
     * General method for checking if the text is of proper structure
     * @param text the text to be checked
     * @param type the type of checking (Ex: InputType.TYPE_CLASS_PHONE)
     * @return if the text complies to the types standards
     */
    public static boolean checkText(String text, int type) {
        if (!TextUtils.isEmpty(text) && !(text.length() < 2)) {
            switch (type) {
                case InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS:
                    return checkIfEmailProper(text);
                case InputType.TYPE_CLASS_PHONE:
                    return isNumeric(text);
                case InputType.TYPE_TEXT_VARIATION_PERSON_NAME:
                    break;
                case InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS:
                    break;
            }

            if (hasIlegalChars(text)) return false;
        }
        return true;
    }

    /**
     * Returns if the text contains any illegal characters
     * @param text the text to be checked
     * @return if it contains illegal chars or not
     */
    public static boolean hasIlegalChars(String text) {
        String[] arr = text.split("[~#@*+%{}<>\\[\\]|\"\\_)(^]", 2);
        return arr.length > 1;
    }

    /**
     * Checks if the Email text given is proper
     * @param email the email text
     * @return if it's proper or not
     */
    public static boolean checkIfEmailProper(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Checks if the whole contents of the text are of numeric value
     * @param str the text to be checked
     * @return if the given text is numeric or not
     */
    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }
}
