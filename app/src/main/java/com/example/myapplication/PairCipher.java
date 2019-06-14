package com.example.myapplication;

public class PairCipher extends Cipher {
    private String cipher;
    private String name;

    public PairCipher(String code) {
        validateCode(code);
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
