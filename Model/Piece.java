package Model;

/**
 * Stores the color of a piece and if it is the king
 *
 * @author csleys
 */
public class Piece{
   private int color;
   public boolean king;

    Piece(int c){
        color = c;
    }

    public int getColor(){
        return color;
    }

    public boolean isKing(){
        return king;
    }
}
