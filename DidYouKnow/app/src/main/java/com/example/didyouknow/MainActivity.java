package com.example.didyouknow;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.didyouknow.models.CategoryModel;
import com.example.didyouknow.models.CategorySpinnerAdapter;
import com.example.didyouknow.receivers.NotificationsBroadcastReceiver;
import com.example.didyouknow.services.TriviaIntentService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends Activity {

    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    public static final byte INTERNET_PERMISSION_REQUEST_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CategoriesReceiver receiver = new CategoriesReceiver(new Handler());
        TriviaIntentService.getCategories(this, receiver);

        FloatingActionButton buttonInfo = findViewById(R.id.button_info);
        FloatingActionButton buttonSuggestion = findViewById(R.id.button_open_suggestion);
        FloatingActionButton buttonSettings = findViewById(R.id.button_settings);

        spawnNotificationChannel();
        NotificationsBroadcastReceiver.scheduleNotification(this, 5000);

        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openInfoActivity = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(openInfoActivity);
            }
        });

        buttonSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSuggestionActivity = new Intent(MainActivity.this, SuggestionActivity.class);
                startActivity(openSuggestionActivity);
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(openSettingsActivity);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case INTERNET_PERMISSION_REQUEST_ID: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, R.string.message_granted_internet_permission, Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(MainActivity.this, R.string.message_error_no_internet_permission, Toast.LENGTH_LONG);
                }

                return;
            }
        }
    }

    private class CategoriesReceiver extends ResultReceiver {
        public CategoriesReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == RESULT_OK) {
                final CategoryModel[] categories = (CategoryModel[])resultData.getParcelableArray(TriviaIntentService.RESULT_RECEIVER_CATEGORY);

                // Should always receive three categories.
                Button buttonFirstOption = findViewById(R.id.button_first);
                Button buttonSecondOption = findViewById(R.id.button_second);
                Button buttonThirdOption = findViewById(R.id.button_third);

                buttonFirstOption.setText(categories[0].name);
                buttonSecondOption.setText(categories[1].name);
                buttonThirdOption.setText(categories[2].name);

                buttonFirstOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent openTriviaActivity = getTriviaActivityIntent(categories[0].id);
                        startActivityIfPermissionsGranted(Manifest.permission.INTERNET, openTriviaActivity);
                    }
                });

                buttonSecondOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent openTriviaActivity = getTriviaActivityIntent(categories[1].id);
                        startActivityIfPermissionsGranted(Manifest.permission.INTERNET, openTriviaActivity);
                    }
                });

                buttonThirdOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent openTriviaActivity = getTriviaActivityIntent(categories[2].id);
                        startActivityIfPermissionsGranted(Manifest.permission.INTERNET, openTriviaActivity);
                    }
                });

                toggleButtonsVisibility(true);
            }
        }
    }

    public void spawnNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(notificationManager);
    }

    public void createNotificationChannel(NotificationManager manager) {
        String channelId = MainActivity.NOTIFICATION_CHANNEL_ID;
        CharSequence channelName = "Common channel";

        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        manager.createNotificationChannel(notificationChannel);
    }

    private Intent getTriviaActivityIntent(long categoryId) {
        Intent openTriviaActivity = new Intent(this, TriviaActivity.class);
        openTriviaActivity.putExtra(TriviaActivity.CATEGORY_KEY, categoryId);

        return openTriviaActivity;
    }

    private void startActivityIfPermissionsGranted(String permission, Intent intent) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Application can not access Internet.", Toast.LENGTH_LONG);
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION_REQUEST_ID);
        } else {
            startActivity(intent);
        }
    }

    private void toggleButtonsVisibility(boolean isShown) {
        Button buttonFirstOption = findViewById(R.id.button_first);
        Button buttonSecondOption = findViewById(R.id.button_second);
        Button buttonThirdOption = findViewById(R.id.button_third);

        ProgressBar progressBarTopics = findViewById(R.id.progressbar_topics);

        buttonFirstOption.setVisibility(isShown ? View.VISIBLE : View.INVISIBLE);
        buttonSecondOption.setVisibility(isShown ? View.VISIBLE : View.INVISIBLE);
        buttonThirdOption.setVisibility(isShown ? View.VISIBLE : View.INVISIBLE);

        progressBarTopics.setVisibility(isShown ? View.INVISIBLE : View.VISIBLE);
    }
}
