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

import com.smartbear.swagger4j.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default SwaggerReader implementation
 *
 * @see SwaggerReader
 */

public class SwaggerReaderImpl implements SwaggerReader {

    private final static Logger logger = Logger.getLogger( SwaggerReaderImpl.class.getName() );

    @Override
    public ResourceListing readResourceListing(URI uri) throws IOException {
        assert uri != null;

        return readResourceListing(new URISwaggerSource(uri));
    }

    @Override
    public ResourceListing readResourceListing(SwaggerSource source) throws IOException {
        assert source != null;

        SwaggerParser parser = SwaggerParser.newParser(source.readResourceListing(), source.getFormat());
        SwaggerVersion swaggerVersion = SwaggerVersion.fromIdentifier(parser.getString(Constants.SWAGGER_VERSION));
        Constants constants = Constants.get(swaggerVersion);

        // basePath was mandatory in V1.1
        String basePath = parser.getString(constants.BASE_PATH);

        ResourceListingImpl resourceListing = new ResourceListingImpl(swaggerVersion);
        resourceListing.setBasePath( basePath );
        resourceListing.setApiVersion(parser.getString(constants.API_VERSION));
        resourceListing.setSwaggerVersion(swaggerVersion);

        for (SwaggerParser node  :parser.getChildren(constants.APIS))
        {
            String path = node.getString(constants.PATH);
            Reader reader = source.readApiDeclaration( basePath, path);

            ApiDeclaration apiDeclaration = readApiDeclaration(reader, source.getFormat());
            ResourceListing.ResourceListingApi api = resourceListing.addApi(apiDeclaration, path);
            api.setDescription(node.getString(constants.DESCRIPTION));
        }

        SwaggerParser child = parser.getChild(constants.INFO);
        if( child != null )
        {
            Info info = resourceListing.getInfo();
            info.setContact( child.getString( constants.INFO_CONTACT));
            info.setDescription( child.getString( constants.INFO_DESCRIPTION ));
            info.setLicense( child.getString( constants.INFO_LICENSE ));
            info.setLicenseUrl( child.getString( constants.INFO_LICENSE_URL ));
            info.setTermsOfServiceUrl( child.getString(constants.INFO_TERMSOFSERVICEURL));
            info.setTitle( child.getString( constants.INFO_TITLE));
        }

        return resourceListing;
    }

    @Override
    public ApiDeclaration readApiDeclaration(URI uri) throws IOException {

        assert uri != null : "uri can not be null";

        SwaggerFormat format = URISwaggerSource.extractFormat(uri);
        Reader reader = new InputStreamReader(uri.toURL().openStream());

        return readApiDeclaration(reader, format);
    }

    public ApiDeclaration readApiDeclaration(Reader reader, SwaggerFormat format) throws IOException {
        SwaggerParser parser = SwaggerParser.newParser(reader, format);

        SwaggerVersion swaggerVersion = SwaggerVersion.fromIdentifier(parser.getString(Constants.SWAGGER_VERSION));
        Constants constants = Constants.get(swaggerVersion);

        String basePath = parser.getString(constants.BASE_PATH);
        String resourcePath = parser.getString(constants.RESOURCE_PATH);

        ApiDeclaration apiDeclaration = new ApiDeclarationImpl(basePath, resourcePath);
        apiDeclaration.setSwaggerVersion(swaggerVersion);
        apiDeclaration.setApiVersion(parser.getString(constants.API_VERSION));

        for (String produces : parser.getArray(constants.PRODUCES)) {
            apiDeclaration.addProduces(produces);
        }

        for (String consumes : parser.getArray(constants.CONSUMES)) {
            apiDeclaration.addConsumes(consumes);
        }

        for (SwaggerParser apiNode : parser.getChildren(constants.APIS)) {
            String apiPath = apiNode.getString(constants.PATH);

            if( apiDeclaration.getApi( apiPath ) != null )
            {
                logger.log( Level.INFO, "Skipping duplicate API at path [" + apiPath +
                        "] in ApiDeclaration at [" + basePath + resourcePath + "]");
            }
            else
            {
                Api api = apiDeclaration.addApi(apiPath);
                api.setDescription(apiNode.getString(constants.DESCRIPTION));

                for( SwaggerParser opNode : apiNode.getChildren( constants.OPERATIONS ))
                {
                    String nickName = opNode.getString(constants.NICKNAME);
                    if( api.getOperation(nickName ) != null )
                    {
                        logger.log( Level.INFO, "Skipping duplicate Operation with nickName [" +
                                nickName + "] in API at path [" + apiPath +
                                "] in ApiDeclaration at [" + basePath + resourcePath + "]");
                    }
                    else
                    {
                        Operation operation = api.addOperation(nickName,
                                Operation.Method.valueOf(opNode.getString(constants.METHOD).toUpperCase()));

                        operation.setSummary(opNode.getString(constants.SUMMARY));
                        operation.setNotes(opNode.getString(constants.NOTES));
                        operation.setResponseClass(opNode.getString(constants.RESPONSE_CLASS));

                        for (SwaggerParser parameterNode : opNode.getChildren(constants.PARAMETERS)) {
                            Parameter parameter = operation.addParameter(parameterNode.getString(constants.NAME),
                                    Parameter.ParamType.valueOf(parameterNode.getString(constants.PARAM_TYPE)));

                            parameter.setAllowMultiple(parameterNode.getBoolean(constants.ALLOW_MULTIPLE));
                            parameter.setDescription(parameterNode.getString(constants.DESCRIPTION));
                            parameter.setRequired(parameterNode.getBoolean(constants.REQUIRED));
                            parameter.setType(parameterNode.getString(constants.TYPE));
                        }

                        for (SwaggerParser responseMessage : opNode.getChildren(constants.RESPONSE_MESSAGES)) {
                            operation.addResponseMessage(
                                    responseMessage.getInteger(constants.CODE), responseMessage.getString(constants.MESSAGE)
                            ).setResponseModel(responseMessage.getString(constants.RESPONSE_MODEL));
                        }

                        for (String produces : opNode.getArray(constants.PRODUCES)) {
                            operation.addProduces(produces);
                        }

                        for (String consumes : opNode.getArray(constants.CONSUMES)) {
                            operation.addConsumes(consumes);
                        }
                    }
                }
            }
        }

        return apiDeclaration;
    }

}
