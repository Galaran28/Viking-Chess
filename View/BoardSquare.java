package View;

import javax.swing.JButton;

/**
 * Custom button that sets the icon and stores its x,y coordinates
 *
 * @author csleys
 */
public class BoardSquare extends JButton {

    private int x, y;

    /**
     * Disables some button features and sets the x,y coordinates
     *
     * @param xval  The x value of the button
     * @param yval  The y value of the button
     */
    BoardSquare(int xval, int yval) {
        setRolloverEnabled(false);
        x = xval;
        y = yval;
    }


    /**
     * Sets icon null and then sets it to requested icon
     *
     * @param color Number that corresponds the a color, 0 for black, 1 for white,
     * 2 for king.
     */
    public void setType(int color) {
        setIcon(null);
        setText("");
        switch (color) {
            case 0:
                setIcon(new IconBlack());
                break;
            case 1:
                setIcon(new IconWhite());
                break;
            case 2:
                setIcon(new IconKing());
                break;
        }
    }

    public int getXCoor() {
        return x;
    }

    public int getYCoor() {
        return y;
    }
}
