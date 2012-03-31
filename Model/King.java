package Model;

/**
 * DIfferentiates the king from other pieces while allowing it to retain its
 * color.
 *
 * @author csleys
 */
public class King extends Piece{
    King(int c){
        super (c);
        king = true;
    }
}
