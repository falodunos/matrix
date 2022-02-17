package com.league.test.matrix.service.util;

public class AppConstants {
    public static final String APP_CONTEXT = "/api/matrix/v1/";
    public static final String FILE_TYPE = "text/csv";
    public static final String FILE_UPLOAD_PATH = getFileUploadPath();
    public static final String TARGET_FILE_NAME = "matrix.csv";
    public static final String TARGET_FILE_PATH = getFileUploadPath() + TARGET_FILE_NAME;
    public static final String INTEGER_REGEX_PATTERN = "[+-]?[0-9]+";


    /**
     * Return the path of uploaded files
     * @return String
     */
    private static String getFileUploadPath() {
        String path = System.getProperty("user.dir") + "\\uploads\\";
        path = path.replace("\\", "/");
        return path;
    }

    public interface ApiResponseMessage {
        String SUCCESSFUL = "Successful";
        String FAILED = "Failed";
    }
}
