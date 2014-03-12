package com.zenan.watchout;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends Activity {
	private SensorManager mSensorManager = null;
	private Sensor mSensor = null;
	private float x, y, z;
	private float x1 = 0, y1 = 0, z1 = 0;
	private float x2 = 0, y2 = 0, z2 = 0;
	private TextView text;
	private TextView text1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		text = (TextView) findViewById(R.id.text);
		text1 = (TextView) findViewById(R.id.text1);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	}

	SensorEventListener listener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			x = event.values[SensorManager.DATA_X];
			y = event.values[SensorManager.DATA_Y];
			z = event.values[SensorManager.DATA_Z];

		}

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {

		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mSensorManager.registerListener(listener, mSensor,
					SensorManager.SENSOR_DELAY_GAME);

			double ab = x * x1 + y * y1 + z * z1;
			double tt = (x * x + y * y + z * z) * (x1 * x1 + y1 * y1 + z1 * z1);
			double angle = Math.acos(ab / Math.sqrt(tt))/2.0/Math.PI*360;
			
			text.setText("x: " + x + "\ny: " + y + "\nz: " + z);
			
			text1.setText("angle: " + angle);
			x1 = x;
			y1 = y;
			z1 = z;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
