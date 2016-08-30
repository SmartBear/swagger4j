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

import com.smartbear.swagger4j.impl.Constants;
import junit.framework.TestCase;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import java.io.File;
import java.net.URI;
import java.util.List;

public class ReadResourceListingTestCase extends TestCase {

    public void testReadV1_0JsonResourceListing() throws Exception
    {
        String url = "file:src/test/resources/v1_0/api-docs";
        Swagger.createReader().readResourceListing( URI.create( url ));
    }

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

        List<Authorizations.Authorization> oas = resourceListing.getAuthorizations().getAuthorizationsByType(Authorizations.AuthorizationType.OAUTH2);
        assertEquals( 1, oas.size() );

        Authorizations.OAuth2Authorization.Scope[] scopes = ((Authorizations.OAuth2Authorization) oas.get(0)).getScopes();
        assertEquals(1, scopes.length);
        assertEquals( "user", scopes[0].getName());
        assertEquals( "Grants read-only access to public information (includes public user profile info, public repository info, and gists)", scopes[0].getDescription());
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
        Swagger.createReader().readResourceListing(new URI("http://petstore.swagger.io/v2/swagger.json"));
    }

    public void testExtendedSwaggerDefinition() throws Exception
    {
        Server server = new Server(8080);

        ResourceHandler handler = new ResourceHandler();
        handler.setResourceBase("src/test/resources/v1_2/swaggerDefinition");

        server.setHandler( handler );
        server.start();

        String url = "http://localhost:8080/resourceList.json";
        Swagger.createReader().readResourceListing( URI.create( url ));
    }

}
