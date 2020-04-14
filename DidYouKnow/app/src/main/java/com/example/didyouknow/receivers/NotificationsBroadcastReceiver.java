package com.example.didyouknow.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;

import com.example.didyouknow.MainActivity;
import com.example.didyouknow.R;
import com.example.didyouknow.SettingsActivity;

public class NotificationsBroadcastReceiver extends BroadcastReceiver {

    public static int MAX_NUMBER_NOTIFICATIONS = 50;
    public static int CURRENT_NOTIFICATION_NUMBER = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(getPreference(context, SettingsActivity.PREFERENCES_NOTIFICATIONS, true)) {
            sendNotification(context);
        }

        scheduleNotification(context, 5000);
    }

    public static void scheduleNotification(Context context, int delay) {

        if (CURRENT_NOTIFICATION_NUMBER < MAX_NUMBER_NOTIFICATIONS) {
            Intent notificationIntent = new Intent(context, NotificationsBroadcastReceiver.class);
            long futureInMillis = SystemClock.elapsedRealtime() + delay;

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

            ++CURRENT_NOTIFICATION_NUMBER;
        }
    }

    private static boolean getPreference(Context context, String preferenceKey, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SettingsActivity.APP_PREFERENCES_KEY, Context.MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(preferenceKey, defaultValue);
        return value;
    }

    private void sendNotification(Context context) {

        Notification notification = new NotificationCompat.Builder(context.getApplicationContext())
                .setChannelId(MainActivity.NOTIFICATION_CHANNEL_ID)
                .setContentTitle("How are you?")
                .setContentText("Are you ready to impress your friends with these facts?")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notification);
    }
}
