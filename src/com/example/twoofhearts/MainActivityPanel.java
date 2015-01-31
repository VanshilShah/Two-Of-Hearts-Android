package com.example.twoofhearts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ClickableViewAccessibility")
public class MainActivityPanel extends SurfaceView implements SurfaceHolder.Callback {
	
	MainActivity context;
	PanelThread _thread;	  
	public Paint paint = new Paint();
	public Bitmap heart = BitmapFactory.decodeResource(getResources(), R.drawable.biggestheart);
	public Bitmap card = BitmapFactory.decodeResource(getResources(), R.drawable.twocard);
	int meters = 100;
	Rect heartSpace = new Rect(250, 250, 850, 850);
	Rect heartSpaceTwo = new Rect(200, 200, 900, 900);
	Rect cardSpaceOne = new Rect(150, 300, 650, 900);
	Rect cardSpaceTwo = new Rect(350, 300, 800, 900);
	Rect signUpB = new Rect(100, 1150, 300,1350);
	Rect loginB = new Rect(425, 1150, 625,1350);
	Rect accountB = new Rect(750, 1150, 950,1350);
	Matrix flipHorizontalMatrix = new Matrix();

	//Constructors
	public MainActivityPanel(Context context) { 
		super(context);
		this.context = (MainActivity) context;	        
		this.setBackgroundColor(Color.WHITE);
		paint.setColor(Color.BLACK);
	}

	//Essentially the main method, runs multiple times and is where updating and drawing is done.
	@Override 
	public void onDraw(Canvas canvas) {
		//do drawing stuff here.
		update();
		draw(canvas, paint);	 		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		setWillNotDraw(false); //Allows us to use invalidate() to call onDraw()
		_thread = new PanelThread(getHolder(), this); //Start the thread that
		_thread.setRunning(true);                     //will make calls to 
		_thread.start();                              //onDraw()
		init();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			_thread.setRunning(false);                //Tells thread to stop
			_thread.join();                           //Removes thread from mem.
		} catch (InterruptedException e) {}
	}

	public void init() {
		
	}   
	public static boolean up = false;
	int count = 0;
	public void update() {
		count++;
		if(count%30 == 0 && up){
			MainActivity.regularUpdate();
		}
		 
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float eventX = event.getX();
		float eventY = event.getY();
		switch (event.getAction()) {
		  case MotionEvent.ACTION_DOWN:
			screenTouched(eventX, eventY);
			return true;
		  case MotionEvent.ACTION_MOVE:
			screenMoved(eventX, eventY);
			break;
		  case MotionEvent.ACTION_UP:
			screenReleased(eventX, eventY);
			break;
		  default:
			return false;
		}
		return true;
	}
	
	private void screenTouched(float eventX, float eventY) {
		int intX = (int) eventX, intY = (int) eventY;
		if (signUpB.contains(intX, intY)) {
			context.openSignUp();
		}
		else if (loginB.contains(intX, intY)) {
			context.openLogin();
		}
		else if (accountB.contains(intX, intY)) {
			context.openAccount();
		}
		this.postInvalidate();
	}
	
	private void screenMoved(float eventX, float eventY) {

		this.postInvalidate();
	}
	
	private void screenReleased(float eventX, float eventY) {

	}

	public void draw(Canvas canvas, Paint paint) {
		//canvas.draw
		canvas.drawBitmap(heart, null, heartSpace, paint);
		canvas.drawBitmap(card, null, cardSpaceOne, paint);
		canvas.drawBitmap(card, null, cardSpaceTwo, paint);
		canvas.drawCircle(200, 1250, 100, paint);
		canvas.drawCircle(525, 1250, 100, paint);
		canvas.drawCircle(850, 1250, 100, paint);
	} 
}