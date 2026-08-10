package com.suryansh;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

public class LambdaHandler implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event event, Context context) {

        context.getLogger().log("S3 Event received");

        event.getRecords().forEach(record -> {
            String bucketName = record.getS3().getBucket().getName();
            String fileName = record.getS3().getObject().getKey();

            context.getLogger().log(
                "New file uploaded: " + fileName +
                " to bucket: " + bucketName
            );
        });

        return "S3 event processed successfully";
    }
}