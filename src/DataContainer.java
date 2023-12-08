import java.io.Serial;
import java.io.Serializable;

public class DataContainer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int[][] matrix;
    private final String error;

    public DataContainer(int[][] matrix) {
        this(matrix, "");
    }

    public DataContainer(int[][] matrix, String error) {
        this.matrix = matrix;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public int[][] getMatrix() {
        return matrix;
    }
}