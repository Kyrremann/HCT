package no.heroclix.tournament.pairing.gui;

import no.heroclix.tournament.pairing.Node;
import no.heroclix.tournament.pairing.NodeApplication;
import no.heroclix.tournament.pairing.R;
import android.R.style;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class PlayerRow extends RelativeLayout {

	Context context;
	Activity activity;

	public PlayerRow(Context context, Activity act) {
		super(context);
		this.context = context;
		activity = act;
	}

	public PlayerRow(final Context context, final int rowCount, Activity act) {
		super(context);
		this.context = context;
		activity = act;

		Log.d("ADD", "new row");

		final TextView playerOne, playerTwo;
		TextView vs, droppedOne, droppedTwo;
		final TextView oneScore = new TextView(context), twoScore = new TextView(
				context);
		RelativeLayout.LayoutParams paramsOne, paramsTwo, paramsVs, paramsRl, paramsOneScore, paramsTwoScore, paramsDroppedOne, paramsDroppedTwo;

		paramsRl = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		paramsOne = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		paramsOne.addRule(RelativeLayout.LEFT_OF, 64);

		paramsTwo = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		paramsTwo.addRule(RelativeLayout.RIGHT_OF, 64);

		paramsVs = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		paramsVs.setMargins(5, 5, 5, 5);
		paramsVs.addRule(RelativeLayout.CENTER_HORIZONTAL);
		paramsVs.addRule(RelativeLayout.CENTER_VERTICAL);

		paramsOneScore = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsOneScore.addRule(RelativeLayout.BELOW, 16);
		paramsOneScore.addRule(RelativeLayout.LEFT_OF, 64);

		paramsTwoScore = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsTwoScore.addRule(RelativeLayout.BELOW, 32);
		paramsTwoScore.addRule(RelativeLayout.RIGHT_OF, 64);

		paramsDroppedOne = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsDroppedOne.addRule(RelativeLayout.CENTER_VERTICAL);
		paramsDroppedOne.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		paramsDroppedTwo = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsDroppedTwo.addRule(RelativeLayout.CENTER_VERTICAL);
		paramsDroppedTwo.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		vs = new TextView(context);
		vs.setId(64);
		vs.setText("vs");
		vs.setLayoutParams(paramsVs);
		vs.setTextAppearance(context, style.TextAppearance_Medium);

		playerOne = new TextView(context);
		playerOne.setId(16);
		playerOne.setClickable(true);
		playerOne.setSingleLine(true);
		playerOne.setGravity(Gravity.RIGHT);
		playerOne.setLayoutParams(paramsOne);
		playerOne.setTextAppearance(context, style.TextAppearance_Large);

		oneScore.setLayoutParams(paramsOneScore);
		oneScore.setClickable(true);

		droppedOne = new TextView(context);
		droppedOne.setText("X");
		droppedOne.setPadding(5, 0, 0, 0);
		droppedOne.setLayoutParams(paramsDroppedOne);
		droppedOne.setClickable(true);

		playerTwo = new TextView(context);
		playerTwo.setId(32);
		playerTwo.setClickable(true);
		playerTwo.setSingleLine(true);
		playerTwo.setGravity(Gravity.RIGHT);
		playerTwo.setLayoutParams(paramsTwo);
		playerTwo.setTextAppearance(context, style.TextAppearance_Large);

		twoScore.setLayoutParams(paramsTwoScore);
		twoScore.setClickable(true);

		droppedTwo = new TextView(context);
		droppedTwo.setText("X");
		droppedTwo.setPadding(0, 0, 5, 0);
		droppedTwo.setLayoutParams(paramsDroppedTwo);
		droppedTwo.setClickable(true);

		setId(rowCount);
		setLayoutParams(paramsRl);
		setPadding(0, 0, 0, 10);
		addView(playerOne);
		addView(oneScore);
		addView(droppedOne);
		addView(vs);
		addView(playerTwo);
		addView(twoScore);
		addView(droppedTwo);
	}

	public PlayerRow(final Context context, final Node nOne, final Node nTwo,
			final int rowCount, Activity act) {
		super(context);
		this.context = context;
		activity = act;

		Log.d("ADD", nOne.toString() + " vs " + nTwo.toString());
		nOne.played(nTwo);
		nTwo.played(nOne);

		final TextView playerOne, playerTwo;
		TextView vs, droppedOne, droppedTwo;
		final TextView oneScore = new TextView(context), twoScore = new TextView(
				context);
		RelativeLayout.LayoutParams paramsOne, paramsTwo, paramsVs, paramsRl, paramsOneScore, paramsTwoScore, paramsDroppedOne, paramsDroppedTwo;
		OnClickListener listener;

		paramsRl = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		paramsOne = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		paramsOne.addRule(RelativeLayout.LEFT_OF, 64);

		paramsTwo = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		paramsTwo.addRule(RelativeLayout.RIGHT_OF, 64);

		paramsVs = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		paramsVs.setMargins(5, 5, 5, 5);
		paramsVs.addRule(RelativeLayout.CENTER_HORIZONTAL);
		paramsVs.addRule(RelativeLayout.CENTER_VERTICAL);

		paramsOneScore = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsOneScore.addRule(RelativeLayout.BELOW, 16);
		paramsOneScore.addRule(RelativeLayout.LEFT_OF, 64);

		paramsTwoScore = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsTwoScore.addRule(RelativeLayout.BELOW, 32);
		paramsTwoScore.addRule(RelativeLayout.RIGHT_OF, 64);

		paramsDroppedOne = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsDroppedOne.addRule(RelativeLayout.CENTER_VERTICAL);
		paramsDroppedOne.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		paramsDroppedTwo = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		paramsDroppedTwo.addRule(RelativeLayout.CENTER_VERTICAL);
		paramsDroppedTwo.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		final Dialog edit = editPlayerDialog(nOne, nTwo, oneScore, twoScore,
				rowCount);
		listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				edit.show();
			}
		};

		vs = new TextView(context);
		vs.setId(64);
		vs.setText("vs");
		vs.setLayoutParams(paramsVs);
		vs.setTextAppearance(context, style.TextAppearance_Medium);

		playerOne = new TextView(context);
		playerOne.setId(16);
		playerOne.setText(nOne.getName());
		playerOne.setClickable(true);
		playerOne.setSingleLine(true);
		playerOne.setGravity(Gravity.RIGHT);
		playerOne.setLayoutParams(paramsOne);
		playerOne.setTextAppearance(context, style.TextAppearance_Large);
		playerOne.setOnClickListener(listener);

		oneScore.setText(nOne.getScore());
		oneScore.setLayoutParams(paramsOneScore);
		oneScore.setClickable(true);
		oneScore.setOnClickListener(listener);

		droppedOne = new TextView(context);
		droppedOne.setText("X");
		droppedOne.setPadding(5, 0, 0, 0);
		droppedOne.setLayoutParams(paramsDroppedOne);
		droppedOne.setClickable(true);

		playerTwo = new TextView(context);
		playerTwo.setId(32);
		playerTwo.setText(nTwo.getName());
		playerTwo.setClickable(true);
		playerTwo.setSingleLine(true);
		playerTwo.setGravity(Gravity.RIGHT);
		playerTwo.setLayoutParams(paramsTwo);
		playerTwo.setTextAppearance(context, style.TextAppearance_Large);
		playerTwo.setOnClickListener(listener);

		twoScore.setText(nTwo.getScore());
		twoScore.setLayoutParams(paramsTwoScore);
		twoScore.setClickable(true);
		twoScore.setOnClickListener(listener);

		droppedTwo = new TextView(context);
		droppedTwo.setText("X");
		droppedTwo.setPadding(0, 0, 5, 0);
		droppedTwo.setLayoutParams(paramsDroppedTwo);
		droppedTwo.setClickable(true);

		setId(rowCount);
		setLayoutParams(paramsRl);
		setPadding(0, 0, 0, 10);
		addView(playerOne);
		addView(oneScore);
		addView(droppedOne);
		addView(vs);
		addView(playerTwo);
		addView(twoScore);
		addView(droppedTwo);
	}

	public Dialog editPlayerDialog(final Node one, final Node two,
			final TextView vOne, final TextView vTwo, final int row) {

		AlertDialog.Builder builder;
		final CheckBox checkOne, checkTwo;
		final EditText editOne, editTwo;
		final Context mContext;
		LayoutInflater inflater;
		View layout;

		mContext = context.getApplicationContext();
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.editplayer,
				(ViewGroup) findViewById(R.layout.main));

		((TextView) layout.findViewById(R.id.editNameOne)).setText(one
				.getName());

		((TextView) layout.findViewById(R.id.editNameTwo)).setText(two
				.getName());

		checkOne = (CheckBox) layout.findViewById(R.id.editCheckOne);
		checkTwo = (CheckBox) layout.findViewById(R.id.editCheckTwo);

		editOne = (EditText) layout.findViewById(R.id.editPointsOne);
		editOne.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
		editOne.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		editTwo = (EditText) layout.findViewById(R.id.editPointsTwo);
		editTwo.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
		editTwo.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		editOne.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					editTwo.requestFocus();
					return true;
				}
				return false;
			}
		});

		editTwo.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					editOne.requestFocus();
					return true;
				}
				return false;
			}
		});

		checkOne.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					checkTwo.setChecked(!isChecked);
					editTwo.setText("");
					editTwo.requestFocus();
					if (((NodeApplication) activity.getApplication())
							.getAutoFill())
						editOne.setText(Integer.toString(two.getTeamPoints()));
				} else {
					checkTwo.setChecked(!isChecked);
					editOne.setText("");
					editOne.requestFocus();
					if (((NodeApplication) activity.getApplication())
							.getAutoFill())
						editTwo.setText(Integer.toString(two.getTeamPoints()));

				}
			}
		});

		checkTwo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					checkOne.setChecked(!isChecked);
					editOne.setText("");
					editOne.requestFocus();
					if (((NodeApplication) activity.getApplication())
							.getAutoFill())
						editTwo.setText(Integer.toString(two.getTeamPoints()));
				} else {
					checkOne.setChecked(!isChecked);
					editTwo.setText("");
					editTwo.requestFocus();
					if (((NodeApplication) activity.getApplication())
							.getAutoFill())
						editOne.setText(Integer.toString(two.getTeamPoints()));

				}
			}
		});

		// need to check for orientation
		int[] oneScore = one.getBackupScore();
		int[] twoScore = two.getBackupScore();
		if (oneScore != null && twoScore != null) {

			if (oneScore[0] > twoScore[0])
				checkOne.setChecked(true);
			else
				checkTwo.setChecked(true);

			String onePoints = Integer.toString(oneScore[2]);
			String twoPoints = Integer.toString(twoScore[2]);

			if (onePoints.equals("0"))
				editOne.setText("");
			else
				editOne.setText(onePoints);

			if (twoPoints.equals("0"))
				editTwo.setText("");
			else
				editTwo.setText(twoPoints);

			vOne.setText(createScore(one, onePoints, checkOne.isChecked()));
			vTwo.setText(createScore(two, twoPoints, checkTwo.isChecked()));
			one.setBackupScore(null);
			two.setBackupScore(null);
		}

		builder = new AlertDialog.Builder(context);
		builder.setTitle("Choose winner");
		builder.setView(layout);
		builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (checkOne.isChecked() || checkTwo.isChecked()) {

					String onePoints = editOne.getText().toString();
					String twoPoints = editTwo.getText().toString();

					if (onePoints.equals(""))
						onePoints = "0";
					if (twoPoints.equals(""))
						twoPoints = "0";

					vOne.setText(createScore(one, onePoints,
							checkOne.isChecked()));
					vTwo.setText(createScore(two, twoPoints,
							checkTwo.isChecked()));
					((NodeApplication) activity.getApplication()).setScore(row,
							true);
				}
				dialog.cancel();
			}

		});

		Dialog dialog = builder.create();
		return dialog;
	}

	private String createScore(Node n, String nPoints, boolean checked) {
		int win = n.getWin();
		int loss = n.getLoss();
		int points = n.getScorePoints() + Integer.parseInt(nPoints);

		if (checked)
			win++;
		else
			loss++;
		if (n.getBye())
			return "" + win + "/" + loss + "/" + points + "/bye";
		return "" + win + "/" + loss + "/" + points;
	}
}
