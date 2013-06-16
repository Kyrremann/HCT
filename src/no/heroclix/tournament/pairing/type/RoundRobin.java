package no.heroclix.tournament.pairing.type;

import java.util.LinkedList;

import no.heroclix.tournament.pairing.Node;

public class RoundRobin extends TournamentType {

	private LinkedList<Node> playersList;

	public RoundRobin(LinkedList<Node> players) {
		playersList = new LinkedList<Node>(players);
	}

	public LinkedList<Node> nextRound() {
		playersList.add(playersList.remove(0));
		return playersList;
	}

	public boolean addPlayer(Node n) {
		return playersList.add(n);
	}

	public boolean removePlayer(Node n) {
		return playersList.remove(n);
	}

	@Override
	public LinkedList<Node> getLinkedList() {
		return playersList;
	}
}
