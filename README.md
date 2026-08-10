# 🚀 AWS Lambda S3 Event Handler

A Java-based **AWS Lambda function** that listens to **Amazon S3 events** and logs details about newly uploaded files — bucket name and object key — using the AWS Lambda runtime.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Dependencies](#dependencies)
- [Build](#build)
- [Deploy to AWS Lambda](#deploy-to-aws-lambda)
- [How It Works](#how-it-works)
- [Example S3 Event](#example-s3-event)
- [Expected Output](#expected-output)

---

## Overview

This project demonstrates a minimal but production-ready AWS Lambda handler written in **Java 21**. When a file is uploaded to an S3 bucket, Lambda triggers this handler, which:

1. Receives the `S3Event` payload
2. Iterates over all event records
3. Logs the **bucket name** and **file name (object key)** for each upload
4. Returns a success message

---

## Project Structure

```
Lamda/
└── lambda-demo/
    ├── pom.xml                          # Maven build configuration
    └── src/
        └── main/
            └── java/
                └── com/
                    └── suryansh/
                        └── LambdaHandler.java   # Core Lambda handler
```

---

## Prerequisites

| Tool | Version |
|------|---------|
| Java | 21+ |
| Apache Maven | 3.6+ |
| AWS CLI | Configured with appropriate IAM permissions |
| AWS Account | With Lambda & S3 access |

---

## Dependencies

Declared in [`pom.xml`](lambda-demo/pom.xml):

| Dependency | Version | Purpose |
|---|---|---|
| `aws-lambda-java-core` | 1.2.3 | Lambda `Context` and `RequestHandler` interfaces |
| `aws-lambda-java-events` | 3.11.5 | Strongly-typed `S3Event` and record models |

The build uses the **Maven Shade Plugin** (`3.5.3`) to produce a fat/uber JAR — bundling all dependencies into a single deployable artifact.

---

## Build

```bash
cd lambda-demo
mvn clean package
```

The shaded JAR will be generated at:

```
lambda-demo/target/lambda-demo-1.0.0.jar
```

---

## Deploy to AWS Lambda

### 1. Create the Lambda Function

```bash
aws lambda create-function \
  --function-name s3-event-handler \
  --runtime java21 \
  --role arn:aws:iam::<YOUR_ACCOUNT_ID>:role/<YOUR_LAMBDA_ROLE> \
  --handler com.suryansh.LambdaHandler::handleRequest \
  --zip-file fileb://lambda-demo/target/lambda-demo-1.0.0.jar \
  --timeout 30 \
  --memory-size 512
```

### 2. Add S3 Trigger Permission

```bash
aws lambda add-permission \
  --function-name s3-event-handler \
  --principal s3.amazonaws.com \
  --statement-id s3-trigger \
  --action lambda:InvokeFunction \
  --source-arn arn:aws:s3:::<YOUR_BUCKET_NAME>
```

### 3. Configure S3 Bucket Notification

```bash
aws s3api put-bucket-notification-configuration \
  --bucket <YOUR_BUCKET_NAME> \
  --notification-configuration '{
    "LambdaFunctionConfigurations": [{
      "LambdaFunctionArn": "arn:aws:lambda:<REGION>:<ACCOUNT_ID>:function:s3-event-handler",
      "Events": ["s3:ObjectCreated:*"]
    }]
  }'
```

### 4. Update an Existing Function

```bash
aws lambda update-function-code \
  --function-name s3-event-handler \
  --zip-file fileb://lambda-demo/target/lambda-demo-1.0.0.jar
```

---

## How It Works

```java
public class LambdaHandler implements RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event event, Context context) {
        context.getLogger().log("S3 Event received");

        event.getRecords().forEach(record -> {
            String bucketName = record.getS3().getBucket().getName();
            String fileName   = record.getS3().getObject().getKey();

            context.getLogger().log(
                "New file uploaded: " + fileName +
                " to bucket: " + bucketName
            );
        });

        return "S3 event processed successfully";
    }
}
```

| Component | Description |
|---|---|
| `RequestHandler<S3Event, String>` | Typed interface — input is an S3 event, output is a String |
| `S3Event` | AWS-provided POJO representing one or more S3 notifications |
| `Context` | Provides Lambda metadata and a CloudWatch-backed logger |
| `getRecords()` | Returns all S3 records in the event (batch-safe) |

---

## Example S3 Event

```json
{
  "Records": [
    {
      "s3": {
        "bucket": {
          "name": "my-upload-bucket"
        },
        "object": {
          "key": "uploads/photo.png"
        }
      }
    }
  ]
}
```

---

## Expected Output

Logs visible in **Amazon CloudWatch Logs** (`/aws/lambda/s3-event-handler`):

```
S3 Event received
New file uploaded: uploads/photo.png to bucket: my-upload-bucket
```

Lambda return value:

```
S3 event processed successfully
```

---

## 📄 License

This project is open-source and available for personal and educational use.
