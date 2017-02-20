package com.iglin.lab3_database;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        Calendar cal2 = (Calendar) cal.clone();
        System.out.println(new Date(cal.getTimeInMillis()).toString());
        for (int i = 0; i < 5; i++) {
            cal.add(Calendar.MONTH, -1);
            System.out.println(new Date(cal.getTimeInMillis()).toString());
        }
        System.out.println(new Date(cal2.getTimeInMillis()).toString());
    }
}