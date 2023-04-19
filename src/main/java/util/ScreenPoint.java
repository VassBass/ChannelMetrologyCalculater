package util;

import java.awt.*;

public class ScreenPoint {
    public static Point center(Component parent, Component child) {
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

    public static Point center(Dimension parent, Component child) {
        int pw = parent.width;
        int cw = child.getWidth();

        int ph = parent.height;
        int ch = child.getHeight();

        int x = (pw/2) - (cw/2);
        int y = (ph/2) - (ch/2);

        return new Point(x,y);
    }

    public static Point leftFromParent(Component parent, Component child){
        int parentX = parent.getLocation().x;
        int childWidth = child.getWidth();

        int x = parentX - childWidth;
        int y = parent.getLocation().y;

        return new Point(x,y);
    }

    public static Point rightFromParent(Component parent){
        int parentX = parent.getLocation().x;
        int parentWidth = parent.getWidth();

        int x = parentX + parentWidth;
        int y = parent.getLocation().y;

        return new Point(x,y);
    }

    public static Point topLeftCornerOf(Component parent){
        return new Point(parent.getLocation().x, parent.getLocation().y);
    }
}