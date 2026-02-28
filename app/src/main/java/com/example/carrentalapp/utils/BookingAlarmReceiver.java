package com.example.carrentalapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BookingAlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(EXTRA_TITLE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        int id = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 1);

        NotificationUtils.show(context, id, title, message);
    }
}
