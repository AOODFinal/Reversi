package main;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ai.AI;
import ai.GeneralAI;
import util.ImageGrab;

public class MainScreen extends JFrame {
	private static final long serialVersionUID = 5855171752561318095L;
	private static Piece[][] board;
	private static PieceLabel[][] visualBoard;
	private static int player;
	private static AI comp;
	public static void main(String[] args) {
		new MainScreen();
	}
	public MainScreen() {
		super("Reversi/Othello");
		comp = new GeneralAI();
		Object[] colors = {"Black" , "White"};
		int answer = JOptionPane.showOptionDialog(this, "Would you like to play as black or white?",
				"Select Player",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				colors,
				colors[0]);
		player = answer == 0?Piece.BLACK:Piece.WHITE;
		setLayout(new GridLayout(8,8));
		new ImageGrab();
		board = new Piece[8][8];
		visualBoard = new PieceLabel[8][8];
		for (Piece[] row : board) {
			for (Piece space : row) {
				space = new Piece();
			}
		}
		for (int i=0;i<visualBoard[0].length;i++) {
			for (int j=0;j<visualBoard.length;j++) {
				forceUpdateBoard(i,j,Piece.BLANK);
				if (i==3 && j==3 || i==4 && j==4) {
					forceUpdateBoard(i,j,Piece.WHITE);
				} else if (i==4 && j == 3 || i==3 && j==4) {
					forceUpdateBoard(i,j,Piece.BLACK);
				}
				add(visualBoard[i][j]);
			}
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setResizable(false);
		checkForGhosts(player);
		for (Point ghost : ghosts) {
			visualBoard[ghost.x][ghost.y].setPiece((player+1)/2 + Piece.BLACKGHOST);
		}
	}
	
	/**
	 * Check to place ghosts (where the player is allowed to place tiles)
	 */
	private static ArrayList<Point> ghosts;
	private static ArrayList<Point> compGhosts;
	private static void checkForGhosts(int toCheck) {
		if (ghosts==null)
			ghosts = new ArrayList<>();
		if (compGhosts==null)
			compGhosts = new ArrayList<>();
		//TODO Check for and place ghosty points in ghosts
		ArrayList<Point> playerPieces = new ArrayList<>();
		for (int x=0;x<board[0].length;x++) {
			for (int y=0;y<board.length;y++) {
				if (board[x][y].getState() == toCheck) {
					playerPieces.add(new Point(x,y));
				}
			}
		}
		//playerPieces has all of the player's pieces
		for (Point p : playerPieces) {
			for (int i=1;i<=8;i++) {
				try {
					if (toCheck==player)
						ghosts.add(checkInLine(p,i,toCheck));
					else
						compGhosts.add(checkInLine(p,i,toCheck));
				} catch (NoSuchPointException e) {
					//System.out.println(e.getMessage()); //Uncomment to display invalid points in console
				}
			}
		}
	}
	/**
	 * Checks for an empty space at the end of a line 
	 * @param start the point at which the method starts
	 * @param direction the direction the checker goes
	 * </br>
	 * 812</br>
	 * 7 3</br>
	 * 654</br>
	 * @return the point the player can play at
	 * @throws NoSuchPointException if no such point exists
	 */
	private static Point checkInLine(Point start, int direction, int toCheck) throws NoSuchPointException {
		int dx=0; //change in x
		int dy=0; //change in y
		/*
		 * set dx, dy based on direction
		 * 812
		 * 7 3
		 * 654
		 */
		switch (direction) {
		case 1:
		case 5:dx=0;break;
		case 2:
		case 3:
		case 4:dx=1;break;
		case 6:
		case 7:
		case 8:dx=-1;break;
			default:
		}
		switch (direction) {
		case 8:
		case 1:
		case 2:dy=1;break;
		case 3:
		case 7:dy=0;break;
		case 4:
		case 5:
		case 6:dy=-1;break;
			default:
		}
		//determine if you can go
		try {
			if (board[start.x+dx][start.y+dy].getState() != -toCheck) { //if the adjacent piece isn't opposing player's color
				throw new NoSuchPointException("(Direction "+direction + ", Point "+start+") Adjacent Piece isn't opposite of player's color");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new NoSuchPointException("(Direction "+direction + ", Point "+start+") Adjacent Piece off grid");
		}
		//go all the way in the direction of dx,dy
		Point current = start.clone();
		try {
			do {
				current.x += dx;
				current.y += dy;
			} while (board[current.x][current.y].getState() == -toCheck);
			if (board[current.x][current.y].getState() != Piece.BLANK) //Ends on a piece of same color
				throw new NoSuchPointException("(Direction "+direction + ", Point "+start+") Endpoint "+current+" isn't blank");
			return current;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new NoSuchPointException("(Direction "+direction + ", Point "+start+") Endpoint "+current+" is off grid");
		}
	}
	/**
	 * Determines if the game is over
	 * @return true if the game is over, false otherwise
	 */
	private static boolean gameOver() {
		// TODO
		return false;
	}
	/**
	 * Updates the board & the visualBoard
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param piece the piece to change it to
	 * @return true if the update succeeded, false otherwise
	 */
	private static boolean updateBoard(int x, int y, int piece) {
		if (visualBoard[x][y].getPiece() == Piece.getGhost(piece)) {
			visualBoard[x][y].setPiece(piece);
			board[x][y] = new Piece(piece);
			return true;
		}
		return false;
	}
	/**
	 * Forces the board to add a piece, whether or not it wants to
	 * @see main.MainScreen.updateBoard(int,int,int)
	 */
	private static void forceUpdateBoard(int x, int y, int piece) {
		if (visualBoard[x][y] == null) {
			visualBoard[x][y] = new PieceLabel(x,y,piece);
		} else {
			visualBoard[x][y].setPiece(piece);
		}
		board[x][y] = new Piece(piece);
	}
	private static class Point {
		int x,y;
		public Point(int x, int y) {
			this.x=x;
			this.y=y;
		}
		public Point() {
			x=0;y=0;
		}
		public Point clone() {
			return new Point(x,y);
		}
		public String toString() {
			return "("+x+","+y+")"; 
		}
	}
	private static class PieceLabel extends JLabel {
		private static final long serialVersionUID = 5423659178406010534L;
		private int piece,x,y;
		public PieceLabel(int x, int y, int piece) {
			super(ImageGrab.getIconFromPiece(piece));
			this.piece=piece;
			this.x=x;
			this.y=y;
			addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					updateBoard(x,y,player);
					checkForGhosts(player);
				}
			});
		}
		public int getPiece() {
			return piece;
		}
		public void setPiece(int piece) {
			this.piece=piece;
			setIcon(ImageGrab.getIconFromPiece(piece));
		}
	}
}
