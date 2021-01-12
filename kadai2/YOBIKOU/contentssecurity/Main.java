package contentssecurity;

import java.io.*;
import java.util.*;

import utils.*;

public class Main {
	static String NAME = "YOBIKOU";
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
		double[][] buf = null;

		Matrix B = load_matrix_from_csv("./omomi.txt");
		Matrix S = load_matrix_from_csv("./saiteiten.txt");
		B.printMatrix(System.out);
		S.printMatrix(System.out);

		buf = connector.getTable();
		Matrix M = new Matrix(buf);
		M.printMatrix(System.out);

		MatrixPair MiP = M.inverse().vsplit(3);
		Matrix Mitop = MiP.first;
		Matrix Mibottom = MiP.second;
		Mitop.printMatrix(System.out);
		Mibottom.printMatrix(System.out);

		buf = connector.getTable();
		Matrix Ap = new Matrix(buf);
		Ap.printMatrix(System.out);

		Matrix Bp = Mibottom.multiply(B);
		Bp.printMatrix(System.out);

		connector.sendTable(Bp.array);

		Matrix Bpp = Ap.multiply(Mitop).multiply(B);
		Bpp.printMatrix(System.out);

		buf = connector.getTable();
		Matrix App = new Matrix(buf);
		App.printMatrix(System.out);

		Matrix Ans = App.add(Bpp);
		Ans.printMatrix(System.out);

		connector.endConnection();
	}

	/*
	// メソッド(関数)の記述例
	private static void method(double argument) {
		// 処理を記述
	}
	*/
}

