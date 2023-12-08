import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private static final int SERVER_PORT = 8080;

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is listening on port " + SERVER_PORT);

            ExecutorService requestExecutorService = Executors.newCachedThreadPool();

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    requestExecutorService.execute(() -> processClientRequest(clientSocket));
                } catch (IOException e) {
                    handleServerException("Error appeared while accepting client connection", e);
                }
            }
        } catch (IOException e) {
            handleServerException("Error appeared while creating server socket", e);
        }
    }

    private void processClientRequest(Socket clientSocket) {
        try (
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            DataContainer requestPayload1 = (DataContainer) inputStream.readObject();
            DataContainer requestPayload2 = (DataContainer) inputStream.readObject();

            if (!requestPayload1.getError().isEmpty() || !requestPayload2.getError().isEmpty()) {
                handleServerException(requestPayload1.getError(), outputStream);
                handleServerException(requestPayload2.getError(), outputStream);
                return;
            }

            if (requestPayload1.getMatrix().length != requestPayload2.getMatrix()[0].length) {
                handleServerException("Error appeared while multiplying matrices", outputStream);
                return;
            }

            int[][] resultMatrix = Main.MatrixHelper.multiplyMatrices(requestPayload1.getMatrix(), requestPayload2.getMatrix());
            outputStream.writeObject(new DataContainer(resultMatrix));

        } catch (IOException | ClassNotFoundException e) {
            handleServerException("Error appeared while handling client request", e);
        } finally {
            closeClientSocket(clientSocket);
        }
    }

    private void handleServerException(String errorMessage, ObjectOutputStream outputStream) throws IOException {
        System.out.println("Error:\n" + errorMessage);
        outputStream.writeObject(new DataContainer(new int[0][0], errorMessage));
    }

    private void closeClientSocket(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            handleServerException("Error appeared while closing client socket", e);
        }
    }

    private void handleServerException(String message, Exception e) {
        System.err.println("Error:\n" + message);
        e.printStackTrace();
    }
}