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
import java.net.URI;

/**
 * Reads Swagger objects from a URI or a generic SwaggerSource
 */

public interface SwaggerReader {

    /**
     * Reads a Resource Listing and all contained API Declarations from the specified URI. The format will be
     * based on the extension of the URI (.json or .xml). Use the SwaggerSource version of this method for
     * more flexible format extrapolation.
     *
     * @param uri the URI of the starting api-docs document (with either json or xml extension)
     * @return the created ResourceListing
     *
     * @throws IOException
     */

    public ResourceListing readResourceListing(URI uri) throws IOException;

    /**
     * Reads an API Declaration from the specified URI - use this is you have standalone API Declarations. The
     * format will be based on the extension of the URI (.json or .xml)
     *
     * @param uri the URI of the API declaration document (with either json or xml extension)
     * @return
     * @throws IOException
     */

    public ApiDeclaration readApiDeclaration(URI uri) throws IOException;

    /**
     * Reads a Resource Listing and all contained API Declarations from the specified SwaggerSource
     *
     * @param source the SwaggerSource providing the Resource Listing and its API Declarations
     * @return the created ResourceListing
     * @throws IOException
     */

    public ResourceListing readResourceListing(SwaggerSource source) throws IOException;
}
