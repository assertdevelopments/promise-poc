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

package org.assertdevelopments.promise.poc.server;

/**
 * A base interface for representing stream handlers.
 *
 * @author Stefan Bangels
 * @see Stream
 * @since 2015-01-20
 */
public interface StreamHandler {

    /**
     * Process the provided stream (read the stream input, process it and write the stream output). All exceptions
     * thrown by this handler, will be included in the stream output status.
     *
     * @param httpMethod the request method
     * @param stream     the stream
     * @see Stream
     */
    void handleStream(String httpMethod, Stream stream) throws Throwable;

}
