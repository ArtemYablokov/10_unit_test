package com.yabloko;

import org.junit.Assert;
import org.junit.Test;

// самый тупой пример ЮНИТ/МОДУЛЬНОГО теста
public class SimpleTest {

    @Test
    public void simpleTestMethod(){
        int[] x = new int[]{1,};
        int[] y = new int[]{1,};

        Assert.assertArrayEquals(x, y);
    }
}
