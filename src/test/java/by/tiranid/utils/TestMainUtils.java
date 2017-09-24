package by.tiranid.utils;

import org.junit.Assert;
import org.junit.Test;

public class TestMainUtils {


    // config.properties is created
    @Test
    public void testLoadDefaultProperties() throws Exception {
        MainUtils.loadDefaultProperties();
        String user = MainClientProperties.properties.getProperty("userLogin");
        Assert.assertEquals(user, "tiranid");
    }

}
