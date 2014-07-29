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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.smartbear.swagger4j.SwaggerFormat;
import com.smartbear.swagger4j.SwaggerStore;
import com.smartbear.swagger4j.impl.Utils.Types.Formats;

/**
 * General utilities methods, classes and constants
 */

public class Utils {

	/**
	 * Swagger Types
	 *
	 * https://github.com/wordnik/swagger-spec/blob/master/versions/1.2.md#431-primitives
	 */
	public static class Types {

		public final static String Void = "void";

		public final static String String = "string";

		public final static String Integer = "integer";

		public final static String Number = "number";

		public final static String Boolean = "boolean";

		public final static String File = "File";

		public final static String Array = "array";

		public final static String Model = "model";

		/**
		 * Swagger Primitive Type Formats
		 *
		 * https://github.com/wordnik/swagger-spec/blob/master/versions/1.2.md#431-primitives
		 */
		public static class Formats {

			final static String Byte = "byte";

			final static String Int32 = "int32";

			final static String Int64 = "int64";

			final static String Float = "float";

			final static String Double = "double";

			final static String Date = "date";

			final static String DateTime = "date-time";
		}
	}

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
     * Returns the Swagger Type of the class, either the Primitive Type of the
     * Model Type flag.
     *
     * @param clazz
     * @return the Primitive Type or the Model Type flag.
     */
    public static String determineSwaggerType(Class<?> clazz) {
        if (int.class.equals(clazz) || long.class.equals(clazz)) {
            return Types.Integer;
        } else if (float.class.equals(clazz) || double.class.equals(clazz)) {
            return Types.Number;
        } else if (byte.class.equals(clazz)) {
            return Types.String;
        } else if (clazz.getName().startsWith("java.lang.")) {
            // basic type
            switch (clazz.getSimpleName().toLowerCase()) {
            case "integer":
            case "long":
                return Types.Integer;
            case "float":
            case "double":
                return Types.Number;
            case "boolean":
                return Types.Boolean;
            default:
                return Types.String;
            }
        } else if (BigDecimal.class.isAssignableFrom(clazz)) {
            return Types.Number;
        } else if (Date.class.isAssignableFrom(clazz)) {
            return Types.String;
        } else if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
            // array of something
            return determineSwaggerType(clazz.getComponentType());
        }

        // model type
        return Types.Model;
    }

    /**
     * Determines a type's Swagger format.
     *
     * @param clazz
     * @return a format or null if no format is defined
     */
    public static String determineSwaggerFormat(Class<?> clazz) {
        if (int.class.equals(clazz)) {
            return Formats.Int32;
        } else if (long.class.equals(clazz)) {
            return Formats.Int64;
        } else if (float.class.equals(clazz)) {
            return Formats.Float;
        } else if (double.class.equals(clazz)) {
            return Formats.Double;
        } else if (clazz.getName().startsWith("java.lang.")) {
            // basic type
            switch (clazz.getSimpleName().toLowerCase()) {
            case "integer":
                return Formats.Int32;
            case "long":
                return Formats.Int64;
            case "float":
                return Formats.Float;
            case "double":
                return Formats.Double;
            case "byte":
                return Formats.Byte;
            default:
                return null;
            }
        } else if (BigDecimal.class.isAssignableFrom(clazz)) {
            return Formats.Double;
        } else if (Date.class.isAssignableFrom(clazz)) {
            return Formats.DateTime;
        } else if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
            // array of something
            return determineSwaggerFormat(clazz.getComponentType());
        }

        return null;
    }
}
