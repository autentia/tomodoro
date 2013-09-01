package com.autentia.tomodoro.custom.components;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class VerticalTextView extends TextView {

	public VerticalTextView(Context context) {
		super(context);
	}

	public VerticalTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VerticalTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void draw(Canvas canvas) {
		
		canvas.translate(getHeight(), 0);
		canvas.rotate(-90);
		canvas.clipRect(0, 0, getWidth(), getHeight(),android.graphics.Region.Op.REPLACE);
		super.draw(canvas);
		super.draw(canvas);
	}
}