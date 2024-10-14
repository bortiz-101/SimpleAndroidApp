package edu.luc.etl.cs313.android.shapes.model;

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
        return g.accept(this);
    }

    @Override
    public Location onLocation(final Location l) {
        final int x = l.getX();
        final int y = l.getY();
        final var a = (Rectangle) l.getShape();
        final int w = a.getWidth();
        final int h = a.getHeight();
        return new Location(x, y, new Rectangle(w,h));
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        final int width = r.getWidth();
        final int height = r.getHeight();
        return new Location(0,0, new Rectangle(width, height));
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        return null;
    }
}
