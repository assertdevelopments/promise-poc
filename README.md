# Promise POC

Welcome to Promise POC!

Promise POC is a proof of concept for the Promise Framework (https://github.com/assertdevelopments/promise). Promise is a JAX-RS extension that allow you to stream large as well as small streams of data in a memory efficient, time optimized and controlled way.

The goals of this proof of concept are:
- prove that the solution works and that the framework goals are realistic
- test the RESTfulness of the solution
- compare the differences between traditional JAX-RS serialization and the Promise Framework
- identify problems and blocking issues

Important Note: This proof of concept does not depend on JAX-RS. We chose to build a very low level POC (on top of servlets) so we can study the effects on the HTTP Protocol better.

The results of this proof of concept will be bundled in an article and will be used as guidelines for building the Promise Framework.

# Building

To build this project, you will need Maven 3 (or higher) and JDK 1.8.

Run the following command:

```
mvn clean package
```

# Usage

## Stream Server

The following code will create a stream server. When a client connects, it will first read the request body from the input stream. When all data is received, it will write the response body to the output stream.

``` java
@WebServlet(name = "ExampleServlet", urlPatterns = "/example")
public class ExampleStreamServlet extends AbstractStreamServlet {

    @Override
    protected StreamHandler getStreamHandler(String uri) {
        return (httpMethod, stream) -> {
            // read request
            BufferedReader in = new BufferedReader(new InputStreamReader(stream.getInputStream()));
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }

            // write response
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream.getOutputStream()));
            for (int n = 0; n < 1000000; n++) {
                writer.write("test-" + n + "\n");
            }
            writer.flush();
        };
    }

}
```

## Stream Client

The following code will create a stream client. It will connect to the stream server and write the request body to the output stream. After all data is sent, the server response is being read from the server. 

``` java
StreamClient client = new StreamClient();
try {
    StreamRequest request = outputStream -> {
        // write request
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        for (int n = 0; n < 1000000; n++) {
            writer.write("test-" + n + "\n");
        }
        writer.flush();
    };

    StreamResponse response = client.sendRequest("http://localhost:8080/example", request);
    try {
        // read response
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getInputStream()));
        while (true) {
            String line = in.readLine();
            if (line == null) {
                break;
            }
            System.out.println(line);
        }
    } finally {
        response.close();
    }
} finally {
    client.close();
}
```

## Error Handling

Todo

## Entities

Todo


# Samples

Download and install WildFly 10.

Deploy **promise-poc-c.v-SNAPSHOT.war** to the Wild Fly application server.

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


