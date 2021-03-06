package com.github.maximkirko.training_2017_android.service;

import android.content.Context;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.sharedpreference.AppSharedPreferences;

/**
 * Created by MadMax on 10.01.2017.
 */

public final class VKService {

    // region base urls
    private static final String BASE_AUTH_URL = "https://oauth.vk.com/authorize?";
    private static final String BASE_REQUEST_URL = "https://api.vk.com/method/";
    // endregion

    // region preferences
    public static final String ACCESS_TOKEN_PREFERENCE = "ACCESS_TOKEN";
    public static final String USER_ID_PREFERENCE = "USER_ID";
    public static final String ACCESS_PERMISSION_PREFERENCE = "ACCESS_PERMISSION";
    // endregion

    // region request params
    public static final String ACCESS_TOKEN_PARAM = "access_token";
    public static final String REDIRECT_URI_PARAM = "redirect_uri";
    public static final String SCOPE_PARAM = "scope";
    public static final String RESPONSE_TYPE_PARAM = "response_type";
    public static final String API_VERSION_PARAM = "v";
    public static final String GET_FRIENDS_METHOD_NAME_PARAM = "friends.get?";
    public static final String GET_USER_METHOD_NAME_PARAM = "users.get?";
    public static final String COUNT_PARAM = "count";
    public static final String FIELDS_PARAM = "fields";
    // endregion

    // region request values
    public static final String REDIRECT_URI = "https://oauth.vk.com/blank.html";
    public static final String SCOPE = "friends";
    public static final String RESPONSE_TYPE = "token";
    public static final String API_VERSION = "5.62";
    public static final String COUNT = "10";
    public static final String[] FIELDS = {"online", "photo_100", "last_seen"};
    // endregion

    public static String getUserRequestUrl() {
        String url = BASE_REQUEST_URL + GET_USER_METHOD_NAME_PARAM;
        url += ACCESS_TOKEN_PARAM + "=" + AppSharedPreferences.getString(ACCESS_TOKEN_PREFERENCE, null);
        url += "&" + COUNT_PARAM + "=" + COUNT;
        url += "&" + FIELDS_PARAM + "=" + setFields();
        url += "&" + API_VERSION_PARAM + "=" + API_VERSION;
        url = url.replaceAll(" ", "%20");
        return url;
    }

    private static String setFields() {
        String url = "";
        int i = 0;
        for (String field : FIELDS) {
            url += field;
            if (i < FIELDS.length - 1) {
                url += ", ";
            }
            i++;
        }
        return url;
    }

    public static String getFriendsRequestUrl() {
        String url = BASE_REQUEST_URL + GET_FRIENDS_METHOD_NAME_PARAM;
        url += ACCESS_TOKEN_PARAM + "=" + AppSharedPreferences.getString(ACCESS_TOKEN_PREFERENCE, null);
        url += "&" + FIELDS_PARAM + "=" + setFields();
        url += "&" + API_VERSION_PARAM + "=" + API_VERSION;
        url = url.replaceAll(" ", "%20");
        return url;
    }

    public static String getAuthUrl(Context context) {
        String url = context.getString(R.string.vk_oauth_url);
        url += "client_id=" + context.getResources().getInteger(R.integer.com_vk_sdk_AppId);
        url += "&" + REDIRECT_URI_PARAM + "=" + REDIRECT_URI;
        url += "&" + SCOPE_PARAM + "=" + SCOPE;
        url += "&" + RESPONSE_TYPE_PARAM + "=" + RESPONSE_TYPE;
        url += "&" + API_VERSION_PARAM + "=" + API_VERSION;
        return url;
    }

    public static String getAccessTokenFromRedirectUrl(String url) {
        return parseSuccessRedirectUri(url)[1];
    }

    public static String getExpiresInFromRedirectUrl(String url) {
        return parseSuccessRedirectUri(url)[2];
    }

    public static String getUserIdFromRedirectUrl(String url) {
        return parseSuccessRedirectUri(url)[3];
    }

    /*
    * @returns matches[1] - access token, matches[2] - expires in, matches[3] - user id
    * Example: http://api.vk.com/blank.html#access_token=$access_token$&expires_in=$expires_in$&user_id=$user_id$
     */
    private static String[] parseSuccessRedirectUri(String url) {
        String[] matches = url.split(REDIRECT_URI + "#" + ACCESS_TOKEN_PARAM + "=" + "|&expires_in=|&user_id=");
        for (int i = 0; i < matches.length; i++) {
            System.out.println("I=" + i + " " + matches[i]);
        }
        return matches;
    }
}
