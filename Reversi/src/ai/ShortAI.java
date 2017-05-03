package ai;

import main.Piece;
/**
 * Pick whatever is first available. (Short refers to the program length)
 *
 */
public class ShortAI implements AI {
	public int[] getBestMove(Piece[][] board, boolean[][] ghosts) {
		for (int i=0;i<ghosts[0].length;i++) {
			for (int j=0;j<ghosts[0].length;j++) {
				if (ghosts[i][j]) return new int[] {i,j};
			}
		}
		return null;
	}

}
