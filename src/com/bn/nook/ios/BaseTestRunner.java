package com.bn.nook.ios;

import com.bn.nook.ios.annotation.Condition;
import com.bn.nook.ios.annotation.PostCondition;
import com.bn.nook.ios.annotation.PreCondition;
import com.bn.nook.ios.assistant.Preparer;
import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.model.TestCase;
import com.bn.nook.ios.param.ParamsParser;
import com.bn.nook.ios.screen.*;
import com.bn.nook.ios.screen.reader.DrpReaderScreen;
import com.bn.nook.ios.screen.reader.EpubReaderScreen;
import com.bn.nook.ios.utils.LoggerUtil;
import com.bn.nook.ios.utils.NookUtil;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.helpers.Clicker;
import com.sofment.testhelper.driver.ios.helpers.Getter;
import com.sofment.testhelper.driver.ios.helpers.Scroller;
import com.sofment.testhelper.driver.ios.helpers.Waiter;
import com.sofment.testhelper.driver.ios.models.IDevice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static com.bn.nook.ios.utils.LoggerUtil.i;

/**
 * Created by automation on 4/16/15.
 */
public class BaseTestRunner extends Base{

    protected PreCondition mPreCondition = null;
    protected PostCondition mPostCondition = null;

    protected IDevice iDevice;
    protected TestHelper testHelper;
    protected TestManager testManager;
    protected ParamsParser paramsParser;
    protected Waiter waiter;
    protected Getter getter;
    protected Clicker clicker;
    protected Scroller scroller;
    protected NookUtil nookUtil;
    protected BaseScreen baseScreen;
    protected OobeScreen oobeScreen;
    protected DrpReaderScreen drpReaderScreen;
    protected LibraryScreen libraryScreen;
    protected ReaderScreen readerScreen;
    protected SettingsScreen settingsScreen;
    protected SearchScreen searchScreen;
    protected ProductDetailsScreen productDetailsScreen;
    protected Preparer preparer;

//    public abstract void setUpAlertHandler(IDevice iDevice);

    public BaseTestRunner() {
        testManager = TestManager.getInstance();
        testHelper = testManager.getTestHelper();
        paramsParser = ParamsParser.getInstance();
        iDevice = testManager.getIDevice(paramsParser.getDeviceUuid());
        if(iDevice == null) {
            LoggerUtil.e("Device with uuid " + paramsParser.getDeviceUuid() + " is not found");
            System.exit(0);
        }
        waiter = iDevice.getWaiter();
        getter = iDevice.getGetter();
        clicker = iDevice.getClicker();
        scroller = iDevice.getScroller();

        nookUtil = new NookUtil(testManager, testHelper, paramsParser, iDevice);
        preparer = new Preparer(iDevice, nookUtil, paramsParser);
//        setUpAlertHandler(iDevice);

        setUp();
    }

    private void launchApp() {
        TestCase.TestCaseConfig testCaseConfig = TestLauncher.testCaseConfig;
        String pathToBuild = testCaseConfig.getPathToApp();
        if(testCaseConfig.isReinstallApp()) {
            iDevice.uninstallApplication(pathToBuild);
            iDevice.installApplication(pathToBuild);
        }
        iDevice.launchApplication(pathToBuild);
    }

    protected void setUp() {
        i("setUp");
        launchApp();
        paramsParser = ParamsParser.getInstance();
        parseAnnotations();

        try {
            processPreConditions();
        } catch (TestException e) {
            e.printStackTrace();
        }
    }

    private void parseAnnotations() {
        for(Method m: this.getClass().getMethods()) {
            if (!m.getName().contains(paramsParser.getTestId())) continue;

            i("ANNOTATIONS FOUND!");
            for(Annotation a: m.getDeclaredAnnotations()) {
                if(a instanceof PreCondition) {
                    i("PreCondition FOUND!");
                    mPreCondition = (PreCondition) a;
                } else if(a instanceof PostCondition) {
                    i("PostCondition FOUND!");
                    mPostCondition = (PostCondition) a;
                }
            }
        }
        TestManager.testCaseInfo.setTitle(mPreCondition.testTitle());
        TestManager.testCaseInfo.setId(mPreCondition.testId());
    }

    private void processPreConditions() throws TestException {
        if (mPreCondition == null) return;

        Condition[] preconditionActions = mPreCondition.preConditions();
        for(Condition precondition : preconditionActions) {
            TestManager.addStep("PRECONDITION: " + precondition.name());
            executeCondition(precondition);
        }
    }

    private void processPostConditions() throws Exception {
        if(mPostCondition == null) return;
        Condition[] postConditions = mPostCondition.postConditions();
        for(Condition postCondition : postConditions) {
            TestManager.addStep("POST CONDITION: " + postCondition.name());
            executeCondition(postCondition);
        }
    }

    private void executeCondition(Condition condition) throws TestException {
        iDevice.i("Annotation condition : " + condition.name());
        TestManager.addStep("Annotation condition:" + condition.name());
       switch (condition) {
            case LOGIN:
                baseScreen = nookUtil.getCurrentScreen(true);
                iDevice.i("##################### Detected " + nookUtil.screenModel.name());
                if(nookUtil.screenModel == ScreenModel.OOBE) {
                    oobeScreen = (OobeScreen) baseScreen;
                    oobeScreen.signIn(Constants.DEFAULT_TIMEOUT);
                }
                break;
            case SIGN_OUT:
                baseScreen = nookUtil.getCurrentScreen(true);
                iDevice.i("##################### Detected " + nookUtil.screenModel.name());
                if(nookUtil.screenModel == ScreenModel.LIBRARY) {
                    libraryScreen = (LibraryScreen) baseScreen;
                    libraryScreen.openSettings();
                    nookUtil.waitForScreenModel(ScreenModel.SETTINGS, 5000);
                    baseScreen = nookUtil.getCurrentScreen(false);
                    if(nookUtil.screenModel == ScreenModel.SETTINGS) {
                        settingsScreen = (SettingsScreen) baseScreen;
                        settingsScreen.clickOnLogOutButton();
                        nookUtil.waitForScreenModel(ScreenModel.OOBE, Constants.DEFAULT_TIMEOUT);
                    }
                }
                break;
           case OPEN_PRODUCT:
               String product = TestManager.getTestProperty(mPreCondition.productName().name());
                preparer.openProduct(product, mPreCondition.productType());
               break;
           case UNARCHIVE_PRODUCT:
               preparer.unArchiveProduct(nookUtil, TestManager.getTestProperty(mPreCondition.productName().name()));
               break;
           case ARCHIVE_PRODUCT:
               preparer.archiveProduct(nookUtil, TestManager.getTestProperty(mPreCondition.productName().name()));
               break;
            default: break;
        }
    }

    public void initOobeScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.OOBE, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.OOBE) testManager.retest("Necessary oobe screen is not found. Expected: " + ScreenModel.OOBE.name() + ", actual : " + nookUtil.screenModel.name());
        oobeScreen = ((OobeScreen) baseScreen);
    }

    public void initLibraryScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.LIBRARY, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.LIBRARY) testManager.retest("Necessary library screen is not found. Expected: " + ScreenModel.LIBRARY.name() + ", actual : " + nookUtil.screenModel.name());
        libraryScreen = ((LibraryScreen) baseScreen);
    }

    public void initReaderScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.READER, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.EPUB_READER &&
                nookUtil.screenModel != ScreenModel.DRP_READER) testManager.retest("Necessary reader screen is not found. Expected: " + ScreenModel.READER.name() + ", actual : " + nookUtil.screenModel.name());
        if(baseScreen instanceof EpubReaderScreen) {
            readerScreen = (EpubReaderScreen) baseScreen;
        } else if(baseScreen instanceof DrpReaderScreen) {
            readerScreen = (DrpReaderScreen) baseScreen;
        }
    }

    public void initEbubReaderScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.EPUB_READER, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.EPUB_READER) testManager.retest("Necessary epub reader screen is not found. Expected: " + ScreenModel.READER.name() + ", actual : " + nookUtil.screenModel.name());
        if(baseScreen instanceof EpubReaderScreen) {
            readerScreen = (EpubReaderScreen) baseScreen;
        } else if(baseScreen instanceof DrpReaderScreen) {
            readerScreen = (DrpReaderScreen) baseScreen;
        }
    }

    public void initDrpReaderScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.DRP_READER, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.DRP_READER) testManager.retest("Necessary drp reader screen is not found. Expected: " + ScreenModel.READER.name() + ", actual : " + nookUtil.screenModel.name());
        if(baseScreen instanceof EpubReaderScreen) {
            readerScreen = (EpubReaderScreen) baseScreen;
        } else if(baseScreen instanceof DrpReaderScreen) {
            readerScreen = (DrpReaderScreen) baseScreen;
        }
    }

    public void initSearchScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.SEARCH, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.SEARCH) testManager.retest("Necessary search screen is not found. Expected: " + ScreenModel.SEARCH.name() + ", actual : " + nookUtil.screenModel.name());
        searchScreen = ((SearchScreen) baseScreen);
    }

    public void initProductDetailsScreen() throws TestException {
        nookUtil.waitForScreenModel(ScreenModel.PRODUCT_DETAILS, Constants.DEFAULT_TIMEOUT);
        baseScreen = nookUtil.getCurrentScreen(false);
        if(nookUtil.screenModel != ScreenModel.PRODUCT_DETAILS)
            testManager.retest("Necessary search screen is not found. Expected: " +
                    ScreenModel.PRODUCT_DETAILS.name() + ", actual : " +
                    nookUtil.screenModel.name());
        productDetailsScreen = ((ProductDetailsScreen) baseScreen);
    }

    protected void tearDown() {
        i("tearDown");
        try {
            processPostConditions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
