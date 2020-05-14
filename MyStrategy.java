package Game;
import java.util.Random;

/**
 * A custom strategy to play cheat.
 * 1. Cheat if you have to, if you don't have to cheat, do it sometimes anyway.
 *    If a cheat is required, play a low randomly selected card.
 * 2. If not cheating, always play the maximum number of cards
 *    possible of the highest rank possible.
 * 3. Call another player a cheat if certain they are cheating, sometimes call
 *    another player a cheat if not sure.
 *
 * @author White, Robin
 */
public class MyStrategy implements Strategy {

    /**
     * Method to determine whether the current bid will be a cheat
     * @param b current bid
     * @param h current hand
     * @return true if the strategy wants to cheat, false if not
     */
    @Override
    public boolean cheat(Bid b, Hand h) {
        Card.Rank rank = b.getRank();
        Random rand = new Random();

        //Generate a random integer at each play to cheat at random intervals
        int  cheatChance = rand.nextInt(100) + 1;

        //Cheats if there's no alternative.
        if(h.countRank(rank) < 1 && h.countRank(rank.getNext()) < 1)
            return true;
            //Cheat 55% of the time, even if it's not needed
        else if(cheatChance > 55)
            return true;
        return false;
    }

    /**
     * Chooses a bid for the current strategy, if cheat select a random card from the lower
     * bounds of the hand. If !cheat play the highest rank possible in the current hand.
     * @param b current bid
     * @param h current hand
     * @param cheat true if the strategy wants to cheat
     * @return the new bid generated by the strategy
     */
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) {
        if (cheat) {
            Random rand = new Random();
            //half the hand and select a random position
            int n = rand.nextInt((h.handSize() / 2) + 1);
            Card card = (Card) h.hand.toArray()[n];

            Hand newHand = new Hand();
            h.remove(card);
            newHand.add(card);

            Bid newBid = new Bid(newHand, b.r.getNext());
            return newBid;
        }
        else {
            Card.Rank lastRank = b.getRank();
            Card.Rank rank;

            //Play the highest rank possible.
            if(h.countRank(lastRank) > h.countRank(lastRank.getNext()))
                rank = lastRank.getNext();
            else
                rank = lastRank;

            Hand newHand = new Hand();

            for (Object card : h) {
                Card newCard = (Card) card;
                if(newCard.getRank() == rank)
                    newHand.add(newCard);
            }

            h.remove(newHand);
            Bid newBid = new Bid(newHand, rank);
            return newBid;
        }
    }

    /**
     * Method to call cheat on the current play, calls cheat randomly
     * @param h current hand
     * @param b current bid
     * @return
     */
    @Override
    public boolean callCheat(Hand h, Bid b) {
        int numOfRank = h.countRank(b.getRank());
        int cardsBid = b.getCount();
        Random rand = new Random();
        //Generate a random number to determine when to cheat
        int  callCheatChance = rand.nextInt(100) + 1;

        //Calls cheat if the current play is impossible
        if((cardsBid + numOfRank) > 4)
            return true;
            //Call cheat randomly
        else if(callCheatChance > 70)
            return true;
        return false;
    }
}