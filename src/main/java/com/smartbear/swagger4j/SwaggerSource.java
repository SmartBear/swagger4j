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
import java.io.Reader;

/**
 * Generic interface for abstracting the source of a swagger definition - allowing Swagger definitions to be read
 * from a non path-based storage (databases, etc)
 */

public interface SwaggerSource {

    /**
     * Gets a Reader for the ResourceListing of this SwaggerSource
     *
     * @return a Reader for the Swagger Resource Listing
     * @throws IOException
     */

    public Reader readResourceListing() throws IOException;

    /**
     * Gets a Reader for an API Declaration with the specified path. Generally this path will be taken from
     * the path of containing Resource Listings API
     *
     *
     *
     * @param basePath
     * @param path the path of the API Declaration
     * @return a Reader for the API Declaration
     * @throws IOException
     */

    public Reader readApiDeclaration(String basePath, String path) throws IOException;

    /**
     * Gets the format of the Swagger objects returned by this Source
     *
     * @return the format of the Swagger objects returned by this Source
     */

    public SwaggerFormat getFormat();
}
