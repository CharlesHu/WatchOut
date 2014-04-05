package com.zenan.watchout.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class RingtoneUtil {
	private static MediaPlayer mediaPlayer = null;

	public static Uri getDefaultRingtoneUri(Context context, int type) {
		return RingtoneManager.getActualDefaultRingtoneUri(context, type);
	}

	public static Ringtone getDefaultRingtone(Context context, int type) {
		return RingtoneManager.getRingtone(context,
				RingtoneManager.getActualDefaultRingtoneUri(context, type));
	}

	/**
	 * @param type
	 *            </br>&nbsp;&nbsp;&nbsp;&nbsp;通知声音
	 *            RingtoneManager.TYPE_NOTIFICATION;</br>
	 *            &nbsp;&nbsp;&nbsp;&nbsp; 警告 RingtoneManager.TYPE_ALARM;</br>
	 *            &nbsp;&nbsp;&nbsp;&nbsp; 铃声
	 *            RingtoneManager.TYPE_RINGTONE;</br>
	 */
	public static void playRingtone(Context context, int type) {
		mediaPlayer = MediaPlayer.create(context,
				getDefaultRingtoneUri(context, type));
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
	}

	public static void stopRingtone() {
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
		}
	}
}
