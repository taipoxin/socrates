package by.tiranid.swing;

import by.tiranid.sync.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class MainGUITest {

    MainGUI gui;

    @Before
    public void setUp() {
        gui = new MainGUI();
    }

    // TODO : пофиксить establishing ssl connection
    // TODO : дописать
    @Test
    public void testSyncAndClean() {
        FileUtils.setFilePath();
        gui.syncAndClean("login");
    }
}
