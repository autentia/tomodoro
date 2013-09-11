package com.autentia.tomodoro;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autentia.tomodoro.custom.formatter.TimeFormatter;
import com.autentia.tomodoro.services.CountDownService;

public class ChronoActivity extends Activity{

	private int minutes1 = 0;
	private int minutes2 = 0;
	private TextView chronoText;
	Intent serviceIntent;
	private boolean firstView = true;
	
	
	private BroadcastReceiver simpleReceiver = new BroadcastReceiver() {  
        
        @Override  
        public void onReceive(Context context, Intent intent) {  
              
            if ( intent.getAction().equals(CommonNames.ChronoActivityNames.BROADCAST_TIME_INTENT_NAME) ) {
            	final String time;
                if (intent.hasExtra(CommonNames.ChronoActivityNames.TIME_EXTRA)) {
                	time = intent.getExtras().getString(CommonNames.ChronoActivityNames.TIME_EXTRA);
                	if (TimeFormatter.FINAL_HOUR.equals(time)) {
                		endCountDownActions();
                	}	
                	chronoText.setText(time);
                }
            }  
        }  
    };
    
    
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
        initializeStopButton();
    }
	 
	private void initializeStopButton() {
		
		final View.OnClickListener stopButtonClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				stopTomodoro();
				goToMainActivity();
			}

		};
		final Button stopButton = (Button) findViewById(R.id.stopButton);
		stopButton.setOnClickListener(stopButtonClickListener);
		
	}

	private void initChrono() {
		
		chronoText = (TextView) findViewById(R.id.chronoText);
		setCustomFonts();
		serviceIntent = new Intent(this, CountDownService.class);
		serviceIntent.putExtra(CommonNames.ChronoActivityNames.MINUTES_EXTRA, minutes1*10 + minutes2);
		startService(serviceIntent);
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		if (!CountDownService.running && !firstView) {
			endCountDownActions();
		}
		firstView = false;
		final IntentFilter intentFilter = new IntentFilter();  
        intentFilter.addAction("ACTION_TIME_CHANGED");  
        registerReceiver(simpleReceiver, intentFilter);
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
		unregisterReceiver(simpleReceiver);
	}
	
	private void endCountDownActions() {
		
		changeChronoValue();
		changeBackButtonBehavior();
		changeTomatoePic();
	}
	
	private void changeChronoValue() {
		
		chronoText.setTextSize(100);
		chronoText.setTextColor(Color.parseColor("#451405"));
		chronoText.setText(TimeFormatter.FINAL_HOUR);
	}
	
	private void changeBackButtonBehavior() {
	
		final Button backButton = (Button) findViewById(R.id.stopButton);
		changeBackButtonText(backButton);
		changeBackButtonSound(backButton);
	}

	private void changeBackButtonText(Button backButton) {
		
		backButton.setText(R.string.back_button);
	}
	
	private void changeBackButtonSound(Button backButton) {
		
		final View.OnClickListener startButtonClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				goToMainActivity();
			}

		};
		backButton.setOnClickListener(startButtonClickListener);
	}
	
	private void changeTomatoePic() {
	
		final ImageView tomatoe = (ImageView) findViewById(R.id.tomate);
		tomatoe.setImageDrawable(getResources().getDrawable(R.drawable.aplastado));
	}
	
	private void setCustomFonts() {
		
		setEightOneFontToComponent((TextView) findViewById(R.id.chronoTitle));
		setBebasFontToComponent((TextView) findViewById(R.id.titleChrono));
		setBebasFontToComponent((TextView) findViewById(R.id.stopButton));
	}
	
	//TODO abarranco: extraer a método comun setFontToComponent
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
	
	private void goToMainActivity() {
		
		final Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private void stopTomodoro() {
		
		final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.crowdboo);
		mediaPlayer.start();
		stopService(serviceIntent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	stopTomodoro();
			Toast.makeText(getApplicationContext(), R.string.canceled, Toast.LENGTH_SHORT).show();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}