package com.example.wnabudgetbackend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

/** Entry point for API Gateway/Lambda → Spring Boot */
public class StreamLambdaHandler implements RequestStreamHandler {

    /** one per Lambda execution environment – re-used on warm invocations */
    private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            // Application.class is your @SpringBootApplication main class
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(WnabudgetBackendApplication.class);
            // for HTTP API v2 use:
            // handler = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(Application.class);
        } catch (ContainerInitializationException e) {
            // force another cold-start if the Spring context failed to load
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    @Override
    public void handleRequest(InputStream input,
                              OutputStream output,
                              Context context) throws IOException {
        handler.proxyStream(input, output, context);
    }
}