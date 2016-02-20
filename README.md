# Promise POC

Welcome to Promise POC!

Promise POC is a proof of concept for the Promise HTTP Streaming Framework. Promise is a JAX-RS extension that allow you to stream large and small streams of data in a memory efficient, time optimized and controlled way.

The goals of this proof of concept are:
- prove that the solution works
- assert that the framework goals are realistic
- assert the RESTfulness of the solution
- detect difficulties and blocking issues early
- compare the differences between JAX-RS serialization and the Promise Framework

Important Note: This proof of concept does not depend on JAX-RS. We chose to build a very low level POC (on top of servlets) so we can study the effects on the HTTP Protocol better.

The results of this proof of concept will be bundled in an article and will be used as guidelines for building the Promise Framework.

# Building

To build this project, you will need Maven 3 (or higher) and JDK 1.8.

Run the following command:

```
mvn clean package
```

# Deployment & Testing

Download and install WildFly 10.

Deploy **promise-poc-samples-c.v-SNAPSHOT.war** to the Wild Fly application server.

Try to run the samples in **org.assertdevelopments.promise.poc.samples.client** package.


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


