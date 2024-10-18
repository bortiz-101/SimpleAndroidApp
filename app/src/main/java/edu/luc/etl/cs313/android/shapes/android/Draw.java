package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        int color = paint.getColor();
        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(color);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        Style style = paint.getStyle();
        paint.setStyle(Style.FILL);
        f.getShape().accept(this);
        paint.setStyle(style);
        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for(Shape shape : g.getShapes()){
            shape.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.save();
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.restore();
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0,0, r.getWidth(),r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        int num = s.getPoints().size();
        if(num > 1){
            final float[] points = new float[(num -1) * 4];
            int i=0;
            Point last = s.getPoints().get(0);
            for(int j=1; j<num; j++){
                Point curr =s.getPoints().get(j);
                points[i++] = last.getX();
                points[i++] = last.getY();
                points[i++] = curr.getX();
                points[i++] = curr.getY();
                last = curr;
            }
            canvas.drawLines(points, paint);
            Point first = s.getPoints().get(0);
            canvas.drawLine(last.getX(), last.getY(), first.getX(), first.getY(), paint);
        }
        return null;
    }
}
