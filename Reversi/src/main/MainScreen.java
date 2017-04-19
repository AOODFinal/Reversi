package main;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
				updateBoard(i,j,Piece.BLANK);
				if (i==3 && j==3 || i==4 && j==4) {
					updateBoard(i,j,Piece.WHITE);
				} else if (i==4 && j == 3 || i==3 && j==4) {
					updateBoard(i,j,Piece.BLACK);
				}
				add(visualBoard[i][j]);
			}
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setResizable(false);
		do {
			//TODO turncycle
		} while (!gameOver());
	}
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
		try {
			if (visualBoard[x][y]==null) {
				visualBoard[x][y] = new PieceLabel(x,y,piece);
			} else {
				if (board[x][y].getState() != Piece.BLANK)
					return false;
				visualBoard[x][y].setPiece(piece);
			}
			board[x][y] = new Piece(piece);
			return true;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
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
