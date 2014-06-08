package com.blitz.app.utilities.string;

/**
 * Created by Miguel Gaeta on 6/1/14.
 */
@SuppressWarnings("unused")
public class StringHelper {

    /**
     * Convert a camel cased string into a lower case
     * string with underscores.
     *
     * @param camelCaseString Camel cased string.
     *
     * @return Input string as lowercase with underscores.
     */
    public static String camelCaseToLowerCaseUnderscores(String camelCaseString) {
        StringBuilder sb = new StringBuilder();

        // If at least one character.
        if (camelCaseString.length() > 0) {

            // Automatically lowercase the first character.
            sb.append(Character.toLowerCase(camelCaseString.charAt(0)));
        }

        for (int index = 1; index < camelCaseString.length(); index++) {

            Character character = camelCaseString.charAt(index);

            if ((Character.isUpperCase(character) || Character.isDigit(character))) {

                sb.append("_").append(Character.toLowerCase(character));
            } else {
                sb.append(character);
            }
        }
        return sb.toString();
    }
}