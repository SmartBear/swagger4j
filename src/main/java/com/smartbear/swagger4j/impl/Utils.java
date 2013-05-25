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

package com.smartbear.swagger4j.impl;

import com.smartbear.swagger4j.Constants;
import com.smartbear.swagger4j.SwaggerStore;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * General utilities methods, classes and constants
 */

public class Utils {

    /**
     * Constants for the names used in Swagger definitions
     */

    public static final String API_VERSION = "apiVersion";
    public static final String SWAGGER_VERSION = "swaggerVersion";
    public static final String BASE_PATH = "basePath";
    public static final String API_DOCUMENTATION = "ApiDocumentation";
    public static final String PATH = "path";
    public static final String APIS = "apis";
    public static final String DESCRIPTION = "description";
    public static final String RESOURCE_PATH = "resourcePath";
    public static final String OPERATIONS = "operations";
    public static final String NICKNAME = "nickname";
    public static final String HTTP_METHOD = "httpMethod";
    public static final String SUMMARY = "summary";
    public static final String NOTES = "notes";
    public static final String RESPONSE_CLASS = "responseClass";
    public static final String PARAMETERS = "parameters";
    public static final String NAME = "name";
    public static final String PARAM_TYPE = "paramType";
    public static final String ALLOW_MULTIPLE = "allowMultiple";
    public static final String REQUIRED = "required";
    public static final String DATA_TYPE = "dataType";
    public static final String ERROR_RESPONSES = "errorResponses";
    public static final String CODE = "code";
    public static final String REASON = "reason";
    public static final String PRODUCES = "produces";
    public static final String CONSUMES = "consumes";

    /**
     * Method for creating a filename from a path - replaces format references and adds the extension.
     *
     * @param path the path to fix
     * @param format the format to use
     * @return the created fileName
     */

    public static String createFileNameFromPath(String path, Constants.Format format) {
        assert path != null && format != null : "Path and format must not be null";

        String name = path.replaceAll("\\{format\\}", format.getExtension());

        if (name.indexOf('.') == -1)
            name += "." + format.getExtension();
        return name;
    }

    /**
     * SwaggerStore implementation that writes Swagger definitions to a map of fileName to StringWriter
     *
     * @see SwaggerStore
     */

    public static class MapSwaggerStore implements SwaggerStore
    {
        Map<String,StringWriter> files = new HashMap<String, StringWriter>();

        @Override
        public Writer createResource(String path) throws IOException {
            StringWriter writer = new StringWriter();
            files.put(path, writer);
            return writer;
        }

        public Map<String,StringWriter> getFileMap() {
            return files;
        }
    }

    /**
     * SwaggerStore implementation that writes Swagger definitions to the Console; for debugging
     *
     * @see SwaggerStore
     */

    public static class ConsoleSwaggerStore implements SwaggerStore
    {
        @Override
        public Writer createResource(String path) {
            System.out.println( "\nCreating file [" + path + "]");
            return new OutputStreamWriter(System.out);
        }
    }

}
