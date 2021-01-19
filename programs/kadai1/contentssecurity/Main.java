package contentssecurity;

import utils.Matrix;

import java.io.*;
import java.util.*;

public class Main {
    static LinkedList<String[]> load_csv(String filename) throws Exception {
        FileInputStream fis = new FileInputStream(filename);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        LinkedList<String[]> data = new LinkedList<>();
        String row;
        while ((row = br.readLine()) != null) data.add(row.split(","));

        br.close();
        isr.close();
        fis.close();

        return data;
    }

     static Matrix load_array_from_csv(String filename) throws Exception {
        LinkedList<String[]> data = load_csv(filename);

        int rows = data.size() - 1;
        int cols = data.getFirst().length - 1;

        double[][] array = new double[rows][cols];

        String[] row;
        ListIterator<String[]> rowIter = data.listIterator(1);
        for (int r = 0; rowIter.hasNext(); r++) {
            row = rowIter.next();
            for (int c = 1; c < row.length; c++)
                array[r][c-1] = Double.parseDouble(row[c]);
        }

        return new Matrix(array);
    }

    public static void save_tekisei_matrix(String filename, Matrix matrix) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);
        PrintWriter pw = new PrintWriter(bw);

        pw.println("適性行列,高校A,高校B,高校C,高校D");
        for (int r = 0; r < matrix.rows; r++) {
            pw.printf("生徒%d", r+1);
            for (int c = 0; c < matrix.cols; c++) {
                pw.printf(",%.3f", matrix.array[r][c]);
            }
            pw.print("\n");
        }

        pw.close();
        bw.close();
        osw.close();
        fos.close();
    }

    public static void save_gouhi_matrix(String filename, Matrix matrix) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        BufferedWriter bw = new BufferedWriter(osw);
        PrintWriter pw = new PrintWriter(bw);

        pw.println("合否行列,高校A,高校B,高校C,高校D");
        for (int r = 0; r < matrix.rows; r++) {
            pw.printf("生徒%d", r+1);
            for (int c = 0; c < matrix.cols; c++) {
                pw.printf(",%d", (int)matrix.array[r][c]);
            }
            pw.print("\n");
        }

        pw.close();
        bw.close();
        osw.close();
        fos.close();
    }

    public static void main(String[] args) {
        try {
            Matrix seiseki = load_array_from_csv("./seiseki.txt");
            Matrix omomi = load_array_from_csv("./omomi.txt");
            Matrix saiteiten = load_array_from_csv("./saiteiten.txt");

            Matrix tekisei = Matrix.multiply(seiseki, omomi);

            System.out.println("成績行列:");
            seiseki.printMatrix(System.out);

            System.out.println("重み行列:");
            omomi.printMatrix(System.out);

            System.out.println("最低点:");
            saiteiten.printMatrix(System.out);

            System.out.println("適性行列:");
            tekisei.printMatrix(System.out);

            double[][] arr_gouhi = new double[tekisei.rows][tekisei.cols];
            for (int r = 0; r < tekisei.rows; r++) {
                for (int c = 0; c < tekisei.cols; c++) {
                    arr_gouhi[r][c] = tekisei.array[r][c] >= saiteiten.array[0][c] ? 1 : 0;
                }
            }
            Matrix gouhi = new Matrix(arr_gouhi);

            System.out.println("合否行列:");
            gouhi.printMatrix(System.out);

            save_tekisei_matrix("./tekisei.txt", tekisei);
            save_gouhi_matrix("./gouhi.txt", gouhi);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
