package com.autentia.tomodoro;

import com.autentia.pomodoro.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	NumberPicker numberPicker1 = null;
	NumberPicker numberPicker2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

	private void init() {
		
		initNumberPickers();
        initStartButton();
        setCustomFonts();
	}

	private void setCustomFonts() {
		
		setBebasFontToComponent((TextView) findViewById(R.id.title));
		setBebasFontToComponent((TextView) findViewById(R.id.startButton));
		setEightOneFontToComponent((TextView) findViewById(R.id.minutes));
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

	private void initNumberPickers() {
		numberPicker1 = (NumberPicker) findViewById(R.id.minutePicker1);
		numberPicker2 = (NumberPicker) findViewById(R.id.minutePicker2);
		configureNumberPicker(numberPicker1);
		configureNumberPicker(numberPicker2);
	}
	
	private void configureNumberPicker(NumberPicker numberPicker) {
		
		numberPicker.setMinValue(0);
		numberPicker.setMaxValue(9);
		numberPicker.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChange(NumberPicker view, int scrollState) {
				
				final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.open);
				mediaPlayer.start();
			}
		});
	}

	private void initStartButton() {
		
		final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.clocktick);
		final View.OnClickListener startButtonClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				final Intent startIntent = new Intent("com.autentia.CHRONO_ACTIVATED");
				startIntent.putExtra("minutes1", numberPicker1.getValue());
				startIntent.putExtra("minutes2", numberPicker2.getValue());
				mediaPlayer.start();
			    startActivity(startIntent); 
			}
		};
		final Button startButton = (Button) findViewById(R.id.startButton);
		startButton.setOnClickListener(startButtonClickListener);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        moveTaskToBack(true);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}