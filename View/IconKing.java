
package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

/**
 * Creates icon the represents the King
 *
 * @author csleys
 */
public class IconKing implements Icon{
 @Override
    public void paintIcon(Component com, Graphics g, int i, int i1) {
        g.setColor(Color.BLUE);
        g.fillOval(3, 3, com.getWidth()-5, com.getHeight()-5);
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
