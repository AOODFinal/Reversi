package util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import main.Piece;

public class ImageGrab {
	public static final int BLACKGHOST=2,WHITEGHOST=3;
	private static Icon[] pieces;
	public ImageGrab() {
		pieces = new Icon[5];
		try {
			pieces[Piece.BLANK+1] = new ImageIcon(ImageIO.read(new File("image/blank.png")));
			pieces[Piece.WHITE+1] = new ImageIcon(ImageIO.read(new File("image/white.png")));
			pieces[Piece.BLACK+1] = new ImageIcon(ImageIO.read(new File("image/black.png")));
			pieces[BLACKGHOST+1] = new ImageIcon(ImageIO.read(new File("image/ghostblack.png")));
			pieces[WHITEGHOST+1] = new ImageIcon(ImageIO.read(new File("image/ghostwhite.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
