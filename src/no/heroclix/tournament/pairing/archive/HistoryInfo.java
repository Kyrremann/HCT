package no.heroclix.tournament.pairing.archive;

import no.heroclix.tournament.pairing.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HistoryInfo extends Activity {

	TextView name, players, round, type, date;
	Bundle bundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historyinfo);

		bundle = getIntent().getExtras();
		name = (TextView) findViewById(R.id.historyName);
		players = (TextView) findViewById(R.id.historyPlayers);
		round = (TextView) findViewById(R.id.historyRound);
		date = (TextView) findViewById(R.id.historyDate);
		type = (TextView) findViewById(R.id.historyType);

		name.setText(bundle.getString(Database.KEY_TITLE));
		players.setText(bundle.getString(Database.KEY_PLAYERS));
		round.setText(bundle.getString(Database.KEY_ROUNDS));
		date.setText(bundle.getString(Database.KEY_DATE));
		type.setText(bundle.getString(Database.KEY_TYPE));
	}
}
