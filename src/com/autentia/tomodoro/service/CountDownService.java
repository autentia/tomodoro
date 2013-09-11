package com.autentia.tomodoro.service;

import java.util.GregorianCalendar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.autentia.tomodoro.CommonNames;
import com.autentia.tomodoro.MainActivity;
import com.autentia.tomodoro.R;
import com.autentia.tomodoro.custom.formatter.TimeFormatter;

public class CountDownService extends Service {
	
	private int minutes;
	
	private CountDownTimer timer;
	
	private final TimeFormatter timeFormatter = new TimeFormatter();
	
	public static boolean running = false;
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		minutes = intent.getIntExtra(CommonNames.ChronoActivityNames.MINUTES_EXTRA, 0);
		doTimer();
		running = true;
		return START_STICKY;
	}
	
	private void doTimer() {
		
		final GregorianCalendar gc = new GregorianCalendar(0, 0, 0, 0, minutes, 0);
		final long totalMilisecondsCounting = calculateTimeInMiliseconds(minutes);
		timer = new CountDownTimer(totalMilisecondsCounting, 1000) {
			
			public void onTick(long millisUntilFinished) {
				
				gc.setTimeInMillis(gc.getTimeInMillis() - 1000);
				sendBroadcastTime(timeFormatter.toString(gc.getTime()));
			}

			private void sendBroadcastTime(final String timeFormatted) {
				
				final Intent intent = new Intent(CommonNames.ChronoActivityNames.BROADCAST_TIME_INTENT_NAME);
				intent.putExtra(CommonNames.ChronoActivityNames.TIME_EXTRA, timeFormatted);
				getApplication().sendBroadcast(intent);
			}
		 
			@Override
			public void onFinish() {
				
				sendBroadcastTime(TimeFormatter.FINAL_HOUR);
				sendNotification();
				reproduceSound();
				stopSelf();
			}
		};
		
		timer.start();
	}
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		timer.cancel();
		running = false;
	}
	
	private long calculateTimeInMiliseconds(int minutes) {
		
		return minutes * 60000;
	}
	
	private void reproduceSound() {
		
		final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.trill);
		mediaPlayer.start();
	}
	
	private void sendNotification() {
		
		final Bitmap lageIcon = BitmapFactory.decodeResource(getResources(), R.drawable.tomate);
		final Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext())
		.setContentTitle(getText(R.string.greetings))
		.setContentText(getText(R.string.finished))
		.setSmallIcon(R.drawable.notificationiconsmall)
		.setLargeIcon(lageIcon)
		.setContentIntent(pendingIntent)
		.setAutoCancel(true)
		.setNumber(1);
		final NotificationManager notificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, notificationBuilder.build());
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}