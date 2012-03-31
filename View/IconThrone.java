
package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;


/**
 * Creates icon the represents a throne at one of the corners
 *
 * @author csleys
 */
public class IconThrone implements Icon{

    @Override
    public void paintIcon(Component com, Graphics g, int i, int i1) {
        g.setColor(Color.RED);
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
