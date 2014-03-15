package com.zenan.watchout;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zenan.watchout.util.RingtoneUtil;

public class MainActivity extends Activity {
	private Context context;

	private SensorManager mSensorManager = null;
	private Sensor mSensor = null;
	private float x, y, z;
	private float x1 = 0, y1 = 0, z1 = 0;
	private float x0 = 0, y0 = 0, z0 = 0;
	private TextView text;
	private TextView text1;
	private TextView timerTextView;

	private Button startListening;
	private Button stopPlaying;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = getApplicationContext();

		text = (TextView) findViewById(R.id.text);
		text1 = (TextView) findViewById(R.id.text1);
		timerTextView = (TextView) findViewById(R.id.timerTextView);

		startListening = (Button) findViewById(R.id.startListening);
		stopPlaying = (Button) findViewById(R.id.stopPlaying);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		mSensorManager.registerListener(listener, mSensor,
				SensorManager.SENSOR_DELAY_GAME);

		startListening.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new CountDownTimer(5000, 1000) {
					@Override
					public void onTick(long millisUntilFinished) {
						timerTextView.setText("Remaining: "
								+ (millisUntilFinished / 1000) + "s");
					}

					@Override
					public void onFinish() {
						timerTextView.setText("Listening...");
						x0 = x;
						y0 = y;
						z0 = z;
					}
				}.start();

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
							if (angle > 45) {
								// Toast.makeText(getApplicationContext(),
								// "angle: " + angle, Toast.LENGTH_LONG)
								// .show();

								System.out.println("angle: " + angle);
								RingtoneUtil.playRingtone(context,
										RingtoneManager.TYPE_RINGTONE);
								break;
							}
						}
					}
				}.start();
			}
		});

		stopPlaying.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RingtoneUtil.stopRingtone();
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
