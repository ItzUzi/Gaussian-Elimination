import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Asks user for num of linear equations; n <= 10;
 * Use Gaussian elimination with SPP (Scaled Partial Pivoting) method
 * asks user for choice of command line entries or txt file
 */

public class LinearEquations {
    public static void main(String[] args) {

        double[][] matrix = {{2, 3, 0, 8},
                            {-1, 2, -1, 0},
                            {3, 0, 2, 9}};

        Stack<Integer> rowsDone = new Stack<>();
        ArrayList<Double> valuesOfX = new ArrayList<>();

        for (double[] y: matrix) {
            for (double x: y)
                System.out.printf("%.2f ", x);
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
            System.out.printf("x%d = %.2f\n", i+1, valuesOfX.get(i));
        }

        /*
        double[] highestInRow = getHighestInRow(matrix, rowsDone);
        double multiplier = getMultiplier(0,0, matrix, getPivotRow(0, matrix, rowsDone, highestInRow));
        System.out.println(rowsDone);
        getPivotRow(1, matrix, rowsDone, highestInRow);
        System.out.println(rowsDone);
        getPivotRow(2, matrix, rowsDone, highestInRow);
        System.out.println(rowsDone);
        */
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
            matrix[row][columnStart] = 0;
            counter++;
        }
        matrix[row][matrix[row].length-1] /= matrix[row][column];
        valuesOfX.add(matrix[row][matrix[row].length-1]);
    }

    public static void multiplierOps(int columnStart, double[][] matrix, Stack<Integer> rowsDone, double[] highestPerRow) {

        int column = columnStart;
        int pivotRow = getPivotRow(columnStart, matrix, rowsDone, highestPerRow);
        for (int row = 0; row < matrix.length; row++) {
            if (rowsDone.contains(row))
                continue;

            double multiplier = getMultiplier(columnStart, row, matrix, pivotRow);
            for (;column < matrix[row].length; column++) {
                matrix[row][column] -= matrix[pivotRow][column] * multiplier;
            }
            column = columnStart;
        }

        for(double[] dd : matrix) {
            for (double d : dd)
                System.out.printf("%.2f ", d);
            System.out.println();
        }
    }
}