package no.heroclix.tournament.pairing.judge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import no.heroclix.tournament.pairing.Node;
import no.heroclix.tournament.pairing.NodeApplication;
import no.heroclix.tournament.pairing.R;
import no.heroclix.tournament.pairing.archive.Database;
import no.heroclix.tournament.pairing.gui.Duration;
import no.heroclix.tournament.pairing.gui.PlayerRow;
import no.heroclix.tournament.pairing.type.Swiss;
import no.heroclix.tournament.pairing.type.TournamentType;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class JudgeRound extends Activity {

	int rounds, roundCount;
	long durationMs, notifyFirst, notifySecond;
	String pairing, tournamentName;
	boolean timerClicked, timerPaused, rotatedPhone;
	Object data;

	TextView playersBye, roundCountView, addPlayer, nextRound, nameView,
			endtTournament, counterView, editRound;
	Bundle bundle;
	LinearLayout linear;
	CheckBox checkAutoFill;
	ScrollView scroll;
	Button timerStop, timerStart;

	Node bye;
	TournamentType tournament;
	Duration timer;
	NodeApplication app;

	ArrayList<RelativeLayout> viewPlayerList;
	LinkedList<Node> playersNode;
	LinkedList<Dialog> dialogScore;

	Comparator<Node> compRounds;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.judgeround);

		// Set up views
		linear = (LinearLayout) findViewById(R.id.judgeRoundLinearLayout);
		addPlayer = (TextView) findViewById(R.id.judgeAddPlayer);
		nextRound = (TextView) findViewById(R.id.judgeNextRound);
		endtTournament = (TextView) findViewById(R.id.judgeEndTournament);
		playersBye = (TextView) findViewById(R.id.judgeBye);
		roundCountView = (TextView) findViewById(R.id.judgeRoundCount);
		nameView = (TextView) findViewById(R.id.judgeName);
		checkAutoFill = (CheckBox) findViewById(R.id.judgeAutoFill);
		scroll = (ScrollView) findViewById(R.id.judgeRoundScroll);
		counterView = (TextView) findViewById(R.id.judgeRoundTimer);
		timerStop = (Button) findViewById(R.id.timerStop);
		timerStart = (Button) findViewById(R.id.timerStart);
		editRound = (TextView) findViewById(R.id.judgeEditRound);

		app = (NodeApplication) getApplication();
		bundle = getIntent().getExtras();
		playersNode = new LinkedList<Node>();

		Object data = getLastNonConfigurationInstance();
		if (data != null)
			onOrientationChange(data);
		else if (app.isPlayersDumped()) {
			String[] extra = bundle.getString("EXTRA").split(";");
			rounds = Integer.valueOf(extra[0]);
			roundCount = Integer.valueOf(extra[1]);
			pairing = extra[2];
			tournamentName = extra[3];

			Database database = new Database(this);
			database.open();
			Cursor c = database.getDuration();
			c.moveToFirst();
			durationMs = Long.valueOf(c.getString(1)) * 60000;
			notifyFirst = Long.valueOf(c.getString(2)) * 60000;
			notifySecond = Long.valueOf(c.getString(3)) * 60000;
			Log.d("TIME", "" + durationMs + " " + notifyFirst + " "
					+ notifySecond);
			timer = new Duration(durationMs, 1000, counterView, this,
					notifyFirst, notifySecond);
			Log.d("TIME", "" + timer.getTime());
			c.close();
			database.close();
			String[] dump = bundle.getString("DUMP").split("#");
			for (int i = 0; i < dump.length; i++) {
				String[] s = dump[i].split(";");
				Node n = new Node(s[0], Integer.parseInt(s[1]));
				n.setWin(Integer.parseInt(s[2]));
				n.setLoss(Integer.parseInt(s[3]));
				n.setPoints(Integer.parseInt(s[4]));
				playersNode.add(n);
			}
			app.setDumpedPlayers(false);
		} else {
			// Retrieve bundle info
			roundCount = 1;
			String[] players = bundle.getStringArray("PLAYERS");
			int[] points = bundle.getIntArray("POINTS");
			rounds = bundle.getInt("ROUNDS");
			String[] durations = bundle.getString("DURATION").split(";");
			durationMs = Long.parseLong(durations[0]) * 60000;
			notifyFirst = Long.parseLong(durations[1]) * 60000;
			notifySecond = Long.parseLong(durations[2]) * 60000;
			// durationMs = (long)
			// getIntent().getExtras().getInt("DURATION") *
			// 60000;
			timer = new Duration(durationMs, 1000, counterView, this,
					notifyFirst, notifySecond);
			pairing = bundle.getString("PAIRING");
			tournamentName = bundle.getString("NAME");

			for (int i = 0; i < players.length; i++)
				playersNode.add(new Node(players[i], points[i]));
			Collections.shuffle(playersNode);
		}

		tournament = decideTournament(pairing);
		viewPlayerList = new ArrayList<RelativeLayout>();
		nameView.setText(tournamentName);
		compRounds = tournament.comparator;

		checkAutoFill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (app.getAutoFill())
					app.setAutoFill(false);
				else
					app.setAutoFill(true);
			}
		});

		initializeTimer();
		startTournament();
	}

	/**
	 * Initializing the timer
	 */
	private void initializeTimer() {
		long currentTime = timer.getTime();
		if ((currentTime / 1000 % 60) < 10)
			counterView.setText(currentTime / 60000 + ":0"
					+ (currentTime / 1000) % 60);
		else
			counterView.setText(currentTime / 60000 + ":"
					+ (currentTime / 1000) % 60);
		timerClicked = timerPaused = false;

		timerStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!timerClicked) {
					timer.start();
					timerClicked = true;
					return;
				} else if (timerPaused) {
					timer = timer.onResume();
					timer.start();
					timerPaused = false;
					return;
				}
				timer.onPause();
				timerPaused = true;
			}
		});

		timerStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				timer.cancel();
				if ((durationMs / 1000 % 60) < 10)
					counterView.setText(durationMs / 60000 + ":0"
							+ (durationMs / 1000) % 60);
				else
					counterView.setText(durationMs / 60000 + ":"
							+ (durationMs / 1000) % 60);
				timer = new Duration(durationMs, 1000, counterView, v
						.getContext(), notifyFirst, notifySecond);
				timerClicked = timerPaused = false;
			}
		});
	}

	/**
	 * Starts the tournament type
	 */
	private void startTournament() {

		addPlayer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v
						.getContext());
				builder.setTitle("Name of player");
				LinearLayout ll = new LinearLayout(v.getContext());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,
						(float) 1);
				final EditText inputName = new EditText(v.getContext());
				inputName.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
				inputName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				inputName.setLayoutParams(params);

				final EditText inputTeam = new EditText(v.getContext());
				inputTeam.setLayoutParams(params);
				inputTeam.setInputType(InputType.TYPE_CLASS_NUMBER);

				inputName
						.setOnEditorActionListener(new OnEditorActionListener() {

							@Override
							public boolean onEditorAction(TextView v,
									int actionId, KeyEvent event) {
								if (actionId == EditorInfo.IME_ACTION_NEXT) {
									inputTeam.requestFocus();
									return true;
								}
								return false;
							}
						});

				ll.addView(inputName);
				ll.addView(inputTeam);
				builder.setView(ll);

				builder.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String name = inputName.getText().toString();
								String score = inputTeam.getText().toString();
								if (name.equals("") || score.equals("")) {
									Toast.makeText(v.getContext(),
											"Missing something",
											Toast.LENGTH_SHORT).show();
								} else {
									addExtraPlayer(name,
											Integer.parseInt(score));
									dialog.cancel();
								}
							}
						});

				builder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						});

				Dialog dialog = builder.create();
				dialog.show();
			}
		});

		nextRound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (saveData()) {
					timer.cancel();
					if ((durationMs / 1000 % 60) < 10)
						counterView.setText(durationMs / 60000 + ":0"
								+ (durationMs / 1000) % 60);
					else
						counterView.setText(durationMs / 60000 + ":"
								+ (durationMs / 1000) % 60);
					timer = new Duration(durationMs, 1000, counterView, v
							.getContext(), notifyFirst, notifySecond);
					timerClicked = timerPaused = false;
					roundCount++;
					nextRound();
				}
			}
		});

		endtTournament.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(v
						.getContext());
				builder.setTitle("End tournament?");
				builder.setMessage(R.string.endTournament);
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (saveData())
									tournamentFinished();
								dialog.cancel();
							}
						});

				builder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		// editRound.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// app.setNewPlayerList(playersNode);
		// Intent intent = new Intent(v.getContext(), JudgeEditRound.class);
		// startActivityForResult(intent, RESULT_FIRST_USER);
		// }
		// });

		createLayout();

		nextRound();
	}

	private void createLayout() {
		// currentRound = tournament.getLinkedList();

		for (int i = 0; i < playersNode.size() - 1; i += 2) {
			PlayerRow pr = new PlayerRow(this, i / 2, this);
			viewPlayerList.add(pr);
			linear.addView(pr);
			if (!rotatedPhone)
				app.addScore(false);
		}
	}

	/**
	 * Goes through the rounds, one round at a time. Special cases are first
	 * round and rounds with only two players.
	 */
	private void nextRound() {

		if (roundCount > rounds) {
			tournamentFinished();
			return;
		} else if ((roundCount + 1) == rounds) {
			nextRound.setText("Last round");
		} else if (roundCount == rounds) {
			nextRound.setText("Finish");
		}

		if (!rotatedPhone)
			playersNode = tournament.nextRound();

		dialogScore = new LinkedList<Dialog>();
		bye = null;
		scroll.fullScroll(ScrollView.FOCUS_UP);

		Log.d("LOG", "Round " + roundCount + " - " + pairing);
		roundCountView.setText("Round " + roundCount + "/" + rounds);

		if (playersNode.size() % 2 != 0)
			setBye(playersNode.getLast());

		if (playersNode.size() == 2) {
			if (!rotatedPhone)
				playersNode.add(playersNode.removeFirst());
			updateView(playersNode.getFirst(), playersNode.getLast(), 0);
			return;
		}

		for (int i = 0; i < playersNode.size() - 1; i++) {
			updateView(playersNode.get(i++), playersNode.get(i), i / 2);
		}
		rotatedPhone = false;
	}

	/**
	 * Ends the tournaments rounds and start the leaderboard activity.
	 */
	private void tournamentFinished() {
		Log.d("LOG", "finished tournament");
		app.setNewPlayerList(playersNode);
		Intent intent = new Intent(getApplicationContext(), JudgeFinish.class);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}

	/**
	 * Used to update the views with new player info.
	 * 
	 * @param nOne
	 *            player one Node
	 * @param nTwo
	 *            player two Node
	 * @param row
	 *            which row to update
	 */
	private void updateView(Node nOne, Node nTwo, int row) {
		final TextView oneName, twoName, oneScore, twoScore;
		final PlayerRow pr = new PlayerRow(this, this);
		nOne.played(nTwo);
		nTwo.played(nOne);

		Log.d("UPDATE", nOne.toString() + " vs " + nTwo.toString());
		RelativeLayout rl = viewPlayerList.get(row);
		oneName = (TextView) rl.getChildAt(0);
		oneName.setText(nOne.getName());
		oneScore = (TextView) rl.getChildAt(1);
		oneScore.setText(nOne.getScore());
		((TextView) rl.getChildAt(2)).setOnClickListener(droppedListener(nOne,
				nTwo, rl));

		twoName = (TextView) rl.getChildAt(4);
		twoName.setText(nTwo.getName());
		twoScore = (TextView) rl.getChildAt(5);
		twoScore.setText(nTwo.getScore());
		((TextView) rl.getChildAt(6)).setOnClickListener(droppedListener(nTwo,
				nOne, rl));

		dialogScore.add(pr
				.editPlayerDialog(nOne, nTwo, oneScore, twoScore, row));

		if (!rotatedPhone)
			app.setScore(row, false);
		OnClickListener listener = editScoreListener(row);

		oneName.setOnClickListener(listener);
		twoName.setOnClickListener(listener);
		oneScore.setOnClickListener(listener);
		twoScore.setOnClickListener(listener);
	}

	private OnClickListener editScoreListener(final int row) {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogScore.get(row).show();
			}
		};
	}

	private OnClickListener droppedListener(final Node dropped, final Node bye,
			final RelativeLayout view) {

		return new OnClickListener() {

			@Override
			public void onClick(final View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(
						v.getContext());
				builder.setTitle("Drop " + dropped.getName());
				builder.setMessage("Really drop player " + dropped.getName()
						+ " from the tournament?");
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Log.d("DROPPED", dropped.getName());
								playersNode.remove(dropped);
								tournament.removePlayer(dropped);
								linear.removeView(view);
								viewPlayerList.remove(view);
								app.removeScore(0);
								bye.removeLastPlayed();
								setBye(bye);

								if (viewPlayerList.size() == 0) {
									Toast.makeText(
											v.getContext(),
											"Easter Egg: Congratz for deleting all but one player!",
											Toast.LENGTH_SHORT).show();
									finish();
								}
								dialog.cancel();
							}
						});

				builder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		};
	}

	/**
	 * Set bye. If previous bye is null, then n gets to be the new bye. Else the
	 * old bye will be paired with n.
	 * 
	 * @param n
	 */
	private void setBye(Node n) {
		if (bye == null) {
			bye = n;
			bye.setBye(true);
			playersBye.setText("Bye goes to " + bye.getName());
			Log.d("BYE", "Bye goes to " + bye.toString());
		} else {
			bye.setBye(false);
			RelativeLayout rl = new PlayerRow(this, n, bye,
					viewPlayerList.size(), this);
			viewPlayerList.add(rl);
			linear.addView(rl);
			((TextView) rl.getChildAt(2)).setOnClickListener(droppedListener(n,
					bye, rl));
			((TextView) rl.getChildAt(6)).setOnClickListener(droppedListener(
					bye, n, rl));
			app.addScore(false);
			bye = null;
			playersBye.setText("No bye today...");
			Log.d("BYE", "No bye today...");
		}
	}

	/**
	 * Adds another player to the tournament
	 * 
	 * @param name
	 *            of player
	 * @param points
	 *            of players team
	 */

	private void addExtraPlayer(String name, int points) {
		final Node n = new Node(name, points);
		playersNode.add(n);
		setBye(n);
	}

	private boolean saveData() {

		if (app.findScore()) {
			Toast.makeText(this, R.string.scoreNotSet, Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		TextView viewName, viewScore;
		String name, score;

		for (RelativeLayout rl : viewPlayerList) {
			viewName = (TextView) rl.getChildAt(0);
			viewScore = (TextView) rl.getChildAt(1);

			name = viewName.getText().toString();
			score = viewScore.getText().toString();

			for (Node n : playersNode) {
				if (n.getName().equals(name)) {
					n.updateScore(score);
					break;
				}
			}

			viewName = (TextView) rl.getChildAt(4);
			viewScore = (TextView) rl.getChildAt(5);

			name = viewName.getText().toString();
			score = viewScore.getText().toString();

			for (Node n : playersNode) {
				if (n.getName().equals(name)) {
					n.updateScore(score);
					break;
				}
			}
		}
		return true;
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (bye != null)
			bye.setBye(false);
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(rounds); // int
		data.add(roundCount); // int
		data.add(durationMs); // long
		data.add(timer.onResume()); // Duration
		data.add(pairing); // String
		data.add(tournamentName); // String
		TextView viewName, viewScore;
		String name, score;
		for (int i = 0; i < viewPlayerList.size(); i++) {
			if (app.getScore(i)) {
				viewName = (TextView) viewPlayerList.get(i).getChildAt(0);
				viewScore = (TextView) viewPlayerList.get(i).getChildAt(1);

				name = viewName.getText().toString();
				score = viewScore.getText().toString();

				for (Node n : playersNode) {
					if (n.getName().equals(name)) {
						n.setBackupScore(score);
						n.removeLastPlayed();
						break;
					}
				}

				viewName = (TextView) viewPlayerList.get(i).getChildAt(4);
				viewScore = (TextView) viewPlayerList.get(i).getChildAt(5);

				name = viewName.getText().toString();
				score = viewScore.getText().toString();

				for (Node n : playersNode) {
					if (n.getName().equals(name)) {
						n.setBackupScore(score);
						n.removeLastPlayed();
						break;
					}
				}
			}
		}
		data.add(playersNode); // LinkedList
		data.add(app.getScoreSetList()); // ArrayList
		this.data = data;
		return data;
	}

	@SuppressWarnings("unchecked")
	private void onOrientationChange(Object lastData) {
		ArrayList<Object> data = (ArrayList<Object>) lastData;
		rounds = (Integer) data.remove(0);
		roundCount = (Integer) data.remove(0);
		durationMs = (Long) data.remove(0);
		timer = (Duration) data.remove(0);
		if (durationMs != timer.getTime())
			timer.start();
		pairing = (String) data.remove(0);
		tournamentName = (String) data.remove(0);
		playersNode = (LinkedList<Node>) data.remove(0);
		app.setNewScoreSet((ArrayList<Boolean>) data.remove(0));
		rotatedPhone = true;
	}

	public TournamentType decideTournament(String name) {
		// TODO check for pairing
		if (pairing.equals("Swiss"))
			return new Swiss(playersNode);
		else if (pairing.equals("Round Robin"))
			finish();
		// return = new RoundRobin(playersNode);
		else
			finish();
		return null;
	}

	@Override
	protected void onPause() {
		super.onPause();

		// ByteArrayOutputStream bos = new ByteArrayOutputStream();
		//
		// try {
		// ObjectOutput out = new ObjectOutputStream(bos);
		// out.writeObject(data);
		// out.close();
		//
		// // Get the bytes of the serialized object
		// byte[] buf = bos.toByteArray();
		//
		// Log.d("OUTPUT", "" + buf);
		// } catch (IOException ioe) {
		// Log.e("serializeObject", "error", ioe);
		// }
		Log.d("INFO", "onPause()");
		dumpPlayers();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		app.restart();
	}

	public void dumpPlayers() {
		Database db = new Database(this);
		db.open();
		// db.dumpPlayer(app.getPlayerList());
		app.setDumpedPlayers(true);
		String extra = Integer.toString(rounds) + ";"
				+ Integer.toString(roundCount) + ";" + pairing + ";"
				+ tournamentName;
		db.dumpPlayer(playersNode, extra);
		db.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("TEST", "" + requestCode + " - " + resultCode);
		if (requestCode == resultCode)
			nextRound();
		// TODO it's force close since you mess up the team sorter...maybe not
		// force it to make "perfect" team fights.

	}
	/*
	 * @Override public void onBackPressed() { super.onBackPressed();
	 * 
	 * // AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 * 
	 * }
	 */
}
