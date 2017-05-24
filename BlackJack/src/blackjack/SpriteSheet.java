
package blackjack;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class SpriteSheet {
    
    private BufferedImage spriteSheet;
    private BufferedImage[] cards = new BufferedImage[52];
    private BufferedImage[] chips = new BufferedImage[5];
    
    /**
     * Skapar ett "spritesheet" och delar ut alla korts och chips bilder till var sin array av bilder
     */
    
    public SpriteSheet() {
        try {
            URL url = this.getClass().getResource("ss.png");
            this.spriteSheet = ImageIO.read(url);
        } catch (IOException e) {
            System.out.println("Invalid spritesheet");
        }
        
        setCards();
        setChips();
    }
    
    /**
     * Tilldelar ett korts bild till kort-arrayen
     */
    
    private void setCards() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                cards[i*13+j] = spriteSheet.getSubimage(j*124, i*180, 124, 180);
            }
        }
    }
    
    /**
     * Tilldelar ett chips bild till chips-arrayen
     */
    
    private void setChips() {
        for (int i = 0; i < 5; i++) {
            chips[i] = spriteSheet.getSubimage(i*181, 720, 180, 180);
        }
    }
    
    /**
     * Returnar ett korts bild
     * @param x vilken kort
     * @return bilden
     */
    
    public BufferedImage getCardImage(int x) {
        return cards[x];
    }
    
    /**
     * Returnar ett chips bild
     * @param x vilket chip
     * @return bilden
     */
    
    public BufferedImage getChipImage(int x) {
        return chips[x];
    }
    
    /**
     * Returnar bakgrundsbilden
     * @return bakgrundsbilden
     */
    
    public BufferedImage getBackgroundImage() {
        return spriteSheet.getSubimage(0, 900, 1800, 1417);
    }
    
}
