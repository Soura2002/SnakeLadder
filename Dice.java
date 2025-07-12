package desktop.SnakeLadder;


import java.util.Random;
class Dice{
	Dice(){}
	int roll(){return new Random().nextInt(6)+1;}
}