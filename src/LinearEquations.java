import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * Asks user for num of linear equations; n <= 10;
 * Use Gaussian elimination with SPP (Scaled Partial Pivoting) method
 * asks user for choice of command line entries or txt file
 */

public class LinearEquations {
    public static void main(String[] args) {
        double[][] matrix;

        if (menu())
            matrix = usrInput();
        else
            matrix = txtInput();

        /*/
        matrix = new double[][]{{3, -13, 9, 3, -19},
                {-6, 4, 1, -18, -34},
                {6, -2, 2, 4, 16},
                {12, -8, 6, 10, 26}};
        /*
        double[][] matrix = {{2, 3, 0, 8},
                            {-1, 2, -1, 0},
                            {3, 0, 2, 9}};
        */
        Stack<Integer> rowsDone = new Stack<>();
        ArrayList<Double> valuesOfX = new ArrayList<>(matrix.length);

        for (double[] y: matrix) {
            for (double x: y)
                System.out.printf("%10.2f ", x);
            System.out.println();
        }
        double[] highest = getHighestInRow(matrix);
        for (int column = 0; column < matrix[0].length-1; column++) {
            System.out.println();
            multiplierOps(column, matrix, rowsDone, highest);
        }

        for (int column = matrix[0].length-2; column >= 0; column--) {
            xSolver(column, matrix, rowsDone, valuesOfX);
        }
        System.out.println();
        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("x%d = %5.2f\n", i + 1, valuesOfX.get(matrix.length - (i + 1)));
        }
    }

    /**
     * gets the highest absolute value in column
     * @param column column in matrix that is getting checked
     * @param matrix matrix that gets checked
     * @param rowsDone ArrayList showing what rows have already been operated in
     * @return Pivot row
     */
    public static int getPivotRow(int column, double[][] matrix, Stack<Integer> rowsDone, double[] highestInRow) {
        double temp = 0;
        int index = 0;

        for (int row = 0; row < matrix.length; row++) {
            if (rowsDone.contains(row)) continue;

            if (Math.abs(temp) < Math.abs(matrix[row][column] / Math.abs(highestInRow[row]))) {
                temp = (matrix[row][column] / highestInRow[row]);
                index = row;
            }
        }
        rowsDone.add(index);

        return index;
    }

    /**
     * Gets multiplier for the row
     * @param column column being checked
     * @param row row being checked
     * @param matrix matrix for column/row
     * @param pivotRow pivot row used to find new multiplier
     * @return multiplier for the row
     */
    public static double getMultiplier(int column, int row, double[][] matrix, int pivotRow) {
        return matrix[row][column] / matrix[pivotRow][column];
    }

    /**
     * Gets the highest abs() value per row
     * @param matrix matrix being checked
     * @return double[] of all highest values per row
     */
    public static double[] getHighestInRow(double[][] matrix) {

        double[] highestInRow = new double[matrix.length];
        for (int row = 0; row < matrix.length; row++) {
            double temp = 0;
            for (int column = 0; column < matrix[row].length - 1; column++) {
                if (Math.abs(temp) < Math.abs(matrix[row][column])) {
                    temp = matrix[row][column];
                }
            }
            highestInRow[row] = temp;
        }

        return highestInRow;
    }

    /**
     * Iterates through matrix and starts to solve for x
     */
    public static void xSolver(int column, double[][] matrix, Stack<Integer> rowsDone, ArrayList<Double> valuesOfX) {
        int row = rowsDone.pop();
        int counter = 0;

        for (int columnStart = matrix[row].length - 2; columnStart > column; columnStart--) {
            matrix[row][matrix[row].length-1] -= matrix[row][columnStart] * valuesOfX.get(counter);
            counter++;
        }
        matrix[row][matrix[row].length-1] /= matrix[row][column];
        valuesOfX.add(matrix[row][matrix[row].length-1]);
    }

    public static void multiplierOps(int columnStart, double[][] matrix, Stack<Integer> rowsDone, double[] highestPerRow) {

        int pivotRow = getPivotRow(columnStart, matrix, rowsDone, highestPerRow);
        for (int row = 0; row < matrix.length; row++) {
            if (rowsDone.contains(row))
                continue;

            double multiplier = getMultiplier(columnStart, row, matrix, pivotRow);
            for (int column = columnStart;column < matrix[row].length; column++) {
                matrix[row][column] -= matrix[pivotRow][column] * multiplier;
            }
        }

        for(double[] dd : matrix) {
            for (double d : dd)
                System.out.printf("%10.2f ", d);
            System.out.println();
        }
    }

    public static boolean menu() {
        Scanner kbInput = new Scanner(System.in);
        System.out.println("Welcome to Gaussian Elimination with Scaled Partial Pivoting!!");
        char c;
        while (true) {
            System.out.println("1) User inputted Equations\n" +
                    "2) Equations from txt file");
            c = kbInput.nextLine().charAt(0);
            if (c == '1' || c == '2')
                break;
            System.out.println("Invalid input");
        }

        return c == '1';
    }

    public static double[][] usrInput() {
        Scanner kbInput = new Scanner(System.in);
        int n;
        try {
            System.out.println("Input the amount of rows\n(Max is 10)");
            n = Integer.parseInt(kbInput.nextLine());
            if (n < 1 || n > 10)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");
            return usrInput();
        }
        double[][] matrix = new double[n][n+1];
        for (int y = 0; y < matrix.length; y++) {
                try {
                    System.out.printf("""
                            Input matrix at row %d
                            (space every entry with a space)
                            (Each row should have %d entries)
                            """, y, n+1);
                    String[] s = kbInput.nextLine().split(" ");
                    for (int x = 0; x < matrix[y].length; x++) {
                        matrix[y][x] = Double.parseDouble(s[x]);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Format");
                    return usrInput();
                }
        }
        return matrix;
    }

    public static double[][] txtInput() {
        Scanner kbInput = new Scanner(System.in);
        File file;
        Scanner fileReader;
        try {
            System.out.println("Input the name of the txt file");
            file = new File(kbInput.nextLine());
            fileReader = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Invalid file name");
            return txtInput();
        }

        return  new double[10][11];
    }
}