package no.heroclix.tournament.pairing;

import no.heroclix.tournament.pairing.archive.Database;
import no.heroclix.tournament.pairing.archive.History;
import no.heroclix.tournament.pairing.judge.JudgeRound;
import no.heroclix.tournament.pairing.judge.JudgeStart;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {

	TextView timer;
	Database database;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		database = new Database(this);
		database.open();
		try {
			Cursor c = database.getDump();
			c.moveToFirst();
			final String dump = c.getString(1);
			final String extra = c.getString(2);
			c.close();
			((NodeApplication) getApplication()).setDumpedPlayers(true);
			final TextView continueButton = (TextView) findViewById(R.id.continueButton);
			continueButton.setVisibility(Button.VISIBLE);
			continueButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(),
							JudgeRound.class);
					intent.putExtra("EXTRA", extra);
					intent.putExtra("DUMP", dump);
					startActivity(intent);
					finish();
					// TODO husk app changeDumpedPlayers() true/false
					continueButton.setVisibility(Button.GONE);
				}
			});
		} catch (SQLException e) {
			Log.d("EXCEPTION", e.getMessage());
		}
		database.close();

		TextView judge = (TextView) findViewById(R.id.judgeButton);
		judge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						JudgeStart.class);
				startActivity(intent);
			}
		});

		TextView history = (TextView) findViewById(R.id.historyButton);
		history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						History.class);
				startActivity(intent);
			}
		});

		TextView help = (TextView) findViewById(R.id.helpButton);
		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v
						.getContext());
				builder.setTitle("Help");
				builder.setMessage(R.string.help);
				builder.setNeutralButton("Done",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				Dialog dialog = builder.create();
				dialog.show();
			}
		});
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Report");
		menu.add(0, 1, 0, "Donate");
		menu.add(0, 2, 0, "About");
		return result;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			createReport();
			break;
		case 1:
			createDonate();
			break;
		case 2:
			createAbout();
			break;
		}
		return false;
	}

	private void createAbout() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("About");
		builder.setMessage(R.string.about);
		builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void createDonate() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Donation");
		builder.setMessage(R.string.donate);
		builder.setPositiveButton("Okey",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent browserIntent = new Intent(
								"android.intent.action.VIEW",
								Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=ATWBY2VZ3SW7Q"));
						startActivity(browserIntent);
					}
				});
		builder.setNegativeButton("Later",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void createReport() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "heroclix@fifthfloorstudio.net" });
		String version = "0.*";
		try {
			version = getPackageManager().getPackageInfo(
					"no.heroclix.tournament.pairing", 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		intent.putExtra(Intent.EXTRA_SUBJECT,
				"Feedback: Heroclix Tournament Pairing BETA " + version);
		startActivity(Intent.createChooser(intent, ""));
	}
}