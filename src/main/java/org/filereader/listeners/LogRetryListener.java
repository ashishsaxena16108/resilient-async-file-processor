package org.filereader.listeners;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class LogRetryListener extends RetryListenerSupport {

    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        int attempt = context.getRetryCount();
        String methodName = context.getAttribute(RetryContext.NAME) != null ?
                context.getAttribute(RetryContext.NAME).toString() : "DB Write";

        // This prints a high-visibility warning every time Hikari chokes and forces a backoff
        System.err.println("⚠️ [RETRY WARNING] " + methodName + " failed on attempt #" + attempt +
                ". Backing off... Reason: " + throwable.getClass().getSimpleName());

        super.onError(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        if (context.getRetryCount() > 0 && throwable == null) {
            // This prints the victory lap when a previously failing task finally succeeds
            System.out.println("✅ [RETRY SUCCESS] A task successfully recovered after " +
                    context.getRetryCount() + " failed attempts!");
        }
        super.close(context, callback, throwable);
    }
}
