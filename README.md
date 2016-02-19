# Promise POC

Welcome to Promise POC!

With this experiment, we will try to expose a missed opportunity in the architecture of JAX-RS. I will use it to discover why certain design decisions were made and why things were built the way they are.

The results of this experiment will be summarised in an article, and will form the foundations of Promise, a framework that adds advanced streaming capabilities to JAX-RS. Its goal is to lower memory consumption and response time and to provide error handling.


# Building

To build this project, you will need Maven 3 (or higher) and JDK 1.8.

Run the following command:

```
mvn clean package
```

# Deployment & Testing

Download and install WildFly 10.

Deploy **promise-poc-samples-c.v-SNAPSHOT.war** to the Wild Fly application server.

Try to run the samples in org.assertdevelopments.promise.poc.samples.client.



