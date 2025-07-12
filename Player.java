package desktop.SnakeLadder;


import javax.swing.*;
import java.awt.*;

class PlayerCoin extends JLabel {
    Color color;
        public PlayerCoin(Color color, int size) {
        this.color = color;
        setPreferredSize(new Dimension(size, size));
        setSize(size, size);
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillOval(0, 0, getWidth(), getHeight());
    }
}

class Player {
    private int score=0;
    private String name;
    private PlayerCoin coin;
    private Dice dice;
    private int CurrentLocation=-1;
    
    public Player(int tileSize, Color coinColor, String playerName) {
        this.name = playerName;
        this.coin = new PlayerCoin(coinColor, tileSize / 2);
	dice = new Dice();
    }
    public int roll_dice(){
    	return dice.roll();
    }

    public void moveTo(int x, int y) {
        coin.setLocation(x, y);
    }

    public PlayerCoin getCoinComponent() {
        return coin;
    }
    

    public int get_curr_loc(){return CurrentLocation;}

    public void set_curr_loc(int Location){CurrentLocation=Location;}

    public int get_score(){return score;}
	
    public void set_score(int score){this.score=score;}

    public String getName(){return name;}
}

