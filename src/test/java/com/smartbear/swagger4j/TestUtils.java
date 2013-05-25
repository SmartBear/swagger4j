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

    public final static String ONLINE_XML_RESOURCE_LISTING_URL = "http://petstore.swagger.wordnik.com/api/api-docs.xml";
    public final static String ONLINE_JSON_RESOURCE_LISTING_URL = "http://petstore.swagger.wordnik.com/api/api-docs.json";
    public final static String OFFLINE_XML_RESOURCE_LISTING_URL = "file:src/test/resources/api-docs.xml";
    public final static String OFFLINE_JSON_RESOURCE_LISTING_URL = "file:src/test/resources/api-docs.json";

    public static String getTestJsonResourceListingUrl() {
        return System.getProperties().containsKey("com.smartbear.swagger4j.offline") ?
                OFFLINE_JSON_RESOURCE_LISTING_URL : ONLINE_JSON_RESOURCE_LISTING_URL;
    }

    public static String getTestXmlResourceListingUrl() {
        return System.getProperties().containsKey("com.smartbear.swagger4j.offline") ?
                OFFLINE_XML_RESOURCE_LISTING_URL : ONLINE_XML_RESOURCE_LISTING_URL;
    }
}
