package com.example.myapplication;

import android.content.Context;

import java.util.HashSet;
import java.util.Set;

public class PairCipher extends Cipher {
    private String cipher;
    private String name;

    public PairCipher(String code) {
        String cipher = parseCipher(code);
        this.name = code;
        this.cipher = cipher;
    }

    @Override
    public String translate(String word) {
        StringBuilder result = new StringBuilder();

        for(char c : word.toCharArray()) {
            int index = cipher.indexOf((int)c);
            if (index != -1) {
                result.append(getReplacement(index));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static void validate(String input, Context context) throws IllegalArgumentException {
        checkForEmptyString(input);

        String parsedInput = input.replaceAll(" ", "").toUpperCase();

        checkForOddLetterNumber(parsedInput);
        checkForExistingCipher(parsedInput, context);
        checkForDuplicateLetters(parsedInput);
    }


    private static void checkForEmptyString(String input) {
        if(input == null || input.equals(""))
            throw new IllegalArgumentException("Podany szyfr jest pusty!");
    }
    private static void checkForOddLetterNumber(String parsedInput) {
        if (parsedInput.length() %2 != 0) {
            throw new IllegalArgumentException("Szyfr musi mieć parzystą liczbę liter.");
        }
    }

    private static void checkForExistingCipher(String parsedInput, Context context) {
        for( Cipher cipher : CipherProvider.getCiphers(context)) {
            if (parsedInput.equals(cipher.toString()))
                throw new IllegalArgumentException("Podany szyfr już istnieje");
        }
    }
    private static void checkForDuplicateLetters(String parsedInput) {
        Set<Character> characterSet = new HashSet<>();

        for(Character ch : parsedInput.toCharArray()) {
            characterSet.add(ch);
        }

        if (characterSet.size() != parsedInput.length()) {
            throw new IllegalArgumentException("W podanym szyfrze są duplikaty liter");
        }
    }


    public static PairCipher addCipher(String cipher, Context context) throws IllegalArgumentException{
        validate(cipher, context);
        PairCipher newCipher = new PairCipher(cipher);
        CipherProvider.addCipher(newCipher, context);
        return newCipher;
    }

    private char getReplacement(int index) {
        return index%2 == 0 ? cipher.charAt(index+1) : cipher.charAt(index-1);
    }

    public String getCipher() {
        return name;
    }

    @Override
    public String toString() {
        return getCipher().toUpperCase();
    }
}
