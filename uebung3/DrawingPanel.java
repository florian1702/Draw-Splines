package uebung3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class DrawingPanel extends JPanel implements MouseListener, MouseInputListener
{

    private Points points;

    private Points tPoints;

    private HermiteNatural hmN;

    private HermiteParabolic hmP;

    private HermiteClamped hmC;

    private HermiteClosed hmCl;

    private Points tangents = new Points();

    private boolean natural = true;

    private boolean parabolic = false;

    private boolean clamped = false;

    private boolean show_bezier = false;

    private boolean closed = false;

    public DrawingPanel()
    {
        super();
        setBackground(Color.GREY);
        setPreferredSize(new Dimension(1000, 750));
        addMouseListener(this);
        addMouseMotionListener(this);
        points = new Points();
        tPoints = new Points();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(3.0f));
        g.setColor(Color.GREEN);

        drawPoints(g);
        drawPolygon(g);
        // Highlighting
        if (points.isMarked())
        {
            g.setColor(Color.RED);
            this.drawPoint(points.getMarked(), g);
        }
        if (tPoints.isMarked())
        {
            g.setColor(Color.RED);
            this.drawPoint(tPoints.getMarked(), g);
        }

        if (natural)
        {
            if (points.size() > 2)
            {
                hmN = new HermiteNatural(points);
                tangents.clear();
                for (int i = 0; i < hmN.getpSx().length; i++)
                {
                    Point newPoint = new Point(hmN.getpSx()[i], hmN.getpSy()[i]);
                    tangents.add(newPoint);
                }
                drawHermiteCurve(hmN, g);
                drawTangents(g);
            }
        }
        else if (clamped)
        {
            if (points.size() > 2)
            {
                if (tangents.size() >= 3)
                {
                    HermiteClamped.setStartTangent(tangents.get(0));
                    HermiteClamped.setEndTangent(tangents.get(tangents.size() - 1));
                }
                else
                {
                    HermiteClamped.setStartTangent(new Point(50, 0));
                    HermiteClamped.setEndTangent(new Point(-50, 0));
                }
                hmC = new HermiteClamped(points);
                tangents.clear();
                for (int i = 0; i < hmC.getpSx().length; i++)
                {
                    Point newPoint = new Point(hmC.getpSx()[i], hmC.getpSy()[i]);
                    tangents.add(newPoint);
                }
                drawHermiteCurve(hmC, g);
                drawTangents(g);
                drawTangentPoints(g);
            }
        }
        else if (closed)
        {
            if (points.size() > 2)
            {

                hmCl = new HermiteClosed(points);
                for (int i = 0; i < hmCl.getpSx().length; i++)
                {
                    Point newPoint = new Point(hmCl.getpSx()[i], hmCl.getpSy()[i]);
                    if (i >= tangents.size())
                    {
                        tangents.add(newPoint);
                    }
                    else
                    {
                        tangents.set(i, newPoint);
                    }
                }

                drawHermiteCurve(hmCl, g);

                drawTangents(g);
            }
        }
        else if (parabolic)
        {
            if (points.size() > 2)
            {
                hmP = new HermiteParabolic(points);
                for (int i = 0; i < hmP.getpSx().length; i++)
                {
                    Point newPoint = new Point(hmP.getpSx()[i], hmP.getpSy()[i]);
                    if (i >= tangents.size())
                    {
                        tangents.add(newPoint);
                    }
                    else
                    {
                        tangents.set(i, newPoint);
                    }
                }
                drawHermiteCurve(hmP, g);
                drawTangents(g);
            }
        }

        if (show_bezier)
        {
            drawBezierCurve(g);
        }
    }

    private void drawPoint(Point p, Graphics g)
    {
        g.fillOval((int) p.getX() - 5, (int) p.getY() - 5, 10, 10);

    }

    private void drawTangents(Graphics g)
    {
        // Draw the tangents
        g.setColor(Color.ORANGE);
        if (clamped)
        {
            Point sT = new Point(points.get(0).getX() + tangents.get(0).getX(), points.get(0).getY() + tangents.get(0).getY());
            Point eT = new Point(points.get(points.size() - 1).getX() + tangents.get(points.size() - 1).getX(), points.get(points.size() - 1).getY() + tangents.get(points.size() - 1).getY());
            tPoints.clear();
            tPoints.add(sT);
            tPoints.add(eT);
        }

        for (int i = 0; i < points.size(); i++)
        {
            g.drawLine((int) points.get(i).getX(), (int) points.get(i).getY(), (int) (points.get(i).getX() + tangents.get(i).getX()), (int) (points.get(i).getY() + tangents.get(i).getY()));
        }

    }

    private void drawHermiteCurve(HermiteNatural h, Graphics g)
    {
        if (points.size() > 2)
        {
            g.setColor(Color.BLUE);
            int p0X = (int) h.getPx()[0];
            int p0Y = (int) h.getPy()[0];
            for (int i = 1; i < points.size(); i++)
            {
                double segments = 100;
                for (double t = 0; t < 1d; t += 1.d / segments)
                {
                    double[] tmp = hermiteBlend(t);
                    int p1x = (int) (h.getPx()[i - 1] * tmp[0] + h.getPx()[i] * tmp[1] + h.getpSx()[i - 1] * tmp[2] + h.getpSx()[i] * tmp[3]);
                    int p1y = (int) (h.getPy()[i - 1] * tmp[0] + h.getPy()[i] * tmp[1] + h.getpSy()[i - 1] * tmp[2] + h.getpSy()[i] * tmp[3]);
                    g.drawLine(p0X, p0Y, p1x, p1y);
                    p0X = p1x;
                    p0Y = p1y;
                }
                g.drawLine(p0X, p0Y, (int) h.getPx()[i], (int) h.getPy()[i]);
            }
        }
    }

    private void drawHermiteCurve(HermiteParabolic h, Graphics g)
    {
        if (points.size() > 2)
        {
            g.setColor(Color.BLUE);
            int p0X = (int) h.getPx()[0];
            int p0Y = (int) h.getPy()[0];
            for (int i = 1; i < points.size(); i++)
            {
                double segments = 100;
                for (double t = 0; t < 1d; t += 1.d / segments)
                {
                    double[] tmp = hermiteBlend(t);
                    int p1x = (int) (h.getPx()[i - 1] * tmp[0] + h.getPx()[i] * tmp[1] + h.getpSx()[i - 1] * tmp[2] + h.getpSx()[i] * tmp[3]);
                    int p1y = (int) (h.getPy()[i - 1] * tmp[0] + h.getPy()[i] * tmp[1] + h.getpSy()[i - 1] * tmp[2] + h.getpSy()[i] * tmp[3]);
                    g.drawLine(p0X, p0Y, p1x, p1y);
                    p0X = p1x;
                    p0Y = p1y;
                }
                g.drawLine(p0X, p0Y, (int) h.getPx()[i], (int) h.getPy()[i]);
            }
        }
    }

    private void drawHermiteCurve(HermiteClamped h, Graphics g)
    {
        if (points.size() > 2)
        {
            g.setColor(Color.BLUE);
            int p0X = (int) h.getPx()[1];
            int p0Y = (int) h.getPy()[1];
            for (int i = 1; i < h.getpSx().length; i++)
            {
                double segments = 100;
                for (double t = 0; t < 1d; t += 1.d / segments)
                {
                    double[] tmp = hermiteBlend(t);
                    int p1x = (int) (h.getPx()[i] * tmp[0] + h.getPx()[i + 1] * tmp[1] + h.getpSx()[i - 1] * tmp[2] + h.getpSx()[i] * tmp[3]);
                    int p1y = (int) (h.getPy()[i] * tmp[0] + h.getPy()[i + 1] * tmp[1] + h.getpSy()[i - 1] * tmp[2] + h.getpSy()[i] * tmp[3]);
                    g.drawLine(p0X, p0Y, p1x, p1y);
                    p0X = p1x;
                    p0Y = p1y;
                }
            }
        }
    }

    private void drawHermiteCurve(HermiteClosed h, Graphics g)
    {
        if (points.size() > 2)
        {
            g.setColor(Color.BLUE);
            int p0X = (int) h.getPx()[0];
            int p0Y = (int) h.getPy()[0];

            double segments = 100;

            for (int i = 1; i < points.size(); i++)
            {
                for (double t = 0; t < 1d; t += 1.d / segments)
                {
                    double[] tmp = hermiteBlend(t);
                    int p1x = (int) (h.getPx()[i - 1] * tmp[0] + h.getPx()[i] * tmp[1] + h.getpSx()[i - 1] * tmp[2] + h.getpSx()[i] * tmp[3]);
                    int p1y = (int) (h.getPy()[i - 1] * tmp[0] + h.getPy()[i] * tmp[1] + h.getpSy()[i - 1] * tmp[2] + h.getpSy()[i] * tmp[3]);
                    g.drawLine(p0X, p0Y, p1x, p1y);
                    p0X = p1x;
                    p0Y = p1y;
                }
                g.drawLine(p0X, p0Y, (int) h.getPx()[i], (int) h.getPy()[i]);
            }

            // draw Line between last and first point
            for (double t = 0; t < 1d; t += 1.d / segments)
            {
                double[] tmp = hermiteBlend(t);
                int p1x = (int) (h.getPx()[points.size() - 1] * tmp[0] + h.getPx()[0] * tmp[1] + h.getpSx()[points.size() - 1] * tmp[2] + h.getpSx()[0] * tmp[3]);
                int p1y = (int) (h.getPy()[points.size() - 1] * tmp[0] + h.getPy()[0] * tmp[1] + h.getpSy()[points.size() - 1] * tmp[2] + h.getpSy()[0] * tmp[3]);
                g.drawLine(p0X, p0Y, p1x, p1y);
                p0X = p1x;
                p0Y = p1y;
            }
            g.drawLine(p0X, p0Y, (int) h.getPx()[0], (int) h.getPy()[0]);
        }
    }

    private void drawBezierCurve(Graphics g)
    {
        if (points.size() > 2)
        {
            g.setColor(Color.pink);

            Points bezierPoints = new Points();
            double segments = 100;
            for (double t = 0; t < 1d; t += 1.d / segments)
            {

                bezierPoints.addAll(calculateBezierCurve(points, t));
            }

            for (int i = 1; i < bezierPoints.size() - 1; i++)
            {
                int p0X = (int) bezierPoints.get(i - 1).getX();
                int p0Y = (int) bezierPoints.get(i - 1).getY();

                int p1x = (int) bezierPoints.get(i).getX();
                int p1y = (int) bezierPoints.get(i).getY();
                g.drawLine(p0X, p0Y, p1x, p1y);

            }

        }
    }

    public Points calculateBezierCurve(Points points, double t)
    {
        Points curvePoints = new Points();

        if (points.size() == 1)
        {
            curvePoints.add(points.get(0));
            return curvePoints;
        }

        Points newPoints = new Points();
        for (int i = 0; i < points.size() - 1; i++)
        {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);

            double x = (1 - t) * p1.getX() + t * p2.getX();
            double y = (1 - t) * p1.getY() + t * p2.getY();

            newPoints.add(new Point(x, y));
        }

        curvePoints.addAll(calculateBezierCurve(newPoints, t));
        return curvePoints;
    }

    private void drawPoints(Graphics g)
    {
        for (Point p : points)
        {
            drawPoint(p, g);
        }
    }

    private void drawTangentPoints(Graphics g)
    {
        for (Point p : tPoints)
        {
            g.drawOval((int) p.getX() - 5, (int) p.getY() - 5, 10, 10);
        }
    }

    private void drawPolygon(Graphics g)
    {
        if (points.size() > 1)
        {
            Point p0 = points.get(0);
            int x0 = (int) p0.getX();
            int y0 = (int) p0.getY();
            for (int i = 1; i < points.size(); i++)
            {
                Point p1 = points.get(i);
                int x1 = (int) p1.getX();
                int y1 = (int) p1.getY();
                g.drawLine(x0, y0, x1, y1);
                x0 = x1;
                y0 = y1;
            }
            if (closed)
            {
                g.drawLine(x0, y0, (int) points.get(0).getX(), (int) points.get(0).getY());
            }
        }
    }

    double[] hermiteBlend(double t)
    {
        double t2 = t * t;
        double t3 = t2 * t;
        double h1 = 2 * t3 - 3 * t2 + 1;
        double h2 = -2 * t3 + 3 * t2;
        double h3 = t3 - 2 * t2 + t;
        double h4 = t3 - t2;

        return new double[]
        { h1, h2, h3, h4 };
    }

    double[] bezierBlend(double t, int n)
    {
        double[] blend = new double[n + 1];
        blend[0] = Math.pow(1 - t, n);
        for (int i = 1; i < n; i++)
        {
            blend[i] = Math.pow(n * t, i) * Math.pow(1 - t, n - i);
        }
        blend[n] = Math.pow(t, n);
        return blend;
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (points.isMarked())
        {
            Point p = points.getMarked();
            p.move(new Point(e.getX(), e.getY()));
        }
        if (tPoints.isMarked())
        {

            if (tPoints.marked == 0)
                tangents.set(0, new Point(e.getX() - points.get(0).getX(), e.getY() - points.get(0).getY()));
            else if (tPoints.marked == 1)
                tangents.set(tangents.size() - 1, new Point(e.getX() - points.get(points.size() - 1).getX(), e.getY() - points.get(points.size() - 1).getY()));

        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (!points.isMarked() && !tPoints.isMarked())
        {
            if (e.getButton() == MouseEvent.BUTTON1)
            {
                points.add(new Point(e.getX(), e.getY()));
                points.marked = points.size() - 1;
            }
        }
        else
        {
            if (e.getButton() == MouseEvent.BUTTON3)
            {
                points.removeMarked();
            }
        }

        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        points.markNearest(e.getX(), e.getY());

        if (clamped)
            tPoints.markNearest(e.getX(), e.getY());

        repaint();

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    public void setNatural(boolean natural)
    {
        this.natural = natural;
        repaint();
    }

    public void setParabolic(boolean parabolic)
    {
        this.parabolic = parabolic;
        repaint();
    }

    public void setClamped(boolean clamped)
    {
        this.clamped = clamped;
        repaint();
    }

    public void setShow_bezier(boolean show_bezier)
    {
        this.show_bezier = show_bezier;
        repaint();
    }

    public void setClosed(boolean closed)
    {
        this.closed = closed;
        repaint();
    }

}
