package ai;

import main.Piece;

public interface AI {
	/**
	 * 
	 * @param board The current board
	 * @return {x,y} of best spot
	 */
	public int[] getBestMove(Piece[][] board, boolean[][] ghosts);
}
