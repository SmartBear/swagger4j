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
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import com.smartbear.swagger4j.Api;
import com.smartbear.swagger4j.ApiDeclaration;
import com.smartbear.swagger4j.Authorizations;
import com.smartbear.swagger4j.DataType;
import com.smartbear.swagger4j.Info;
import com.smartbear.swagger4j.Model;
import com.smartbear.swagger4j.Operation;
import com.smartbear.swagger4j.Parameter;
import com.smartbear.swagger4j.Property;
import com.smartbear.swagger4j.ResourceListing;
import com.smartbear.swagger4j.ResponseMessage;
import com.smartbear.swagger4j.SwaggerFormat;
import com.smartbear.swagger4j.SwaggerStore;
import com.smartbear.swagger4j.SwaggerVersion;
import com.smartbear.swagger4j.SwaggerWriter;

/**
 * Default implementation of the SwaggerWriter interface
 *
 * @see SwaggerWriter
 */

public class SwaggerWriterImpl implements SwaggerWriter {

    private final SwaggerFormat format;

    public SwaggerWriterImpl(SwaggerFormat format) {
        this.format = format;
    }

    @Override
    public void writeApiDeclaration(ApiDeclaration declaration, Writer writer) throws IOException {
        SwaggerGenerator w = SwaggerGenerator.newGenerator( writer, format );

        SwaggerVersion swaggerVersion = declaration.getSwaggerVersion();
        Constants constants = Constants.get(swaggerVersion);
        w.addString(Constants.SWAGGER_VERSION, swaggerVersion.getIdentifier());
        w.addString(Constants.API_VERSION, declaration.getApiVersion());
        w.addString(Constants.BASE_PATH, declaration.getBasePath());
        w.addString(Constants.RESOURCE_PATH, declaration.getResourcePath());

        if( swaggerVersion.isGreaterThan( SwaggerVersion.V1_1 ) )
        {
            Collection<String> produces = declaration.getProduces();
            if( !produces.isEmpty())
                w.addArray(Constants.PRODUCES, produces.toArray(new String[produces.size()]));

            Collection<String> consumes = declaration.getConsumes();
            if( !consumes.isEmpty())
                w.addArray(Constants.CONSUMES, consumes.toArray(new String[consumes.size()]));
        }

        for (Api api : declaration.getApis()) {
            SwaggerGenerator aw = w.addArrayObject(Constants.APIS);
            aw.addString(Constants.PATH, api.getPath());
            aw.addString(Constants.DESCRIPTION, api.getDescription());

            for (Operation operation : api.getOperations()) {
                SwaggerGenerator ow = aw.addArrayObject(Constants.OPERATIONS);
                ow.addString(Constants.NICKNAME, operation.getNickName());
                ow.addString(constants.METHOD, operation.getMethod().name());
                ow.addString(Constants.SUMMARY, operation.getSummary());
                ow.addString(Constants.NOTES, operation.getNotes());
                ow.addString(constants.OPERATION_TYPE, operation.getType());
                if (operation.isDeprecated()) {
                	ow.addBoolean(Constants.DEPRECATED, true);
                }

                // an Operation is a DataType
                writeDataType(ow, operation);

                for (Parameter parameter : operation.getParameters()) {
                	// Parameter Fields
                    SwaggerGenerator pw = ow.addArrayObject(Constants.PARAMETERS);
                    pw.addString(Constants.NAME, parameter.getName());
                    pw.addString(Constants.PARAM_TYPE, parameter.getParamType().name());
                    pw.addBoolean(Constants.ALLOW_MULTIPLE, parameter.isAllowMultiple());
                    pw.addBoolean(Constants.REQUIRED, parameter.isRequired());

                    // a Parameter is a Property
                    writeProperty(pw, parameter);

                }

                for (ResponseMessage responseMessage : operation.getResponseMessages()) {
                    SwaggerGenerator ew = ow.addArrayObject(constants.RESPONSE_MESSAGES);
                    ew.addInt(Constants.CODE, responseMessage.getCode());
                    ew.addString(constants.MESSAGE, responseMessage.getMessage());

                    if( swaggerVersion.isGreaterThan( SwaggerVersion.V1_1 ))
                        ew.addString( Constants.RESPONSE_MODEL, responseMessage.getResponseModel() );
                }

                Collection<String> produces = operation.getProduces();
                if( !produces.isEmpty())
                    ow.addArray(Constants.PRODUCES, produces.toArray(new String[produces.size()]));

                Collection<String> consumes = operation.getConsumes();
                if( !consumes.isEmpty())
                    ow.addArray(Constants.CONSUMES, consumes.toArray(new String[consumes.size()]));
            }
        }

        if (!declaration.getModels().isEmpty()) {
        	SwaggerGenerator mws = w.addObject(Constants.MODELS);

        	for (Model model : declaration.getModels()) {
        		SwaggerGenerator mw = mws.addObject(model.getId());
        		mw.addString(Constants.ID, model.getId());
        		mw.addArray(Constants.REQUIRED, model.getRequiredProperties().toArray(new String[0]));

        		SwaggerGenerator pw = mw.addObject(Constants.PROPERTIES);
        		for (Map.Entry<String, Property> entry : model.getProperties().entrySet()) {
        			Property property = entry.getValue();
        			SwaggerGenerator po = pw.addObject(entry.getKey());

        			writeProperty(po, property);
        		}
        	}
        }

        w.finish();
    }

    private void writeDataType(SwaggerGenerator writer, DataType dataType) {
        // Data Type Fields
        writer.addString(Constants.TYPE_V1_2, dataType.getType());
        writer.addString(Constants.REF, dataType.getRef());

        if (dataType.getItems() != null) {
        	SwaggerGenerator object = writer.addObject(Constants.ITEMS);
        	object.addString(Constants.TYPE_V1_2, dataType.getItems().getType());
        	object.addString(Constants.REF, dataType.getItems().getRef());

            if (dataType.isUniqueItems()) {
            	writer.addBoolean(Constants.UNIQUE_ITEMS, true);
            }
        }

        writer.addString(Constants.FORMAT, dataType.getFormat());

        writer.addString(Constants.DEFAULT_VALUE, dataType.getDefaultValue());

        if (dataType.getAllowedValues() != null) {
        	writer.addArray(Constants.ENUM, dataType.getAllowedValues());
        }

        writer.addString(Constants.MINIMUM, dataType.getMinimum());
        writer.addString(Constants.MAXIMUM, dataType.getMaximum());
    }

    private void writeProperty(SwaggerGenerator writer, Property property) {
    	// a Property is a DataType
    	writeDataType(writer, property);

        // Property fields
        writer.addString(Constants.DESCRIPTION, property.getDescription());
    }

    @Override
    public void writeResourceListing(ResourceListing listing, Writer writer) throws IOException {
        SwaggerGenerator w = SwaggerGenerator.newGenerator( writer, format );

        Constants constants = Constants.get(listing.getSwaggerVersion());
        w.addString(Constants.API_VERSION, listing.getApiVersion());
        w.addString(Constants.SWAGGER_VERSION, listing.getSwaggerVersion().getIdentifier());
        w.addString(Constants.BASE_PATH, listing.getBasePath());

        for (ResourceListing.ResourceListingApi api : listing.getApis()) {
            SwaggerGenerator sw = w.addArrayObject(Constants.APIS);
            sw.addString(Constants.DESCRIPTION, api.getDescription());
            sw.addString(Constants.PATH, api.getPath());
        }

        if( listing.getSwaggerVersion().isGreaterThan( SwaggerVersion.V1_1 ))
        {
            Info info = listing.getInfo();
            SwaggerGenerator sw = w.addObject(Constants.INFO);
            sw.addString( Constants.INFO_TITLE, info.getTitle() );
            sw.addString( Constants.INFO_DESCRIPTION, info.getDescription() );
            sw.addString( Constants.INFO_TERMSOFSERVICEURL, info.getTermsOfServiceUrl() );
            sw.addString( Constants.INFO_CONTACT, info.getContact() );
            sw.addString( Constants.INFO_LICENSE, info.getLicense() );
            sw.addString( Constants.INFO_LICENSE_URL, info.getLicenseUrl() );

            Authorizations authorizations = listing.getAuthorizations();
            if( authorizations != null && authorizations.getAuthorizations() != null && !authorizations.getAuthorizations().isEmpty())
            {
                writeAuthorizations(w, constants, authorizations);
            }
        }

        w.finish();
    }

    private void writeAuthorizations(SwaggerGenerator w, Constants constants, Authorizations authorizations) {
        SwaggerGenerator sg = w.addObject(Constants.AUTHORIZATIONS);
        for( Authorizations.Authorization aut : authorizations.getAuthorizations())
        {
            if( aut.getType() == Authorizations.AuthorizationType.BASIC )
            {
                sg.addObject( aut.getName() ).addString( Constants.AUTHORIZATION_TYPE, Constants.BASIC_AUTH_TYPE );
            }
            else if( aut.getType() == Authorizations.AuthorizationType.API_KEY )
            {
                Authorizations.ApiKeyAuthorization aka = (Authorizations.ApiKeyAuthorization) aut;
                if( hasContent( aka.getKeyName() ) && hasContent( aka.getPassAs() ))
                {
                    sg.addObject( aut.getName() ).addString( Constants.AUTHORIZATION_TYPE, Constants.API_KEY_TYPE ).
                            addString(Constants.API_KEY_KEY_NAME, aka.getKeyName()).
                            addString(Constants.API_KEY_PASS_AS, aka.getPassAs() );
                }
            }
            else if( aut.getType() == Authorizations.AuthorizationType.OAUTH2 )
            {
                Authorizations.OAuth2Authorization oaa = (Authorizations.OAuth2Authorization) aut;
                if( oaa.getAuthorizationCodeGrant() != null || oaa.getImplicitGrant() != null)
                {
                    sg = sg.addObject( aut.getName() ).addString( Constants.AUTHORIZATION_TYPE, Constants.API_KEY_TYPE );


                    final Authorizations.OAuth2Authorization.Scope[] scopes = oaa.getScopes();
                    if( scopes.length > 0 )
                    {
                        for( Authorizations.OAuth2Authorization.Scope s : scopes)
                        {
                            SwaggerGenerator so = sg.addArrayObject(Constants.OAUTH2_SCOPES);
                            so.addString( Constants.OAUTH2_SCOPE, s.getName() );
                            so.addString( Constants.OAUTH2_SCOPE_DESCRIPTION, s.getDescription() );
                        }
                    }

                    sg = sg.addObject( Constants.OAUTH2_GRANT_TYPES );
                    Authorizations.OAuth2Authorization.ImplicitGrant ig = oaa.getImplicitGrant();
                    if( ig != null )
                    {
                        sg.addObject( Constants.OAUTH2_IMPLICIT_GRANT ).
                                addString( Constants.OAUTH2_IMPLICIT_TOKEN_NAME, ig.getTokenName() ).
                                addObject( Constants.OAUTH2_IMPLICIT_LOGIN_ENDPOINT ).
                                    addString(Constants.OAUTH2_IMPLICIT_LOGIN_ENDPOINT_URL, ig.getLoginEndpointUrl());
                    }

                    Authorizations.OAuth2Authorization.AuthorizationCodeGrant acg = oaa.getAuthorizationCodeGrant();
                    if( acg != null )
                    {
                        sg = sg.addObject( Constants.OAUTH2_AUTHORIZATION_CODE_GRANT );

                        sg.addObject( Constants.OAUTH2_AUTHORIZATION_GRANT_TOKEN_REQUEST_ENDPOINT ).
                                addString( Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_URL, acg.getTokenRequestEndpointUrl() ).
                                addString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_CLIENT_ID_NAME, acg.getClientIdName()).
                                addString( Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_CLIENT_SECRET_NAME, acg.getClientSecretName() );

                        sg.addObject( Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT ).
                                addString( Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT_URL, acg.getTokenEndpointUrl() ).
                                addString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT_TOKEN_NAME, acg.getTokenName());
                    }
                }
            }
        }
    }

    @Override
    public SwaggerFormat getFormat() {
        return format;
    }

    @Override
    public void writeSwagger(SwaggerStore store, ResourceListing resourceListing) throws IOException {
        Writer writer = store.createResource("api-docs." + format.getExtension());
        writeResourceListing(resourceListing, writer);

        for (ResourceListing.ResourceListingApi api : resourceListing.getApis()) {
            ApiDeclaration declaration = api.getDeclaration();
            String path = Utils.createFileNameFromPath(api.getPath(), format);

            writer = store.createResource(path);
            writeApiDeclaration(declaration, writer);
        }
    }

    private static boolean hasContent( String str )
    {
        return str != null && str.trim().length() > 0 ;
    }
}
