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

import com.smartbear.swagger4j.SwaggerFormat;
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
     * Method for creating a filename from a path - replaces format references and adds the extension.
     *
     * @param path the path to fix
     * @param format the format to use
     * @return the created fileName
     */

    public static String createFileNameFromPath(String path, SwaggerFormat format) {
        assert path != null && format != null : "Path and format must not be null";

        String name = path.replaceAll("\\{format\\}", format.getExtension());

        return name;
    }

    /**
     * SwaggerStore implementation that writes Swagger definitions to a map of fileName to StringWriter
     *
     * @see SwaggerStore
     */

    public static class MapSwaggerStore implements SwaggerStore
    {
        final Map<String,StringWriter> files = new HashMap<String, StringWriter>();

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
    
    /**
     * Utility class to get finer data type parsing.
     */
    public static class SwaggerDataParser {

        private final SwaggerParser parser;

        public SwaggerDataParser(SwaggerParser parser) {
            this.parser = parser;
        }

        public SwaggerParser getParser() {
            return parser;
        }

        public Boolean getBoolean(String name) {
            return parser.getBoolean(name);
        }
        
        public String getString(String name) {
            return parser.getString(name);
        }
        
        public Integer getInteger(String name) {
            Number number = parser.getNumber(name);
            return number != null ? number.intValue() : null;
        }

        public Long getLong(String name) {
            Number number = parser.getNumber(name);
            return number != null ? number.longValue() : null;
        }

        public Double getDouble(String name) {
            Number number = parser.getNumber(name);
            return number != null ? number.doubleValue() : null;
        }

        public Float getFloat(String name) {
            Number number = parser.getNumber(name);
            return number != null ? number.floatValue() : null;
        }
    }
}
