package com.ubergeek42.WeechatAndroid.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.ubergeek42.WeechatAndroid.relay.BufferList;


public class SyncAlarmReceiver extends BroadcastReceiver {
    final private static int SYNC_EVERY_MS = 60 * 5 * 1000; // 5 minutes

    @WorkerThread public static void start(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;
        Intent intent = new Intent(context, SyncAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + SYNC_EVERY_MS, SYNC_EVERY_MS, pi);
    }

    @AnyThread public static void stop(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) return;
        Intent intent = new Intent(context, SyncAlarmReceiver.class);
        am.cancel(PendingIntent.getBroadcast(context, 0, intent, 0));
    }

    @MainThread @Override public void onReceive(Context context, Intent intent) {
        if (!BufferList.syncHotlist()) stop(context);
    }
}
