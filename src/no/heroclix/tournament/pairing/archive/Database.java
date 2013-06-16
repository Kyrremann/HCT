package no.heroclix.tournament.pairing.archive;

import java.util.LinkedList;

import no.heroclix.tournament.pairing.Node;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import android.util.Log;

public class Database {// extends SQLiteOpenHelper {

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private Context context;
	private static final int DATABASE_VERSION = 3;
	public static final String DB_NAME = "HCT";
	public static final String TABLE_NAME = "history";
	public static final String TABLE_OPTIONS = "options";
	public static final String TABLE_DUMP = "dump";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_DATE = "date";
	public static final String KEY_TITLE = "name";
	public static final String KEY_TYPE = "type";
	public static final String KEY_ROUNDS = "rounds";
	public static final String KEY_PLAYERS = "players";
	private static final String TABLE_CREATE_HISTORY = "CREATE TABLE "
			+ TABLE_NAME
			+ " (_id TEXT, date TEXT, name TEXT, type TEXT, rounds TEXT, players TEXT);";

	private static final String TABLE_CREATE_OPTIONS = "CREATE TABLE "
			+ TABLE_OPTIONS
			+ " (_id TEXT, duration TEXT, first TEXT, second TEXT);";

	// private static final String TABLE_OPTIONS_CREATE =
	// "CREATE TABLE options (_id TEXT, duration TEXT, first TEXT, second TEXT);";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_CREATE_HISTORY);
			db.execSQL(TABLE_CREATE_OPTIONS);
			populate(db);
		}

		public void onCreate(SQLiteDatabase db, boolean upgrade) {
			db.execSQL(TABLE_CREATE_OPTIONS);
			populate(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Logs that the database is being upgraded
			Log.w("UPGRADE", "Upgrading database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data");

			// Kills the table and existing data
			db.execSQL("DROP TABLE IF EXISTS options");
			db.execSQL("DROP TABLE IF EXISTS dump");

			// Recreates the database with a new version
			onCreate(db, true);
		}

		private void populate(SQLiteDatabase db) {
			Log.d("DB", "POPULATING");
			db.execSQL("INSERT INTO " + TABLE_OPTIONS
					+ " VALUES ('duration', '50', '25', '5')");
		}
	}

	public Database(Context context) {
		this.context = context;
	}

	public Database open() throws SQLException {
		mDbHelper = new DatabaseHelper(context);
		mDb = mDbHelper.getWritableDatabase();
		if (mDb.getVersion() != DATABASE_VERSION)
			mDbHelper.onUpgrade(mDb, mDb.getVersion(), DATABASE_VERSION);
		return this;
	}

	public void close() {
		mDb.close();
		mDbHelper.close();
	}

	public Cursor getTables() {

		return mDb.query(TABLE_NAME, new String[] { KEY_ROWID, KEY_DATE,
				KEY_TITLE, KEY_TYPE, KEY_ROUNDS, KEY_PLAYERS }, null, null,
				null, null, null);
	}

	public void setTable(String players, String name, String type, String rounds) {
		Time now = new Time();
		now.setToNow();
		String date = now.toString().substring(0, 4) + "."
				+ now.toString().substring(4, 6) + "."
				+ now.toString().substring(6, 8);

		String toSQL = "INSERT INTO history VALUES ('" + now.toString()
				+ "', '" + date + "', '" + name + "', '" + type + "', '"
				+ rounds + "', '" + players + "');";

		mDb.execSQL(toSQL);
	}

	public Cursor getDuration() {
		return mDb.query(TABLE_OPTIONS, null, null, null, null, null, null);
	}

	public int setDuration(String duration, String first, String second) {
		ContentValues values = new ContentValues();
		values.put("duration", duration);
		values.put("first", first);
		values.put("second", second);
		return mDb.update(TABLE_OPTIONS, values, null, null);
	}

	public long dumpPlayer(LinkedList<Node> players, String extra) {
		// Log.d("DB", "Hva skjer her?!");
		mDb.execSQL("DROP TABLE IF EXISTS dump");
		final String TABLE_CREATE_DUMP = "CREATE TABLE " + TABLE_DUMP
				+ " (_id TEXT, dump TEXT, extra TEXT);";
		mDb.execSQL(TABLE_CREATE_DUMP);
		String dump = "";
		for (Node n : players) {
			dump += n.getName() + ";" + n.getTeamPoints() + ";" + n.getWin()
					+ ";" + n.getLoss() + ";" + n.getScorePoints() + "#";
		}
		Log.d("DUMP", dump);
		ContentValues values = new ContentValues();
		values.put("_id", "0");
		values.put("dump", dump);
		values.put("extra", extra);
		return mDb.insert(TABLE_DUMP, null, values);
	}

	public Cursor getDump() throws SQLException {
		return mDb.query(TABLE_DUMP, null, null, null, null, null, null);
	}
}
