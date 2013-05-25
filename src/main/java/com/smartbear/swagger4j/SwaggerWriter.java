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

import java.io.IOException;
import java.io.Writer;

/**
 * Writes Swagger objects to writers or a generic SwaggerStore
 */

public interface SwaggerWriter {

    /**
     * Writes the specified ApiDeclaration to the specified Writer in the configured format
     *
     * @param declaration the declaration to write
     * @param writer the writer to write to
     * @throws IOException
     */

    public void writeApiDeclaration(ApiDeclaration declaration, Writer writer) throws IOException;

    /**
     * Writes the specified ResourceListing to the specified Writer in the configured format
     *
     * @param listing the listing to write
     * @param writer the writer to write to
     * @throws IOException
     */

    public void writeResourceListing(ResourceListing listing, Writer writer) throws IOException;

    /**
     * Gets the format of this SwaggerWriter
     *
     * @return the format used by this writer when writing Swagger definitions
     */

    public Constants.Format getFormat();

    /**
     * Writes an entire Swagger definition (both ResourceListing and referred ApiDeclarations) to a generic
     * SwaggerStore
     *
     * @param store the SwaggerStore to write to
     * @param resourceListing the ResourceListing to write
     * @throws IOException
     */

    public void writeSwagger(SwaggerStore store, ResourceListing resourceListing) throws IOException;
}
