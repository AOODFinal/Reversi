package util;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import main.Piece;

public class ImageGrab {
	private static Icon[] pieces;
	public ImageGrab() {
		pieces = new Icon[5];
		try {
			pieces[Piece.BLANK+1] = new ImageIcon(ImageIO.read(new File("/blank.png")));
			pieces[Piece.WHITE+1] = new ImageIcon(ImageIO.read(new File("/white.png")));
			pieces[Piece.BLACK+1] = new ImageIcon(ImageIO.read(new File("/black.png")));
			pieces[Piece.BLACKGHOST+1] = new ImageIcon(ImageIO.read(new File("/ghostblack.png")));
			pieces[Piece.WHITEGHOST+1] = new ImageIcon(ImageIO.read(new File("/ghostwhite.png")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Instantiate before use
	 */
	public static Icon getIconFromPiece(int piece) {
		return pieces[piece+1];
	}
}
