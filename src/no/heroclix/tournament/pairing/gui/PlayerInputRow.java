package no.heroclix.tournament.pairing.gui;

import android.R.style;
import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class PlayerInputRow extends TableRow {

	EditText playerPoints;

	public PlayerInputRow(Context context, int index) {
		super(context);
		EditText playerName;
		TextView delete;
		LayoutParams params;

		params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, (float) 1);

		playerName = new EditText(context);
		playerPoints = new EditText(context);
		delete = new TextView(context);

		playerName.setHint("Name");
		playerName.setId(index);
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
		playerPoints.setInputType(InputType.TYPE_CLASS_NUMBER);
		playerPoints.setId(index + 1000);
		playerPoints.setSingleLine(true);
		playerPoints.setFocusableInTouchMode(true);
		playerPoints.setLayoutParams(params);
		playerPoints.setInputType(InputType.TYPE_CLASS_NUMBER);

		delete.setText("X");
		delete.setId(index + 2000);
		delete.setClickable(true);
		delete.setTextAppearance(context, style.TextAppearance_Large);
		delete.setPadding(10, 0, 10, 0);
		delete.setGravity(Gravity.CENTER);

		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		addView(playerName);
		addView(playerPoints);
		addView(delete);

		playerName.requestFocus();
	}
}
