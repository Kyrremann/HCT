package no.heroclix.tournament.pairing;

import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Application;

public class NodeApplication extends Application {

	private LinkedList<Node> playerList;
	private ArrayList<Boolean> scoreSet;
	private boolean autoFill, dumpedPlayers;

	public void onCreate() {
		playerList = new LinkedList<Node>();
		autoFill = true;
		scoreSet = new ArrayList<Boolean>();
		dumpedPlayers = false;
	}

	// Player list methods
	public LinkedList<Node> setNewPlayerList(LinkedList<Node> list) {
		this.playerList = list;
		return list;
	}

	public LinkedList<Node> getPlayerList() {
		return playerList;
	}

	public Node getPlayer(String name) {
		for (Node n : playerList)
			if (n.getName().equals(name))
				return n;
		return null;
	}

	// Auto fill methods
	public boolean getAutoFill() {
		return autoFill;
	}

	public boolean setAutoFill(boolean b) {
		return autoFill = b;
	}

	// Score methods
	public boolean addScore(boolean b) {
		return scoreSet.add(b);
	}

	public boolean setScore(int index, boolean b) {
		return scoreSet.set(index, b);
	}

	public boolean findScore() {
		return scoreSet.contains(false);
	}

	public boolean removeScore(int index) {
		return scoreSet.remove(index);
	}

	public boolean getScore(int index) {
		return scoreSet.get(index);
	}

	public ArrayList<Boolean> setNewScoreSet(ArrayList<Boolean> list) {
		this.scoreSet = list;
		return list;
	}

	public ArrayList<Boolean> getScoreSetList() {
		return scoreSet;
	}

	// Other methods
	public void restart() {
		onCreate();
	}

	public boolean setDumpedPlayers(boolean b) {
		return dumpedPlayers = b;
	}

	public boolean isPlayersDumped() {
		return dumpedPlayers;
	}

	public int size() {
		return playerList.size();
	}
}