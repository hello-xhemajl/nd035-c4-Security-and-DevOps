package com.udacity.examples.Testing;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HelperTest {

    @Test
    public void isCountThree() {
        List<String> people = Arrays.asList("mali", "tringa");
        long count = Helper.getCount(people);

        assertEquals(3, count);
    }

    @Test
    public void allStatisticCorrect() {
        List<Integer> yearsOfExperience
                = Arrays.asList(13,4,15,6,17,8,19,1,2,3);
        IntSummaryStatistics stats = Helper.getStats(yearsOfExperience);

        assertEquals(19, stats.getMax());
        assertEquals(88 , stats.getSum());
        assertEquals(8.8, stats.getAverage(), 0.01);
    }

    @Test
    public void merge(){
        List<String> people = Arrays.asList("mali", "tringa");
        String peopleJoined = Helper.getMergedList(people);

        assertEquals("mali, tringa", peopleJoined);

    }
	
}
