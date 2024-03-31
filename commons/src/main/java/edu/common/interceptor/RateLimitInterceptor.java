package edu.common.interceptor;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {

    private static final int BUCKET_SIZE = 100;

    private static final String QUOTA_EXHAUSTED_MESSAGE =
        "Your API request quota has been exhausted. Retry after %d seconds";

    private final Map<String, Bucket> rateLimitingCache = new HashMap<>();

    @Override
    @SuppressWarnings("MagicNumber")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {
        String ip = request.getRemoteAddr();
        Bucket bucket = rateLimitingCache.get(ip);
        if (bucket == null) {
            bucket = createNewBucket();
            rateLimitingCache.put(ip, bucket);
        }

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long secondsUntilBucketRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(secondsUntilBucketRefill));
            response.sendError(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                QUOTA_EXHAUSTED_MESSAGE.formatted(secondsUntilBucketRefill)
            );
            return false;
        }
    }

    private Bucket createNewBucket() {
        Bandwidth bandwidth = Bandwidth.simple(BUCKET_SIZE, Duration.of(1, ChronoUnit.MINUTES));
        return Bucket.builder().addLimit(bandwidth).build();
    }
}
