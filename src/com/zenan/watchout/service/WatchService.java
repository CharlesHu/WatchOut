package com.zenan.watchout.service;

import com.zenan.watchout.util.RingtoneUtil;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;

public class WatchService extends Service {
	LocalBinder mLocalBinder;
	private SensorManager mSensorManager = null;
	private Sensor mSensor = null;

	private float x, y, z;
	private float x1 = 0, y1 = 0, z1 = 0;
	private float x0 = 0, y0 = 0, z0 = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return mLocalBinder;
	}

	@Override
	public void onCreate() {
		System.out.println("onCreate()");
		mLocalBinder = new LocalBinder();

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		mSensorManager.registerListener(listener, mSensor,
				SensorManager.SENSOR_DELAY_GAME);

		new CountDownTimer(1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				mThread.start();
			}
		}.start();
	}

	private Thread mThread = new Thread() {
		@Override
		public void run() {
			x0 = x;
			y0 = y;
			z0 = z;
			while (true) {
				x1 = x;
				y1 = y;
				z1 = z;

				double ab = x0 * x1 + y0 * y1 + z0 * z1;
				double tt = (x0 * x0 + y0 * y0 + z0 * z0)
						* (x1 * x1 + y1 * y1 + z1 * z1);
				double angle = Math.acos(ab / Math.sqrt(tt)) / 2.0 / Math.PI
						* 360;

				System.out.println("x0: " + x0 + " y0: " + y0 + " z0: " + z0);
				System.out.println("x: " + x + " y: " + y + " z: " + z);
				System.out.println("x1: " + x1 + " y1: " + y1 + " z1: " + z1);
				System.out.println("angle: " + angle);
				if (angle > 45) {
					System.out.println("angle: " + angle);
					RingtoneUtil.playRingtone(getApplicationContext(),
							RingtoneManager.TYPE_RINGTONE);
					break;
				}
			}
		}
	};

	SensorEventListener listener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			x = event.values[0];
			y = event.values[1];
			z = event.values[2];
		}

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}
	};

	public class LocalBinder extends Binder {
		public WatchService getService() {
			return WatchService.this;
		}
	}
}
