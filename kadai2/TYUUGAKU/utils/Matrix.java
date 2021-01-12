package utils;

import java.io.PrintStream;

public class Matrix {
    public int cols;           // the number of columns in matrix
    public int rows;           // the number of rows in matrix
    public double[][] array;   // the data of matrix

    public Matrix(double[][] array) throws IllegalArgumentException {
        this.rows = array.length;
        if (this.rows <= 0) throw new IllegalArgumentException("matrix must have 1 row at least");

        this.cols = array[0].length;
        if (this.cols <= 0) throw new IllegalArgumentException("matrix must have 1 col at least");

        this.array = new double[this.rows][];
        for (int r = 0; r < this.rows; r++) this.array[r] = array[r].clone();
    }

    @Override
    protected Matrix clone() {
        return new Matrix(this.array);
    }

    public void printMatrix(PrintStream s) {
        for (int r = 0; r < this.rows; r++) {
            s.format("%c[%.3f", (r == 0 ? '[' : ' '), this.array[r][0]);
            for (int c = 1; c < this.cols; c++) s.format(", %.3f", this.array[r][c]);
            s.format("]%c\n", (r == this.rows - 1 ? ']' : ' '));
        }
    }

    public Matrix add(Matrix B) throws IllegalArgumentException {
        return add(this, B);
    }

    public Matrix multiply(Matrix B) throws IllegalArgumentException {
        return multiply(this, B);
    }

    public Matrix inverse() throws IllegalArgumentException {
        return inverse(this);
    }

    public MatrixPair vsplit(int row) throws IllegalArgumentException {
        if (!(0 < row && row < this.rows)) throw new IllegalArgumentException("row is invalid");

        double[][] upper = new double[row][];
        double[][] bottom = new double[this.rows - row][];

        for (int r = 0; r < row; r++) upper[r] = this.array[r].clone();

        for (int r = row; r <= this.rows; r++) bottom[r - (row + 1)] = this.array[r].clone();

        return new MatrixPair(new Matrix(upper), new Matrix(bottom));
    }

    public MatrixPair hsplit(int col) throws IllegalArgumentException {
        if (!(0 < col && col < this.cols)) throw new IllegalArgumentException("col is invalid");

        double[][] left = new double[this.rows][col];
        double[][] right = new double[this.rows][this.cols - col];

        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (c < col) left[r][c] = this.array[r][c];
                else right[r][c - col] = this.array[r][c];
            }
        }

        return new MatrixPair(new Matrix(left), new Matrix(right));
    }

    public static Matrix add(Matrix A, Matrix B) throws IllegalArgumentException {
        if (A.cols != B.cols) throw new IllegalArgumentException("add expects that A.cols == B.cols");
        if (A.rows != B.rows) throw new IllegalArgumentException("add expects that A.rows == B.rows");

        int rows = A.rows;
        int cols = A.cols;

        double[][] array = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                array[r][c] = A.array[r][c] + B.array[r][c];
            }
        }

        return new Matrix(array);
    }

    public static Matrix multiply(Matrix A, Matrix B) throws IllegalArgumentException {
        if (A.cols != B.rows) throw new IllegalArgumentException("multiply expects that A.cols == B.rows");

        int rows = A.rows;
        int cols = B.cols;
        int elems = A.cols;

        double[][] array = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                array[r][c] = 0;
                for (int e = 0; e < elems; e++) array[r][c] += A.array[r][e] * B.array[e][c];
            }
        }

        return new Matrix(array);
    }

    public static Matrix inverse(Matrix A) throws IllegalArgumentException {
        if (A.cols != A.rows) throw new IllegalArgumentException("inverse expects that A.cols == A.rows");

        A = A.clone();
        int rows = A.rows;
        int cols = A.cols;

        double[][] array = new double[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) array[r][c] = (r == c ? 1 : 0);
        }

        for (int r = 0; r < rows; r++) {
            double v = A.array[r][r];
            for (int c = 0; c < cols; c++) {
                A.array[r][c] /= v;
                array[r][c] /= v;
            }

            for (int r2 = r + 1; r2 < rows; r2++) {
                double v2 = A.array[r2][r];
                for (int c = 0; c < cols; c++) {
                    A.array[r2][c] -= v2 * A.array[r][c];
                    array[r2][c] -= v2 * array[r][c];
                }
            }
        }

        for (int r = rows - 1; r >= 0; r--) {
            for (int r2 = r - 1; r2 >= 0; r2--) {
                double v2 = A.array[r2][r];
                A.array[r2][r] = 0;
                for (int c = 0; c != cols; c++) {
                    array[r2][c] -= v2 * array[r][c];
                }
            }
        }

        return new Matrix(array);
    }
}
