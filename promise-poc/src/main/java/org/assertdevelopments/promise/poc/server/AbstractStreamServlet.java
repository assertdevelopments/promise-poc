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

import org.assertdevelopments.promise.poc.core.protocol.StreamConstants;
import org.assertdevelopments.promise.poc.core.protocol.status.StreamStatus;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An abstract base class for servlets that do promise stream processing.
 *
 * @author Stefan Bangels
 * @since 2015-01-20
 */
public abstract class AbstractStreamServlet extends HttpServlet {

    private static final String SERVER = "Promise Server/1.0";
    private static final String DOWNLOAD_FILE_NAME = "data.stream";
    private static final String FULL_DUPLEX_INIT_PARAMETER = "org.assertdevelopments.promise.fullDuplex";

    private final Logger logger = Logger.getLogger(getClass());

    private boolean fullDuplex = false;

    @Override
    public void init(ServletConfig config) throws ServletException {
        initFullDuplex(config);
    }

    /**
     * Read the full duplex setting from the servlet configuration. This can be configured by passing the servlet init
     * parameter "org.assertdevelopments.stream.fullDuplex" with value "true" or "false". If the full duplex parameter
     * is set to "true", the servlet will support processing full duplex streams (reading and writing at the same time).
     * If it is set to "false", the servlet will only support half duplex streams (read first, process next and write
     * last). Please note that full duplex streams are experimental, since they are not supported by the current HTTP
     * specifications (version 1.1).
     *
     * @param config the servlet configuration
     * @see #isFullDuplex()
     */
    private void initFullDuplex(ServletConfig config) {
        fullDuplex = "true".equals(config.getInitParameter(FULL_DUPLEX_INIT_PARAMETER));
        if (fullDuplex) {
            logger.warn("full duplex is enabled (experimental)");
        }
    }

    /**
     * Returns true if this servlet will support processing full duplex streams (reading and writing at the same time),
     * or false when the servlet will only support half duplex streams (read first, process next and write last). This
     * can be configured by passing the servlet init parameter "org.assertdevelopments.stream.fullDuplex" with value
     * "true" or "false". Please note that full duplex streams are experimental, since they are not supported by the
     * current HTTP specifications (version 1.1).
     *
     * @return true when full duplex stream support is enabled
     */
    private boolean isFullDuplex() {
        return fullDuplex;
    }

    @Override
    protected final void service(
            HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // start timer
            long time = System.currentTimeMillis();

            // fetch uri from request
            String uri = getRelativeURI(request);
            String method = request.getMethod();
            logger.info("processing stream request " + method + " " + uri + "...");

            // set request http headers
            response.setHeader("Server", SERVER);
            response.setHeader("Accepts", StreamConstants.CONTENT_TYPE);
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");

            // find a stream handler for the uri
            StreamHandler handler = getStreamHandler(uri);
            if (handler == null) {
                logger.warn("aborting, no stream handler found for uri: " + uri);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // check content type
            String contentType = request.getContentType();
            if (!StreamConstants.CONTENT_TYPE.equals(contentType)) {
                logger.warn("aborting, unsupported content type: " + contentType);
                response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                return;
            }

            // set response http headers
            response.setHeader("Content-Type", StreamConstants.CONTENT_TYPE);
            response.setHeader("Content-Disposition", "attachment; filename=" + DOWNLOAD_FILE_NAME);

            // change the http status code to accepted (status code 202)
            // beyond this point, the request has been accepted by the server
            // all exceptions will be reported in the representation body, not as an http error
            // because once we start writing to the http output stream, we can no longer change the http status code
            logger.debug("accepted stream request");
            response.setStatus(HttpServletResponse.SC_ACCEPTED);

            // handle stream request
            logger.debug("handling stream request...");
            handleRequest(request, response, handler);

            logger.info("processed stream request in " + (System.currentTimeMillis() - time) + "ms.");
        } catch (Throwable t) {
            logger.error("error while processing stream request", t);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the relative URI of a request (this will remove the servlet path from the request URI, and will return
     * the URI relative to the servlet path).
     *
     * @param request the request
     * @return the relative URI
     */
    private String getRelativeURI(HttpServletRequest request) {
        String servletUri = request.getRequestURI().substring(request.getContextPath().length());
        return servletUri.substring(request.getServletPath().length());
    }

    /**
     * Handle a request with the provided stream handler. This method will instantiate a half duplex or full duplex
     * stream (depending on the servlet configuration), that wraps the request input stream and response output stream
     * and passes it to the provided stream handler. The stream handler will read the request from the stream,
     * process it and write the response back to the stream. After handling the stream, the stream will be finished,
     * and a status code will be sent (success status when the stream is handled successfully, or error status if
     * an exception has occurred while handling the stream).
     *
     * @param request  the request
     * @param response the response
     * @param handler  the stream handler
     * @see StreamHandler
     * @see Stream
     */
    private void handleRequest(
            HttpServletRequest request, HttpServletResponse response, StreamHandler handler) {
        try {
            ClosableStream stream = createStream(request.getInputStream(), response.getOutputStream());
            try {
                handler.handleStream(request.getMethod(), stream);
                stream.finishSuccess();
            } catch (Throwable t) {
                logger.error("error while handling stream", t);
                stream.finishError(StreamStatus.STATUS_ERROR, t.getMessage());
            }
        } catch (Throwable t) {
            logger.error("an unexpected error has occurred", t);
        }
    }

    /**
     * Instantiate a half duplex or full duplex stream (depending on the servlet configuration), and wrap it around
     * the provided input- and output stream.
     *
     * @param inputStream  the input stream
     * @param outputStream the output stream
     * @return the stream
     * @see #isFullDuplex()
     * @see ClosableStream
     */
    private ClosableStream createStream(InputStream inputStream, OutputStream outputStream) {
        if (isFullDuplex()) {
            return new FullDuplexStream(inputStream, outputStream);
        } else {
            return new HalfDuplexStream(inputStream, outputStream);
        }
    }

    /**
     * Get the stream handler for the provided URI (relative to the servlet).
     *
     * @param uri the URI (relative to the servlet)
     * @return the stream handler
     * @see StreamHandler
     * @see #getRelativeURI(HttpServletRequest)
     */
    protected abstract StreamHandler getStreamHandler(String uri);

}
