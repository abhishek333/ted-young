package me.tedyoung.solitaire.tester;

import java.util.HashSet;

import me.tedyoung.solitaire.framework.AbstractTester;
import me.tedyoung.solitaire.game.Card;
import me.tedyoung.solitaire.game.Denomination;
import me.tedyoung.solitaire.game.Game;
import me.tedyoung.solitaire.game.MutableGame;
import me.tedyoung.solitaire.game.MutableStack;

public class PlayabilityTester extends AbstractTester {
	@Override
	public boolean isSolvable(Game g) {
		MutableGame game = (MutableGame) g;

		HashSet<Card> stackCards = new HashSet<>();
		HashSet<Card> holders = new HashSet<>();

		for (MutableStack stack : game.getTable()) {
			Card card = stack.getTopCard();
			if (card.getDenomination() == Denomination.ACE)
				return true;
			stackCards.add(card);
			holders.addAll(card.getHolders());
		}

		for (Card card : game.getDeck().getThisAndSubsequentVisibleCards()) {
			if (card.getDenomination() == Denomination.ACE)
				return true;
			holders.addAll(card.getHolders());
		}

		for (Card holder : holders)
			if (stackCards.contains(holder))
				return true;

		return false;
	}

	@Override
	public String getName() {
		return "Playable";
	}
}
