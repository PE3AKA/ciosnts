package com.bn.nook.ios.param;

import java.util.ArrayList;

import static com.bn.nook.ios.utils.LoggerUtil.e;

/**
 * Created by avsupport on 4/13/15.
 */
public class ParamsParser {

    private static ParamsParser paramsParser;

    private ParamsParser() {}

    public static ParamsParser getInstance(){
        if(paramsParser == null) paramsParser = new ParamsParser();
        return paramsParser;
    }

    private ArrayList<String> implementedTests = new ArrayList<String>();
    {
        implementedTests.add("434245");
        implementedTests.add("435985");
        implementedTests.add("435987");
        implementedTests.add("435990");
        implementedTests.add("435993");
        implementedTests.add("435986");
        implementedTests.add("435988");
        implementedTests.add("435989");
        implementedTests.add("435992");
        implementedTests.add("435994");
        implementedTests.add("435995");
        implementedTests.add("435997");
        implementedTests.add("435998");
        implementedTests.add("436026");
        implementedTests.add("436027");
        implementedTests.add("435999");
        implementedTests.add("436012");
        implementedTests.add("436013");
        implementedTests.add("436000");
        implementedTests.add("436002");
        implementedTests.add("436014");
        implementedTests.add("436015");
        implementedTests.add("123456");
        implementedTests.add("436003");
        implementedTests.add("436004");
        implementedTests.add("439472");
    }

    private String deviceUuid;
    private String testId;
    private String pathToResultsFolder;
    private String pathToBuild;

    private ArrayList<String> argsName = new ArrayList<String>();
    {
        argsName.add("testId");
        argsName.add("uuid");
        argsName.add("pathToResultFolder");
        argsName.add("pathToBuild");
    }

    public boolean parse(String... args) {
        if(args.length % 3 != 0 || args.length == 0) {
            e("Wrong arguments count or format\n" +
                    "usage: \n" +
                    "java -jar IOsCiTests -e testId testId -uuid deviceUUID -e pathToResultFolder \"path/to/reults/folder\" -e pathToBuild(Optional) \"path/to/build.app(ipa)\"");
            System.exit(0);
            return false;
        }

        String command = "";

        for(int currentIndex = 0; currentIndex < args.length; currentIndex ++){
            if(!args[currentIndex].equals("-e") || !argsName.contains(command = args[currentIndex + 1])) {
                e("Wrong arguments count or format\n" +
                        "usage: \n" +
                        "java -jar IOsCiTests -e testId testId -e uuid deviceUUID -e pathToResultFolder \"path/to/reults/folder\" -e pathToBuild(Optional) \"path/to/build.app(ipa)\"");
                System.exit(0);
                return false;
            }
            currentIndex = currentIndex + 2;
            if(command.equals("testId") && !implementedTests.contains(testId = args[currentIndex])) {
                String testsId = "";
                for(String testId : implementedTests) {
                    testsId = testsId + (testsId.length() > 0 ? "\n" : "") + testId;
                }
                e("Wrong test id\n" +
                        "Available tests id: \n" +
                        testsId);
                System.exit(0);
                return false;
            } else if(command.equals("uuid")) {
                deviceUuid = args[currentIndex];
            } else if(command.equals("pathToResultFolder")) {
                pathToResultsFolder = args[currentIndex];
            } else if(command.equals("pathToBuild")) {
                pathToBuild = args[currentIndex];
            }
        }
        if(pathToResultsFolder == null || deviceUuid == null || testId == null) {
            e("Missed argument\n" +
                    "usage: \n" +
                    "java -jar IOsCiTests -e testId testId -e uuid deviceUUID -e pathToResultFolder \"path/to/reults/folder\" -e pathToBuild(Optional) \"path/to/build.app(ipa)\"");
            System.exit(0);
            return false;
        }
        return true;
    }

    public String getDeviceUuid() {
        return deviceUuid;
    }

    public String getTestId() {
        return testId;
    }

    public String getPathToBuild() {
        return pathToBuild;
    }

    public String getPathToResultsFolder() {
        return pathToResultsFolder;
    }
}
