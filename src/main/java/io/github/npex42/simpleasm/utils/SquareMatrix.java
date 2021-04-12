package io.github.npex42.simpleasm.utils;

@SuppressWarnings("ALL")
public class SquareMatrix {

    protected int[][] mat;

    private static int[][] Multiply(int[][] a, int[][] b) {
        int n = a.length;
        int[][] c = new int[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                for (int k = 0; k < n; k++)
                {
                    c[i][j] = c[i][j] + a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }

    public static SquareMatrix Identity(int size) {
        SquareMatrix m = new SquareMatrix();
        for(int i = 0; i < size; i++) {
            m.mat[i][i] = 1;
        }
        return m;
    }
}
