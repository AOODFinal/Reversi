package main;

import javax.swing.Icon;

import util.ImageGrab;

public class Piece {
	private int state; //0 - Blank, 1 - White, -1 - Black
	public static final int BLANK=0,WHITE=1,BLACK=-1;
	public static final int BLACKGHOST=2,WHITEGHOST=3;
	public Piece() {
		state=0;
	}
	public Piece(int state) {
		this.state=state;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state=state;
	}
	public Icon getIcon() {
		return ImageGrab.getIconFromPiece(state);
	}
	public static int getGhost(int player) {
		return (player+1)/2+BLACKGHOST;
	}
	public String toString() {
		switch (state) {
		case BLANK:return "O";
		case WHITE:return "W";
		case BLACK:return "B";
		case BLACKGHOST:return "b";
		case WHITEGHOST:return "w";
		}
		return "?";
	}
}
