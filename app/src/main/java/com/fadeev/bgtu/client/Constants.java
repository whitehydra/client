package com.fadeev.bgtu.client;

public class Constants {
    public static class URL{
        public static final String DEPLOY_VERSION =  "com.denis.test.server-1.0-SNAPSHOT";


        public static final String AMAZON_HOST = "http://35.157.234.188:8080/";
        public static final String LOCAL_HOST = "http://192.168.1.5:8080/";



        public static final String HOST = "http://35.157.234.188:8080/";
        public static final String VERSION = DEPLOY_VERSION;



        public static final String AVATARS = HOST + VERSION +  "/avatars/";
        public static final String FILES = HOST + VERSION +  "/portfolio/files/load?";

        public static final String AUTH = "/authentication";
        public static final String LOGIN = "/login";
        public static final String USERS = "/users/get";
        public static final String ACCESS_RECOVERY = "/recovery/{username}";
        public static final String PIN_RECOVERY = "/recovery-pin/{username}";

        public static final String UPLOAD_AVATAR = "/file";
        public static final String UPLOAD_FILE = "/portfolio/upload";

        public static final String ADD_PORTFOLIO = "/portfolio/add";
        public static final String GET_PORTFOLIO = "/portfolio/list";
        public static final String GET_USER_PORTFOLIO = "/users/portfolio/list";
        public static final String UPDATE_PORTFOLIO = "/portfolio/update";
        public static final String DELETE_PORTFOLIO = "/portfolio/delete";
        public static final String ADD_FILE_INFO = "/portfolio/files/post";
        public static final String GET_FILE_INFO = "/portfolio/files/get";

        public static final String GET_CATEGORIES = "/categories";
        public static final String GET_CRITERIA = "/criteria/{id}";
        public static final String GET_TYPE = "/types/{id}";

        public static final String GET_FACULTIES = "/faculties";
        public static final String GET_GROUPS = "/groups/{id}";

        public static final String GET_PIN_STATUS = "/pin/{username}";
        public static final String CHECK_PIN = "/pin/check";
        public static final String SET_PIN = "/pin/set";

        public static final String EDIT_PROFILE = "/edit/profile";
        public static final String EDIT_PASSWORD = "/edit/password";

    }

    public  static class PREFERENCES{
        public static final String USERNAME = "username";
        public static final String TOKEN = "token";
        public static final String MAIN = "main";

        public static final String NIGHT_MODE = "night_mode_preference";
        public static final String TEXT_SIZE = "size_list_preference";
        public static final String LANGUAGE = "language_preference";

    }

    public static class FILES{
        public static final String AVATAR = "avatar.jpg";
        public static final String IMG_TEMP = "temp.jpg";
    }
}
