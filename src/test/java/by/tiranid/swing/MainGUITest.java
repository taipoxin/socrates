package by.tiranid.swing;

import by.tiranid.swing.layouts.MainGUI;
import by.tiranid.sync.FileUtils;
import by.tiranid.web.RequestSender;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.util.List;

public class MainGUITest {

    MainGUI gui;

    @Before
    public void setUp() {
        gui = new MainGUI();
    }

    // TODO : пофиксить establishing ssl connection
    @Test
    public void testSyncAndClean() throws Exception {
        String path = FileUtils.setFilePath();
        FileUtils.saveDataToFile("123456");
        FileUtils.saveDataToFile("223456");
        FileUtils.saveDataToFile("323456");


        InetAddress addr = InetAddress.getLocalHost();
        String server_Ip = addr.getHostAddress();
        RequestSender.setServerIp(server_Ip);


        gui.syncAndClean("login");


        String uri = "http://" + RequestSender.getServerIp() + ":" + RequestSender.getServerPort() + RequestSender.getPostIterationURI();
        boolean bool = RequestSender.isGetConnectionTo(uri);
        if (bool) {
            String filepath = path + "login.dxl";
            System.out.println(filepath);
            List<String> pair = FileUtils.readFromDxlToList(filepath);
            Assert.assertEquals(0, pair.size());
        }


    }
}
