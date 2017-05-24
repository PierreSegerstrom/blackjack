
package blackjack;

import java.util.ArrayList;

public class Hand {
    
    private ArrayList<Card> hand = new ArrayList<>();
    
    /**
     * Tömmer handen på kort
     */
    
    public void emptyHand() {
        hand.clear();
    }
    
    /**
     * Lägger till ett kort i handen
     * @param c Kortet som läggs till
     */
    
    public void addCard(Card c) {
        hand.add(c);
    }
    
    public Card getCard(int i) {
        return this.hand.get(i);
    }
    
    public ArrayList<Card> getHand() {
        return this.hand;
    }
    
    /**
     * Returnera handens totala värde
     * @return handens totala värde
     */
    
    public int getHandValue() {
        int handValue = 0;
        int totAces = 0;
        
        for (int c = 0; c < this.hand.size(); c++) {
            
            if (this.hand.get(c).getNumber() == 1) {
                totAces++;
            }
            
            handValue += this.hand.get(c).getValue();
        }
        
        while (handValue > 21 && totAces > 0) {
            handValue -= 10;
            totAces--;
        }
        
        return handValue;
    }
    
}
