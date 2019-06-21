package com.example.myapplication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PairCipherTest {

    PairCipher pairCipher;

    @Before
    public void init() {
        pairCipher = new PairCipher("GADERYPOLUKI");
    }

    @Test
    public void testPairCipherConstructorEmptyCode() {
        pairCipher = new PairCipher("");
        Assert.assertEquals("AbC", pairCipher.translate("AbC"));
    }

    @Test
    public void testPairCipherConstructorWithSpecialCharacter() {
        pairCipher = new PairCipher("ąć!@");
        Assert.assertEquals("ąć!@", pairCipher.translate("ćą@!"));
    }

    @Test
    public void testPairCipherEmptyMessage() {
        Assert.assertEquals("", pairCipher.translate(""));
    }

    @Test
    public void testPairCipherNormalMessage() {
        Assert.assertEquals("AGEDYROPULIK", pairCipher.translate("GADERYPOLUKI"));
    }

    @Test
    public void testPairCipherWithUpperAndLowerCase() {
        Assert.assertEquals("aAa", pairCipher.translate("gGg"));
    }

    @Test
    public void testPairCipherWithSpace() {
        Assert.assertEquals("aA a", pairCipher.translate("gG g"));
    }

    @Test
    public void testPairCipherWithSpecialChars() {
        Assert.assertEquals("aA !@ąć Aa", pairCipher.translate("gG !@ąć Gg"));
    }
}
