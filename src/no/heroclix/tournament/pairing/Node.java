package no.heroclix.tournament.pairing;

import java.util.ArrayList;

import android.util.Log;

public class Node implements Comparable<String> {

	private String name, backupScore;
	private int pointsTeam, pointsScore, win, loss;
	boolean bye;
	private ArrayList<Node> played;

	public Node(String name, int points) {
		this.name = name;
		this.pointsTeam = points;
		played = new ArrayList<Node>();
		bye = false;
		win = 0;
		loss = 0;
		backupScore = null;
	}

	public String getName() {
		return name;
	}

	/**
	 * 
	 */
	public boolean haveWePlayed(Node n) {
		return played.contains(n);
	}

	/**
	 * 
	 * @return last opponent, or null if there is none
	 */
	public Node getLastPlayer() {
		return played.get(played.size() - 1);
	}

	public boolean getBye() {
		return bye;
	}

	public int getScorePoints() {
		return pointsScore;
	}

	/**
	 * 
	 * @return win plus bye
	 */
	public int getWin() {
		if (bye)
			return win + 1;
		return win;
	}

	public int getLoss() {
		return loss;
	}

	public int incLoss() {
		return ++loss;
	}

	public int incWin() {
		return ++win;
	}

	public boolean setBye(boolean b) {
		return bye = b;
	}

	public int decWin() {
		if (win > 0)
			return --win;
		return 0;
	}

	public int decLoss() {
		if (loss > 0)
			return --loss;
		return 0;
	}

	public void played(Node n) {
		played.add(n);
	}

	public int incPoints(int i) {
		return pointsScore += i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name + " - " + getScore();
	}

	/**
	 * 
	 * @return a players score in the format 0/0/0 or 0/0/0/bye
	 */
	public String getScore() {
		if (bye)
			return "" + getWin() + "/" + loss + "/" + pointsScore + "/bye";
		return "" + win + "/" + loss + "/" + pointsScore;
	}

	public int getTeamPoints() {
		return pointsTeam;
	}

	@Override
	public int compareTo(String another) {
		Log.d("COMPARE", another);
		return name.compareTo(another);
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Will update the players score based on the difference of the input and
	 * the score
	 * 
	 * @param score
	 *            with the following format 0/0/0 or 0/0/0/bye
	 */
	public void updateScore(String score) {
		String[] s = score.split("/");

		if (bye)
			win = Integer.parseInt(s[0]) - 1;
		else
			win = Integer.parseInt(s[0]);
		loss = Integer.parseInt(s[1]);
		pointsScore = Integer.parseInt(s[2]);
	}

	/**
	 * 
	 * @return the player removed or null if there is no played user
	 */
	public Node removeLastPlayed() {
		if (!played.isEmpty())
			return played.remove(played.size() - 1);
		return null;
	}

	/**
	 * Will be called when phone is rotate and backupScore will go from null
	 * till new score. So that it later can be used to update the AlertDialog
	 * 
	 * @param score
	 *            backup score
	 */
	public void setBackupScore(String score) {
		backupScore = score;
	}

	public int[] getBackupScore() {
		if (backupScore == null)
			return null;
		String[] s = backupScore.split("/");
		int[] i = new int[s.length];

		i[0] = ((Integer.parseInt(s[0]) - win) == 0) ? 0 : 1;
		i[1] = ((Integer.parseInt(s[1]) - loss) == 0) ? 0 : 1;
		i[2] = ((Integer.parseInt(s[2]) - pointsScore) < 0) ? 0 : (Integer
				.parseInt(s[2]) - pointsScore);
		return i;
	}

	public void setWin(int i) {
		win = i;
	}

	public void setLoss(int i) {
		loss = i;
	}

	public void setPoints(int i) {
		pointsScore = i;
	}
}
