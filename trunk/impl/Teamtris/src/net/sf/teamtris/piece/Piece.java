package net.sf.teamtris.piece;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A bean that describes a tetris piece.
 * @author Leonardo
 * @version 1.0
 * @created 31-dez-2007 14:02:46
 */
public class Piece {
	private static final Log log = LogFactory.getLog(Piece.class);
	
	/**
	 * The internal block representation for this piece.
	 * Indexed first by lines and second by columns.
	 */
	private final boolean[][] blocks;
	
	private final String color;
	
	/**
	 * Constructs a piece using its string representation, like
	 * <code>' ##:## '</code> for the <code>S</code> piece.
	 * @param pieceSerialization The piece string representation.
	 * @param color The color string, as hexa RRGGBB.
	 * @throws ParseException If the piece serialization or the color is invalid.
	 */
	public Piece(String pieceSerialization, String color) throws ParseException{
		String[] lines = pieceSerialization.split(":");
		blocks = new boolean[lines.length][];
		for (int l = 0; l < lines.length; l++) {
			blocks[l] = new boolean[lines[l].length()];
			for(int c = 0; c < lines[l].length(); ++c){
				if(lines[l].charAt(c) != '#' && lines[l].charAt(c) != ' ')
					throw new ParseException("Invalid character '" +  lines[l].charAt(c) + "' in line " + l + ".", c);
				blocks[l][c] = lines[l].charAt(c) == '#';
			}
		}
		if(!color.matches("[0-9A-Fa-f]{6}?")){
			throw new ParseException("Invalid color specification '" + color + "'.", 0);
		}
		this.color = color;
	}

	/**
	 * Obtains this piece color.
	 * @return This piece color.
	 */
	public String getColor() {
		return color;
	}
	
	/**
	 * A simple factory method for pieces.
	 * @param pieceSerialization The piece string representation.
	 * @param color The color string, as hexa RRGGBB.
	 * @return The new piece, or null if there is an error on parsing.
	 */
	public static Piece newInstance(String pieceSerialization, String color){
		try {
			return new Piece(pieceSerialization, color);
		} catch (ParseException e) {
			log.error("Fail to create piece.", e);
		}
		return null;
	}
	
}