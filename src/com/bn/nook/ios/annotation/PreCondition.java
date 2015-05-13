package com.bn.nook.ios.annotation;

import com.bn.nook.ios.constants.Constants;
import com.bn.nook.ios.param.ConfigParam;
import com.bn.nook.ios.screen.ScreenModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Properties;

/**
 * Created by nikolai on 04.03.2015.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreCondition {
    Condition[] preConditions() default {Condition.NONE};

    boolean checkInternetState() default true;
    // prepareLogin
    ConfigParam login() default ConfigParam.LOGIN;
    ConfigParam password() default ConfigParam.PASSWORD;

    // TestCase
    String testTitle();
    long testId();

    // Product
    ConfigParam productName() default ConfigParam.UNKNOWN_PRODUCT;
    ScreenModel productType () default ScreenModel.EPUB_READER;

    // open screen
    ScreenModel screenModel() default ScreenModel.LIBRARY;
    String screenTitle() default Constants.Library.Menu.LIBRARY;

    String profileName() default "Child";

//    String libreryCategoyOfProduct() default Constants.Library.Text.BOOKS;
//    boolean isCloseTutorial() default false;
//    int productType() default Constants.ProductType.EPUB;
//    ConfParEnum freeSample() default  ConfParEnum.FREE_SAMPLE;
//
//    // intent
//    String intent() default "";
//
//    // orientation
//    Screen.Orientation orientation() default Screen.Orientation.DEFAULT;
//
//    //shelf
//    String shelfName() default "Test Shelf";
//
//    //profile
//    String childProfileName() default Constants.Settings.Text.PROFILE_CHILD;
//    String profileName() default Constants.Settings.Text.ADULT_PROFILE;
//    String selectProfile() default Constants.Settings.Text.ADULT_PROFILE;
//    boolean statePasscode() default true;
//    //purchase
//    boolean purchasePasscodeState() default false;
//    //Settings
//    boolean checkBoxState() default false;
//    String settingsItemName() default "";
//    String partOfdictionaryName() default "Fran";
//    boolean loadDictionaryState() default false;
//    //Oobe
//    String defaultLanguage() default "US";
}
