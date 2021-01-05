package kadai1.contentssecurity;

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
            for (int c = 1; c != row.length; c++)
                array[r][c-1] = Double.parseDouble(row[c]);
        }

        return new Matrix(array);
    }

    public static void main(String[] args) {
        try {
            Matrix seiseki = load_array_from_csv("./data/kadai1/seiseki.txt");
            Matrix omomi = load_array_from_csv("./data/kadai1/omomi.txt");
            Matrix saiteiten = load_array_from_csv("./data/kadai1/saiteiten.txt");

            Matrix mikomiten = Matrix.multiply(seiseki, omomi);

            seiseki.printMatrix(System.out);
            omomi.printMatrix(System.out);
            saiteiten.printMatrix(System.out);

            mikomiten.printMatrix(System.out);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
