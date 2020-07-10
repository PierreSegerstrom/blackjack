import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import javax.swing.JFrame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Table extends JPanel implements java.awt.event.MouseListener{
    
    private SpriteSheet ss = new SpriteSheet();
    private ArrayList<Chip> chips = new ArrayList<>();
    private ArrayList<Button> bbuttons = new ArrayList<>();
    private ArrayList<Button> rbuttons = new ArrayList<>();
    private boolean playing = false;
    private int roundOver = 0;
    private boolean coverDouble = false;
    private int split = 0;
    private boolean coverSurrender = false;
    private int currency = 500;
    private int bet = 0;
    private int message = 0;
    Hand p = new Hand();
    Hand pSplit = new Hand();
    Hand dealer = new Hand();
    Deck d = new Deck();
    
    /**
     * Skapar fönstret och ritar upp "start-layouten"
     */
    
    public Table() {
        JFrame f = new JFrame("BlackJack");
        addMouseListener(this);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
        f.add(this);
        f.setSize(850, 669);
        
        String[] choices = {"deal", "clear", "hit", "stand", "double", "split", "surrender"};
        
        for (int i = 0; i < 2; i++) {
            bbuttons.add(new Button(choices[i], false));
        }
        
        for (int i = 0; i < 5; i++) {
            chips.add(new Chip(i));
            rbuttons.add(new Button(choices[2+i], true));
        }
        
        playMusic();
    }
    
    /**
     * Ritar ut spelet beroende på vilka villkor som uppfyllts
     * @param g Graphics g
     */
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ss.getBackgroundImage(), 0, 0, 850, 669, null);
        drawCurrency(g);
        
        if (!playing) {
            drawBetBoard(g);
        } else {
            drawRoundBoard(g);
            drawHandValues(g);
            drawHand(dealer, g, false, true);
            
            if (split < 2) {
                drawHand(p, g, false, false);
            } else {
                drawHand(p, g, true, false);
            }
            
            
            g.setColor(new Color(60, 60, 60, 255));
            
            if (split > 0) {
                g.fillRoundRect(540, 567, 105, 55, 10, 10);
            }
            
            if (coverDouble) {
                g.fillRoundRect(370, 567, 105, 55, 10, 10);
            }
            
            if (coverSurrender) {
                g.fillRoundRect(710, 567, 105, 55, 10, 10);
            }
            
            if (split == 2) {
                g.setColor(Color.YELLOW);
                g.fillPolygon(new int[] {270, 280, 290}, new int[] {355, 365, 355}, 3);
                g.setColor(Color.BLACK);
                g.drawPolygon(new int[] {270, 280, 290}, new int[] {355, 365, 355}, 3);
            } else if (split == 3) {
                g.setColor(Color.YELLOW);
                g.fillPolygon(new int[] {570, 580, 590}, new int[] {355, 365, 355}, 3);
                g.setColor(Color.BLACK);
                g.drawPolygon(new int[] {570, 580, 590}, new int[] {355, 365, 355}, 3);
            }
            
            if (roundOver == 1) {
                g.setColor(new Color(25, 25, 25, 245));
                g.fillRect(0, 542, 850, 127);
                g.setFont(new Font("default", Font.BOLD, 30));
                g.setColor(Color.WHITE);
                switch (message) {
                    case 0:
                        g.drawString("YOU LOST", 356, 600);
                        break;
                    case 1:
                        g.drawString("YOU WON!", 363, 600);
                        break;
                    case 2:
                        g.drawString("WUSS. YOU GOT BACK " + bet/2 + "€", 205, 600);
                        playSound("surrender");
                        break;
                    case 3:
                        g.drawString("YOU GOT BLACKJACK!", 250, 600);
                        playSound("blackjack");
                        break;
                    case 4:
                        g.drawString("PUSH! NOTHING LOST, NOTHING GAINED", 110, 600);
                        break;   
                    case 5:
                        String operator = "+";
                        if (bet < 0) {
                            operator = "";
                        }
                        g.drawString("SPLIT OUTCOME: " + operator + bet + "€", 245, 600);
                }
                roundOver++;
            }
            
            switch (roundOver) {
                case 2:
                    if (currency == 0) {
                        roundOver++;
                    } else {
                        playing = false;
                        resetValues();
                    }   break;
                case 3:
                    g.setColor(new Color(25, 25, 25, 251));
                    g.fillRect(0, 0, 850, 669);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("default", Font.BOLD, 25));
                    g.drawString("YOU'RE OUT OF MONEY. GET OUT!", 200, 350);
                    playSound("gameover");
                    roundOver++;
                    break;
                case 4:
                    System.exit(0);
                default:
                    break;
            }
            
        }
    }
    
    /**
     * När användaren klickar ska följande hända beroende på vilka villkor som uppfyllts
     * @param evt händelsen
     */
    
    @Override
    public void mousePressed(java.awt.event.MouseEvent evt) {
        boolean hit = false;
        if (!playing) {
            for (int i = 0; i < 5 && !hit && chips.size() < 13; i++) {
                if (chips.get(i).isHit(evt.getX(), evt.getY()) && currency - chips.get(i).getValue() >= 0) {
                    playSound("chip");
                    bet += chips.get(i).getValue();
                    currency -= chips.get(i).getValue();
                    chips.add(new Chip(i));
                    chips.get(chips.size()-1).setNumber(chips.size()-1);
                    hit = true;
                }
            }
            for (int i = 0; i < bbuttons.size() && !hit; i++) {
                if (bbuttons.get(i).isHit(evt.getX(), evt.getY())) {
                    playSound("click");
                    if (i == 0 && bet > 0) {
                        playing = true;
                        newRound();
                    } else {
                        chips.clear();
                        for (int j = 0; j < 5; j++) {
                            chips.add(new Chip(j));
                        }
                        currency += bet;
                        bet = 0;
                    }
                    hit = true;
                }
            }
        } else {
            for (int i = 0; i < rbuttons.size() && !hit; i++) {
                if (rbuttons.get(i).isHit(evt.getX(), evt.getY())) {
                    playSound("click");
                    if (split < 2) {
                        if (i == 0 && p.getHandValue() < 21) {
                            p.addCard(d.hit());
                            coverDouble = true;
                            split = 1;
                            coverSurrender = true;
                            if (p.getHandValue() > 20) {
                                bet = compareWithDealer(false);
                                roundOver = 1;
                            }
                        } else if (i == 1) {
                            bet = compareWithDealer(false);
                            roundOver = 1;
                        } else if (i == 2 && !coverDouble && p.getHand().size() < 3) {
                            currency -= bet;
                            bet *= 2;
                            coverDouble = true;
                            split = 1;
                            coverSurrender = true;
                            p.addCard(d.hit());
                            bet = compareWithDealer(false);
                            roundOver = 1;
                        } else if (i == 3 && split == 0 && p.getHand().size() < 3) {
                            coverDouble = true;
                            coverSurrender = true;
                            split = 2;

                            pSplit.addCard(p.getHand().remove(1));
                            p.addCard(d.hit());
                            pSplit.addCard(d.hit());     
                            
                            if (p.getHandValue() == 21 && pSplit.getHandValue() == 21) {
                                bet = compareWithDealer(true);
                                roundOver = 1;
                                split = 4;
                            } else if (p.getHandValue() == 21) {
                                split = 3;
                            }
                        } else if (i == 4 && p.getHand().size() < 3) {
                            message = 2;
                            roundOver = 1;
                            currency += bet/2;
                        }
                        
                    } else {                    
                        if (split == 2) {
                            if (i == 0 && p.getHandValue() < 21) {
                                p.addCard(d.hit());
                            }
                            
                            if (i == 1 || p.getHandValue() > 20) {
                                split++;
                                if (pSplit.getHandValue() == 21) {
                                    bet = compareWithDealer(true);
                                    roundOver = 1;
                                    split = 4;
                                }
                            }
                        } else if (split == 3) {
                            if (i == 0 && pSplit.getHandValue() < 21) {
                                pSplit.addCard(d.hit());
                            }
                            if (i == 1 || pSplit.getHandValue() > 20) {
                                bet = compareWithDealer(true);
                                roundOver = 1;
                                split = 4;
                            }
                        }

                    }
                    
                    hit = true;
                }
                    
            }
            
        }
        
        repaint();
    }

    /**
     * Ritar upp layouten när en runda inte är pågående
     * @param g Graphics g
     */
    
    public void drawBetBoard(Graphics g) {       
        for (Button bbutton : bbuttons) {
            bbutton.drawButton(g);
        }
        
        for (Chip chip : chips) {
            int[] info = chip.getInfo();
            g.drawImage(ss.getChipImage(info[0]), info[1], info[2], info[3] * 2, info[3] * 2, null);
        }
    }
    
    /**
     * Ritar upp layouten när en runda är pågående
     * @param g Graphics g
     */
    
    public void drawRoundBoard(Graphics g) {
        for (Button rbutton : rbuttons) {
            rbutton.drawButton(g);
        }
        
        for (int i = 5; i < chips.size(); i++) {
            int[] info = chips.get(i).getInfo();
            g.drawImage(ss.getChipImage(info[0]), info[1], info[2], info[3] * 2, info[3] * 2, null);
        }   
    }
    
    /**
     * Ritar ut spelarens saldo
     * @param g Graphics g
     */
    
    public void drawCurrency(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("default", Font.BOLD, 20));
        g.drawString(Integer.toString(currency) + "€", 15, 470);
    }
    
    /**
     * Ritar ut varje hands värden olika beroende på om spelaren har "splittat" eller inte
     * @param g Graphics g
     */
    
    public void drawHandValues(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("default", Font.BOLD, 20));
        if (split > 1) {
            g.drawString(Integer.toString(dealer.getHandValue()), 330, 150);
            g.drawString(Integer.toString(p.getHandValue()), 160, 440);
            g.drawString(Integer.toString(pSplit.getHandValue()), 470, 440);
        } else {
            g.drawString(Integer.toString(dealer.getHandValue()), 300, 150);
            g.drawString(Integer.toString(p.getHandValue()), 300, 440);
        }
    }
    
    /**
     * Delar ut de första korten och ändrar värden ifall villkor har uppfyllts
     */
    
    public void newRound() {   
        p.addCard(d.hit());
        p.addCard(d.hit());
        dealer.addCard(d.hit());

        if (bet > currency) {
            coverDouble = true;
        }
        
        if (p.getCard(0).getValue() != p.getCard(1).getValue()) {
            split = 1;
        }
        
        if (p.getCard(0).getValue() + p.getCard(1).getValue() == 21) {
            message = 3;
            roundOver = 1;
            currency += (bet + (int)bet*1.5);
        }
        
    }
    
    /**
     * Jämför spelarens hand/händer med "dealerns" hand
     * @param split Ifall spelaren har "splittat" eller inte
     * @return Hur mycket spelaren har vunnit/förlorat
     */
    
    public int compareWithDealer(boolean split) {
        int won = 0;
        
        while (dealer.getHandValue() < 17) {
            dealer.addCard(d.hit());
        }
        
        if (!split) {
            won = bet;
            if ((p.getHandValue() > dealer.getHandValue() && p.getHandValue() < 22) || (dealer.getHandValue() > 21 && p.getHandValue() < 22) || p.getHandValue() == 21) {
                message = 1;
                currency += won*2;
            } else if (p.getHandValue() == dealer.getHandValue() && p.getHandValue() < 22) {
                message = 4;
                currency += won;
            } else {
                won = -bet;
            }
        } else {
            if ((pSplit.getHandValue() > dealer.getHandValue() && pSplit.getHandValue() < 22) || (dealer.getHandValue() > 21 && pSplit.getHandValue() < 22) || pSplit.getHandValue() == 21) {
                won += bet/2;
                currency += bet;
            } else if (pSplit.getHandValue() == dealer.getHandValue() && pSplit.getHandValue() < 22) {
                currency += bet/2;
            } else {
                won -= bet/2;
            }
            
            if ((p.getHandValue() > dealer.getHandValue() && p.getHandValue() < 22) || (dealer.getHandValue() > 21 && p.getHandValue() < 21) || p.getHandValue() == 21) {
                won += bet/2;
                currency += bet;
            } else if (p.getHandValue() == dealer.getHandValue() && p.getHandValue() < 22) {
                currency += bet/2;
            } else {
                won -= bet/2;
            }
            
            message = 5;
        }
        return won;
    }
    
    /**
     * 
     * @param h Handen som ska ritas ut
     * @param g Graphics g
     * @param split Om spelaren har "splittat" eller inte
     * @param dealer Om handen som ska ritas ut är "dealern" eller inte
     */
    
    public void drawHand(Hand h, Graphics g, boolean split, boolean dealer) {
        int thisCardX,thisCardY;
        int splitCardX = 525;
        int splitCardY = 380;
        
        if (dealer) {
            thisCardX = 390;
            thisCardY = 95;
        } else {
            thisCardX = 375;
            thisCardY = 380;
        }
        
        if (split) {
            thisCardX = 225;
        }
        
        for (int i = 0; i < h.getHand().size(); i++) {
            int num = h.getCard(i).getNumber();
            int suit = h.getCard(i).getSuit();
            g.drawImage(ss.getCardImage(((suit - 1) * 13 + num) - 1), thisCardX, thisCardY, 81, 118, null);
            thisCardX += 30;
        }
        
        if (split) {
            for (int i = 0; i < pSplit.getHand().size(); i++) {
                int sNum = pSplit.getCard(i).getNumber();
                int sSuit = pSplit.getCard(i).getSuit();
                g.drawImage(ss.getCardImage(((sSuit - 1) * 13 + sNum) - 1), splitCardX, splitCardY, 81, 118, null);
                splitCardX += 30;
            }
        }
        
    }
    
    /**
     * Nollställer alla värden vid slutet av en runda
     */
    
    public void resetValues() {
        p.emptyHand();
        pSplit.emptyHand();
        dealer.emptyHand();
        chips.clear();
        for (int j = 0; j < 5; j++) {
            chips.add(new Chip(j));
        }
        bet = 0;
        d.resetDeck();
        roundOver = 0;
        coverDouble = false;
        split = 0;
        coverSurrender = false;
        message = 0;
    }
    
    /**
     * Spelar upp ett ljud
     * @param fileName filens namn
     */
    
    public void playSound(String fileName) {
        try {
            URL url = getClass().getResource("sounds/" + fileName + ".wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(url.getPath()));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error with playing sound.");
        }
    }
    
    /**
     * Spelar upp en ljudfil som ska loopas under spelets gång
     */
    
    public void playMusic() {
        Timer myTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (roundOver != 3) {
                    playSound("casino_music");
                }
            }
        };
        
        myTimer.scheduleAtFixedRate(task, 0, 123925);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
       
}
