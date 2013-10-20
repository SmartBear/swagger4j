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

import junit.framework.TestCase;

public class CreateResourceListingTestCase extends TestCase {
    public static final String PATH = "http://www.swagger4j.org/test";

    public void testCreation() throws Exception {
        SwaggerFactory factory = Swagger.createSwaggerFactory();
        ResourceListing resourceListing = factory.createResourceListing(PATH);

        assertEquals(resourceListing.getSwaggerVersion(),SwaggerVersion.DEFAULT_VERSION);
        assertEquals(PATH, resourceListing.getBasePath());
        assertNull(resourceListing.getApiVersion());
        assertTrue(resourceListing.getApis().isEmpty());

        resourceListing.setApiVersion("1.0");
        assertEquals("1.0", resourceListing.getApiVersion());

        ApiDeclaration apiDeclaration = factory.createApiDeclaration("/test", "Test API");
        ResourceListing.ResourceListingApi api = resourceListing.addApi(apiDeclaration, "/test.{format}");

        assertEquals(1, resourceListing.getApis().size());
        assertEquals(api, resourceListing.getApis().get(0));

        resourceListing.removeApi(api);
        assertTrue(resourceListing.getApis().isEmpty());
    }
}
