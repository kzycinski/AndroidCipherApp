package com.example.myapplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MorseCipher extends Cipher {

    private Map<Character, String> morseCode = new HashMap<>();
    private Map<String, Character> reverseMorseCode = new HashMap<>();

    public MorseCipher() {
        initMorseMap();
    }


    @Override
    public String translate(String word) throws IllegalArgumentException{
        if(word == null || word.equals(""))
            return "";

        if (word.charAt(0) == ' ' || word.charAt(0) == '.' || word.charAt(0) == '-')
            return decode(word);
        else
            return encode(word);
    }

    private void initMorseMap(){
        try {
            InputStream props = MorseCipher.class.getResourceAsStream("/morse_code.properties");
            Properties properties = new Properties();
            properties.load(props);
            for (String key : properties.stringPropertyNames()) {
                String morse = properties.getProperty(key);
                morseCode.put(key.charAt(0), morse);
                reverseMorseCode.put(morse, key.charAt(0));
            }

        } catch (IOException e) {
            System.out.println("No properties file. Contact with administrator");
        }
    }

    public final String encode(String input) throws IllegalArgumentException {
        StringBuilder stringBuilder = new StringBuilder();
        for(char character : input.toLowerCase().toCharArray()) {
            String result = morseCode.get(character);
            if (result == null)
                throw new IllegalArgumentException("Znak: " + character + " nie jest obsługiwany, zastąp go lub skontaktuj się z administratorem");
            else
                stringBuilder.append(result).append(" ");
        }
        return stringBuilder.toString();
    }

    public final String decode(String input) throws IllegalArgumentException{
        StringBuilder stringBuilder = new StringBuilder();
        for(String string : input.split(" ")) {
            Character result = reverseMorseCode.get(string);
            if (result == null)
                throw new IllegalArgumentException("Błędny znak:" + string);
            else
                stringBuilder.append(result);
        }
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "Alfabet Morse'a";
    }
}
