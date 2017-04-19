package main;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import util.ImageGrab;

public class MainScreen extends JFrame {
	private static final long serialVersionUID = 5855171752561318095L;
	private static Piece[][] board;
	private static PieceLabel[][] visualBoard;
	private static int player;
	public static void main(String[] args) {
		new MainScreen();
	}
	public MainScreen() {
		//set player to what they choose
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
				visualBoard[i][j] = new PieceLabel(Piece.BLANK);
				if (i==3 && j==3 || i==4 && j==4) {
					visualBoard[i][j] = new PieceLabel(Piece.WHITE);
					board[i][j] = new Piece(Piece.WHITE);
				} else if (i==4 && j == 3 || i==3 && j==4) {
					visualBoard[i][j] = new PieceLabel(Piece.BLACK);
					board[i][j] = new Piece(Piece.BLACK);
				}
				add(visualBoard[i][j]);
			}
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	private static class PieceLabel extends JLabel {
		private static final long serialVersionUID = 5423659178406010534L;
		private int piece;
		public PieceLabel(int piece) {
			super(ImageGrab.getIconFromPiece(piece));
			this.piece=piece;
		}
		public int getPiece() {
			return piece;
		}
	}
}
