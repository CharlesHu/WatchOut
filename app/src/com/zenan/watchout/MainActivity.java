package com.zenan.watchout;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zenan.watchout.service.WatchService;
import com.zenan.watchout.util.RingtoneUtil;

public class MainActivity extends Activity {
	private Context context;

	
	private TextView text;
	private TextView text1;
	private TextView timerTextView;

	private Button addCountDownButton;
	private Button subtractCountDownButton;

	private Button startListening;
	private Button stopPlaying;

	private int timeRemaining;
	private int mAngle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = getApplicationContext();

		timeRemaining = 5;
		mAngle = 45;

		text = (TextView) findViewById(R.id.text);
		text1 = (TextView) findViewById(R.id.text1);
		timerTextView = (TextView) findViewById(R.id.timerTextView);

		addCountDownButton = (Button) findViewById(R.id.addCountDownButton);
		subtractCountDownButton = (Button) findViewById(R.id.subtractCountDownButton);
		addCountDownButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (timeRemaining < 999) {
					timeRemaining++;
					timerTextView.setText("Remaining: " + timeRemaining + "s");
				}
			}
		});
		subtractCountDownButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (timeRemaining > 0) {
					timeRemaining--;
					timerTextView.setText("Remaining: " + timeRemaining + "s");
				}
			}
		});

		startListening = (Button) findViewById(R.id.startListening);
		stopPlaying = (Button) findViewById(R.id.stopPlaying);

		startListening.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startListening.setClickable(false);
				addCountDownButton.setClickable(false);
				subtractCountDownButton.setClickable(false);
				System.out.println("" + timeRemaining);
				new CountDownTimer(timeRemaining*1000, 1000) {
					@Override
					public void onTick(long millisUntilFinished) {
						timerTextView.setText("Remaining: "
								+ (millisUntilFinished / 1000) + "s");
					}

					@Override
					public void onFinish() {
						timerTextView.setText("Listening...");
					}
				}.start();

				Intent intent = new Intent(MainActivity.this,
						WatchService.class);
				System.out.println("bind");
				bindService(intent, connection, Context.BIND_AUTO_CREATE);
				System.out.println("start");
				startService(intent);
			}
		});

		stopPlaying.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RingtoneUtil.stopRingtone();
				Intent intent = new Intent(MainActivity.this,
						WatchService.class);
				unbindService(connection);
				stopService(intent);
				startListening.setClickable(true);
				addCountDownButton.setClickable(true);
				subtractCountDownButton.setClickable(true);
			}
		});
	}

	private WatchService serviceBinder;

	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder service) {
			serviceBinder = ((WatchService.LocalBinder) service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			serviceBinder = null;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
