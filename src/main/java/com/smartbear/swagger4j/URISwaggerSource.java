/**
 * Copyright 2013 SmartBear Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartbear.swagger4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * SwaggerSource for reading Swagger definitions from a base URI. The extension of the URI will be used to
 * decide on the format (.json / .xml)
 */

public class URISwaggerSource implements SwaggerSource {

    private final URI uri;

    public URISwaggerSource(URI uri) {
        this.uri = uri;
    }

    public static URI buildUri(URI uri, String path, SwaggerFormat format) {
        assert uri != null && path != null && format != null;

        String base = uri.toString();
        if (base.contains("?")) {
            base = base.substring(0, base.indexOf('?'));
        }

        path = path.replaceAll("\\{format\\}", format.getExtension());
        if (path.startsWith("/")) {
            base = base.substring(0, base.lastIndexOf('/'));
        }

        return URI.create(base + path);
    }

    public static SwaggerFormat extractFormat(URI uri) {
        String path = uri.getPath() == null ? uri.toString() : uri.getPath().toLowerCase();
        SwaggerFormat format = SwaggerFormat.json;

        if (path.endsWith(".xml")) {
            format = SwaggerFormat.xml;
        } else if (path.endsWith(".json")) {
            format = SwaggerFormat.json;
        } else if (path.contains(".xml/")) {
            format = SwaggerFormat.xml;
        }

        return format;
    }

    @Override
    public Reader readResourceListing() throws IOException {
        return new InputStreamReader(uri.toURL().openStream());
    }

    @Override
    public Reader readApiDeclaration(String basePath, String path) throws IOException {

        try {
            path = path.replaceAll("\\{format\\}", getFormat().getExtension());

            // absolute path?
            if (path.toLowerCase().startsWith("http://") || path.toLowerCase().startsWith("https://") ||
                path.toLowerCase().startsWith("file:")) {
                basePath = "";
            } else {

                if (basePath == null) {
                    basePath = uri.toString();
                } else if (!basePath.toLowerCase().startsWith("file:") && !basePath.contains("://")) {
                    String uriString = uri.toString();
                    if (basePath.equals(".")) {
                        basePath = "";
                    }

                    // find index to which the uriString should be used; depends on if the basePath is
                    // absolute or relative.
                    int ix = basePath.startsWith("/") ?
                        uriString.indexOf("/", uriString.indexOf(":") +
                            (uriString.startsWith("file:") ? 1 : 4)) :
                        uriString.lastIndexOf("/");

                    basePath = uriString.substring(0, ix) + basePath;
                }
            }

            URI uri = new URI(basePath + path);

            return new InputStreamReader(uri.toURL().openStream());
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    @Override
    public SwaggerFormat getFormat() {
        return extractFormat(uri);
    }
}
