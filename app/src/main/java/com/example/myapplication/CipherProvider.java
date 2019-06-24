package com.example.myapplication;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CipherProvider {
    private static List<Cipher> ciphers;
    private static String[] defaultPairCiphers = {"GADERY POLUKI", "POLITYKA RENU", "KACE MINUTOWY", "OKULAR MINETY", "KONIEC MATURY"};
    private static final String FILE_NAME = "pairCiphers.txt";

    public static void initCiphers(Context context) {
        getConfig(context);
        ciphers = getCiphersFromInternalStorage(context);
    }

    private static void getConfig(Context context) {
        try {
            context.openFileInput(FILE_NAME);
        } catch (FileNotFoundException e) {
            initConfig(context);
        }
    }

    private static void initConfig(Context context) {
        for (String cipher : defaultPairCiphers) {
            saveCipherToInternalStorage(cipher, context);
        }
    }

    private static List<Cipher> getCiphersFromInternalStorage(Context context) {
        List<Cipher> ciphers = new ArrayList<>();
        ciphers.add(new MorseCipher());
        FileInputStream inputStream = null;
        try {
            inputStream = context.openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                ciphers.add(new PairCipher(text));
            }

        } catch (IOException e) {
            System.out.println("Cannot find file: " + FILE_NAME + ". Please contact with administrator.");

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("Cannot close file: " + FILE_NAME + ". Please contact with administrator.");
                }
            }
            return ciphers;
        }
    }

    public static List<Cipher> getCiphers(Context context) {
        if (ciphers == null) {
            initCiphers(context);
        }
        return new ArrayList<>(ciphers);
    }

    public static void addCipher(Cipher cipher, Context context) {
        ciphers.add(cipher);
        saveCipherToInternalStorage(cipher.toString(), context);
    }

    private static void saveCipherToInternalStorage(String cipher, Context context) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fileOutputStream.write((cipher + "\n").getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void removeCipher(Cipher cipher, Context context) {
        ciphers.remove(cipher);
        updateInternalStorageCiphers(context);
    }

    private static void updateInternalStorageCiphers(Context context) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            for (Cipher cipher : ciphers) {
                if( cipher instanceof PairCipher)
                    fileOutputStream.write((cipher.toString() + "\n").getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void restoreDefaults(Context context) {
        context.deleteFile(FILE_NAME);
        initCiphers(context);
    }
}
