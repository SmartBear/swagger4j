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

public class TestUtils {

    public final static String V1_1_XML_RESOURCE_LISTING_URL = "file:src/test/resources/v1_1/api-docs.xml";
    public final static String V1_1_JSON_RESOURCE_LISTING_URL = "file:src/test/resources/v1_1/api-docs.json";
    public final static String V1_2_JSON_RESOURCE_LISTING_URL = "file:src/test/resources/v1_2/api-docs.json";

    public static String getTestJsonResourceListingUrl( SwaggerVersion swaggerVersion ) {
        switch ( swaggerVersion )
        {
            case V1_1: return V1_1_JSON_RESOURCE_LISTING_URL;
            case V1_2: return V1_2_JSON_RESOURCE_LISTING_URL;
        }

        throw new RuntimeException( "Unknown swagger version: " + swaggerVersion.toString());
    }

    public static String getTestXmlResourceListingUrl( SwaggerVersion swaggerVersion ) {
        switch ( swaggerVersion )
        {
            case V1_1: return V1_1_JSON_RESOURCE_LISTING_URL;
            case V1_2:  throw new RuntimeException( "XML not supported by Swagger version: " + swaggerVersion.toString());
        }

        throw new RuntimeException( "Unknown Swagger version: " + swaggerVersion.toString());
    }
}
