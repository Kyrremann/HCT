package no.heroclix.tournament.pairing.type;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import no.heroclix.tournament.pairing.Node;

public class Swiss extends TournamentType {

	public Comparator<Node> comparator;
	public LinkedList<Node> playersList;

	public Swiss(LinkedList<Node> players) {

		playersList = new LinkedList<Node>(players);

		comparator = new Comparator<Node>() {

			@Override
			public int compare(Node one, Node two) {
				if (one.getWin() == two.getWin()) {
					if (one.getScorePoints() > two.getScorePoints()) {
						return -1;
					} else {
						return 1;
					}
				} else if (one.getWin() > two.getWin()) {
					return -1;
				} else {
					return 1;
				}
			}
		};
	}

	public LinkedList<Node> nextRound() {
		Node one, two;
		int player;
		LinkedList<Node> current = new LinkedList<Node>();
		Collections.sort(playersList, comparator);

		while (playersList.size() > 1) {
			one = playersList.remove();
			two = playersList.getFirst();

			player = 1;
			while (one.haveWePlayed(two)) {
				two = playersList.get(player++);
			}

			playersList.remove(two);
			current.add(one);
			current.add(two);
			one.played(two);
			two.played(one);
		}

		if (playersList.size() % 2 != 0) {
			current.add(playersList.removeFirst());
			playersList = current;
			int index = playersList.size() - 1;

			Node n = playersList.get(index--);
			while (n.getBye()) {
				n = playersList.get(index--);
			}

			playersList.remove(n);
			playersList.add(n);

		} else {
			playersList = current;
		}

		return playersList;
	}

	public boolean addPlayer(Node n) {
		return playersList.add(n);
	}

	@Override
	public boolean removePlayer(Node n) {
		return playersList.remove(n);
	}

	@Override
	public LinkedList<Node> getLinkedList() {
		return playersList;
	}
}
