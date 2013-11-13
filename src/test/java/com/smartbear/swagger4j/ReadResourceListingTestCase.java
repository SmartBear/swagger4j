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

import java.net.URI;
import java.util.List;

public class ReadResourceListingTestCase extends TestCase {

    public void testReadV1_1JsonResourceListing() throws Exception {
        String resourceListingUlr = TestUtils.getTestJsonResourceListingUrl( SwaggerVersion.V1_1);

        ResourceListing resourceListing = Swagger.createReader().readResourceListing(URI.create(resourceListingUlr));
        checkResourceListing(resourceListing, SwaggerVersion.V1_1, new TestDataHelper.V1_TestData());
    }

    public void testReadV1_2JsonResourceListing() throws Exception {
        String resourceListingUlr = TestUtils.getTestJsonResourceListingUrl( SwaggerVersion.V1_2);

        ResourceListing resourceListing = Swagger.createReader().readResourceListing(URI.create(resourceListingUlr));
        checkResourceListing(resourceListing, SwaggerVersion.V1_2, new TestDataHelper.V2_TestData());

        Info info = resourceListing.getInfo();
        assertNotNull(info );
        assertEquals("Swagger Sample App", info.getTitle());
    }

    public void testReadV1_1XmlResourceListing() throws Exception {
        String resourceListingUlr = TestUtils.getTestXmlResourceListingUrl(SwaggerVersion.V1_1);

        ResourceListing resourceListing = Swagger.createReader().readResourceListing(URI.create(resourceListingUlr));
        checkResourceListing(resourceListing, SwaggerVersion.V1_1, new TestDataHelper.V1_TestData());
    }

    private void checkResourceListing(ResourceListing resourceListing, SwaggerVersion version, TestDataHelper testData ) throws Exception {
        assertEquals(testData.getApiVersion(), resourceListing.getApiVersion());
        assertEquals(version, resourceListing.getSwaggerVersion());

        List<ResourceListing.ResourceListingApi> apiList = resourceListing.getApis();
        assertEquals(2, apiList.size());

        ApiDeclaration apiDeclaration = apiList.get(0).getDeclaration();
        assertNotNull(apiDeclaration);

        assertEquals(testData.getApiVersion(), apiDeclaration.getApiVersion());
        assertEquals("http://petstore.swagger.wordnik.com/api", apiDeclaration.getBasePath());
        assertEquals("/user", apiDeclaration.getResourcePath());

        testData.validateUserApiDeclaration(apiDeclaration);

        apiDeclaration = apiList.get(1).getDeclaration();
        assertNotNull(apiDeclaration);

        assertEquals(testData.getApiVersion(), apiDeclaration.getApiVersion());
        assertEquals("http://petstore.swagger.wordnik.com/api", apiDeclaration.getBasePath());
        assertEquals("/pet", apiDeclaration.getResourcePath());

        testData.validatePetApiDeclaration( apiDeclaration );
    }

    public void testOnlinePetStoreSwagger() throws Exception
    {
//        ResourceListing rl = Swagger.createReader().readResourceListing(new URI("http://restapiv3-cur.alertsite.com/swag/api-docs"));
    }
}
