package no.heroclix.tournament.pairing.archive;

import no.heroclix.tournament.pairing.R;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class History extends ListActivity {

	Database db;
	Cursor historyTable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		db = new Database(this);
		fillList();
	}

	private void fillList() {
		db.open();
		historyTable = db.getTables();
		startManagingCursor(historyTable);

		String[] from = new String[] { Database.KEY_TITLE, Database.KEY_DATE };
		int[] to = new int[] { R.id.historyRow, R.id.historyRow2 };

		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.notes_row, historyTable, from, to);
		setListAdapter(notes);
		db.close();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Cursor c = historyTable;
		c.moveToPosition(position);
		Intent i = new Intent(this, HistoryInfo.class);
		i.putExtra(Database.KEY_ROWID, id);
		i.putExtra(Database.KEY_TITLE,
				c.getString(c.getColumnIndexOrThrow(Database.KEY_TITLE)));
		i.putExtra(Database.KEY_PLAYERS,
				c.getString(c.getColumnIndexOrThrow(Database.KEY_PLAYERS)));
		i.putExtra(Database.KEY_DATE,
				c.getString(c.getColumnIndexOrThrow(Database.KEY_DATE)));
		i.putExtra(Database.KEY_ROUNDS,
				c.getString(c.getColumnIndexOrThrow(Database.KEY_ROUNDS)));
		i.putExtra(Database.KEY_TYPE,
				c.getString(c.getColumnIndexOrThrow(Database.KEY_TYPE)));
		historyTable.close();
		startActivityForResult(i, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("RES", "Not here?!");
		fillList();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		historyTable.close();
	}
}
