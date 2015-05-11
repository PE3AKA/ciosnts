package com.bn.nook.ios.screen.reader;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.screen.ReaderScreen;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.models.IDevice;
import com.sofment.testhelper.enums.Matcher;

/**
 * Created by avsupport on 5/5/15.
 */
public class EpubReaderScreen extends ReaderScreen {

    public EpubReaderScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    @Override
    public boolean isReaderMenuOpened() {
        return waiter.waitForElementByNameVisible(Constants.Reader.Epub.CONTENTS, 1, new IConfig().setMaxLevelOfElementsTree(2).setMatcher(Matcher.ContainsIgnoreCase)) != null;
    }

    @Override
    public boolean openReaderMenu() {
        if(isReaderMenuOpened()) return true;
        int[] screenSize = iDevice.getScreenSize();
        clicker.clickByXY(screenSize[0]/2, screenSize[1]/2);
        return isReaderMenuOpened();
    }

    @Override
    public boolean hideReaderMenu() {
        if(!isReaderMenuOpened()) return true;
        int[] screenSize = iDevice.getScreenSize();
        clicker.clickByXY(screenSize[0]/2, screenSize[1]/2);
        return !isReaderMenuOpened();
    }

    @Override
    public boolean openContents() throws TestException {
        if(!isContentsOpened()) {
            openReaderMenu();
            Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.CONTENTS, 1, new IConfig().setMaxLevelOfElementsTree(2));
            if(element == null) {
                testManager.retest("Contents button " + Constants.Reader.Epub.CONTENTS + " is not found");
            }
            TestManager.addStep("open contents");
            clicker.clickOnElement(element);
            return isContentsOpened();
        }
        return false;
    }

    @Override
    public boolean closeContents() throws TestException {
        if(isContentsOpened()) {
            Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.Contents.CLOSE_BUTTON, 1, new IConfig().setMaxLevelOfElementsTree(2));
            if(element == null) {
                testManager.retest("close button " + Constants.Reader.Epub.Contents.CLOSE_BUTTON + " is not found");
            }
            TestManager.addStep("click on button " + Constants.Reader.Epub.Contents.CLOSE_BUTTON);
            clicker.clickOnElement(element);
        }
        return false;
    }

    @Override
    public boolean isContentsOpened() {
        return waiter.waitForElementByNameVisible(Constants.Reader.Epub.Contents.CONTENTS_TAB, 1, new IConfig().setMaxLevelOfElementsTree(2)) != null;
    }

    @Override
    public boolean openFontSettings() {
        return false;
    }

    @Override
    public boolean closeFontSettings() {
        return false;
    }

    @Override
    public boolean isFontSettingsOpened() {
        return false;
    }

    @Override
    public boolean openInformation() {
        return false;
    }

    @Override
    public boolean closeInformation() {
        return false;
    }

    @Override
    public boolean isInformationOpend() {
        return false;
    }

    @Override
    public boolean openBrightness() {
        return false;
    }

    @Override
    public boolean closeBrightness() {
        return false;
    }

    @Override
    public boolean isBrightnessOpened() {
        return false;
    }

    @Override
    public boolean openSearch() {
        return false;
    }

    @Override
    public boolean closeSearch() {
        return false;
    }

    @Override
    public boolean isSearchOpened() {
        return false;
    }

    @Override
    public boolean addBookmark() throws TestException {
        if(isBookmarkAdded()) return true;
        Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.ADD_BOOKMARK, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) {
            testManager.retest("add bookmark button is not found (" + Constants.Reader.Epub.ADD_BOOKMARK + ")");
        }
        TestManager.addStep("click on button add bookmark " + Constants.Reader.Epub.ADD_BOOKMARK);
        clicker.clickOnElement(element);
        return isBookmarkAdded();
    }

    @Override
    public boolean removeBookmark() throws TestException {
        if(!isBookmarkAdded()) return true;
        Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.REMOVE_BOOKMARK, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) {
            testManager.retest("add bookmark button is not found (" + Constants.Reader.Epub.REMOVE_BOOKMARK + ")");
        }
        TestManager.addStep("click on button add bookmark " + Constants.Reader.Epub.REMOVE_BOOKMARK);
        clicker.clickOnElement(element);
        return !isBookmarkAdded();
    }

    @Override
    public boolean isBookmarkAdded() {
        return waiter.waitForElementByNameVisible(Constants.Reader.Epub.REMOVE_BOOKMARK, 1, new IConfig().setMaxLevelOfElementsTree(2)) != null;
    }

    @Override
    public int[] getCurrentPageInfo() {
        openReaderMenu();
        int[] pageInfo = new int[]{0, 0};
        Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.PAGE_POSITION, 1, new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(2));
        if(element == null) return pageInfo;
        String info = element.getValue();
        String[] splittedInfo = info.trim().split(Constants.Reader.Epub.PAGE_POSITION);
        if(splittedInfo.length == 2) {
            pageInfo[0] = Integer.parseInt(splittedInfo[0]);
            pageInfo[1] = Integer.parseInt(splittedInfo[1]);
        }
        return pageInfo;
    }

    @Override
    public boolean openBookmarkTab() throws TestException {
        openReaderMenu();
        openContents();
        Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.Contents.BOOKMARKS_TAB, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) {
            testManager.retest("can not found " + Constants.Reader.Epub.Contents.BOOKMARKS_TAB);
        }
        TestManager.addStep("click on " + Constants.Reader.Epub.Contents.BOOKMARKS_TAB);
        return clicker.clickOnElement(element);
    }

    @Override
    public boolean openContentTab() throws TestException {
        openReaderMenu();
        openContents();
        Element element = waiter.waitForElementByNameVisible(Constants.Reader.Epub.Contents.CONTENTS_TAB, 1, new IConfig().setMaxLevelOfElementsTree(2));
        if(element == null) {
            testManager.retest("can not found " + Constants.Reader.Epub.Contents.CONTENTS_TAB);
        }
        TestManager.addStep("click on " + Constants.Reader.Epub.Contents.CONTENTS_TAB);
        return clicker.clickOnElement(element);
    }


}
