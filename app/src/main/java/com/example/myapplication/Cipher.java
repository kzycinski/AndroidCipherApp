package com.example.myapplication;

public abstract class Cipher {
    public abstract String translate(String word);

    protected String parseCipher(String cipher){
        String newCipher = cipher.toLowerCase().replaceAll(" ", "");
        return newCipher + newCipher.toUpperCase();
    }
}
