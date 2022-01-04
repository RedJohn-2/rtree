package rtree.utils;
import rtree.Point;
import rtree.Rectangle;

import java.util.List;

public class RTreeUtil {
    public static Rectangle findMinRoundRegion(Rectangle[] regions) {
        double maxX = 0;
        double maxY = 0;
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;

        for (Rectangle region : regions) {
            if (region.getMinP().getX() < minX) {
                minX = region.getMinP().getX();
            }

            if (region.getMinP().getY() < minY) {
                minY = region.getMinP().getY();
            }

            if (region.getMaxP().getX() > maxX) {
                maxX = region.getMaxP().getX();
            }

            if (region.getMaxP().getY() > maxY) {
                maxY = region.getMaxP().getY();
            }
        }

        return new Rectangle(new Point(minX, minY), new Point(maxX, maxY));
    }

    public static boolean isCrossing(Rectangle rec1, Rectangle rec2) {
        return !(rec1.getMinP().getX() > rec2.getMaxP().getX()) &&
                !(rec1.getMaxP().getX() < rec2.getMinP().getX()) &&
                !(rec1.getMaxP().getY() < rec2.getMinP().getY()) &&
                !(rec1.getMinP().getY() > rec2.getMaxP().getY());
    }
}
