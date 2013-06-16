package no.heroclix.tournament.pairing.gui;

import no.heroclix.tournament.pairing.Node;
import no.heroclix.tournament.pairing.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class EditPlayerDialog extends AlertDialog {

	Context context;
	TextView textOne, textTwo, scoreOne, scoreTwo;
	CheckBox checkOne;
	CheckBox checkTwo;
	EditText editOne;
	EditText editTwo;
	Node one, two;
	int row;

	protected EditPlayerDialog(Context context, Node one, Node two,
			TextView vOne, TextView vTwo, int row) {
		super(context);

		this.context = context;
		scoreOne = vOne;
		scoreTwo = vTwo;
		this.row = row;
		this.one = one;
		this.two = two;

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.editplayer);

		setTitle("Choose a winner");

		textOne = (TextView) findViewById(R.id.editNameOne);
		textOne.setText(one.getName());

		textTwo = (TextView) findViewById(R.id.editNameTwo);
		textTwo.setText(two.getName());

		checkOne = (CheckBox) findViewById(R.id.editCheckOne);
		checkTwo = (CheckBox) findViewById(R.id.editCheckTwo);

		editOne = (EditText) findViewById(R.id.editPointsOne);
		editOne.setImeActionLabel("Next", EditorInfo.IME_ACTION_NEXT);
		editOne.setImeOptions(EditorInfo.IME_ACTION_NEXT);

		editTwo = (EditText) findViewById(R.id.editPointsTwo);

		setButton(BUTTON_POSITIVE, "test", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (checkTwo.isChecked()) {
					checkTwo.setChecked(false);
					editTwo.setText("");
				}

				editOne.setText(Integer.toString(two.getTeamPoints()));
			}
		});
	}
}

// editOne.setOnEditorActionListener(new OnEditorActionListener() {
//
// @Override
// public boolean onEditorAction(TextView v, int actionId,
// KeyEvent event) {
// if (actionId == EditorInfo.IME_ACTION_NEXT) {
// Log.d("IME", "Going to editTwo");
// editTwo.requestFocus();
// return true;
// }
// return false;
// }
// });
//
// checkOne.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// if (checkTwo.isChecked()) {
// checkTwo.setChecked(false);
// editTwo.setText("");
// }
// // if (autoFill)
// editOne.setText(Integer.toString(two.getTeamPoints()));
// }
// });
//
// checkTwo.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// if (checkOne.isChecked()) {
// checkOne.setChecked(false);
// editOne.setText("");
// }
// // if (autoFill)
// editTwo.setText(Integer.toString(one.getTeamPoints()));
// }
// });
// }
//
// private String createScore(Node n, String nPoints, boolean checked) {
// int win = n.getWin();
// int loss = n.getLoss();
// int points = n.getScorePoints() + Integer.parseInt(nPoints);
//
// if (checked)
// win++;
// else
// loss++;
// if (n.getBye())
// return "" + win + "/" + loss + "/" + points + "/bye";
// return "" + win + "/" + loss + "/" + points;
// }
// }

// setButton("Save", new DialogInterface.OnClickListener() {
//
// @Override
// public void onClick(DialogInterface dialog, int which) {
// String onePoints = editOne.getText().toString();
// String twoPoints = editTwo.getText().toString();
//
// if (onePoints.equals("")) {
// if (checkTwo.isChecked()) {
// onePoints = "0";
// } else {
// Toast.makeText(context, "Points are missing",
// Toast.LENGTH_SHORT).show();
// return;
// }
// } else if (twoPoints.equals("")) {
// if (checkOne.isChecked()) {
// twoPoints = "0";
// } else {
// Toast.makeText(context, "Points are missing",
// Toast.LENGTH_SHORT).show();
// return;
// }
// }
//
// vOne.setText(createScore(one, onePoints, checkOne.isChecked()));
// vTwo.setText(createScore(two, twoPoints, checkTwo.isChecked()));
// // setScore.set(row, true);
//
// dialog.cancel();
// }
//
// });
// }

// // ////////////////
//
// private Dialog editPlayerDialog(final Node one, final Node two,
// final TextView vOne, final TextView vTwo, final int row) {
//
// AlertDialog.Builder builder;
// TextView textOne, textTwo;
// final CheckBox checkOne;
// final CheckBox checkTwo;
// final EditText editOne;
// final EditText editTwo;
// final Context mContext;
// LayoutInflater inflater;
// View layout;
//
// editOne.setOnEditorActionListener(new OnEditorActionListener() {
//
// @Override
// public boolean onEditorAction(TextView v, int actionId,
// KeyEvent event) {
// if (actionId == EditorInfo.IME_ACTION_NEXT) {
// Log.d("IME", "Going to editTwo");
// editTwo.requestFocus();
// return true;
// }
// return false;
// }
// });
//
// checkOne.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// if (checkTwo.isChecked()) {
// checkTwo.setChecked(false);
// editTwo.setText("");
// }
// if (autoFill)
// editOne.setText(Integer.toString(two.getTeamPoints()));
// }
// });
//
// checkTwo.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View v) {
// if (checkOne.isChecked()) {
// checkOne.setChecked(false);
// editOne.setText("");
// }
// if (autoFill)
// editTwo.setText(Integer.toString(one.getTeamPoints()));
// }
// });
//
// builder.setPositiveButton("Save",
// new DialogInterface.OnClickListener() {
//
// @Override
// public void onClick(DialogInterface dialog, int which) {
// String onePoints = editOne.getText().toString();
// String twoPoints = editTwo.getText().toString();
//
// if (onePoints.equals("")) {
// if (checkTwo.isChecked()) {
// onePoints = "0";
// } else {
// Toast.makeText(mContext, "Points are missing",
// Toast.LENGTH_SHORT).show();
// return;
// }
// } else if (twoPoints.equals("")) {
// if (checkOne.isChecked()) {
// twoPoints = "0";
// } else {
// Toast.makeText(mContext, "Points are missing",
// Toast.LENGTH_SHORT).show();
// return;
// }
// }
//
// vOne.setText(createScore(one, onePoints,
// checkOne.isChecked()));
// vTwo.setText(createScore(two, twoPoints,
// checkTwo.isChecked()));
// setScore.set(row, true);
//
// dialog.cancel();
// }
//
// });
//
// builder.setNegativeButton("Cancel",
// new DialogInterface.OnClickListener() {
//
// @Override
// public void onClick(DialogInterface dialog, int which) {
// dialog.cancel();
// }
// });
//
// Dialog dialog = builder.create();
// return dialog;
// }
//
// }
