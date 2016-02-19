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
 * An interface that extends the stream interface with features for closing/finishing the stream (for internal use
 * only).
 *
 * @author Stefan Bangels
 * @see Stream
 * @since 2015-07-10
 */
interface ClosableStream extends Stream {

    /**
     * Finish the stream and report to the requester that stream was successfully handled, meaning: the request was
     * successfully read from the stream, processed without any problems, and the response was successfully written to
     * the stream. After calling this method, reading/writing from/to the stream will no longer be possible.
     */
    void finishSuccess();

    /**
     * Finish the stream and report to the requester that there were errors while handling the stream (errors during
     * reading from stream, processing the stream or writing to the stream).
     *
     * @param code    the error code
     * @param message the error message
     */
    void finishError(int code, String message);

}
