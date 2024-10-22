package uebung3;

import java.awt.Color;

public class Point
{
    public static Color defaultColor = Color.BLUE;

    private Color color = defaultColor;

    private double[] Coords =
    { 0, 0, 1 };

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public Point()
    {
        this.color = Point.defaultColor;
    }

    public Point(double x, double y)
    {
        Coords[0] = x;
        Coords[1] = y;
    }

    public Point(Point p)
    {
        Coords[0] = p.getCoords()[0];
        Coords[1] = p.getCoords()[1];
    }

    public synchronized double[] getCoords()
    {
        return Coords;
    }

    public synchronized void setCoords(double[] Coords)
    {
        this.Coords = Coords;
    }

    /**
     * Berechnet den Abstand zweier Punkte
     * 
     * @param p
     * @return
     */
    public double getDistance(Point p)
    {
        double dx = Coords[0] - p.getCoords()[0];
        double dy = Coords[1] - p.getCoords()[1];
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Berechnet den quadratischen Abstand zweier Punkte
     * 
     * @param p
     * @return
     */
    public double getQDistance(Point p)
    {
        double dx = Coords[0] - p.getCoords()[0];
        double dy = Coords[1] - p.getCoords()[1];

        return (dx * dx + dy * dy);
    }

    public double getX()
    {
        return Coords[0];
    }

    public double getY()
    {
        return Coords[1];
    }

    public void move(Point p)
    {
        Coords[0] = p.getX();
        Coords[1] = p.getY();
    }

}
