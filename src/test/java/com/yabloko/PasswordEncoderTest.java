package com.yabloko;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class PasswordEncoderTest {

    @Test
    public void testEncode() {
        PasswordEncoderForTest passwordEncoderForTest = new PasswordEncoderForTest();
        Assert.assertEquals("aabc", passwordEncoderForTest.encode("cbaa"));

        // можно выполинть этот тест с использованием МАТЧЕРОВ HAMCREST
        Assert.assertThat("aabc", Matchers.containsString(passwordEncoderForTest.encode("cbaa")));
    }
}
