package com.example.shuka.notificationexample;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.icu.util.GregorianCalendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

// NotificationManager : Allows us to notify the user that something happened in the background
// AlarmManager : Allows you to schedule for your application to do something at a later date
// even if it is in the background

public class MainActivity extends AppCompatActivity {

    Button showNotificationBut, stopNotificationBut, alertButton;

    // Allows us to notify the user that something happened in the background
    NotificationManager notificationManager;

    // Used to track if notification is active in the task bar
    boolean isNotificActive = false;

    // Used to track notifications
    int notifID = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showNotificationBut = (Button) findViewById(R.id.showNotificationBut);
        stopNotificationBut = (Button) findViewById(R.id.stopNotificationBut);
        alertButton = (Button) findViewById(R.id.alertButton);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification(View view) {

        // Builds a notification
        NotificationCompat.Builder notificBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(this)
                .setContentTitle("Message")
                .setContentText("New Message")
                .setTicker("Alert New Message")
                .setSmallIcon(R.drawable.icon);

        // Define that we have the intention of opening MoreInfoNotification
        Intent moreInfoIntent = new Intent(this, MoreInfoNotification.class);

        // Used to stack tasks across activites so we go to the proper place when back is clicked
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);

        // Add all parents of this activity to the stack
        taskStackBuilder.addParentStack(MoreInfoNotification.class);

        // Add our new Intent to the stack
        taskStackBuilder.addNextIntent(moreInfoIntent);

        // Define an Intent and an action to perform with it by another application
        // FLAG_UPDATE_CURRENT : If the intent exists keep it but update it if needed
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Defines the Intent to fire when the notification is clicked
        notificBuilder.setContentIntent(pendingIntent);

        // Gets a NotificationManager which is used to notify the user of the background event
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Post the notification
        notificationManager.notify(notifID, notificBuilder.build());

        // Used so that we can't stop a notification that has already been stopped
        isNotificActive = true;

    }

    public void stopNotification(View view) {

        if (isNotificActive) {
            notificationManager.cancel(notifID);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAlarm(View view) {

        // Define a time value of 5 seconds
        Long alertTime = new GregorianCalendar().getTimeInMillis()+5*1000;

        // Define our intention of executing AlertReceiver
        Intent alertIntent = new Intent(this, AlertReceiver.class);

        // Allows you to schedule for your application to do something at a later date
        // even if it is in he background or isn't active
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // set() schedules an alarm to trigger
        // Trigger for alertIntent to fire in 5 seconds
        // FLAG_UPDATE_CURRENT : Update the Intent if active
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime,
                PendingIntent.getBroadcast(this, 1, alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));

    }
}
