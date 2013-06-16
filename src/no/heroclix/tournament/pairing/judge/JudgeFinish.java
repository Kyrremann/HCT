package no.heroclix.tournament.pairing.judge;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import no.heroclix.tournament.pairing.Node;
import no.heroclix.tournament.pairing.NodeApplication;
import no.heroclix.tournament.pairing.R;
import no.heroclix.tournament.pairing.archive.Database;
import android.R.style;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JudgeFinish extends Activity {

	LinkedList<Node> players;
	TextView currentPlayer, done, nameView;
	LinearLayout scoreboard;
	int place;
	String rounds, type, name, playersList;
	Bundle bundle;
	Comparator<Node> comp;

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.judgefinish);

		bundle = getIntent().getExtras();
		final Object data = getLastNonConfigurationInstance();
		if (data != null)
			players = (LinkedList<Node>) data;
		else
			players = ((NodeApplication) getApplication()).getPlayerList();

		done = (TextView) findViewById(R.id.judgeDone);
		scoreboard = (LinearLayout) findViewById(R.id.scoreboard);
		nameView = (TextView) findViewById(R.id.judgeFinishName);
		rounds = Integer.toString(bundle.getInt("ROUNDS"));
		type = bundle.getString("PAIRING");
		name = bundle.getString("NAME");
		playersList = "";
		place = 1;

		nameView.setText(name);

		comp = new Comparator<Node>() {

			@Override
			public int compare(Node one, Node two) {
				if (one.getWin() == two.getWin()) {
					if (one.getScorePoints() > two.getScorePoints()) {
						return -1;
					} else {
						return 1;
					}
				} else if (one.getWin() > two.getWin()) {
					return -1;
				} else {
					return 1;
				}
			}
		};

		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});

		writeLeaderboard();
		if (data == null)
			saveData();
	}

	private void writeLeaderboard() {

		Collections.sort(players, comp);

		int place = 1;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.leftMargin = 10;
		LinearLayout layout;

		for (Node n : players) {
			String player = place++ + " - " + n.toString();
			Log.d("FINISH", player);
			playersList += player + "\n";
			layout = new LinearLayout(this);
			layout.setHorizontalGravity(LinearLayout.HORIZONTAL);

			currentPlayer = new TextView(this);
			currentPlayer.setLayoutParams(params);
			currentPlayer.setTextAppearance(getApplicationContext(),
					style.TextAppearance_Medium);
			currentPlayer.setText(player);
			layout.addView(currentPlayer);
			// layout.addView(new CheckBox(this));
			scoreboard.addView(layout);
			// scoreboard.addView(currentPlayer);
		}
	}

	private void saveData() {
		Database db = new Database(this);
		db.open();
		db.setTable(playersList, name, type, rounds);
		db.close();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return (Object) players;
	}
}
