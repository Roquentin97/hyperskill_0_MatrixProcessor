package com.roquentin9700;

import java.util.TreeSet;
import java.util.stream.IntStream;

public class DoubleMatrix extends Matrix<Double> {

    public DoubleMatrix(int rows, int columns) {
        super(rows, columns);
    }

    public DoubleMatrix(int rows, int columns, Double... elements) {
        super(rows, columns, elements);
    }

    public DoubleMatrix(DoubleMatrix prototype) {
        super(prototype);
    }

    @Override
    DoubleMatrix getCopy() {
        return new DoubleMatrix(this);
    }

    @Override
    DoubleMatrix getMatrix(int rows, int columns) {
        return new DoubleMatrix(rows, columns);
    }

    /**
     * Accepts matrix and returns new matrix representing result of addition two matrices
     * @param matrix
     * @throws ArithmeticException if matrices are of the different sizes
     * @return result of addition given matrix to the current
     *
     */
    public DoubleMatrix add(DoubleMatrix matrix) {
        if (this.getRowsCount() != matrix.getRowsCount() || this.getColumnsCount() != matrix.getColumnsCount()) {
            throw new ArithmeticException("Both matrices must have the same dimension");
        }

        int rows = getRowsCount();
        int columns = getColumnsCount();

        var result = new DoubleMatrix(this);

        for(int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                result.set(row, column, this.matrix[row][column] + matrix.matrix[row][column]);
            }
        }

        return result;
    }

    /**
     * Returns new matrix representing multiplying current matrix with the given constant
     * @param constant
     * @return resulting matrix after multiplying current with the constant
     */
    public DoubleMatrix multiply(double constant) {
        var result = new DoubleMatrix(this);

        for (int row = 0; row < getRowsCount(); row++) {
            for (int column = 0; column < getColumnsCount(); column++) {
                double product = get(row, column) * constant;
                result.set(row, column, product);
            }
        }

        return result;
    }

    /**
     *  Returns resulting matrix representing current matrix with the given one
     * @param matrix
     * @throws ArithmeticException if number of current matrix`s rows doesn't equals to given matrix`s columns
     * @return new matrix representing multiplication of the current matrix and the given one
     */
    public DoubleMatrix multiply(DoubleMatrix matrix) {
        if (getRowsCount() != matrix.getColumnsCount())
            throw new ArithmeticException("Number of rows of the current matrix must be equivalent to the number of columns of the given matrix");
        var result = new DoubleMatrix(getRowsCount(), matrix.getColumnsCount());
        double product;
        double[] row = new double[matrix.getColumnsCount()];

        for (int i = 0; i < getRowsCount(); i++) {
            for (int e = 0; e < row.length; e++) {
                product = 0;
                for (int j = 0; j < getColumnsCount(); j++) {
                    product += this.matrix[i][j] * matrix.matrix[j][e];
                }
                row[e] = product;
            }
            for (int e = 0; e < row.length; e++) {
                result.matrix[i][e] = row[e];
            }
        }

        return result;
    }

    /**
     * Return the determinant of the current matrix
     * @return determinant of given current matrix
     */
    public Double determinant() {
        if (getRowsCount() != getColumnsCount()) {
            throw new ArithmeticException("Matrix must be a square matrix");
        }

        if (getRowsCount() == 2) {
            return productDifference(0,0,1);
        }

        TreeSet<Integer> all = new TreeSet<>();
        TreeSet<Integer> included;

        IntStream.range(0, getRowsCount()).forEach(all::add);

        int sign = 0;

        Double result = 0d;
        Double sub;
        for (int col = 0; col < getColumnsCount(); col++) {
            if (get(0, col) == 0)
                continue;

            included = new TreeSet<>(all);
            included.remove(col);

            sub = subDeterminant(1, included) * get(0, col);

            result += sign++ % 2 == 0 ? sub : -sub;
        }

        return result;
    }

    /**
     * Returns minor of the current matrix by given indexes
     * @param excludedRow
     * @param excludedColumn
     * @return
     */
    public DoubleMatrix minor(int excludedRow, int excludedColumn) {
        DoubleMatrix subMatrix = new DoubleMatrix(getRowsCount() - 1, getColumnsCount() - 1);
        for (int row = 0; row < excludedRow; row++) {
            for (int col = 0; col < excludedColumn; col++) {
                subMatrix.set(row, col, get(row, col));
            }
            for (int col = excludedColumn + 1; col < getColumnsCount(); col++) {
                subMatrix.set(row, col - 1, get(row, col));
            }
        }
        for (int row = excludedRow + 1; row < getRowsCount(); row++) {
            for (int col = 0; col < excludedColumn; col++) {
                subMatrix.set(row - 1, col, get(row, col));
            }
            for (int col = excludedColumn + 1; col < getColumnsCount(); col++) {
                subMatrix.set(row - 1, col - 1, get(row,col));
            }
        }

        return subMatrix;
    }

    /**
     * Returns determinant of the matrix`s minor by given index
     * @param excludedRow
     * @param excludedColumn
     * @return determinant of the matrix`s minor
     */
    public Double minorDeterminant(int excludedRow, int excludedColumn) {
        return minor(excludedRow, excludedColumn).determinant();
    }

    /**
     * Returns a matrix with elements that are the cofactors, term-by-term, of the current  matrix.
     *
     * @return
     */
    public DoubleMatrix cofactorMatrix() {
        DoubleMatrix cofactor = new DoubleMatrix(getRowsCount(), getColumnsCount());

        for (int row = 0; row < getRowsCount(); row +=2) {
            for (int col = 0; col < getColumnsCount(); col += 2) {
                cofactor.set(row, col, minorDeterminant(row, col));
            }
            for (int col = 1; col < getColumnsCount(); col += 2) {
                cofactor.set(row, col, -minorDeterminant(row, col));
            }
        }

        for (int row = 1; row < getRowsCount(); row +=2) {
            for (int col = 0; col < getColumnsCount(); col += 2) {
                cofactor.set(row, col, -minorDeterminant(row, col));
            }
            for (int col = 1; col < getColumnsCount(); col += 2) {
                cofactor.set(row, col, minorDeterminant(row, col));
            }
        }

        return cofactor;
    }

    /**
     * Returns inverse of the current matrix
     * @return inverse of the matrix or null if determinant equals zero
     */
    public DoubleMatrix inverse() {
        var det = determinant();

        if (det == 0) {
            return null;
        }

        var cofactors = cofactorMatrix();


        cofactors = cofactors.transposed();
        return cofactors.multiply(1 / det);
    }

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * @param row index of the row
     * @param col index of the column
     * @param value new value of any type extending Number. Automatically converted to double
     * @return replaced value (including null)
     */
    public <T extends  Number> Double set(int row, int col, T value) {
      return super.set(row, col, value.doubleValue());
    }


    private Double subDeterminant(int fromRow, TreeSet<Integer> included) {
        if (getRowsCount() - fromRow != included.size())
            throw new ArithmeticException("length of the matrix - fromRow must be equal to included`s size");

        if (included.size() == 2) {
            return productDifference(fromRow, included.first(), included.last());
        }

        Double result = 0d;

        int sign = 0;

        TreeSet<Integer> incl;

        for (Integer i : included) {
            if (get(fromRow, i) == 0)
                continue;

            incl = new TreeSet<>(included);
            incl.remove(i);

            double sub = subDeterminant(fromRow + 1, incl) * get(fromRow, i);
            result += sign++ % 2 == 0 ? sub : -sub;
        }

        return result;
    }
    private Double productDifference(int upperRow, int col1, int col2) {
        return get(upperRow,col1) * get(upperRow + 1, col2) -
                get(upperRow,col2) * get(upperRow + 1, col1);


    }


}
