package com.roquentin9700;
import java.util.Arrays;

public abstract class Matrix<T> {

    /**
     *
     * @param rows
     * @param columns
     *
     * @throws IllegalArgumentException if number of rows or columns is lss than 2
     */
    @SuppressWarnings("uncheked")
    public Matrix(int rows, int columns) {
        if (rows < 2 || columns < 2)
            throw new IllegalArgumentException("The side size cannot be less than 2");
        matrix = (T[][])new Object[rows][columns];
    }

    public Matrix(int rows, int columns, T... elements) {
        this(rows, columns);

        for (int i = 0; i < elements.length; i++) {
            int row = i / columns;
            int column = i % columns;

            matrix[row][column] = elements[i];
        }
    }

    public Matrix(Matrix<T> prototype) {
        matrix = prototype.matrix;
    }

    protected T[][] matrix;

    /**
     * Returns number of the rows
     * @return number of the rows
     */
    public int getRowsCount() {
        return matrix.length;
    }

    /**
     * Returns number of the columns
     * @return number of the columns
     */
    public int getColumnsCount() {
        if (matrix.length == 0)
            return 0;
        return matrix[0].length;
    }

    /**
     *  Returns the element at the specified position
     * @param row index of the row
     * @param col index of the column
     * @return value at the given matrix cell
     */
    public T get(int row, int col) {
        return matrix[row][col];
    }

    /**
     * Inserts the specified element at the specified position in this list
     *
     * @param row index of the row
     * @param col index of the column
     * @param value new value
     * @return replaced value (including null)
     */
    public T set(int row, int col, T value) {
        T old = get(row, col);
        matrix[row][col] = value;
        return old;
    }


    /**
     * Returns copy of the current matrix.
     * @return the copy of the matrix
     */
    abstract Matrix<T> getCopy();

    /**
     * Creates and returns new Matrix of the given size
     * @param rows number of the rows
     * @param columns number of the columns
     * @return new matrix of the given size
     */
    abstract Matrix<T> getMatrix(int rows, int columns);

    /**
     * Returns representation of current Matrix transposed along the main diagonal
     *
     *  1 1 1        1 2 3
     *  2 2 2   ==>  1 2 3
     *  3 3 3        1 2 3
     *
     * @return transposed representation of the current Matrix
     */
    @SuppressWarnings("unchecked")
    public final  <M extends Matrix<T>> M transposed() {
        var newMatrix = getMatrix(getColumnsCount(), getRowsCount());
        for (int i = 0; i < getRowsCount(); i++) {
            for (int j = 0; j < getColumnsCount(); j++) {
                newMatrix.matrix[j][i] = matrix[i][j];
            }
        }

        return (M) newMatrix;
    }
    /**
     * Returns representation of current Matrix transposed along the side diagonal
     *
     *  1 1 -1        -3 -2 -1
     *  2 2 -2   ==>   3  2  1
     *  3 3 -3         3  2  1
     *
     * @return transposed representation of the current Matrix
     */
    @SuppressWarnings("unchecked")
    public final  <M extends Matrix<T>> M transposedBySideDiagonal(){
        var newMatrix = getMatrix(getColumnsCount(), getRowsCount());
        for (int i = 0; i < getRowsCount(); i++) {
            for (int j = 0; j < getColumnsCount(); j++) {
                newMatrix.matrix[getColumnsCount() - 1 - j][getRowsCount() - 1 -i] = matrix[i][j];
            }
        }
        return (M) newMatrix;
    }

    /**
     * Returns representation of current Matrix transposed along the vertical line
     *
     *  1 2 3        3 2 1
     *  1 2 3   ==>  3 2 1
     *  1 2 3        3 2 1
     *
     * @return transposed representation of the current Matrix
     */
    @SuppressWarnings("unchecked")
    public final <M extends Matrix<T>> M transposedByVerticalLine() {
        var newMatrix = getCopy();
        for (int i = 0; i < getRowsCount(); i++) {
            for (int j = 0; j < getColumnsCount(); j++) {
                newMatrix.matrix[i][getColumnsCount() - 1 - j] = matrix[i][j];
            }
        }
        return (M) newMatrix;
    }

    /**
     * Returns representation of current Matrix transposed along the main diagonal
     *
     *  1 1 1        3 3 3
     *  2 2 2   ==>  2 2 2
     *  3 3 3        1 1 1
     *
     * @return transposed representation of the current Matrix
     */
    @SuppressWarnings("unchecked")
    public final <M extends Matrix<T>> M transposedByHorizontalLine() {
        var newMatrix = getCopy();
        for (int i = 0; i < getRowsCount(); i++) {
            for (int j = 0; j < getColumnsCount(); j++) {
                newMatrix.matrix[getRowsCount() - 1 - i][j] = matrix[i][j];
            }
        }
        return (M) newMatrix;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Object[] row : matrix) {
            hash += Arrays.hashCode(row);
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix<?>))
            return false;

        Matrix<T> matrix = (Matrix<T>) obj;

        if (matrix.getRowsCount() != getRowsCount() || matrix.getColumnsCount() != getColumnsCount())
            return false;

        for (int row = 0; row < getRowsCount(); row++) {
            if (!Arrays.equals(matrix.matrix[row], this.matrix[row]))
                return false;
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (T[] row : matrix) {
            for (T m: row) {
                if (m == null)
                    sb.append("null ");
                else
                    sb.append(m.toString()).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
