package com.bn.nook.ios.screen;

import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.models.IDevice;

/**
 * Created by avsupport on 4/24/15.
 */
public abstract class ReaderScreen extends BaseScreen {
    public ReaderScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public abstract boolean isReaderMenuOpened();
    public abstract boolean openReaderMenu();
    public abstract boolean hideReaderMenu();
    public abstract boolean openContents() throws TestException;
    public abstract boolean closeContents() throws TestException;
    public abstract boolean isContentsOpened();
    public abstract boolean openFontSettings() throws TestException;
    public abstract boolean closeFontSettings();
    public abstract boolean isFontSettingsOpened();
    public abstract boolean openInformation();
    public abstract boolean closeInformation();
    public abstract boolean isInformationOpend();
    public abstract boolean openBrightness();
    public abstract boolean closeBrightness();
    public abstract boolean isBrightnessOpened();
    public abstract boolean openSearch();
    public abstract boolean closeSearch();
    public abstract boolean isSearchOpened();
    public abstract boolean addBookmark() throws TestException;
    public abstract boolean removeBookmark() throws TestException;
    public abstract boolean isBookmarkAdded();
    public abstract int[] getCurrentPageInfo();
    public abstract boolean openBookmarkTab() throws TestException;
    public abstract boolean openContentTab() throws TestException;
}
