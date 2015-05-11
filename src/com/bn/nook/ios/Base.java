package com.bn.nook.ios;

import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.driver.ios.models.IDevice;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * Created by avsupport on 5/11/15.
 */
public class Base {

    private IDevice iDevice;

    public Base() {
        TestManager testManager = TestManager.getInstance();
        ParamsParser paramsParser = ParamsParser.getInstance();
        iDevice = testManager.getIDevice(paramsParser.getDeviceUuid());
    }

    public String getFormattedTime(long timeMs) {
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
        return simpleFormatter.format(timeMs);
    }

    public String getFormattedTime() {
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
        return simpleFormatter.format(System.currentTimeMillis());
    }

    public String takeScreenShot(String name) {
        String screenShotName = getFormattedTime() + " " + name;
        iDevice.takeScreenShot(screenShotName);
        File file = waitForScreenshotCreated(ParamsParser.getInstance().getPathToResultsFolder() + "/" + screenShotName + ".png", 5000);
        return file != null ? file.getAbsolutePath() : ParamsParser.getInstance().getPathToResultsFolder() + "/" + screenShotName + ".png";
    }

    private File waitForScreenshotCreated(String screenShotName, long timeoutMs) {
        long start = System.currentTimeMillis();
        File file = new File(screenShotName);
        while (!file.exists()) {
            if(System.currentTimeMillis() - start > timeoutMs) return file;
        }
        return file;
    }

    public int getRandomInt(int min, int max) {
        Random rand = new Random();

        return rand.nextInt((max - min) + 1) + min;
    }
}
