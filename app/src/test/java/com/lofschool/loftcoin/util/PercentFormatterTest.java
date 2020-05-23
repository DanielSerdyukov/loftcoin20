package com.lofschool.loftcoin.util;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class PercentFormatterTest {

    private PercentFormatter formatter;

    @Before
    public void setUp() throws Exception {
        formatter = new PercentFormatter();
    }

    @Test
    public void string_contains_exact_two_fractional_digits() {
        assertThat(formatter.format(1d)).isEqualTo("1.00%");
        assertThat(formatter.format(1.2345)).isEqualTo("1.23%");
        assertThat(formatter.format(1.2356)).isEqualTo("1.24%");
    }

}