import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final ExecutorService serverExecutor = ExecutorServiceFactory.createServiceExecutor();
    private static final ExecutorService clientExecutor = ExecutorServiceFactory.createClientExecutor();

    public static void main(String[] args) {
        serverExecutor.submit(Main::runServer);
        Utils.sleep();

        System.out.print("Amount of client threads: ");
        startMultipleClients(Utils.getInt());

        clientExecutor.shutdown();
        serverExecutor.shutdown();
    }

    private static void startMultipleClients(int numClients) {
        for (int i = 0; i < numClients; i++) {
            clientExecutor.submit(Main::runClient);
        }
        Utils.sleep();
    }

    private static void runServer() {
        System.out.println("Server is running...");
        new Server().run();
    }

    private static void runClient() {
        System.out.println("Client is running...");
        new Client().run();
    }

    private static class Utils {
        private static final Scanner scanner = new Scanner(System.in);

        private static int getInt() {
            while (true) {
                try {
                    return scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Value is incorrect.");
                }
            }
        }

        private static void sleep() {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MatrixHelper {
        private static final Random random = new Random();
        private static final int MIN_VALUE = 1000;

        static int generateRandomMatrixSize() {
            return MIN_VALUE + random.nextInt(2);
        }

        static int[][] generateRandomMatrix(int rows, int cols) {
            int[][] matrix = new int[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = random.nextInt(10);
                }
            }

            return matrix;
        }

        static void printMatrix(int[][] matrix) {
            System.out.println(Arrays.deepToString(matrix));
        }

        static int[][] multiplyMatrices(int[][] firstMatrix, int[][] secondMatrix) {
            int[][] result = new int[firstMatrix.length][secondMatrix[0].length];

            for (int row = 0; row < result.length; row++) {
                for (int col = 0; col < result[row].length; col++) {
                    result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
                }
            }

            return result;
        }

        private static int multiplyMatricesCell(int[][] firstMatrix, int[][] secondMatrix, int row, int col) {
            int cell = 0;
            for (int i = 0; i < secondMatrix.length; i++) {
                cell += firstMatrix[row][i] * secondMatrix[i][col];
            }
            return cell;
        }
    }
}