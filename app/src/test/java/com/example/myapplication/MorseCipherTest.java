package com.example.myapplication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MorseCipherTest {

    MorseCipher morseCipher;

    @Before
    public void init() {
        morseCipher = new MorseCipher();
    }

    @Test
    public void testMorseCipherEmptyMessage() {
        Assert.assertEquals("", morseCipher.translate(""));
    }

    @Test
    public void testMorseCodeDecode() {
        Assert.assertEquals("abc", morseCipher.translate(".- -... -.-."));
    }

    @Test
    public void testMorseCodeDecodeWithSpace() {
        Assert.assertEquals("abc abc", morseCipher.translate(".- -... -.-. / .- -... -.-."));
    }

    @Test
    public void testMorseCodeEncode() {
        Assert.assertEquals(".- -... -.-. ", morseCipher.translate("abc"));
    }

    @Test
    public void testMorseCodeEncodeWithSpace() {
        Assert.assertEquals(".- -... -.-. / .- -... -.-. ", morseCipher.translate("abc abc"));
    }

}
