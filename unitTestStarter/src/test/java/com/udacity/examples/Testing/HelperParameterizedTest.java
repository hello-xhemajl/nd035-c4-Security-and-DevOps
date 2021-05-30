package com.udacity.examples.Testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class HelperParameterizedTest {
    private String actualColor;
    private String expectedColor;

    public HelperParameterizedTest(String actualColor, String expectedColor) {
        super();
        this.actualColor = actualColor;
        this.expectedColor = expectedColor;
    }

    @Parameterized.Parameters
    public static Collection initData() {
        String actualExpected[][] = {{"blue", "blue"}, {"orange", "blue"}};
        return Arrays.asList(actualExpected);
    }

    @Test
    public void eachColor_isEqual() {
        assertEquals(expectedColor, actualColor);
    }
}
