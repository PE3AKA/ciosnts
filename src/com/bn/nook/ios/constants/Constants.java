package com.bn.nook.ios.constants;

import com.bn.nook.ios.screen.OobeScreen;

/**
 * Created by avsupport on 4/13/15.
 */
public class Constants {
    public static final long DEFAULT_TIMEOUT = 30000;
    public static final long DOWNLOAD_PRODUCT_TIMEOUT = 600000;

    public static class CommonElements{
        public static final String DONE_BTN = "Done";
        public static final String CANCEL_BTN = "Cancel";
        public static final String MENU_BTN = "com.bn hub hamburger menu";
    }

    public static class SideMenu{
        public static final String CURRENT_READ = "Current Read";
        public static final String LIBRARY = "Library";
        public static final String MY_SHELVES = "My Shelves";
        public static final String MESSAGES = "Messages";
        public static final String SETTINGS = "Settings";
        public static final String NOOK_LOGO = "com.bn.NookApplication.NookLogo.png";
    }

    public static class Library {
        public static final String MENU_BUTTON = "Navigation Menu";
        public static final String MENU_BUTTON2 = "com.bn hub hamburger menu";
        public static class Menu {
            public static final String MY_SHELVES = "My Shelves";
            public static final String CURRENT_READ = "Current Read";
            public static final String LIBRARY = "Library";
            public static final String QUICK_READS = "Quick Reads";
            public static final String MESSAGES = "Messages";
            public static final String SETTINGS = "Settings";
            public static final String HOME = "HOME";
        }

        public static final String FILTER = " filter";
        public static class Filter {
            public static final String NAN = "NAN";
            public static final String ALL_ITEMS = "All Items";
            public static final String BOOKS = "Books";
            public static final String MAGAZINES = "Magazines";
            public static final String NEWSPAPERS = "Newspapers";
            public static final String MY_FILES = "My Files";
            public static final String ARCHIVED = "Archived";
            public static final String EVERYTHING_ELSE = "Everything Else";
        }

        public static final String SORT = " sort";
        public static class Sort {
            public static final String NAN = "NAN";
            public static final String RECENT = "Recent";
            public static final String TITLE = "Title";
            public static final String AUTHOR = "Author";
        }
    }

    public static class Screens {
        public static final String SETTINGS_SCREEN = "NOOK Settings";
        public static final String OOBE_SCREEN = "Sign In";
        public static final String SEARCH_SCREEN = "Cancel";
        public static final String LIBRARY_SCREEN = "Navigation Menu";
        public static final String LIBRARY_SCREEN2 = "com.bn hub hamburger menu";
        public static final String EPUB_READER = "Back to library";

        public class Classes{
            public static final String OOBE_SCREEN = "OobeScreen";
            public static final String LIBRARY_SCREEN = "LibraryScreen";
            public static final String READER_SCREEN = "ReaderScreen";
            public static final String EPUB_READER_SCREEN = "EpubReaderScreen";
            public static final String DRP_READER_SCREEN = "DrpReaderScreen";
            public static final String SEARCH_SCREEN = "SearchScreen";
            public static final String SETTINGS_SCREEN = "SettingsScreen";

        }
    }

    public static class Settings {
        public static final String DONE = "Done";
        public static final String ACCOUNT_SETTINGS = "ACCOUNT SETTINGS";
        public static final String ACCOUNTS = "Accounts";
        public static final String DICTIONARY_OPTIONS = "Dictionary Options";
        public static final String PROFILES = "PROFILES";
        public static final String SET_PROFILE = "Set Profile";
        public static final String CHILD_PASSCODE = "Child Passcode";
        public static final String SUPPORT = "SUPPORT";
        public static final String FREQUENTLY_ASKED_QUESTIONS = "Frequently Asked Questions";
        public static final String SEND_FEEDBACK = "Send Feedback";
        public static final String LEGAL = "Legal";
        public static final String ABOUT = "About";
        public static final String LOG_OUT = "Log out";
    }

    public static class Search {
        public static final String CANCEL = "Cancel";
        public static final String SAMPLE = "Sample";
        public static final String NOT_DOWNLOADED = "Not downloaded";
        public static final String DOWNLOADING = "Download";
        public static final String READ = "Read";
    }

    public static class Reader {
        public static class Epub {
            public static final String CONTENTS = "Contents, bookmarks and notes";
            public static final String TEXT_OPTIONS = "Text options";
            public static final String BRIGHTNESS = "Brightness";
            public static final String SEARCH = "Search";
            public static final String INFORMATION = "Information";
            public static final String ADD_BOOKMARK = "Add bookmark";
            public static final String REMOVE_BOOKMARK = "Remove bookmark";
            public static final String PAGE_POSITION = " of ";

            public static class Contents {
                public static final String CONTENTS_TAB = "contentsTab";
                public static final String BOOKMARKS_TAB = "bookmarksTab";
                public static final String ANNOTATIONS_TAB = "annotationsTab";
                public static final String GO_TO_PAGE = "Go To Page";
                public static final String CLOSE_BUTTON = "closeButton";
            }
        }
        public static class Drp{
            public static final String COMICS_SETTINGS_BTN = "Comics Settings";
            public static final String CONTENTS_BOOKMARKS = "Contents, bookmarks";
            public static final String BRIGHTNESS = "Brightness";
            public static final String LIBRARY_BTN = "library";
            public static final String CONTENTS_TAB = "Contents tab";
            public static final String PAGE_SLIDER = "pageSlider";
            public static final String ARTICLE_VIEW = "Article View";
            public static final String TEXT = "Text";
            public static final String ARTICLE_VIEW_PAGE = "com.bn.DRPReader.WebviewBackground.png";

        }
        public static class TextOptions{
            public static class Size{
                public static final String EXTRA_SMALL_FONT_BUTTON = "extraSmallFontButton";
                public static final String SMALL_FONT_BUTTON = "extraSmallFontButton";
                public static final String MEDIUM_SMALL_FONT_BUTTON = "mediumSmallFontButton";
                public static final String MEDIUM_LARGE_FONT_BUTTON = "mediumLargeFontButton";
                public static final String LARGE_FONT_BUTTON = "largeFontButton";
            }
            public static class Font{
                public static final String ASCENDER_SANS = "Ascender Sans";
                public static final String CENTURY_SCHOOLBOOK = "Century Schoolbook";
                public static final String DUTCH = "Dutch";
                public static final String GEORGIA = "Georgia";
                public static final String GILL_SANS = "GillSans";
                public static final String TREBUCHET = "Trebuchet";
            }
            public static class Theme{
                public static final String DAY = "Day";
                public static final String NIGHT = "Night";
                public static final String GRAY = "Gray";
                public static final String BUTTER = "Butter";
                public static final String MOCHA = "Mocha";
                public static final String SEPIA = "Sepia";
            }
            public static class LineSpacing{
                public static final String LEADING_1_BUTTON = "leading1Button";
                public static final String LEADING_2_BUTTON = "leading2Button";
                public static final String LEADING_3_BUTTON = "leading3Button";
            }
            public static class Margins{
                public static final String MARGIN_1_BUTTON = "margin1Button";
                public static final String MARGIN_2_BUTTON = "margin2Button";
            }
        }
    }

    public static class ProductDetails {
        public static final String MANAGE_BUTTON = "Manage";
        public static final String ARCHIVE_BUTTON = "Archive";
        public static final String UNARCHIVE_BUTTON = "Unarchive";
        public static final String DELETE_BUTTON = "Delete";
        public static final String ARCHIVE_TITLE = "Archive Title";
        public static final String BACK_BUTTON = "com.bn.NookApplication.btn bac";
    }

}
