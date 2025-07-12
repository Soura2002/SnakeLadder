package desktop.SnakeLadder;

import java.util.*;
import java.awt.*;
class Board{
	static final int BOARD_WIDTH = 600;
    	static final int BOARD_HEIGHT = 600;
    	static final int TILE_SIZE = BOARD_WIDTH / 10;
	HashMap<Integer,Entity> mpp;
	Board(){
		mpp=new HashMap<>();
		populateBoard();
	}
	static class Entity{
		private int destination;
		Entity(int destination){
			this.destination=destination;
		}
		public int get_dest(){
			return destination;
		}
		
		
	}
	// Get cell center coordinates
    	public Point getCellCenter(int num) {
        	int row = 9 - (num - 1) / 10;
        	int col = (num - 1) % 10;
        	if ((row % 2) == 0) col = 9 - col;
        	int x = col * TILE_SIZE + TILE_SIZE / 2;
        	int y = row * TILE_SIZE + TILE_SIZE / 2;
        	return new Point(x, y);
    	}
	public void populateBoard(){
		mpp.put(1,new Entity(38));	
		mpp.put(4,new Entity(14));
		mpp.put(9,new Entity(31));
		mpp.put(17,new Entity(7));
		mpp.put(21,new Entity(42));
		mpp.put(28,new Entity(84));
		mpp.put(51,new Entity(67));
		mpp.put(54,new Entity(34));
		mpp.put(62,new Entity(19));
		mpp.put(64,new Entity(60));
		mpp.put(72,new Entity(91));
		mpp.put(80,new Entity(99));
		mpp.put(87,new Entity(36));
		mpp.put(93,new Entity(73));
		mpp.put(95,new Entity(75));
		mpp.put(98,new Entity(79));
	}
	

}
