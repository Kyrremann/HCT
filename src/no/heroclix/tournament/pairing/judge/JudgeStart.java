package no.heroclix.tournament.pairing.judge;

import java.util.ArrayList;

import no.heroclix.tournament.pairing.NodeApplication;
import no.heroclix.tournament.pairing.R;
import no.heroclix.tournament.pairing.archive.Database;
import android.R.style;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class JudgeStart extends Activity {

	LinearLayout ll;
	TableLayout tl;
	int rowIndex, playerID, pointsID;
	String pairing;
	Spinner pairingType;
	TextView playerRemove, start, add, setDuration;
	EditText editTournamentName, editPlayerName, editRounds, editPlayerPoints;
	ArrayList<TableRow> viewRow;
	Database database;
	Cursor c;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.judgestart);

		start = (TextView) findViewById(R.id.judgeBStart);
		add = (TextView) findViewById(R.id.judgeAdd);
		setDuration = (TextView) findViewById(R.id.judgeStartDuration);
		tl = (TableLayout) findViewById(R.id.judgeStartTable);
		pairingType = (Spinner) findViewById(R.id.judgePairing);
		editTournamentName = (EditText) findViewById(R.id.judgeTournament);
		editRounds = (EditText) findViewById(R.id.judgeRound);
		// editDuration = (EditText) findViewById(R.id.judgeDuration);
		rowIndex = 1;
		playerID = 1001;
		pointsID = 2001;
		pairing = "Swiss";
		viewRow = new ArrayList<TableRow>();
		database = new Database(this);

		final Object data = getLastNonConfigurationInstance();
		if (data != null)
			onOrientationChange(data);
		else
			viewRow.add(creatInputRow(null, null));

		// for (int i = 1; i < 5; i++)
		// testAdd(i);

		/**
		 * TODO gå igjennom alle tidligere view og bytt om ID til ny ID
		 */

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewRow.add(creatInputRow(null, null));
			}
		});

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startTournament();
			}
		});

		// TODO pairingType
		pairingType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				pairing = (String) arg0.getItemAtPosition(arg2);
				if (arg2 == 0) {
					editRounds.setText("3");
					// editDuration.setText("50");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		setDuration.setOnClickListener(new OnClickListener() {

			EditText duration, first, second;
			View layout;
			LayoutInflater inflater;

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v
						.getContext());

				inflater = (LayoutInflater) v.getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				layout = inflater.inflate(R.layout.editduration,
						(ViewGroup) findViewById(R.layout.judgestart));
				duration = (EditText) layout.findViewById(R.id.durationMain);
				first = (EditText) layout.findViewById(R.id.durationFirst);
				second = (EditText) layout.findViewById(R.id.durationSecond);

				database.open();
				c = database.getDuration();
				c.moveToFirst();
				duration.setText(c.getString(1));
				first.setText(c.getString(2));
				second.setText(c.getString(3));
				c.close();

				builder.setView(layout);
				builder.setTitle("Change duration");
				builder.setCancelable(true);
				builder.setPositiveButton("Save",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								database.setDuration(duration.getText()
										.toString(),
										first.getText().toString(), second
												.getText().toString());
								database.close();
								dialog.cancel();
							}
						});

				builder.setNeutralButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								database.close();
								dialog.cancel();
							}
						});

				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
	}

	protected void startTournament() {
		String[] players;
		int[] points;
		EditText playerName, playerPoints, rest;
		String tournamentName, playerString, pointsString;
		int rounds;

		players = new String[viewRow.size()];
		points = new int[viewRow.size()];

		if (players.length < 2) {
			inputMissing("Too few players. At least two.");
			return;
		}

		rest = (EditText) findViewById(R.id.judgeRound);
		if (rest.getText().toString().equals("")) {
			inputMissing("Number of rounds is missing");
			rest.requestFocus();
			return;
		}
		rounds = Integer.parseInt(rest.getText().toString());

		// rest = (EditText) findViewById(R.id.judgeDuration);
		// if (rest.getText().toString().equals("")) {
		// inputMissing("duration is missing");
		// rest.requestFocus();
		// return;
		// }
		// duration = Integer.parseInt(rest.getText().toString());

		rest = (EditText) findViewById(R.id.judgeTournament);
		if (rest.getText().toString().equals("")) {
			inputMissing("Tournament name is missing");
			rest.requestFocus();
			return;
		}
		tournamentName = rest.getText().toString();

		for (int i = 0; i < viewRow.size(); i++) {
			playerName = (EditText) viewRow.get(i).getChildAt(0);
			playerPoints = (EditText) viewRow.get(i).getChildAt(1);

			playerString = playerName.getText().toString();
			if (playerString.equals("")) {
				inputMissing("Name of player is missing");
				playerName.requestFocus();
				return;
			}
			players[i] = playerString;

			pointsString = playerPoints.getText().toString();
			if (pointsString.equals("")) {
				inputMissing("Points of player is missing");
				playerPoints.requestFocus();
				return;
			}
			points[i] = Integer.parseInt(pointsString);
		}

		Intent intent = new Intent(getApplicationContext(), JudgeRound.class);
		intent.putExtra("PLAYERS", players);
		intent.putExtra("POINTS", points);
		intent.putExtra("ROUNDS", rounds);
		// TODO old code!
		database.open();
		c = database.getDuration();
		c.moveToFirst();
		String duration = c.getString(1) + ";" + c.getString(2) + ";"
				+ c.getString(3);
		c.close();
		database.close();
		intent.putExtra("DURATION", duration);
		// intent.putExtra("DURATION", "50");
		intent.putExtra("PAIRING", pairing);
		intent.putExtra("NAME", tournamentName);
		((NodeApplication) getApplication()).setDumpedPlayers(false);
		startActivity(intent);
		finish();
	}

	/**
	 * Can create empty or pre-filled rows. Send null to avoid filling.
	 * 
	 * @param name
	 *            name of player
	 * @param points
	 *            players team points
	 * @return TableRow
	 */
	protected TableRow creatInputRow(String name, String points) {
		EditText playerName;
		final EditText playerPoints;
		TextView delete;
		LayoutParams params;
		Context context;

		context = getApplicationContext();

		params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, (float) 1);

		playerName = new EditText(context);
		playerPoints = new EditText(context);
		delete = new TextView(context);

		playerName.setHint("Name");
		if (name != null)
			playerName.setText(name);
		playerName.setId(playerID++);
		playerName.setSingleLine(true);
		playerName.setLayoutParams(params);
		playerName.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
		playerName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		playerName.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					playerPoints.requestFocus();
					return true;
				}
				return false;
			}
		});

		playerPoints.setHint("Points");
		if (points != null)
			playerPoints.setText(points);
		playerPoints.setInputType(InputType.TYPE_CLASS_NUMBER);
		playerPoints.setId(pointsID++);
		playerPoints.setSingleLine(true);
		playerPoints.setFocusableInTouchMode(true);
		playerPoints.setLayoutParams(params);
		playerPoints.setInputType(InputType.TYPE_CLASS_NUMBER);
		// playerPoints.setImeActionLabel("Next",
		// EditorInfo.IME_ACTION_NEXT);
		// playerPoints
		// .setOnEditorActionListener(new OnEditorActionListener() {
		//
		// @Override
		// public boolean onEditorAction(TextView v,
		// int actionId, KeyEvent event) {
		// if (actionId == EditorInfo.IME_ACTION_NEXT) {
		// if (playerID != v.getId() + 1) {
		// Log.d("IME", "Going to player");
		// tl.findViewById(playerID - 2)
		// .requestFocus();
		// return true;
		// }
		// }
		// return false;
		// }
		// });

		delete.setText("X");
		delete.setTextAppearance(context, style.TextAppearance_Large);
		delete.setId(rowIndex);
		delete.setPadding(10, 0, 10, 0);
		delete.setGravity(Gravity.CENTER);

		final TableRow row = new TableRow(context);
		row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		row.setId(rowIndex);
		row.addView(playerName);
		row.addView(playerPoints);
		row.addView(delete);

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewRow.remove(row);
				tl.removeView(row);
			}
		});

		tl.addView(row, 0);
		playerName.requestFocus();
		return row;
	}

	private void inputMissing(String s) {
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}

	@SuppressWarnings("unused")
	private void testAdd(int i) {
		EditText playerName, playerPoints;

		playerName = new EditText(this);
		playerName.setText("Test" + i);
		playerName.setId(playerID++);

		playerPoints = new EditText(this);
		playerPoints.setText("300");
		playerPoints.setId(pointsID++);

		TableRow row = new TableRow(this);
		row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		row.addView(playerName);
		row.addView(playerPoints);
		viewRow.add(row);
		tl.addView(row, 1);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		ArrayList<Object> data = new ArrayList<Object>();

		data.add(editTournamentName.toString());
		data.add(editRounds.toString());
		// data.add(editDuration.toString());
		for (TableRow row : viewRow) {
			data.add(((EditText) row.getChildAt(0)).getText().toString());
			data.add(((EditText) row.getChildAt(1)).getText().toString());
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	private void onOrientationChange(Object lastData) {
		ArrayList<Object> data = (ArrayList<Object>) lastData;
		String name, points;
		editTournamentName.setText((String) data.remove(0));
		editRounds.setText((String) data.remove(0));
		// editDuration.setText((String) data.remove(0));

		while (!data.isEmpty()) {
			name = data.remove(0).toString();
			points = data.remove(0).toString();
			viewRow.add(creatInputRow(name, points));
		}
	}
}