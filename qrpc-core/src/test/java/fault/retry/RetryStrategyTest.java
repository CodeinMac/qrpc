package fault.retry;

import com.codeinmac.qrpc.fault.retry.NoRetryStrategy;
import com.codeinmac.qrpc.fault.retry.RetryStrategy;
import com.codeinmac.qrpc.model.RpcResponse;
import org.junit.Test;

/**
 * Test class for Retry Strategy
 * This class is used to test different retry strategies. It simulates retry mechanisms to check the behavior
 * in failure cases and ensures the retry logic functions as expected.
 */
public class RetryStrategyTest {

    RetryStrategy retryStrategy = new NoRetryStrategy();

    @Test
    public void doRetry() {
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("Testing retry mechanism");
                throw new RuntimeException("Simulated retry failure");
            });
            System.out.println(rpcResponse);
        } catch (Exception e) {
            System.out.println("Retry attempts failed");
            e.printStackTrace();
        }
    }
}
