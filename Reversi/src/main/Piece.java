package main;

import javax.swing.Icon;

import util.ImageGrab;

public class Piece {
	private int state; //0 - Blank, 1 - White, -1 - Black
	public static final int BLANK=0,WHITE=1,BLACK=-1;
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
}
