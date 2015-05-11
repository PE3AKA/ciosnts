package com.bn.nook.ios.screen;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.exception.TestException;
import com.bn.nook.ios.manager.TestManager;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.TestHelper;
import com.sofment.testhelper.driver.ios.config.IConfig;
import com.sofment.testhelper.driver.ios.elements.Element;
import com.sofment.testhelper.driver.ios.enams.UIAElementType;
import com.sofment.testhelper.driver.ios.models.IDevice;
import com.sofment.testhelper.enums.Matcher;

import java.util.ArrayList;

/**
 * Created by avsupport on 5/5/15.
 */
public class SearchScreen extends BaseScreen {
    public SearchScreen(TestManager testManager, TestHelper testHelper, ParamsParser paramsParser, IDevice iDevice) {
        super(testManager, testHelper, paramsParser, iDevice);
    }

    public boolean closeSearch() {
        Element closeButton = getCloseButton();
        if(getCloseButton() != null) {
            TestManager.addStep("click on the close button");
            return clicker.clickOnElement(closeButton);
        }
        return false;
    }

    public Element getCloseButton() {
        return getter.getElementByName(Constants.Search.CANCEL, new IConfig().setMaxLevelOfElementsTree(2));
    }

    public Element findSample() {
        Element sample = waiter.waitForElementByNameExists(Constants.Search.SAMPLE, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(3));
        if(sample != null) {
            TestManager.addStep("swipe to sample product");
            if(scroller.scrollToVisible(sample)) return sample;
        }
        return null;
    }

    public Element findNotDownloadedProduct() throws TestException {
        Element collectionView = waiter.waitForElementByClassVisible(UIAElementType.UIACollectionView, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(2));
        if(collectionView == null) {
            testManager.retest("collection view with search results is not found");
        }
        Element product = waiter.waitForElementByNameExists(Constants.Search.NOT_DOWNLOADED, Constants.DEFAULT_TIMEOUT, new IConfig().setMaxLevelOfElementsTree(1).setMatcher(Matcher.ContainsIgnoreCase).setParentElement(collectionView));
        if(product != null) {
            TestManager.addStep("swipe to downloaded product");
            scroller.scrollToVisible(product);
            return product;
        }
        return null;
    }

    public ArrayList<Element> getSearchResults(int minSearchResultProducts, long timeout) throws TestException {
        return getProducts(minSearchResultProducts, timeout);
    }

    public boolean downloadProduct(Element product) throws TestException {
        if(product == null) return false;
        TestManager.addStep("click on product " + testManager.getEpubProduct() + " to download");
        clicker.clickOnElement(product);

        int secure = 0;
        while (true) {
            if(waiter.waitForElementByNameGone(Constants.Search.DOWNLOADING, Constants.DOWNLOAD_PRODUCT_TIMEOUT, new IConfig().setMatcher(Matcher.ContainsIgnoreCase).setParentElement(product))) {
                secure ++;
                iDevice.sleep(1000);
            } else {
                return false;
            }
            if(secure >= 5) return true;
        }
    }
}
