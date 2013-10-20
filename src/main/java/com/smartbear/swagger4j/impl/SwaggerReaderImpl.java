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
import java.util.List;
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
        String basePath = parser.getString(Utils.BASE_PATH);

        ResourceListingImpl resourceListing = new ResourceListingImpl(basePath);
        resourceListing.setApiVersion(parser.getString(Utils.API_VERSION));
        resourceListing.setSwaggerVersion( SwaggerVersion.fromIdentifier( parser.getString(Utils.SWAGGER_VERSION)));

        for (SwaggerParser node  :parser.getChildren(Utils.APIS))
        {
            String path = node.getString(Utils.PATH);
            Reader reader = source.readApiDeclaration( basePath, path);

            ApiDeclaration apiDeclaration = readApiDeclaration(reader, source.getFormat());
            ResourceListing.ResourceListingApi api = resourceListing.addApi(apiDeclaration, path);
            api.setDescription(node.getString(Utils.DESCRIPTION));
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

        String basePath = parser.getString(Utils.BASE_PATH);
        String resourcePath = parser.getString(Utils.RESOURCE_PATH);

        ApiDeclaration apiDeclaration = new ApiDeclarationImpl(basePath, resourcePath);
        apiDeclaration.setSwaggerVersion(SwaggerVersion.fromIdentifier(parser.getString(Utils.SWAGGER_VERSION)));
        apiDeclaration.setApiVersion(parser.getString(Utils.API_VERSION));

        for (SwaggerParser apiNode : parser.getChildren(Utils.APIS)) {
            String apiPath = apiNode.getString(Utils.PATH);

            if( apiDeclaration.getApi( apiPath ) != null )
            {
                logger.log( Level.INFO, "Skipping duplicate API at path [" + apiPath +
                        "] in ApiDeclaration at [" + basePath + resourcePath + "]");
            }
            else
            {
                Api api = apiDeclaration.addApi(apiPath);
                api.setDescription(apiNode.getString(Utils.DESCRIPTION));

                for( SwaggerParser opNode : apiNode.getChildren( Utils.OPERATIONS ))
                {
                    String nickName = opNode.getString(Utils.NICKNAME);
                    if( api.getOperation(nickName ) != null )
                    {
                        logger.log( Level.INFO, "Skipping duplicate Operation with nickName [" +
                                nickName + "] in API at path [" + apiPath +
                                "] in ApiDeclaration at [" + basePath + resourcePath + "]");
                    }
                    else
                    {
                        Operation operation = api.addOperation(nickName,
                                Operation.Method.valueOf(opNode.getString(Utils.HTTP_METHOD).toUpperCase()));

                        operation.setSummary(opNode.getString(Utils.SUMMARY));
                        operation.setNotes(opNode.getString(Utils.NOTES));
                        operation.setResponseClass(opNode.getString(Utils.RESPONSE_CLASS));

                        for (SwaggerParser parameterNode : opNode.getChildren(Utils.PARAMETERS)) {
                            Parameter parameter = operation.addParameter(parameterNode.getString(Utils.NAME),
                                    Parameter.ParamType.valueOf(parameterNode.getString(Utils.PARAM_TYPE)));

                            parameter.setAllowMultiple(parameterNode.getBoolean(Utils.ALLOW_MULTIPLE));
                            parameter.setDescription(parameterNode.getString(Utils.DESCRIPTION));
                            parameter.setRequired(parameterNode.getBoolean(Utils.REQUIRED));
                            parameter.setDataType(parameterNode.getString(Utils.DATA_TYPE));
                        }

                        for (SwaggerParser errorNode : opNode.getChildren(Utils.ERROR_RESPONSES)) {
                            operation.addErrorResponse(
                                    errorNode.getInteger(Utils.CODE), errorNode.getString(Utils.REASON)
                            );
                        }

                        for (String produces : opNode.getArray(Utils.PRODUCES)) {
                            operation.addProduces(produces);
                        }

                        for (String consumes : opNode.getArray(Utils.CONSUMES)) {
                            operation.addConsumes(consumes);
                        }
                    }
                }
            }
        }

        return apiDeclaration;
    }

}
