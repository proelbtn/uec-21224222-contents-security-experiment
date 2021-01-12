package contentssecurity;

import java.io.*;
import java.util.*;

import utils.*;

public class Main {
	static String NAME = "TYUUGAKU";
	static String PARTNERSIP = "127.0.0.1";
	static Connector connector = new Connector(NAME,PARTNERSIP);

	static LinkedList<String[]> load_csv(String filename) throws Exception {
        FileInputStream fis = new FileInputStream(filename);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        LinkedList<String[]> data = new LinkedList<>();
        String row;
        while ((row = br.readLine()) != null) data.add(row.split(","));

        return data;
    }

	static Matrix load_matrix_from_csv(String filename) throws Exception {
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
	
	public static void main(String[] args) throws Exception {
		Random gen = new Random();

		double[][] buf = null;
		double[][] m = new double[6][6];

		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 6; c++) {
				m[r][c] = gen.nextDouble();
			}
		}

		Matrix A = load_matrix_from_csv("./seiseki.txt");
		Matrix M = new Matrix(m);

		System.out.println("成績行列");
		A.printMatrix(System.out);

		System.out.println("乱数行列");
		M.printMatrix(System.out);

		connector.sendTable(M.array);

		MatrixPair MP = M.hsplit(3);
		Matrix Mleft = MP.first;
		Matrix Mright = MP.second;

		Matrix Ap = A.multiply(Mleft);

		connector.sendTable(Ap.array);

		buf = connector.getTable();
		Matrix Bp = new Matrix(buf);

		Matrix App = A.multiply(Mright).multiply(Bp);

		connector.sendTable(App.array);

		buf = connector.getTable();
		Matrix res = new Matrix(buf);

		System.out.println("合否行列");
		res.printMatrix(System.out);

		connector.endConnection();
	}
}

