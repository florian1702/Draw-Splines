package uebung3;

import matrix.Matrix;

public class HermiteClamped
{
    private float[] px, py;

    private float[] pSx, pSy;

    static private Point startTangent;

    static private Point endTangent;

    private float[][] matrixA;

    private float[][] matrixB;

    private float[][] aInversB;

    public HermiteClamped(Points points)
    {

        int n = points.size();
        if (n > 2)
        {
            // erster Punkt
            matrixA = new float[n][n];
            matrixB = new float[n][n + 2];
            matrixA[0][0] = 1;
            matrixB[0][0] = 1;

            // Mittelpoints
            for (int i = 1; i < n - 1; i++)
            {
                matrixA[i][i - 1] = 1;
                matrixA[i][i] = 4;
                matrixA[i][i + 1] = 1;
                matrixB[i][i] = -3;
                matrixB[i][i + 2] = 3;
            }
            // letzter Punkt
            matrixA[n - 1][n - 1] = 1;
            matrixB[n - 1][n + 1] = 1;
            float[][] aInvers = Matrix.invertiereMatrix(matrixA);
            aInversB = Matrix.matMult(aInvers, matrixB);
            px = new float[n + 2];
            py = new float[n + 2];
            px[0] = (int) startTangent.getX();
            py[0] = (int) startTangent.getY();
            for (int i = 0; i < n; ++i)
            {
                px[i + 1] = (float) points.get(i).getX();
                py[i + 1] = (float) points.get(i).getY();
            }
            px[n + 1] = (int) endTangent.getX();
            py[n + 1] = (int) endTangent.getY();
            pSx = Matrix.matMult(aInversB, px);
            pSy = Matrix.matMult(aInversB, py);
        }
    }

    public synchronized float[] getPx()
    {
        return px;
    }

    public synchronized float[] getPy()
    {
        return py;
    }

    public synchronized float[] getpSx()
    {
        return pSx;
    }

    public synchronized float[] getpSy()
    {
        return pSy;
    }

    public synchronized float[][] getaInversB()
    {
        return aInversB;
    }

    public static void setStartTangent(Point sT)
    {
        startTangent = sT;
    }

    public static void setEndTangent(Point eT)
    {
        endTangent = eT;
    }

}
