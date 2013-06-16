package no.heroclix.tournament.pairing.judge;

import java.util.ArrayList;
import java.util.LinkedList;

import no.heroclix.tournament.pairing.Node;
import no.heroclix.tournament.pairing.NodeApplication;
import no.heroclix.tournament.pairing.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class JudgeEditRound extends Activity {

	NodeApplication app;
	String[] players;
	ArrayList<Spinner> rows;
	LinearLayout layout;
	TextView cancel, done;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.judgeeditiround);

		app = (NodeApplication) getApplication();
		players = new String[app.size()];
		layout = (LinearLayout) findViewById(R.id.judgeEditRound);
		cancel = (TextView) findViewById(R.id.editRoundCancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		done = (TextView) findViewById(R.id.editRoundDone);
		done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LinkedList<Node> list = new LinkedList<Node>();
				for (String s : players)
					list.add(app.getPlayer(s));
				app.setNewPlayerList(list);
				setResult(RESULT_FIRST_USER);
				finish();
			}
		});
		rows = new ArrayList<Spinner>();

		int i = 0;
		for (Node n : app.getPlayerList()) {
			players[i++] = n.getName();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, players);
		for (int x = 0; x < i / 2; x++) {
			Spinner playerOne = new Spinner(this), playerTwo = new Spinner(this);
			playerOne.setAdapter(adapter);
			rows.add(playerOne);
			playerTwo.setAdapter(adapter);
			rows.add(playerTwo);
			LinearLayout row = new LinearLayout(this);
			row.setVerticalGravity(LinearLayout.VERTICAL);
			row.addView(playerOne);
			row.addView(playerTwo);
			layout.addView(row, 0);
		}

	}
}
