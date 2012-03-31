
package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 * Creates the icon the represents the throne at the center of the board
 *
 * @author csleys
 */
public class IconCenterThrone implements Icon{
 @Override
    public void paintIcon(Component com, Graphics g, int i, int i1) {
        g.setColor(Color.BLUE);
        g.fillOval(25, 25, 20, 20);
    }

    @Override
    public int getIconWidth() {
        return 72;
    }

    @Override
    public int getIconHeight() {
        return 72;
    }
}
