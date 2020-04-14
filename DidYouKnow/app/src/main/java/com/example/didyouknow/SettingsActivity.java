package com.example.didyouknow;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import androidx.core.app.NotificationCompat;

public class SettingsActivity extends Activity {

    public static String APP_PREFERENCES_KEY = "APP_PREFERENCES";
    public static String PREFERENCES_NOTIFICATIONS = "APP_PREFERENCES_NOTIFICATIONS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Switch notificationsSwitch = findViewById(R.id.switch_notifications);
        notificationsSwitch.setChecked(getPreference(PREFERENCES_NOTIFICATIONS, false));

        notificationsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isSetNotification = getPreference(PREFERENCES_NOTIFICATIONS, false);

                if(isSetNotification) {
                    setPreference(PREFERENCES_NOTIFICATIONS, false);
                } else {
                    setPreference(PREFERENCES_NOTIFICATIONS, true);
                }
            }
        });
    }

    public Boolean getPreference(String preferenceKey, boolean defaultValue)
    {
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCES_KEY,Context.MODE_PRIVATE);
        Boolean value = sharedPreferences.getBoolean(preferenceKey, defaultValue);
        return value;
    }

    public void setPreference(String preferenceKey, boolean preferenceValue)
    {

        Context context = getApplicationContext();
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_PREFERENCES_KEY,Context.MODE_PRIVATE).edit();
        editor.putBoolean(preferenceKey, preferenceValue);
        editor.commit();
    }

}
