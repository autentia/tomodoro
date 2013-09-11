package com.autentia.tomodoro.custom.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

public class CustomNumberPicker extends NumberPicker {

	public CustomNumberPicker(Context context) {
		super(context);
	}

	public CustomNumberPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomNumberPicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void addView(View child) {
		super.addView(child);
		updateView(child);
	}

	@Override
	public void addView(View child, int index,
			android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		updateView(child);
	}

	@Override
	public void addView(View child, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, params);
		updateView(child);
	}

	public void updateView(View view) {
		if (view instanceof EditText) {
			final Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/bebas.ttf");
			((EditText) view).setTextSize(30);
			((EditText) view).setTypeface(typeFace);
			((EditText) view).setTextColor(Color.parseColor("#000000"));
		}
	}
}