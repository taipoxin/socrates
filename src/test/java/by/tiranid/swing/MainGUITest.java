package by.tiranid.swing;

import by.tiranid.swing.layouts.MainGUI;
import by.tiranid.sync.FileUtils;
import by.tiranid.web.RequestSender;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MainGUITest {

    MainGUI gui;

    @Before
    public void setUp() {
        gui = new MainGUI();
    }

    // TODO : пофиксить establishing ssl connection
    @Test
    public void testSyncAndClean() {
        String path = FileUtils.setFilePath();
        FileUtils.saveDataToFile("123456");
        FileUtils.saveDataToFile("223456");
        FileUtils.saveDataToFile("323456");
        gui.syncAndClean("login");

        boolean bool = RequestSender.isGetConnectionTo(RequestSender.postIterationURI);
        if (bool) {
            String filepath = path + "login.dxl";
            System.out.println(filepath);
            List<String> pair = FileUtils.readFromDxlToList(filepath);
            Assert.assertEquals(0, pair.size());
        }


    }
}
