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
        checkResourceListing(resourceListing, SwaggerVersion.V1_1);
    }

    public void testReadV1_1XmlResourceListing() throws Exception {
        String resourceListingUlr = TestUtils.getTestXmlResourceListingUrl(SwaggerVersion.V1_1);

        ResourceListing resourceListing = Swagger.createReader().readResourceListing(URI.create(resourceListingUlr));
        checkResourceListing(resourceListing, SwaggerVersion.V1_1);
    }

    private void checkResourceListing(ResourceListing resourceListing, SwaggerVersion version ) throws Exception {
        assertEquals("0.2", resourceListing.getApiVersion());
        assertEquals(version, resourceListing.getSwaggerVersion());

        List<ResourceListing.ResourceListingApi> apiList = resourceListing.getApis();
        assertEquals(2, apiList.size());

        ApiDeclaration apiDeclaration = apiList.get(0).getDeclaration();
        assertNotNull(apiDeclaration);

        assertEquals("0.2", apiDeclaration.getApiVersion());
        assertEquals("http://petstore.swagger.wordnik.com/api", apiDeclaration.getBasePath());
        assertEquals("/user", apiDeclaration.getResourcePath());
        assertEquals(6, apiDeclaration.getApis().size());

        for (int c = 0; c < 6; c++) {
            Api api = apiDeclaration.getApis().get(0);
            assertNotNull(api);
            assertEquals(1, api.getOperations().size());
        }

        assertEquals(2, apiDeclaration.getApis().get(4).getOperations().get(0).getParameters().size());
        assertEquals(2, apiDeclaration.getApis().get(3).getOperations().get(0).getErrorResponses().size());

        apiDeclaration = apiList.get(1).getDeclaration();
        assertNotNull(apiDeclaration);

        assertEquals("0.2", apiDeclaration.getApiVersion());
        assertEquals("http://petstore.swagger.wordnik.com/api", apiDeclaration.getBasePath());
        assertEquals("/pet", apiDeclaration.getResourcePath());
        assertEquals(3, apiDeclaration.getApis().size());

        for (int c = 0; c < 3; c++) {
            Api api = apiDeclaration.getApis().get(0);
            assertNotNull(api);
            assertEquals(1, api.getOperations().size());
        }

        assertEquals(1, apiDeclaration.getApis().get(1).getOperations().get(0).getParameters().size());
        assertEquals(2, apiDeclaration.getApis().get(0).getOperations().get(0).getErrorResponses().size());
    }

    public void testSwaggers() throws Exception
    {
      //  validateApiDeclarationCount( "https://api.groupdocs.com/v2.0/spec/spec-files/resources.json", 13 );
      //  validateApiDeclarationCount("http://composer.nprstations.org/api/api-docs", 11);
//        validateApiDeclarationCount("http://developers.subtledata.com/swagger/api-docs.json", 4);
//
//
//        validateApiCount("http://www.apihub.com/apihub/swagger-api/19201/Folders", 4);
//        validateApiCount("http://www.apihub.com/apihub/swagger-api/9528/commits", 3 );
    }

    private void validateApiCount(String path, int expectedCount) throws Exception
    {
        System.out.println( "Checking [" + path + "] for [" + expectedCount + "] apis");
        ApiDeclaration apiDeclaration = Swagger.createReader().readApiDeclaration(new URI(path));
        assertEquals( expectedCount, apiDeclaration.getApis().size());
    }

    public void validateApiDeclarationCount(String path, int expectedCount) throws Exception
    {
        System.out.println( "Checking [" + path + "] for [" + expectedCount + "] apis");
        ResourceListing resourceListing = Swagger.readSwagger(new URI(path));
        assertEquals( expectedCount, resourceListing.getApis().size() );

    }
}
