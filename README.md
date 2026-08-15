# AWS Lambda S3 Event Handler

A small Java 21 AWS Lambda example that receives an Amazon S3 event, reads the uploaded object's bucket name and key, logs them, and returns a success message.

## Project Structure

```text
LamdaHandler/
├── .gitignore
├── README.md
└── lambda-demo/
    ├── pom.xml
    └── src/
        └── main/
            └── java/
                └── com/
                    └── suryansh/
                        └── LambdaHandler.java
```

## What This Project Contains

The repository currently contains a single Lambda handler and its Maven build configuration.

`LambdaHandler.java` implements:

```java
RequestHandler<S3Event, String>
```

When Lambda receives an S3 event, the handler:

1. Logs that an S3 event was received.
2. Iterates through the event records.
3. Reads the S3 bucket name.
4. Reads the uploaded object key.
5. Logs the uploaded file information.
6. Returns `S3 event processed successfully`.

## Maven Configuration

The project uses Java 21 and Maven.

### Main dependencies

| Dependency | Version | Purpose |
|---|---:|---|
| `aws-lambda-java-core` | `1.2.3` | AWS Lambda `Context` and `RequestHandler` APIs |
| `aws-lambda-java-events` | `3.11.5` | AWS event classes including `S3Event` |
|

The project also uses the Maven Shade Plugin `3.5.3` to package dependencies into the generated JAR.

## Build

From the `lambda-demo` directory:

```bash
mvn clean package
```

The packaged JAR is generated in:

```text
lambda-demo/target/
```

## Handler

The Lambda handler class is:

```text
com.suryansh.LambdaHandler
```

For AWS Lambda configuration, the handler method is:

```text
com.suryansh.LambdaHandler::handleRequest
```

## Example Event

The handler expects an AWS S3 event containing one or more records. A simplified example is:

```json
{
  "Records": [
    {
      "s3": {
        "bucket": {
          "name": "my-bucket"
        },
        "object": {
          "key": "uploads/example.txt"
        }
      }
    }
  ]
}
```

## Example Log Output

For the event above, the handler logs information similar to:

```text
S3 Event received
New file uploaded: uploads/example.txt to bucket: my-bucket
```

## Notes

This repository is a learning/demo project for understanding a Java AWS Lambda handler with Amazon S3 events and Maven packaging.

The repository does not currently include infrastructure-as-code, automated deployment workflows, or additional AWS configuration files.
