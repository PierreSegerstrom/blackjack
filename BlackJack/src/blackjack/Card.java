public class Card{
    private int suit, number, value;
    
    /**
     * Tilldelar kortets värden
     * @param suit kortets färg
     * @param number kortets nummer
     */
    public Card (int suit, int number) {
        this.suit = suit;
        this.number = number;
        
        if (number == 1) {
            value = 11;
        } else if (number > 10) {
            value = 10;
        } else {
            value = number;
        }
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public int getSuit() {
        return this.suit;
    }
    
    public int getValue() {
        return this.value;
    }
}
