import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Button{
    private int x, w, h, textx;
    private int y = 564;
    private int texty = 600;
    private String text;

    /**
     * Ger en knapp ett antal värden när den skapas. Dessa värden avgörs beroende på vilken uppgift den ska utföra
     * @param text Avgör vilken uppgift knappen ska utföra
     * @param b Säger ifall knappen kommer att användas under rundan eller inte
     */
    
    public Button(String text, boolean b) {
        int i = 0;
        
        switch(text) {
            case "deal":    this.textx = 595;
                            break;
            case "clear":   this.textx = 726;
                            i = 1;
                            break;
            case "hit":     this.textx = 72;
                            break;
            case "stand":   this.textx = 231;
                            i = 1;
                            break;
            case "double":  this.textx = 397;
                            i = 2;
                            break;
            case "split":   this.textx = 575;
                            i = 3;
                            break;
            case "surrender":   this.textx = 725;
                                i = 4;
                                break;
        }
        
        if (b) {
            this.w = 105;
            this.h = 55;
            this.x = 30+i*170;
            this.y = 567;
        } else {
            this.w = 82;
            this.h = 60;
            this.x = 570+135*i;
        }
        
        this.text = text;

    }
    
    /**
     * Undersöker ifall musens position är inom knappens dimensioner
     * @param musX Musens x-position
     * @param musY Musens y-position
     * @return Om musen har "träffat" knappen eller inte
     */
    
    public boolean isHit(int musX, int musY) {
        boolean hit = false;
        if (musX > x && musX < (x + w) && musY > y && musY < (y + h)) {
            hit = true;
        }
        return hit;
    }

    /**
     * Ritar upp knappen
     * @param g Graphics g
     */
    
    public void drawButton(Graphics g) {
        g.setColor(new Color(150, 150, 150, 175));
        g.fillRoundRect(x, y, w, h, 10, 10);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, w, h, 10, 10);
        g.setColor(Color.WHITE);
        g.setFont(new Font("default", Font.PLAIN, 13));
        g.drawString(text.toUpperCase(), textx, texty);
    }

}
