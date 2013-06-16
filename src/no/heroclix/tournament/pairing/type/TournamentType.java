package no.heroclix.tournament.pairing.type;

import java.util.Comparator;
import java.util.LinkedList;

import no.heroclix.tournament.pairing.Node;

public abstract class TournamentType {

	public Comparator<Node> comparator;

	public abstract LinkedList<Node> nextRound();

	public abstract boolean addPlayer(Node n);

	public abstract boolean removePlayer(Node n);

	public abstract LinkedList<Node> getLinkedList();
}
