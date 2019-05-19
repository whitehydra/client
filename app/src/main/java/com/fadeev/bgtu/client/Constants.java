package com.fadeev.bgtu.client;

public class Constants {
    public static class URL{
        public static final String HOST = "http://192.168.1.4:8080/";
        public static final String AVATARS = HOST + "avatars/";
        public static final String FILES = HOST + "portfolio/files/load?";

        public static final String AUTH = "/authentication";
        public static final String LOGIN = "/login";

        public static final String UPLOAD_AVATAR = "/file";
        public static final String UPLOAD_FILE = "/portfolio/upload";

        public static final String ADD_PORTFOLIO = "/portfolio/add";
        public static final String GET_PORTFOLIO = "/portfolio/list";
        public static final String UPDATE_PORTFOLIO = "/portfolio/update";
        public static final String DELETE_PORTFOLIO = "/portfolio/delete";
        public static final String ADD_FILE_INFO = "/portfolio/files/post";
        public static final String GET_FILE_INFO = "/portfolio/files/get";

        public static final String GET_CATEGORIES = "/categories";
        public static final String GET_CRITERIA = "/criteria/{id}";
        public static final String GET_TYPE = "/types/{id}";

        public static final String GET_PIN_STATUS = "/pin/{username}";
        public static final String CHECK_PIN = "/pin/check";
        public static final String SET_PIN = "/pin/set";

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
    }
}
