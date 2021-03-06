package com.github.maximkirko.training_2017_android.activity.core;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.maximkirko.training_2017_android.R;
import com.github.maximkirko.training_2017_android.navigator.IntentManager;
import com.github.maximkirko.training_2017_android.contentprovider.FriendsContentProvider;
import com.github.maximkirko.training_2017_android.asynctask.ImageLoadingAsyncTask;
import com.github.maximkirko.training_2017_android.contentprovider.UserContentProvider;
import com.github.maximkirko.training_2017_android.mapper.UserMapper;
import com.github.maximkirko.training_2017_android.model.User;
import com.github.maximkirko.training_2017_android.service.VKService;
import com.github.maximkirko.training_2017_android.sharedpreference.AppSharedPreferences;

/**
 * Created by MadMax on 25.12.2016.
 */

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    // region Views
    private Toolbar toolbar;
    private TextView tvTitle;
    private TextView onlineStatusView;
    private ImageView userPhotoView;
    private Button openPageButton;
    // endregion

    private User user;

    private static final String USER_PAGE_BASE_URL = "https://vk.com/id";
    public static final String USER_EXTRA = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_activity);
        initToolbar();
        getIntentExtras();
        initViews();
        setViewsValues();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_user_details_activity);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getIntentExtras() {
        Uri uri = this.getIntent().getData();
        if (uri != null) {
            getUserFromDB(uri);
            return;
        }
        int id = this.getIntent().getIntExtra(USER_EXTRA, -1);
        initUser(id);
    }

    private void initUser(int id) {
        Uri uri;
        if (id == AppSharedPreferences.getInt(VKService.USER_ID_PREFERENCE, 0)) {
            uri = UserContentProvider.USER_CONTENT_URI;
        } else {
            uri = FriendsContentProvider.FRIENDS_CONTENT_URI;
        }
        uri = ContentUris.withAppendedId(uri, id);
        getUserFromDB(uri);
    }

    private void getUserFromDB(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToNext()) {
            user = UserMapper.convert(cursor);
        }
    }

    private void initViews() {
        tvTitle = (TextView) findViewById(R.id.textview_user_details_activity_name);
        onlineStatusView = (TextView) findViewById(R.id.textview_user_details_activity_description);
        userPhotoView = (ImageView) findViewById(R.id.imageview_user_details_activity_photo);
        openPageButton = (Button) findViewById(R.id.button_user_details_activity_open_page);
        openPageButton.setOnClickListener(this);
    }

    private void setViewsValues() {
        ImageLoadingAsyncTask.newLoader()
                .setTargetView(userPhotoView)
                .setPlaceHolder(R.drawable.all_default_user_image)
                .setImageHeight(getResources().getDimensionPixelSize(R.dimen.size_user_details_photo))
                .setImageWidth(getResources().getDimensionPixelSize(R.dimen.size_user_details_photo))
                .load(user.getPhoto_100());

        tvTitle.setText(user.getFirst_name() + " " + user.getLast_name());
        onlineStatusView.setText(user.isOnline() ? getResources().getString(R.string.all_online_status_true) : "");
    }

    @Override
    public void onClick(View view) {
        String url = USER_PAGE_BASE_URL + user.getId();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(IntentManager.getIntentForFriendsListActivity(this));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
