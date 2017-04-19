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
	private static Icon[] pieces;
	public ImageGrab() {
		pieces = new Icon[3];
		try {
			pieces[Piece.BLANK+1] = new ImageIcon(ImageIO.read(new File("image/blank.png")));
			pieces[Piece.WHITE+1] = new ImageIcon(ImageIO.read(new File("image/white.png")));
			pieces[Piece.BLACK+1] = new ImageIcon(ImageIO.read(new File("image/black.png")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Flippin' instantiate before use
	 */
	public static Icon getIconFromPiece(int piece) {
		return pieces[piece+1];
	}
}
