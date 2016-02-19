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
import org.assertdevelopments.promise.poc.server.StreamHandler;

import javax.servlet.annotation.WebServlet;

/**
 * @author Stefan Bangels
 * @since 2016-02-19
 */
@WebServlet(name="SamplesServlet", urlPatterns = "/ws/*")
public final class SamplesServlet extends AbstractStreamServlet {

    @Override
    protected StreamHandler getStreamHandler(String uri) {
        if ("/bytes".equals(uri)) {
            return new BytesStreamHandler();
        } else if ("/entity".equals(uri)) {
            return new EntityStreamHandler();
        } else if ("/objects".equals(uri)) {
            return new ObjectStreamHandler();
        } else if ("/client-error".equals(uri)) {
            return new ClientErrorStreamHandler();
        } else if ("/server-error".equals(uri)) {
            return new ServerErrorStreamHandler();
        }
        return null;
    }

}
