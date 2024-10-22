/**
 * 
 */
package uebung3;

import java.util.ArrayList;

public class Points extends ArrayList<Point>
{
    public static float box = 5;

    public static float qBox = box * box;

    protected int marked = -1;

    public Point getMarked()
    {
        if (marked > -1)
        {
            return this.get(marked);
        }
        return null;
    }

    public void setMarked(int index)
    {
        if (index >= this.size())
        {
            marked = -1;
        }
        else
        {
            marked = index;
        }
    }

    public void mark(Point p)
    {
        marked = -1;
        for (int i = 0; i < this.size(); i++)
        {
            if (this.get(i) == p)
            {
                marked = i;
                i = this.size();
            }
        }
    }

    /**
     * Markiere den naechsten Punkt, der innerhalb einer Abstandsbox liegt
     * 
     * @param x
     * @param y
     * @return der naechste Punkt
     */
    public Point markNearest(float x, float y)
    {
        marked = -1;
        double dist = Double.MAX_VALUE;
        Point p = new Point(x, y);
        Point nearest = null;
        for (int i = 0; i < this.size(); i++)
        {
            Point pt = this.get(i);
            double dt = pt.getQDistance(p);
            if (dt < qBox & dt < dist)
            {
                marked = i;
                dist = dt;
                nearest = pt;
            }
        }
        return nearest;
    }

    public boolean isMarked()
    {
        return marked > -1;
    }

    public void removeMarked()
    {
        super.remove(this.get(marked));
        marked = -1;
    }
}
