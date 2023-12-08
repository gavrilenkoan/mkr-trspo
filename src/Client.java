import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Runnable {
    private static final int SERVER_PORT = 8080;
    private static final String SERVER_ADDRESS = "127.0.0.1";

    @Override
    public void run() {
        int M = Main.MatrixHelper.generateRandomMatrixSize();
        int N = Main.MatrixHelper.generateRandomMatrixSize();
        int L = Main.MatrixHelper.generateRandomMatrixSize();

        DataContainer requestPayloadA = new DataContainer(Main.MatrixHelper.generateRandomMatrix(M, N));
        DataContainer requestPayloadB = new DataContainer(Main.MatrixHelper.generateRandomMatrix(N, L));

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {

            sendMatrixRequest(outputStream, requestPayloadA);
            sendMatrixRequest(outputStream, requestPayloadB);

            DataContainer response = receiveMatrixResponse(inputStream);

            if (!response.getError().isEmpty()) {
                System.out.println("Error:\n" + response.getError());
                return;
            }

            Main.MatrixHelper.printMatrix(response.getMatrix());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendMatrixRequest(ObjectOutputStream outputStream, DataContainer requestPayload) throws IOException {
        outputStream.writeObject(requestPayload);
    }

    private DataContainer receiveMatrixResponse(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        return (DataContainer) inputStream.readObject();
    }
}