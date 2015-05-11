package com.bn.nook.ios.manager;

import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.json.TestCaseInfo;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.utils.LoggerUtil;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.models.IDevice;
import com.sofment.testhelper.property.PropertiesManager;

import java.awt.*;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by avsupport on 4/13/15.
 */
public class TestManager {
    private TestHelper testHelper;
    private static TestManager testManager;
    private IDevice iDevice;
    private String pathToBuild;
    public static TestCaseInfo testCaseInfo;
    private String login = "";
    private String pass = "";
    private String purchaseLogin = "";
    private String purchasePass = "";
    private static PropertiesManager propertiesManager;
    private static ParamsParser paramsParser;
    private String searchProduct;
    private String epubProduct;
    private String drpMagazine;

    private TestManager () {
        testHelper = new TestHelper();
        testCaseInfo = new TestCaseInfo();
//        testCaseInfo.setStatusId(Status.FAILED);
        initProperties();
    }

    private void initProperties() {
        propertiesManager = testHelper.getPropertiesManager("properties/test.properties");
        paramsParser = ParamsParser.getInstance();
        pathToBuild = paramsParser.getPathToBuild() == null ?
                propertiesManager.getProperty("PATH_TO_BUILD") :
                paramsParser.getPathToBuild();
        login = propertiesManager.getProperty("LOGIN");
        pass = propertiesManager.getProperty("PASSWORD");
        purchaseLogin = propertiesManager.getProperty("PURCHASE_LOGIN");
        purchasePass = propertiesManager.getProperty("PURCHASE_PASSWORD");
        searchProduct = propertiesManager.getProperty("SEARCH_PRODUCT");
        epubProduct = propertiesManager.getProperty("EPUB_PRODUCT");
        drpMagazine = propertiesManager.getProperty("DRP_MAGAZINE");
    }

    public static String getTestProperty(String key) {
        return propertiesManager.getProperty(key);
    }

    public static TestManager getInstance() {
        if(testManager == null)
            testManager = new TestManager();
        return testManager;
    }

    public static void writeResult() {
        testCaseInfo.writeResult(ParamsParser.getInstance().getPathToResultsFolder() + "/result.json");
    }

    public static ParamsParser getParamsParser() {
        return paramsParser;
    }

    public static PropertiesManager getPropertiesManager() {
        return propertiesManager;
    }

    public IDevice getIDevice(String UUID) {
        if(iDevice == null) {
            iDevice = testHelper.getIOsDriver().getDevice(UUID, new IDevice.Config()
                    .setPathToResultsFolder(ParamsParser.getInstance().getPathToResultsFolder())
                    .setScreenShotsFolder(ParamsParser.getInstance().getPathToResultsFolder()));
            if(iDevice == null) {
                System.out.println("Device with UUID: " + UUID + " is not found... ");
                System.exit(0);
            }
            iDevice.i("Path to results folder: " + ParamsParser.getInstance().getPathToResultsFolder());
            File file = new File(ParamsParser.getInstance().getPathToResultsFolder());
            iDevice.i("result folder exists: " + file.exists());
            LoggerUtil.setIDevice(iDevice);
        }
        return iDevice;
    }

    public TestHelper getTestHelper() {
        return testHelper;
    }

    public String getPathToBuild() {
        return pathToBuild;
    }

//    public void setPathToBuild(String pathToBuild) {
//        this.pathToBuild = pathToBuild;
//    }


    public void setTestCaseInfo(TestCaseInfo testCaseInfo) {
        this.testCaseInfo = testCaseInfo;
    }

    public TestCaseInfo getTestCaseInfo() {
        return testCaseInfo;
    }

    public void retest(String message) throws TestException {
        testCaseInfo.setMessage(message);
        testCaseInfo.setStatusId(4);
        throw new TestException(message).retest();
    }

    public void failTest(String message) throws TestException {
        testCaseInfo.setMessage(message);
        testCaseInfo.setStatusId(5);
        throw new TestException(message).failTest();
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public String getPurchaseLogin() {
        return purchaseLogin;
    }

    public String getPurchasePass() {
        return purchasePass;
    }

    public static void addStep(String s) {
        testCaseInfo.addStep(s);
    }

    public String getSearchProduct() {
        return searchProduct;
    }

    public String getEpubProduct() {
        return epubProduct;
    }

    public String getDrpMagazine() {
        return drpMagazine;
    }

    /*** comparing 2 images
     *  method arguments should be string value of the file path
     *  that should consist of /data/local/tmp/filename.png
     *  where filename.png is the name of your screenshot image
     ***/
    public boolean compareTwoImages(String img1path, String img2path) throws TestException {
        if(!new File(img1path).exists() || !new File(img2path).exists())
            testManager.retest("Some of screenshots not found");
        Image image1 = Toolkit.getDefaultToolkit().getImage(img1path);
        Image image2 = Toolkit.getDefaultToolkit().getImage(img2path);

        try {
            PixelGrabber grab1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
            PixelGrabber grab2 = new PixelGrabber(image2, 0, 0, -1, -1, false);
            int[] data1 = null;

            if (grab1.grabPixels()) {
                int width = grab1.getWidth();
                int height = grab1.getHeight();
                data1 = new int[width * height];
                data1 = (int[]) grab1.getPixels();
            }

            int[] data2 = null;

            if (grab2.grabPixels()) {
                int width = grab2.getWidth();
                int height = grab2.getHeight();
                data2 = new int[width * height];
                data2 = (int[]) grab2.getPixels();
            }
            iDevice.i("Pixels equal: " + java.util.Arrays.equals(data1, data2));
            return java.util.Arrays.equals(data1, data2);

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        throw new TestException("Error in compare image").retest();
    }
}
