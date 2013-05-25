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
 * Swagger constants
 */

public interface Constants {
    public final static String SWAGGER_VERSION = "1.1";
    public final static String DEFAULT_API_VERSION = "1.0";

    /**
     * Format of swagger files, either json or xml
     */

    public enum Format {
        xml, json;

        public String getExtension() {
            return this.name().toLowerCase();
        }
    }
}
