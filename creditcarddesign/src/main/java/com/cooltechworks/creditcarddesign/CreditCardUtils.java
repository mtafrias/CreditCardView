package com.cooltechworks.creditcarddesign;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * Created by Harish on 03/01/16.
 * Updated by mtafrias on 23/10/19
 */

public class CreditCardUtils {

    public enum CardType {
        UNKNOWN_CARD, AMEX_CARD, MASTER_CARD, VISA_CARD, DINERS_CARD, ELO_CARD
    }

    private static final String PATTERN_AMEX = "^3([47])[0-9 ]*";
    private static final String PATTERN_VISA = "^4[0-9 ]*";
    private static final String PATTERN_MASTER = "^(5[1-5]|222[1-9]|22[3-9]|2[3-6]|27[01]|2720)[0-9]*";
    private static final String PATTERN_DINERS = "^3(0[0-5]|[68]\\d)[0-9]*";
    private static final String PATTERN_ELO = "^(4011(78|79)|43(1274|8935)|45(1416|7393|763([12]))|50(4175|6699|67[0-7][0-9]|9000)|50(9[0-9][0-9][0-9])|627780|63(6297|6368)|650(03([^4])|04([0-9])|05([01])|05([7-9])|06([0-9])|07([0-9])|08([0-9])|4([0-3][0-9]|8[5-9]|9[0-9])|5([0-9][0-9]|3[0-8])|9([0-6][0-9]|7[0-8])|7([0-2][0-9])|541|700|720|727|901)|65165([2-9])|6516([6-7][0-9])|65500([0-9])|6550([0-5][0-9])|655021|65505([6-7])|6516([8-9][0-9])|65170([0-4]))[0-9]*";

    protected static final int MAX_LENGTH_CARD_NUMBER = 16;
    private static final int MAX_LENGTH_CARD_NUMBER_AMEX = 15;
    private static final int MAX_LENGTH_CARD_NUMBER_DINERS = 14;

    public static final String CARD_NUMBER_FORMAT = "XXXX XXXX XXXX XXXX";
    public static final String CARD_NUMBER_FORMAT_AMEX = "XXXX XXXXXX XXXXX";
    public static final String CARD_NUMBER_FORMAT_DINERS = "XXXX XXXXXX XXXX";

    public static final String EXTRA_CARD_NUMBER = "card_number";
    public static final String EXTRA_CARD_CVV = "card_cvv";
    public static final String EXTRA_CARD_EXPIRY = "card_expiry";
    public static final String EXTRA_CARD_HOLDER_NAME = "card_holder_name";
    public static final String EXTRA_CARD_SHOW_CARD_SIDE = "card_side";
    public static final String EXTRA_VALIDATE_EXPIRY_DATE = "expiry_date";
    public static final String EXTRA_ENTRY_START_PAGE = "start_page";

    public static final int CARD_SIDE_FRONT = 1, CARD_SIDE_BACK = 0;

    public static final int CARD_NUMBER_PAGE = 0, CARD_EXPIRY_PAGE = 1;
    public static final int CARD_CVV_PAGE = 2, CARD_NAME_PAGE = 3;

    public static final String SPACE_SEPERATOR = " ";
    public static final String SLASH_SEPERATOR = "/";
    private static final char CHAR_X = 'X';

    public static String handleCardNumber(String inputCardNumber) {

        return handleCardNumber(inputCardNumber, SPACE_SEPERATOR);
    }

    public static CardType selectCardType(String cardNumber) {
        cardNumber = cardNumber.replaceAll("\\s", "");
        Pattern pCardType = Pattern.compile(PATTERN_ELO);
        if (pCardType.matcher(cardNumber).matches())
            return CardType.ELO_CARD;
        pCardType = Pattern.compile(PATTERN_DINERS);
        if (pCardType.matcher(cardNumber).matches())
            return CardType.DINERS_CARD;
        pCardType = Pattern.compile(PATTERN_VISA);
        if (pCardType.matcher(cardNumber).matches())
            return CardType.VISA_CARD;
        pCardType = Pattern.compile(PATTERN_MASTER);
        if (pCardType.matcher(cardNumber).matches())
            return CardType.MASTER_CARD;
        pCardType = Pattern.compile(PATTERN_AMEX);
        if (pCardType.matcher(cardNumber).matches())
            return CardType.AMEX_CARD;
        return CardType.UNKNOWN_CARD;
    }

    public static int selectCardLength(CardType cardType) {
        return cardType == CardType.AMEX_CARD ? MAX_LENGTH_CARD_NUMBER_AMEX : (cardType == CardType.DINERS_CARD ? MAX_LENGTH_CARD_NUMBER_DINERS : MAX_LENGTH_CARD_NUMBER);
    }

    private static String handleCardNumber(String inputCardNumber, String separator) {
        String unformattedText = inputCardNumber.replace(separator, "");
        CardType cardType = selectCardType(unformattedText);
        String format = CardType.AMEX_CARD.equals(cardType) ? CARD_NUMBER_FORMAT_AMEX : (CardType.DINERS_CARD.equals(cardType) ? CARD_NUMBER_FORMAT_DINERS : CARD_NUMBER_FORMAT);
        StringBuilder sbFormattedNumber = new StringBuilder();
        for (int iIdx = 0, jIdx = 0; (iIdx < format.length()) && (unformattedText.length() > jIdx); iIdx++) {
            if (format.charAt(iIdx) == CHAR_X)
                sbFormattedNumber.append(unformattedText.charAt(jIdx++));
            else
                sbFormattedNumber.append(format.charAt(iIdx));
        }

        return sbFormattedNumber.toString();
    }

    static String formatCardNumber(String inputCardNumber, String separator) {
        String unformattedText = inputCardNumber.replace(separator, "");
        CardType cardType = selectCardType(unformattedText);
        String format = CardType.AMEX_CARD.equals(cardType) ? CARD_NUMBER_FORMAT_AMEX : (CardType.DINERS_CARD.equals(cardType) ? CARD_NUMBER_FORMAT_DINERS : CARD_NUMBER_FORMAT);
        StringBuilder sbFormattedNumber = new StringBuilder();
        for (int iIdx = 0, jIdx = 0; iIdx < format.length(); iIdx++) {
            if ((format.charAt(iIdx) == CHAR_X) && (unformattedText.length() > jIdx))
                sbFormattedNumber.append(unformattedText.charAt(jIdx++));
            else
                sbFormattedNumber.append(format.charAt(iIdx));
        }

        return sbFormattedNumber.toString().replace(SPACE_SEPERATOR, SPACE_SEPERATOR + SPACE_SEPERATOR);
    }

    public static String handleExpiration(String month, String year) {

        return handleExpiration(month + year);
    }


    static String handleExpiration(@NonNull String dateYear) {

        String expiryString = dateYear.replace(SLASH_SEPERATOR, "");

        String text;
        if (expiryString.length() >= 2) {
            String mm = expiryString.substring(0, 2);
            String yy;
            text = mm;

            try {
                if (Integer.parseInt(mm) > 12) {
                    mm = "12"; // Cannot be more than 12.
                }
            } catch (Exception e) {
                mm = "01";
            }

            if (expiryString.length() >= 4) {
                yy = expiryString.substring(2, 4);

                try {
                    Integer.parseInt(yy);
                } catch (Exception e) {

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    yy = String.valueOf(year).substring(2);
                }

                text = mm + SLASH_SEPERATOR + yy;

            } else if (expiryString.length() > 2) {
                yy = expiryString.substring(2);
                text = mm + SLASH_SEPERATOR + yy;
            }
        } else {
            text = expiryString;
        }

        return text;
    }
}
