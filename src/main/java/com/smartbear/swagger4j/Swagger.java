/**
 *  Copyright 2013 SmartBear Software, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.smartbear.swagger4j;

import com.smartbear.swagger4j.impl.SwaggerFactoryImpl;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

import static java.util.ServiceLoader.load;

/**
 * Utility methods to read/write/create Swagger objects
 */

public class Swagger {

    /**
     * Avoid instantiation
     */

    private Swagger() {
    }

    /**
     * Creates an empty ResourceListing with the specified basePath - uses the standard SwaggerFactory
     *
     * @param swaggerVersion the Swagger version to use
     * @return an empty ResourceListing
     */

    public static ResourceListing createResourceListing(SwaggerVersion swaggerVersion) {
        return createSwaggerFactory().createResourceListing(swaggerVersion);
    }

    /**
     * Creates an empty ApiDeclaration with the specified basePath and resourcePath
     *
     * @param basePath used to resolve API paths defined in this declaration
     * @param resourcePath path to the actual resource described in the declaration
     * @return an empty ApiDeclaration
     */

    public static ApiDeclaration createApiDeclaration(String basePath, String resourcePath) {
        return createSwaggerFactory().createApiDeclaration(basePath, resourcePath);
    }

    /**
     * Creates a SwaggerReader using the available SwaggerFactory
     *
     * @return a SwaggerReader
     */

    public static SwaggerReader createReader() {
        return createSwaggerFactory().createSwaggerReader();
    }

    /**
     * Reads a Swagger definition from the specified URI, uses the default SwaggerReader implementation
     *
     * @param uri the URI of the api-docs document defining the Swagger ResourceListing
     * @return the initialized ResourceListing object
     * @throws IOException
     */
    public static ResourceListing readSwagger( URI uri ) throws IOException {
        return createReader().readResourceListing( uri );
    }

    /**
     * Writes the specified Swagger ResourceListing to the specified local path in json format. Uses the default SwaggerWriter
     *
     * @param resourceListing the resourceListing to write
     * @param path path to an existing folder where the api-docs and api declarations will be written
     * @throws IOException
     */

    public static void writeSwagger( ResourceListing resourceListing, String path ) throws IOException {
        writeSwagger( resourceListing, path, SwaggerFormat.json );
    }

    /**
     * Writes the specified Swagger ResourceListing to the specified local path in either json or xml format.
     * Uses the default SwaggerWriter
     *
     * @param resourceListing the resourceListing to write
     * @param path path to an existing folder where the api-docs and api declarations will be written
     * @param format the format to use; either json or xml
     * @throws IOException
     */

    public static void writeSwagger( ResourceListing resourceListing, String path, SwaggerFormat format ) throws IOException {
        createWriter( format ).writeSwagger( new FileSwaggerStore( path ), resourceListing );
    }

    /**
     * Creates a SwaggerWriter for the specified format using the default SwaggerFactory
     *
     * @param format the format the writer should use, either json or xml
     * @return the created SwaggerWriter
     */

    public static SwaggerWriter createWriter(SwaggerFormat format) {
        return createSwaggerFactory().createSwaggerWriter(format);
    }

    /**
     * method for creating a SwaggerFactory; uses java.util.ServiceLoader to find a SwaggerFactory
     * implementation - falls back to the default implementation if none are found
     *
     * @return a SwaggerFactory instance
     */

    public static SwaggerFactory createSwaggerFactory() {
        Iterator<SwaggerFactory> iterator = load(SwaggerFactory.class).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }

        return new SwaggerFactoryImpl();
    }
}
