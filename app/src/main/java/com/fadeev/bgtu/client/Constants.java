package com.fadeev.bgtu.client;

public class Constants {
    public static class URL{
        private static final String HOST = "http://192.168.1.2:8080/";

        public static final String POST_LOGIN = HOST + "/login";
        public static final String POST_AUTHENTICATION = HOST + "/authentication";
    }
    public  static class PREFERENCES{
        public static final String USERNAME = "username";
        public static final String TOKEN = "token";
        public static final String MAIN = "main";
    }
}
