package converters;

import java.awt.*;

public class ConverterUI {
    public static Point POINT_CENTER (Component parent, Component child) {

        int x0 = parent.getLocation().x;
        int pw = parent.getWidth();
        int cw = child.getWidth();

        int y0 = parent.getLocation().y;
        int ph = parent.getHeight();
        int ch = child.getHeight();

        int x = x0 + (pw/2) - (cw/2);
        int y = y0 + (ph/2) - (ch/2);

        return new Point(x,y);
    }

    public static Point POINT_CENTER (Dimension parent, Component child) {

        int pw = parent.width;
        int cw = child.getWidth();

        int ph = parent.height;
        int ch = child.getHeight();

        int x = (pw/2) - (cw/2);
        int y = (ph/2) - (ch/2);

        return new Point(x,y);
    }

    public static Point POINT_TOP_LEFT (Component parent){
        return new Point(parent.getLocation().x, parent.getLocation().y);
    }
}