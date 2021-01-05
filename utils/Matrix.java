package kadai1.contentssecurity;

import java.io.PrintStream;

public class Matrix {
    int cols;           // the number of columns in matrix
    int rows;           // the number of rows in matrix
    double[][] array;   // the data of matrix

    public Matrix(double[][] array) throws IllegalArgumentException {
        this.rows = array.length;
        if (this.rows <= 0) throw new IllegalArgumentException("matrix must have 1 row at least");

        this.cols = array[0].length;
        if (this.cols <= 0) throw new IllegalArgumentException("matrix must have 1 col at least");

        this.array = array;
    }

    public void printMatrix(PrintStream s) {
        for (int r = 0; r != this.rows; r++) {
            s.format("%c[%.3f", (r == 0 ? '[' : ' '), this.array[r][0]);
            for (int c = 1; c != this.cols; c++) s.format(", %.3f", this.array[r][c]);
            s.format("]%c\n", (r == this.rows - 1 ? ']' : ' '));
        }
    }

    public static Matrix multiply(Matrix A, Matrix B) {
        if (A.cols != B.rows) throw new IllegalArgumentException("multiply expects that A.cols == B.rows");

        int rows = A.rows;
        int cols = B.cols;
        int elems = A.cols;

        double[][] array = new double[rows][cols];
        for (int r = 0; r != rows; r++) {
            for (int c = 0; c != cols; c++) {
                array[r][c] = 0;
                for (int e = 0; e != elems; e++) array[r][c] += A.array[r][e] * B.array[e][c];
            }
        }

        return new Matrix(array);
    }
}
