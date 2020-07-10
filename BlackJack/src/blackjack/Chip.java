public class Chip{
    private int x, value, number;
    private int y = 562;
    private int radius = 33;    

    /**
     * Tilldelar chipets värden
     * @param number vilket chip
     */
    
    public Chip(int number) {
        this.x = 35 + 100 * number;
        this.number = number;
        int[] values = {1, 10, 25, 100, 500};
        this.value = values[number];
    }
    
    /**
     * Undersöker ifall musens position är inom chipets dimensioner
     * @param musX musens x-position
     * @param musY musens y-position
     * @return Om musen har "träffat" knappen eller inte
     */
    
    public boolean isHit(int musX, int musY) {
        boolean hit = false;
        int dx = musX - (x + radius);
        int dy = musY - (y + radius);
        int ds = dx * dx + dy * dy;
        if (ds < radius * radius) {
            hit = true;
        }
        return hit;
    }
    
    /**
     * Returnerar chipets värde
     * @return chipets värde
     */
    
    public int getValue() {
        return value;
    }
    
    /**
     * Ändrar chipets nummer
     * @param i nya numret
     */
    
    public void setNumber(int i) {
        this.x = 673;
        this.y = 270 + 15 * (i-5);
    }   
    
    /**
     * Returnerar alla chipens värden i en int-array
     * @return chipens värden
     */
    
    public int[] getInfo() {
        int[] info = {this.number, this.x, this.y, this.radius};
        return info;
    }
    
}
