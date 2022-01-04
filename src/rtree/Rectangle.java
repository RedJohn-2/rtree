package rtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Rectangle {
    private Point minP;
    private Point maxP;

    public Rectangle(Point minP, Point maxP) {
        this.minP = minP;
        this.maxP = maxP;
    }

    public Rectangle() {
    }

    public Point getMinP() {
        return minP;
    }

    public void setMinP(Point minP) {
        this.minP = minP;
    }

    public Point getMaxP() {
        return maxP;
    }

    public void setMaxP(Point maxP) {
        this.maxP = maxP;
    }

    public List<Point> getPoints() {
        List<Point> points = new ArrayList<>();
        points.add(minP);
        points.add(minP);
        points.add(new Point(minP.getX(), maxP.getY()));
        points.add(new Point(maxP.getX(), minP.getY()));
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rectangle)) return false;
        Rectangle rectangle = (Rectangle) o;
        return minP.getY() == rectangle.getMinP().getY() && maxP.getY() == rectangle.getMaxP().getY() &&
                minP.getX() == rectangle.getMinP().getX() && maxP.getX() == rectangle.getMaxP().getX() ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minP, maxP);
    }

    public double getArea() {
        return (maxP.getX() - minP.getX()) * (maxP.getY() - minP.getY());
    }
}
