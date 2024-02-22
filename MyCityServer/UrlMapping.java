package com.example.community;

public class UrlMapping {
    public static final String API_PATH = "/api";

    public static final String ANALYTICS = API_PATH + "/admin/analytics";

    public static final String INSTITUTION_ANALYTICS = API_PATH + "/institution/analytics/{id}/{name}";

    public static final String USER_ANALYTICS = API_PATH + "/user/analytics/{id}";
    public static final String AUTH = API_PATH + "/auth";


    public static final String EXISTS_USERNAME = "/existsusername/{username}";

    public static final String EXISTS_EMAIL = "/existsemail/{email}";

    public static final String LOG_IN = "/login";
    public static final String SIGN_UP = "/signup";


    public static final String USERS = API_PATH+"/admin/users";
    public static final String ID = "/{id}";

    public static final String USER_ISSUES = API_PATH+"/user/issues/{username}";



    public static final String ISSUE = API_PATH+"/admin/issues";

    public static final String INSTITUTION_NAME="/{name}";



    public static final String INSTITUTION_ISSUES = API_PATH+"/institution/issues/{id}";

    public static final String INSTITUTION_ISSUE = API_PATH+"/institution/issue/{id}";

    public static final String UPDATE_ISSUE = API_PATH+"/institution/issue";

    public static final String UPDATE_ISSUE_STATUS = API_PATH+"/institution/issue/update-status";

    public static final String REASSIGN_ISSUE = API_PATH+"/institution/issue/reassign/{id}";

    public static final String SHARE_ISSUE = API_PATH+"/institution/issue/share/{id}";
    public static final String USER_INSTITUTION = API_PATH+"/institution/{id}";

    public static final String INVOLVED_USER = INSTITUTION_ISSUES+"/{username}";


    public static final String EVENT = API_PATH+"/admin/events";

    public static final String USER_EVENTS = "/userevents";

    public static final String FAVORITE_EVENTS = "/favorites";

    public static final String ADD_TO_FAVORITES = FAVORITE_EVENTS+"/add/{userId}/{eventId}";

    public static final String REMOVE_FROM_FAVORITES = FAVORITE_EVENTS+"/remove/{userId}/{eventId}";

    public static final String IS_FAVORITE = FAVORITE_EVENTS+"/{userId}/{eventId}";

    public static final String FILTER_STATUS = "/filter/{status}";

    public static final String UPDATE_EVENT_STATUS = "/update-status";

    public static final String EVENT_TYPES = "/types";

    public static final String ALERT = API_PATH+"/admin/alerts";

    public static final String NEWS = API_PATH+"/admin/news";

    public static final String FACILITY = API_PATH+"/admin/facilities";

    public static final String FILTERED_FACILITIES = "/{type}";

    public static final String INSTITUTION = API_PATH+"/admin/institutions";

    public static final String EVENT_STATUS = "/{eventStatus}";

    public static final String SEND = API_PATH+"/send";
}
