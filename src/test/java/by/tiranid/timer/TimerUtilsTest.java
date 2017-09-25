package by.tiranid.timer;

import org.junit.Assert;
import org.junit.Test;

public class TimerUtilsTest {


    @Test
    public void testConvertMillisToTime() {
        long threeHours_oneMinuite = 1000 * 60 * 60 * 3 + 1000 * 60;
        String time = TimerUtils.convertMillisToTime(threeHours_oneMinuite);
        String checkTime = "03:01:00";
        Assert.assertEquals(checkTime, time);
    }
}
