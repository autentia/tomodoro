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

import com.autentia.tomodoro.services.CountDownService;

public class ChronoActivity extends Activity{

	private int minutes1 = 0;
	private int minutes2 = 0;
	private TextView chronoText;
	Intent serviceIntent;
	
	private BroadcastReceiver simpleReceiver = new BroadcastReceiver() {  
        
		//TODO abarranco: enterarse donde es mejor guardar los atributos de los intent para centralizarlos (time x ejemplo)
        @Override  
        public void onReceive(Context context, Intent intent) {  
              
            if ( intent.getAction().equals("ACTION_TIME_CHANGED") ) {
            	final String time;
                if (intent.hasExtra("time")) {
                	time = intent.getExtras().getString("time");
                	if ("00:00".equals(time)) {
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
    }
	 
	private void initChrono() {
		
		chronoText = (TextView) findViewById(R.id.chronoText);
		setCustomFonts();
		serviceIntent = new Intent(this, CountDownService.class);
		serviceIntent.putExtra(IntentExtraConstant.ChronoActivityExtraNames.MINUTES, minutes1*10 + minutes2);
		startService(serviceIntent);
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		final IntentFilter intentFilter = new IntentFilter();  
        intentFilter.addAction("ACTION_TIME_CHANGED");  
        registerReceiver(simpleReceiver, intentFilter);
	}
	
	@Override
	protected void onPause() {
		
		unregisterReceiver(simpleReceiver);
		super.onPause();
	}
	
	private void endCountDownActions() {
		
		changeChronoValue();
		changeBackButtonBehavior();
		changeTomatoePic();
	}
	
	private void changeChronoValue() {
		
		chronoText.setTextSize(100);
		chronoText.setTextColor(Color.parseColor("#451405"));
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
		
		//TODO: abarranco, añadir un onclicklistener el boton con el sonido de volver que no es uuuuuu
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
	
	public void backToMain(View view) {
		
		final Intent intent = new Intent(this, MainActivity.class);
		stopTomodoro();
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