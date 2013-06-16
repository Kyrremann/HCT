package no.heroclix.tournament.pairing.gui;

import no.heroclix.tournament.pairing.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.TextView;

public class Duration extends CountDownTimer {

	private long pauseTime;
	private long notifyFirst, notifySecond;
	private TextView countView;
	private Context context;

	/**
	 * 
	 * @see android.os.CountDownTimer;
	 * @param millisInFuture
	 * @param countDownInterval
	 */
	public Duration(long millisInFuture, long countDownInterval, TextView view,
			Context context, long first, long second) {
		super(millisInFuture, countDownInterval);
		pauseTime = millisInFuture;
		notifyFirst = first / 1000;
		notifySecond = second / 1000;
		countView = view;
		this.context = context;

	}

	@Override
	public void onFinish() {
		countView.setText("0:00");
		onNotify("FINISH");
	}

	/**
	 * I've added a little twist to the onThick() method, as it will call a
	 * onNotify() method who notify the user that a certain time has passed
	 * since the timer started
	 * 
	 * @see android.os.CountDownTimer.onTick;
	 * @param millisUntilFinished
	 *            milliseconds until finished
	 */
	@Override
	public void onTick(long millisUntilFinished) {
		pauseTime = millisUntilFinished;

		if ((millisUntilFinished / 1000) == notifyFirst)
			onNotify("FIRST");
		else if (millisUntilFinished / 1000 == notifySecond)
			onNotify("SECOND");

		if ((millisUntilFinished / 1000 % 60) < 10)
			countView.setText(millisUntilFinished / 60000 + ":0"
					+ (millisUntilFinished / 1000) % 60);
		else
			countView.setText(millisUntilFinished / 60000 + ":"
					+ (millisUntilFinished / 1000) % 60);
	}

	/**
	 * Just cancel the ongoing Timer. Need to call onResume() to resume
	 */
	public void onPause() {
		super.cancel();
	}

	/**
	 * Creates a new Duration object with the remaining time
	 * 
	 * @return new Duration object
	 */
	public Duration onResume() {
		return new Duration(pauseTime, 1000, countView, context, notifyFirst,
				notifySecond);
	}

	/**
	 * Called when a notify is needed. Will vibrate and make a sound with the
	 * users default tone
	 * 
	 * @param event
	 *            name of the calling event
	 */
	public void onNotify(String event) {
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		String title = "";
		int icon = R.drawable.ic_notification;
		if (event.equals("FINISH"))
			title = "Round over";
		else if (event.equals("FIRST"))
			title = (notifyFirst / 60) + " minutes left";
		else
			title = (notifySecond / 60) + " minutes left";

		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, title, when);

		String contentTitle = "Tournament notifyer";
		String contentText = "Click to remove notification";

		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Intent notificationIntent = new Intent(context, Duration.class);

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		mNotificationManager.notify(0, notification);
	}

	/**
	 * @return current time in milliseconds
	 */
	public long getTime() {
		return pauseTime;
	}

}
