package com.autentia.pomodoro;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.autentia.custom.formatter.TimeFormatter;

public class ChronoActivity extends Activity{

	int minutes1 = 0;
	int minutes2 = 0;
	CountDownTimer timer;
	final TimeFormatter timeFormatter = new TimeFormatter();
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
		 
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.chrono_layout);
	        final Intent intent = getIntent();
	        if ( intent.hasExtra("minutes1") ) {
	        	minutes1 = intent.getExtras().getInt("minutes1");
	        }
	        if ( intent.hasExtra("minutes2") ) {
	        	minutes2 = intent.getExtras().getInt("minutes2");
	        }
	        initChrono();
	    }
	 
	public void backToMain(View view) {
		
		timer.cancel();
		final Intent intent = new Intent(this, MainActivity.class);
		final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.crowdboo);
		mediaPlayer.start();
		startActivity(intent);
	}
	


	private void initChrono() {
		
		final TextView chronoText = (TextView) findViewById(R.id.chronoText);
		setCustomFonts();
		chronoText.setText(minutes1 + minutes2 + ":00");
		final long totalMilisecondsCounting = calculateTotalMiliSecondsCounting();
		final GregorianCalendar gc = new GregorianCalendar(0, 0, 0, 0, minutes1*10 + minutes2, 0);
		timer = new CountDownTimer(totalMilisecondsCounting, 1000) {
			public void onTick(long millisUntilFinished) {
				gc.setTimeInMillis(gc.getTimeInMillis() - 1000);
				chronoText.setText(timeFormatter.toString(gc.getTime()));
			}
		 
			@Override
			public void onFinish() {
				changeChronoValue(chronoText);
				changeButtonText();
				changeTomatoePic();
				reproduceSound();
				sendNotification();
			}

			private void reproduceSound() {
				
				final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.trill);
				mediaPlayer.start();
			}

			private void changeChronoValue(final TextView chronoText) {
				
				chronoText.setText("00:00");
				chronoText.setTextSize(100);
				chronoText.setTextColor(Color.parseColor("#451405"));
			}

			private void changeButtonText() {
				
				final Button backButton = (Button) findViewById(R.id.stopButton);
				backButton.setText(R.string.back_button);
			}

			private void changeTomatoePic() {
				
				final ImageView tomatoe = (ImageView) findViewById(R.id.tomate);
				tomatoe.setImageDrawable(getResources().getDrawable(R.drawable.aplastado));
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
				.setAutoCancel(true);
				final NotificationManager notificationManager =
					    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(1, notificationBuilder.build());
			}
		}.start();
		
	}
	
	private void setCustomFonts() {
		
		setEightOneFontToComponent((TextView) findViewById(R.id.chronoTitle));
		setBebasFontToComponent((TextView) findViewById(R.id.titleChrono));
		setBebasFontToComponent((TextView) findViewById(R.id.stopButton));
	}
	
	private void setBebasFontToComponent(TextView textView) {
		
		final Typeface typeFace = Typeface.createFromAsset(getAssets(),
		            "fonts/bebas.ttf");
		textView.setTypeface(typeFace);
	}
	
	private void setEightOneFontToComponent(TextView textView) {
		
		final Typeface typeFace = Typeface.createFromAsset(getAssets(),
		            "fonts/eightone.ttf");
		textView.setTypeface(typeFace);
	}

	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		timer.cancel();
	}

	private long calculateTotalMiliSecondsCounting() {
		
		final int time1 = minutes1*600000;
		final int time2 = minutes2*60000;
		return Long.valueOf(String.valueOf(time1 + time2).toString());
	}
}