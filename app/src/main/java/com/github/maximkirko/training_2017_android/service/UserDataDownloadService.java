package com.github.maximkirko.training_2017_android.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.maximkirko.training_2017_android.application.VKSimpleChatApplication;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.reader.UserJSONReader;

import java.io.IOException;

/**
 * Created by MadMax on 09.02.2017.
 */

public class UserDataDownloadService extends VKRequestAbstractService<User> {

    public static final String DOWNLOAD_SERVICE_URI = "UserDataDownloadService";
    public static final String SERVICE_CLASS = "USER";

    public UserDataDownloadService() {
        super(DOWNLOAD_SERVICE_URI, SERVICE_CLASS);
        requestUrl = VKService.getUserRequestUrl();
    }

    @Nullable
    @Override
    protected User getDataFromJson(@NonNull String jsonResponse) {
        try {
            reader = new UserJSONReader(jsonResponse);
            return reader.read();
        } catch (IOException e) {
            Log.e(IOException.class.getSimpleName(), e.getMessage());
        }
        return null;
    }

    @Override
    protected void saveData(@NonNull User data) {
        VKSimpleChatApplication.getDbHelper().insertUserData(VKSimpleChatApplication.getDbHelper().getWritableDatabase(), getApplicationContext(), data);
    }
}
