import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceFactory {
    public static ExecutorService createServiceExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static ExecutorService createClientExecutor() {
        return Executors.newCachedThreadPool();
    }
}
