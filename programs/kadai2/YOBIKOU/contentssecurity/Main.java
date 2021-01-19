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
		
		br.close();
        isr.close();
        fis.close();

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

		System.out.println("重み行列");
		B.printMatrix(System.out);

		System.out.println("合格最低点");
		S.printMatrix(System.out);

		buf = connector.getTable();
		Matrix M = new Matrix(buf);

		MatrixPair MiP = M.inverse().vsplit(3);
		Matrix Mitop = MiP.first;
		Matrix Mibottom = MiP.second;

		buf = connector.getTable();
		Matrix Ap = new Matrix(buf);

		Matrix Bp = Mibottom.multiply(B);

		connector.sendTable(Bp.array);

		Matrix Bpp = Ap.multiply(Mitop).multiply(B);

		buf = connector.getTable();
		Matrix App = new Matrix(buf);

		Matrix res = App.add(Bpp);

		System.out.println("適性行列");
		res.printMatrix(System.out);

		for (int r = 0; r < res.rows; r++) {
			for (int c = 0; c < res.cols; c++) {
				res.array[r][c] = res.array[r][c] >= S.array[0][c] ? 1.0 : 0.0;
			}
		}

		connector.sendTable(res.array);

		connector.endConnection();
	}
}

