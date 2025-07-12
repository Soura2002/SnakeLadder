package desktop.SnakeLadder;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Font;
import java.awt.Image;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

class Game {
    JFrame frame;
    JLabel titleLabel;
    JButton startButton;
    JComboBox<Integer> playerCountDropdown;
    JLabel playerSelectLabel;

    
    JLayeredPane layeredPane;
    Queue<Player> players;
    final Color[] COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.CYAN};

    JPanel dicePanel;
    List<JButton> diceButtons = new ArrayList<>();
    Map<Player, JButton> playerDiceButtonMap = new HashMap<>();
    Map<Player, JLabel> playerScoreLabels = new HashMap<>();
    Map<Integer, ImageIcon> diceImages = new HashMap<>();
    JPanel scorePanel;
    Board board;
    int Round=1;

    Game() {
        players = new LinkedList<>();

	board=new Board();
        loadDiceImages();

        // Load board image
        ImageIcon originalIcon = new ImageIcon("desktop/SnakeLadder/board_image.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(Board.BOARD_WIDTH, Board.BOARD_HEIGHT, Image.SCALE_SMOOTH);
        JLabel imagelabel = new JLabel(new ImageIcon(scaledImage));
        imagelabel.setBounds(0, 0, Board.BOARD_WIDTH, Board.BOARD_HEIGHT);

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(Board.BOARD_WIDTH, Board.BOARD_HEIGHT));
        layeredPane.setLayout(null);
        layeredPane.add(imagelabel, JLayeredPane.DEFAULT_LAYER);

        titleLabel = new JLabel("Snake and Ladder", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        playerSelectLabel = new JLabel("Number of Players:");
        playerSelectLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        Integer[] playerOptions = {2, 3, 4, 5, 6};
        playerCountDropdown = new JComboBox<>(playerOptions);
        playerCountDropdown.setMaximumSize(new Dimension(80, 25));
        playerCountDropdown.setSelectedItem(null);

        startButton = new JButton("🎲 Start Game");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        startButton.setBackground(new Color(46, 204, 113));
        startButton.setForeground(Color.WHITE);
        startButton.setEnabled(false);

        // Dice panel setup
        dicePanel = new JPanel();
        dicePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        dicePanel.setPreferredSize(new Dimension(1024, 100));

        // Score panel setup
        scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBackground(new Color(245, 245, 245));
        scorePanel.setPreferredSize(new Dimension(200, 600));
        scorePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
            "Player Scores",
            0, 0,
            new Font("SansSerif", Font.BOLD, 18),
            Color.BLACK
        ));

        // Dropdown Action
        playerCountDropdown.addActionListener(e -> {
            players.clear();
            layeredPane.removeAll();
            layeredPane.add(imagelabel, JLayeredPane.DEFAULT_LAYER);
            dicePanel.removeAll();
            diceButtons.clear();
            playerDiceButtonMap.clear();
            playerScoreLabels.clear();
            scorePanel.removeAll();

            Integer count = (Integer) playerCountDropdown.getSelectedItem();
            if (count != null) {
                for (int i = 0; i < count; i++) {
                    String name = JOptionPane.showInputDialog(frame, "Enter name for Player " + (i + 1));
                    if (name == null || name.trim().isEmpty()) name = "Player " + (i + 1);

                    Player player = new Player(Board.TILE_SIZE, COLORS[i % COLORS.length], name);
                    players.offer(player);
                    PlayerCoin coin = player.getCoinComponent();
                    coin.setLocation(-100, -100);
                    layeredPane.add(coin, JLayeredPane.PALETTE_LAYER);

                    // Dice button
                    JButton diceBtn = new JButton();
                    diceBtn.setIcon(diceImages.get(1));
                    diceBtn.setPreferredSize(new Dimension(64, 64));
                    diceBtn.setEnabled(false);
                    dicePanel.add(new JLabel(name + ":"));
                    dicePanel.add(diceBtn);
                    diceButtons.add(diceBtn);
                    playerDiceButtonMap.put(player, diceBtn);

                    // Score label
                    JLabel scoreLabel = new JLabel(name + ": 0");
                    scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                    scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    scorePanel.add(scoreLabel);
                    playerScoreLabels.put(player, scoreLabel);
                }

                frame.revalidate();
                frame.repaint();
                startButton.setEnabled(true);
            }
        });

        // Start button
        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            startNextTurn();
        });

        // Left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(160, 200));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));

        playerSelectLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerCountDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalStrut(40));
        leftPanel.add(playerSelectLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(playerCountDropdown);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(startButton);

        // Score container 
        JPanel scoreContainer = new JPanel(new GridBagLayout());
        scoreContainer.setPreferredSize(new Dimension(220, 600));
        scoreContainer.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        scoreContainer.add(scorePanel);

        // Frame setup
        frame = new JFrame("Snake and Ladder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 1024);
        frame.setLayout(new BorderLayout());
        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(layeredPane, BorderLayout.CENTER);
        frame.add(scoreContainer, BorderLayout.EAST);
        frame.add(dicePanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // Loads dice images into a map
    private void loadDiceImages() {
        for (int i = 1; i <= 6; i++) {
            String path = "desktop/SnakeLadder/face" + i + ".png";
            diceImages.put(i, new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
        }
    }

    // Starts next player's turn
    private void startNextTurn() {
        if (players.isEmpty()) return;
        Player currentPlayer = players.poll();
        players.offer(currentPlayer);
        for (JButton btn : diceButtons) btn.setEnabled(false);
        JButton diceButton = playerDiceButtonMap.get(currentPlayer);
        diceButton.setEnabled(true);

        diceButton.addActionListener(new ActionListener() {
           boolean clicked = false;
		
            public void actionPerformed(ActionEvent e) {
                if (clicked) return;
                clicked = true;

                new Thread(() -> {
                    Random rand = new Random();
                    for (int i = 0; i < 10; i++) {
                        int face = rand.nextInt(6) + 1;
                        SwingUtilities.invokeLater(() -> diceButton.setIcon(diceImages.get(face)));
                        try {
                            	Thread.sleep(80);
                        } catch (InterruptedException ex) {
                            	ex.printStackTrace();
                        }
                    }

                    int finalFace = currentPlayer.roll_dice();
                    SwingUtilities.invokeLater(() -> diceButton.setIcon(diceImages.get(finalFace)));

                    if (finalFace == 1) {
                        if (currentPlayer.get_curr_loc() != -1) {
                            	int currentLocation= currentPlayer.get_curr_loc();                           
			    	for(int i=currentLocation+1;i<=currentLocation+finalFace;i++){
					currentPlayer.set_curr_loc(currentPlayer.get_curr_loc() + 1);
					Point current=board.getCellCenter(currentPlayer.get_curr_loc());
					try{
						Thread.sleep(280);
					}
					catch(Exception e1){}
                            		SwingUtilities.invokeLater(() -> currentPlayer.moveTo(current.x - 10, current.y - 10));
			    	}
                        } else {
                            	currentPlayer.set_curr_loc(1);
                            	Point origin = board.getCellCenter(1);
                            	SwingUtilities.invokeLater(() -> currentPlayer.moveTo(origin.x - 10, origin.y - 10));
                       	}
                    } else {
                        if (currentPlayer.get_curr_loc() != -1 && currentPlayer.get_curr_loc()+finalFace<=100) {
			    	int currentLocation= currentPlayer.get_curr_loc();
                            	for(int i=currentLocation+1;i<=currentLocation+finalFace;i++){
					currentPlayer.set_curr_loc(currentPlayer.get_curr_loc() + 1);
					Point current=board.getCellCenter(currentPlayer.get_curr_loc());
					try{
						Thread.sleep(280);
					}
					catch(Exception e2){}
                            		SwingUtilities.invokeLater(() -> currentPlayer.moveTo(current.x - 10, current.y - 10));
			   	 }

                        }
                    }
		
		    		    
		    if(board.mpp.containsKey(currentPlayer.get_curr_loc())){
			Board.Entity entity=board.mpp.get(currentPlayer.get_curr_loc());
			
			Point dest=board.getCellCenter(entity.get_dest());
			currentPlayer.set_curr_loc(entity.get_dest());
			SwingUtilities.invokeLater(() -> currentPlayer.moveTo(dest.x - 10, dest.y - 10));

		    }

                    


			
		   if(currentPlayer.get_curr_loc()==100){
			currentPlayer.set_score(currentPlayer.get_score()+1);
			JLabel label = playerScoreLabels.get(currentPlayer);
			Round++;
			if (label != null) {
                        	label.setText(currentPlayer.getName() + ": " + currentPlayer.get_score());
                    	}

			
			for(int i=1;i<=players.size();i++){
				Player p=players.poll();
				p.set_curr_loc(-1);
				p.moveTo(-100,-100);
				players.offer(p);
				
			}
		    }

                    startNextTurn();
                }).start();
            }
        });
    }

}

