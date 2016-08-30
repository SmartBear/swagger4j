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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Simple file-based implementation of the SwaggerStore interface - for writing Swagger definitions
 * to the local file system
 */

public class FileSwaggerStore implements SwaggerStore {
    private final String path;
    private String apiDocsPath;

    public FileSwaggerStore(String path) throws IOException {
        this.path = path;

        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new IOException("Failed to create path [" + path + "] for file storage");
            }
        }
    }

    public Writer createResource(String path) throws IOException {

        // make sure directory exists
        if (path.indexOf('/') > 0) {
            String nm = path.substring(0, path.lastIndexOf('/'));
            File f = new File(nm, this.path);
            if (!f.exists() || !f.isDirectory()) {
                if (!f.mkdirs()) {
                    throw new IOException("Failed to create path [" + path + "] for file storage");
                }
            }
        }

        File file = new File(this.path, path);
        if( path.equals( "/api-docs") || path.equals( "api-docs"))
            apiDocsPath = file.getAbsolutePath();

        return new FileWriter(file);
    }

    /**
     * Gets the path of the generated api-docs root definition
     *
     * @return the path of the generated api-docs root definition
     */

    public String getApiDocsPath() {
        return apiDocsPath;
    }

    public static String writeSwagger(String path, ResourceListing resourceListing, SwaggerFormat format) throws IOException {
        FileSwaggerStore store = new FileSwaggerStore(path);
        Swagger.createWriter(format).writeSwagger(store, resourceListing);
        return store.getApiDocsPath();
    }
}
