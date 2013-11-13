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

/**
 * Factory class for creating Swagger objects and related readers/writers
 */

public interface SwaggerFactory {

    /**
     * Creates an empty ResourceListing with the specified basePath to use for resolving contained API declaration
     * references
     *
     * @param version the Swagger version to use
     * @return an empty ResourceListing object
     */

    public ResourceListing createResourceListing(SwaggerVersion version);

    /**
     * Creates an empty ApiDeclaration with the specified basePath and resourcePath
     *
     * @param basePath used to resolve API paths defined in this declaration
     * @param resourcePath path to the actual resource described in the declaration
     * @return an empty ApiDeclaration
     */

    public ApiDeclaration createApiDeclaration(String basePath, String resourcePath);

    /**
     * Creates a SwaggerReader that can read Swagger objects
     *
     * @return a SwaggerReader
     */

    public SwaggerReader createSwaggerReader();

    /**
     * Creates a SwaggerWriter for the specified format
     * @param format the format to use when writing, either json or xml
     * @return a SwaggerWriter
     */

    public SwaggerWriter createSwaggerWriter(SwaggerFormat format);
}
