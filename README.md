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


# License

Copyright 2016 Assert Developments

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


