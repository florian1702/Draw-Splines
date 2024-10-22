package uebung3;

import matrix.Matrix;

public class HermiteClosed
{
    private float[] px, py;

    private float[] pSx, pSy;

    private float[][] matrixA;

    private float[][] matrixB;

    private float[][] aInversB;

    public HermiteClosed(Points points)
    {

        int n = points.size();
        // erster Punkt
        matrixA = new float[n][n];
        matrixB = new float[n][n];
        matrixA[0][0] = 4;
        matrixA[0][1] = 1;
        matrixA[0][n - 1] = 1;
        matrixB[0][1] = 3;
        matrixB[0][n - 1] = -3;
        // Mittelpoints
        for (int i = 1; i < n - 1; i++)
        {
            matrixA[i][i - 1] = 1;
            matrixA[i][i] = 4;
            matrixA[i][i + 1] = 1;
            matrixB[i][i - 1] = -3;
            matrixB[i][i + 1] = 3;
        }
        // letzter Punkt
        matrixA[n - 1][0] = 1;
        matrixA[n - 1][n - 2] = 1;
        matrixA[n - 1][n - 1] = 4;
        matrixB[n - 1][0] = 3;
        matrixB[n - 1][n - 2] = -3;
        float[][] aInvers = Matrix.invertiereMatrix(matrixA);
        aInversB = Matrix.matMult(aInvers, matrixB);
        px = new float[points.size()];
        py = new float[points.size()];
        for (int i = 0; i < n; ++i)
        {
            px[i] = (float) points.get(i).getX();
            py[i] = (float) points.get(i).getY();
        }
        pSx = Matrix.matMult(aInversB, px);
        pSy = Matrix.matMult(aInversB, py);

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
}
