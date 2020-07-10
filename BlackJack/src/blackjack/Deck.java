import java.util.Stack;
import java.util.Collections;

public class Deck {
    
    Stack<Card> cards = new Stack<>();
    
    Deck() {
        resetDeck();
    }
    
    /**
     * Nollställer kortleken
     */
    
    public void resetDeck() {
        cards.empty();
        for (int s = 1; s < 5; s++) {
            for (int n = 1; n < 14; n++) {
                this.cards.add(new Card(s, n));
            }
        }
        Collections.shuffle(cards);
    }
    
    /**
     * Plockar ut det översta kortet
     * @return kortet som plockas ut
     */
    
    public Card hit() {
        return cards.pop();
    }
    
}
