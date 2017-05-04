/**
 * Copyright 2017 Legion.  All rights reserved.
 * Created by aldo on 3/15/17.
 */
package utils;

import static junit.framework.Assert.*;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LegionUtilsTest {

    @Test
    public void testParseDateFromJsonFormat () throws Exception {
        Date d = LegionUtils.parseDateFromJsonString("2017-03-17T15:00:30.249-07");
        assertEquals("Invalid parsed date", d.getTime(), 1489788030249L);
        System.out.println(d.getTime() == 1489788030249L);
    }

    @Test
    public void testParseDateFromJsonFormatAndCompare () throws Exception {
        Date jsonDate = LegionUtils.parseDateFromJsonString("2017-03-17T15:00:30.249-07");
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        cal.set(2017, 2, 16, 0, 0, 0);
        Date localDate = cal.getTime();
        System.out.println("jsonDate : "+jsonDate);
        System.out.println("localDate : " + localDate);
        assertTrue("Local date after json date", localDate.before(jsonDate));
    }


    @Test
    public void testUntilDateWithCurrent() throws Exception{
        Date untilDate = LegionUtils.parseDateFromJsonString("2017-03-16T05:30:40.254-07");
        Date now = Calendar.getInstance().getTime();

        System.out.println("until : "+untilDate);
        System.out.println("now : "+now);
        System.out.println(untilDate.after(now));

    }
}
