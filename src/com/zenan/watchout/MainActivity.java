package com.zenan.watchout;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private SensorManager mSensorManager = null;
	private Sensor mSensor = null;
	private float x, y, z;
	private float x1 = 0, y1 = 0, z1 = 0;
	private float x0 = 0, y0 = 0, z0 = 0;
	private TextView text;
	private TextView text1;

	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		text = (TextView) findViewById(R.id.text);
		text1 = (TextView) findViewById(R.id.text1);

		button = (Button) findViewById(R.id.button);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		mSensorManager.registerListener(listener, mSensor,
				SensorManager.SENSOR_DELAY_GAME);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				x0 = x;
				y0 = y;
				z0 = z;
//				Toast.makeText(
//						getApplicationContext(),
//						"x0: " + x0 + " y0: " + y0 + " z0: " + z0 + "\nx: " + x
//								+ " y: " + y + " z: " + z, Toast.LENGTH_LONG)
//						.show();
				new Thread() {
					@Override
					public void run() {
						while (true) {
							x1 = x;
							y1 = y;
							z1 = z;

							double ab = x0 * x1 + y0 * y1 + z0 * z1;
							double tt = (x0 * x0 + y0 * y0 + z0 * z0)
									* (x1 * x1 + y1 * y1 + z1 * z1);
							double angle = Math.acos(ab / Math.sqrt(tt)) / 2.0
									/ Math.PI * 360;

							System.out.println("x0: " + x0 + " y0: " + y0
									+ " z0: " + z0);
							System.out.println("x: " + x + " y: " + y + " z: "
									+ z);
							System.out.println("x1: " + x1 + " y1: " + y1
									+ " z1: " + z1);
							System.out.println("angle: " + angle);
							if (angle > 90) {
//								Toast.makeText(getApplicationContext(),
//										"angle: " + angle, Toast.LENGTH_LONG)
//										.show();
								System.out.println("angle: " + angle);
								break;
							}
						}
					}
				}.start();
			}
		});
	}

	SensorEventListener listener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			x = event.values[0];
			y = event.values[1];
			z = event.values[2];
			// System.out.println("x: " + x + " y: " + y + " z: " + z);
		}

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
