package main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import ai.AI;
import ai.ShortAI;
import util.ImageGrab;

public class MainScreen extends JFrame {
	private static final long serialVersionUID = 5855171752561318095L;
	private static Piece[][] board;
	private static PieceLabel[][] visualBoard;
	private static int player;
	private static AI comp;
	public static void main(String[] args) {
		Thread checkEnd = new Thread() { //Thread to check if the game is over, and, if so, who won.
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					checkVisualBoard();
					if (gameOver()) {
						int playerTiles=0,compTiles=0;
						for (Piece[] row : board) {
							for (Piece i : row) {
								if (i.getState()==player) {
									playerTiles++;
								} else if (i.getState()==-player) {
									compTiles++;
								}
							}
						}
						String winner = playerTiles>=compTiles?"Player":"Computer";
						JOptionPane.showMessageDialog(null, "Game over. "+winner+" won!");
						System.exit(0);
					}
				}
			}
		};
		new MainScreen();
		checkEnd.setDaemon(true);
		checkEnd.start();
	}
	public MainScreen() {
		super("Reversi/Othello");
		comp = new ShortAI();
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
		displayGhosts(player);
		addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_S) {
					player = -player;
					checkForGhosts(player);
					displayGhosts(player);
				} else if (e.getKeyCode() == KeyEvent.VK_Q) {
					for (Piece[] row : board) {
						for (Piece i : row) {
							System.out.print(i + " ");
						}
						System.out.println();
					}
				}
			}
		});
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
		//remove current ghost points
		if (toCheck == player) {
			for (Point ghost : ghosts) {
				if (visualBoard[ghost.x][ghost.y].getPiece() == Piece.getGhost(player))
					visualBoard[ghost.x][ghost.y].setPiece(Piece.BLANK);
			}
		}
		if (toCheck == player) {
			ghosts.clear();
		} else {
			compGhosts.clear();
		}
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
					//System.err.println(e.getMessage()); //Uncomment to display invalid points in console
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
			default:throw new NoSuchPointException("Invalid Direction");
		}
		switch (direction) {
		case 8:
		case 1:
		case 2:dy=-1;break;
		case 3:
		case 7:dy=0;break;
		case 4:
		case 5:
		case 6:dy=1;break;
			default:throw new NoSuchPointException("Invalid Direction");
		}
		//determine if you can go
		try {
			if (board[start.x+dx][start.y+dy].getState() != -toCheck) { //if the adjacent piece isn't opposing player's color
				throw new NoSuchPointException("(Direction "+direction + ", Point "+start+", Going to " + new Point(start.x+dx,start.y+dy) + ") Adjacent Piece isn't opposite of player's color, it is " + board[start.x+dx][start.y+dy].getState());
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new NoSuchPointException("(Direction "+direction + ", Point "+start+", Going to " + new Point(start.x+dx,start.y+dy) + ") Adjacent Piece off grid");
		}
		//go all the way in the direction of dx,dy
		Point current = start.clone();
		try {
			do {
				current.x += dx;
				current.y += dy;
			} while (board[current.x][current.y].getState() == -toCheck);
			if (board[current.x][current.y].getState() != Piece.BLANK) //Ends on a non-blank piece
				throw new NoSuchPointException("(Direction "+direction + ", Point "+start+") Endpoint "+current+" isn't blank");
			return current;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new NoSuchPointException("(Direction "+direction + ", Point "+start+") Endpoint "+current+" is off grid");
		}
	}
	/**
	 * Display the ghosts specified in toShow
	 * @param toShow the player's whose ghosts to show
	 */
	public static void displayGhosts(int toShow) {
		for (int x=0;x<visualBoard[0].length;x++) {
			for (int y=0;y<visualBoard.length;y++) {
				if (visualBoard[x][y].getPiece() == Piece.getGhost(toShow)) {
					visualBoard[x][y].setPiece(Piece.BLANK);
				}
			}
		}
		if (toShow == player) {
			for (Point ghost : ghosts) {
				visualBoard[ghost.x][ghost.y].setPiece(Piece.getGhost(toShow));
			}
		} else {
			for (Point ghost : compGhosts) {
				visualBoard[ghost.x][ghost.y].setPiece(Piece.getGhost(toShow));
			}
		}
	}
	/**
	 * Switch all valid points from (x,y) to the switcher's color
	 * @param x the x-coordinate of the initial point
	 * @param y the y-coordinate of the initial point
	 * @param switcher the color to switch intermediate points to
	 */
	public static void switchBetween(int x, int y, int switcher) {
		ArrayList<Point> toChange = new ArrayList<>();
		int cx,cy; //current x,y
		for (int dx=-1;dx<=1;dx++) {
			for (int dy=-1;dy<=1;dy++) {
				if (dx == dy && dx == 0)
					continue;
				cx=x+dx;
				cy=y+dy;
				try {
					while (board[cx][cy].getState() == -switcher) {
						toChange.add(new Point(cx,cy));
						cx+=dx;
						cy+=dy;
					}
					//hit end of row
					if (board[cx][cy].getState() == switcher) { //If the row ended with the switcher's color
						for (Point p : toChange) {
							forceUpdateBoard(p.x,p.y,switcher); //change the pieces to the switcher's color
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				} finally {
					toChange.clear();
				}
			}
		}
	}
	/**
	 * Determines if the game is over
	 * @return true if the game is over, false otherwise
	 */
	private static boolean gameOver() {
		boolean full=true;
		boolean anyBlack = false;
		boolean anyWhite = false;
		for (Piece[] row : board) {
			for (Piece space : row) {
				if (space.getState() == Piece.BLANK){
					full=false;
				} else if (space.getState() == Piece.BLACK) {
					anyBlack = true;
				} else if (space.getState() == Piece.WHITE) {
					anyWhite = true;
				}
			}
		}
		if (!anyBlack || !anyWhite) { //One player has wiped the other off of the board
			return true;
		}
		if(full==true||(ghosts.isEmpty()&&compGhosts.isEmpty())){ //Board full or noone can play
			return true;
		}
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
		if (visualBoard[x][y].getPiece() == Piece.getGhost(piece) || piece == Piece.BLACKGHOST || piece == Piece.WHITEGHOST || piece == Piece.BLANK) {
			visualBoard[x][y].setPiece(piece);
			if (piece != Piece.BLACKGHOST && piece != Piece.WHITEGHOST && piece != Piece.BLANK)
				board[x][y] = new Piece(piece);
			return true;
		}
		return false;
	}
	/**
	 * Forces the board to add a piece, whether or not it wants to
	 * @return void because this method always updates the point
	 * @see #updateBoard(int x, int y, int piece)
	 */
	private static void forceUpdateBoard(int x, int y, int piece) {
		if (visualBoard[x][y] == null) {
			visualBoard[x][y] = new PieceLabel(x,y,piece);
		} else {
			visualBoard[x][y].setPiece(piece);
		}
		board[x][y] = new Piece(piece);
	}
	/**
	 * Checks to make sure all of board's pieces on visualBoard (Only checks Piece.WHITE, .BLACK, .BLANK)
	 * @return true if board corresponds to VisualBoard, false if discrepancy
	 */
	private static boolean checkVisualBoard() {
		boolean correct = true;
		for (int x=0;x<board[0].length;x++) {
			for (int y=0;y<board.length;y++) {
				if (board[x][y].getState() != visualBoard[x][y].getPiece() && (board[x][y].getState() != Piece.BLANK && visualBoard[x][y].getPiece() >= Piece.BLACKGHOST)) {
					System.out.println("Discrepancy at "+new Point(x,y)+" between vb:"+visualBoard[x][y].getPiece()+" and b:"+board[x][y].getState());
					correct = false;
					forceUpdateBoard(x,y,board[x][y].getState());
				}
			}
		}
		return correct;
	}
	/**
	 * Changes the ArrayList of points into a grid
	 * @param compGhosts and array of points
	 * @return a grid where true is where the points were, false elsewhere
	 */
	private static boolean[][] translateBoard(ArrayList<Point> compGhosts) {
		boolean[][] grid = new boolean[8][8];
		for (Point p : compGhosts.toArray(new Point[0])) {
			grid[p.x][p.y] = true;
		}
		return grid;
	}
	/**
	 * A representation of an x,y-coordinate
	 *
	 */
	private static class Point {
		int x,y;
		public Point(int x, int y) {
			this.x=x;
			this.y=y;
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
		private int piece;
		public PieceLabel(int x, int y, int piece) {
			super(ImageGrab.getIconFromPiece(piece));
			this.piece=piece;
			ActionListener compRepeat = new ActionListener() { //Computer repeating turn because player can't play
				public void actionPerformed(ActionEvent e) {
					while (ghosts.isEmpty() && !compGhosts.isEmpty()) { //If player cannot play but computer can, let it play
						int[] compTurn2 = comp.getBestMove(board, translateBoard(compGhosts));
						if (compTurn2==null) {
							System.err.println("ERROR. The computer can play but chose no move");
							break;
						}
						forceUpdateBoard(compTurn2[0],compTurn2[1],-player);
						switchBetween(compTurn2[0],compTurn2[1],-player);
						checkForGhosts(-player);
						checkForGhosts(player);
						displayGhosts(player);
					}
				}
			};
			ActionListener compTurn = new ActionListener() { //Computer's normal turn
				public void actionPerformed(ActionEvent e) {
					checkForGhosts(-player);
					if (!compGhosts.isEmpty()) { //If computer can play
						int[] compTurn = comp.getBestMove(board, translateBoard(compGhosts));
						forceUpdateBoard(compTurn[0],compTurn[1],-player);
						switchBetween(compTurn[0],compTurn[1],-player);
						checkForGhosts(-player);
						checkForGhosts(player);
						displayGhosts(player);
						Timer repeatCompTurn = new Timer(100,compRepeat);
						repeatCompTurn.setRepeats(false);
						if (ghosts.isEmpty() && !compGhosts.isEmpty()) { //If player cannot play but computer can, let it play
							repeatCompTurn.start();
						}
					}
				}
			};
			addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					if (updateBoard(x,y,player)) { //Register player turn
						checkForGhosts(player);
						displayGhosts(player);
						switchBetween(x,y,player);
						Timer comp = new Timer(200,compTurn);
						comp.setRepeats(false);
						comp.start();
					}
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
