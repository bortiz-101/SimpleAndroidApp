package edu.luc.etl.cs313.android.shapes.model;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.Iterator;
import java.util.List;

import static edu.luc.etl.cs313.android.shapes.model.Fixtures.simpleGroup;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
      return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for(Shape shapes : g.getShapes()) {
            Location location = shapes.accept(this);
            if(location != null) {
                Shape shape = location.getShape();
                int x = location.getX();
                int y = location.getY();

                if(shape instanceof Rectangle) {
                    Rectangle r = (Rectangle) shape;
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x + r.getWidth());
                    maxY = Math.max(maxY, y + r.getHeight());
                }
                else if (shape instanceof Circle) {
                    Circle c = (Circle) shape;
                    minX = Math.min(minX, x - c.getRadius());
                    minY = Math.min(minY, y - c.getRadius());
                    maxX = Math.max(maxX, x + c.getRadius());
                    maxY = Math.max(maxY, y + c.getRadius());
                } else if (shape instanceof Polygon) {
                    Location polyLocation = onPolygon((Polygon) shape);
                    minX = Math.min(minX, polyLocation.getX());
                    minY = Math.min(minY, polyLocation.getY());
                    Rectangle polyBounds = (Rectangle) polyLocation.getShape();
                    maxX = Math.max(maxX, polyLocation.getX() + polyBounds.getWidth());
                    maxY = Math.max(maxY, polyLocation.getY() + polyBounds.getHeight());
                }
            }
        }
        return new Location(minX, minY, new Rectangle(maxX- minX, maxY - minY));
    }

    @Override
    public Location onLocation(final Location l) {
        Location a = l.getShape().accept(this);
        Shape shape = a.getShape();
        int x = a.getX() + l.getX();
        int y = a.getY() + l.getY();
        if (shape instanceof Rectangle) {
            Rectangle rect = (Rectangle) shape;
            return new Location(x,y, rect);
        }
        return null;
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0,0, r);
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        List<? extends Point> points = s.getPoints();
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point point : points) {
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());

        }
        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }
}
