/*
 * Copyright 2015 Assert Developments
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.assertdevelopments.promise.poc.samples.server;

import org.assertdevelopments.promise.poc.server.AbstractStreamServlet;
import org.assertdevelopments.promise.poc.server.Stream;

import javax.servlet.annotation.WebServlet;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

/**
 * @author Stefan Bangels
 * @since 2014-10-23
 */
@WebServlet(name = "DownloadStreamServlet", urlPatterns = "/ws/download")
public final class DownloadStreamServlet extends AbstractStreamServlet {

    public void handleStreamRequest(Stream stream) throws Throwable {
        // write response
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream.getOutputStream()));
        for (int n = 0; n < 500000; n++) {
            writer.write("test-" + n + "\n");
        }
        writer.flush();
    }

}
