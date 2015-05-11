package com.bn.nook.ios.manager;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.param.ParamsParser;
import com.sofment.testhelper.driver.ios.config.IAlertConfig;
import com.sofment.testhelper.driver.ios.models.IDevice;
import com.sofment.testhelper.driver.ios.models.alert.AlertCondition;
import com.sofment.testhelper.driver.ios.models.alert.AlertHandler;
import com.sofment.testhelper.driver.ios.models.alert.AlertItem;
import com.sofment.testhelper.enums.Matcher;

/**
 * Created by avsupport on 5/7/15.
 */
public class AlertManager {

    private TestManager testManager;
    private ParamsParser paramsParser;

    public AlertManager() {
        testManager = TestManager.getInstance();
        paramsParser = ParamsParser.getInstance();
    }

    public void inputShelfHandler() {
        TestManager.setRandomShelfName(TestManager.getRandomString(5));
        final IDevice iDevice = testManager.getIDevice(paramsParser.getDeviceUuid());
        final AlertHandler alertHandler = new AlertHandler();
        alertHandler.logMessage("alert appeared");
        AlertItem title = alertHandler.waitForElementByNameVisible(Constants.My_Shelves.ALERT_TITLE_CREATE_SHELF, 1,
                new IAlertConfig().setMaxLevelOfElementsTree(3).setMatcher(Matcher.EqualsIgnoreCase));

        alertHandler.createElementNotNullCondition(title, new AlertCondition.ConditionResults() {
            @Override
            public void positiveResult() {
                final AlertItem buttonOk = alertHandler.waitForElementByNameVisible(Constants.My_Shelves.SAVE_BUTTON, 1,
                        new IAlertConfig().setMatcher(Matcher.EqualsIgnoreCase).setMaxLevelOfElementsTree(4));
                alertHandler.createElementNotNullCondition(buttonOk, new AlertCondition.ConditionResults() {
                    @Override
                    public void positiveResult() {
                        alertHandler.inputText(TestManager.getRandomShelfName());
                        alertHandler.clickOnElement(buttonOk);
                        alertHandler.returnBoolean(true);
                    }

                    @Override
                    public void negativeResult() {}
                });
            }

            @Override
            public void negativeResult() {

            }
        });
        alertHandler.returnBoolean(false);
        alertHandler.push(iDevice);
    }

    public void archiveHandler() {
        final IDevice iDevice = testManager.getIDevice(paramsParser.getDeviceUuid());
        final AlertHandler alertHandler = new AlertHandler();
        alertHandler.logMessage("alert appeared");
        AlertItem title = alertHandler.waitForElementByNameVisible(Constants.ProductDetails.ARCHIVE_TITLE, 1,
                new IAlertConfig().setMaxLevelOfElementsTree(3).setMatcher(Matcher.EqualsIgnoreCase));//  1, 0, false, null, 4);

        alertHandler.createElementNotNullCondition(title, new AlertCondition.ConditionResults() {
            @Override
            public void positiveResult() {
                final AlertItem buttonOk = alertHandler.waitForElementByNameVisible(Constants.ProductDetails.ARCHIVE_BUTTON, 1,
                        new IAlertConfig().setMatcher(Matcher.EqualsIgnoreCase).setMaxLevelOfElementsTree(4));
                alertHandler.createElementNotNullCondition(buttonOk, new AlertCondition.ConditionResults() {
                    @Override
                    public void positiveResult() {
                        alertHandler.clickOnElement(buttonOk);
                        alertHandler.returnBoolean(true);
                    }

                    @Override
                    public void negativeResult() {}
                });
            }

            @Override
            public void negativeResult() {

            }
        });
        alertHandler.returnBoolean(false);
        alertHandler.push(iDevice);
    }

    public void defaultHandler() {
        final IDevice iDevice = testManager.getIDevice(paramsParser.getDeviceUuid());
        final AlertHandler alertHandler = new AlertHandler();
        alertHandler.logMessage("alert appeared");
        AlertItem logOutText = alertHandler.waitForElementByNameVisible("logging out will", 1, new IAlertConfig().setMaxLevelOfElementsTree(4).setMatcher(Matcher.ContainsIgnoreCase));//  1, 0, false, null, 4);
        final AlertItem profiles = alertHandler.waitForElementByNameExists("NOOK Now Supports Profiles", 1, new IAlertConfig().setMaxLevelOfElementsTree(4).setMatcher(Matcher.ContainsIgnoreCase));

        alertHandler.createElementNotNullCondition(logOutText, new AlertCondition.ConditionResults() {
            @Override
            public void positiveResult() {
                final AlertItem buttonOk = alertHandler.waitForElementByNameVisible("OK", 1, new IAlertConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(4));
                alertHandler.createElementNotNullCondition(buttonOk, new AlertCondition.ConditionResults() {
                    @Override
                    public void positiveResult() {
                        alertHandler.clickOnElementByXY(buttonOk, 0.5, 0.5);
                        alertHandler.returnBoolean(true);
                    }

                    @Override
                    public void negativeResult() {}
                });
            }

            @Override
            public void negativeResult() {
                alertHandler.createElementNotNullCondition(profiles, new AlertCondition.ConditionResults() {
                    @Override
                    public void positiveResult() {
                        final AlertItem cancel = alertHandler.waitForElementByNameVisible("Cancel", 1, new IAlertConfig().setMatcher(Matcher.ContainsIgnoreCase).setMaxLevelOfElementsTree(4));
                        alertHandler.createElementNotNullCondition(cancel, new AlertCondition.ConditionResults() {
                            @Override
                            public void positiveResult() {
                                alertHandler.clickOnElementByXY(cancel, 0.5, 0.5);
                                alertHandler.returnBoolean(true);
                            }

                            @Override
                            public void negativeResult() {}
                        });
                    }

                    @Override
                    public void negativeResult() {

                    }
                });
            }
        });
        alertHandler.returnBoolean(false);
        alertHandler.push(iDevice);
    }
}
