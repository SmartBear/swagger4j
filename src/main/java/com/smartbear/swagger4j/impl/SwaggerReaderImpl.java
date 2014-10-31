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

import com.smartbear.swagger4j.PrimitiveType;
import com.smartbear.swagger4j.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.smartbear.swagger4j.DataType;
import com.smartbear.swagger4j.Model;
import com.smartbear.swagger4j.Property;

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

        SwaggerParser child = parser.getChild( constants.INFO);
        if( child != null )
        {
            readResourceListingInfo(constants, resourceListing, child);
        }

        child = parser.getChild( constants.AUTHORIZATIONS);
        if( child != null )
        {
            readAuthorizations(resourceListing, child);
        }

        return resourceListing;
    }

    private void readAuthorizations(ResourceListingImpl resourceListing, SwaggerParser child) {
        String [] names = child.getChildNames();
        if( names!= null)
        {
            for( String name : names )
            {
                SwaggerParser auth = child.getChild(name);
                String type = auth.getString(Constants.AUTHORIZATION_TYPE);
                if( type.equals(Constants.OAUTH_2_TYPE))
                {
                    readOAuth2Authorization(resourceListing, name, auth);
                }
                else if( type.equals(Constants.API_KEY_TYPE))
                {
                    Authorizations.ApiKeyAuthorization apiKey = (Authorizations.ApiKeyAuthorization) resourceListing.getAuthorizations().addAuthorization( name, Authorizations.AuthorizationType.API_KEY );
                    apiKey.setKeyName( auth.getString(Constants.API_KEY_KEY_NAME));
                    apiKey.setPassAs( auth.getString(Constants.API_KEY_PASS_AS));

                }
                else if( type.equals(Constants.BASIC_AUTH_TYPE))
                {
                    resourceListing.getAuthorizations().addAuthorization( name, Authorizations.AuthorizationType.BASIC );
            }
        }
    }
    }

    private void readOAuth2Authorization(ResourceListingImpl resourceListing, String name, SwaggerParser auth) {
        Authorizations.OAuth2Authorization oauth = (Authorizations.OAuth2Authorization) resourceListing.getAuthorizations().addAuthorization( name, Authorizations.AuthorizationType.OAUTH2 );

        List<SwaggerParser> scopes = auth.getChildren(Constants.OAUTH2_SCOPES);
        if( scopes != null && !scopes.isEmpty() )
        {
            for( SwaggerParser p : scopes )
            {
                oauth.addScope( p.getString( Constants.OAUTH2_SCOPE ), p.getString( Constants.OAUTH2_SCOPE_DESCRIPTION ));
            }
        }

        SwaggerParser grants = auth.getChild(Constants.OAUTH2_GRANT_TYPES);
        if( grants != null )
        {
            SwaggerParser implicitGrant = grants.getChild(Constants.OAUTH2_IMPLICIT_GRANT);
            if( implicitGrant != null )
            {
                Authorizations.OAuth2Authorization.ImplicitGrant ig = oauth.getImplicitGrant();
                if( implicitGrant.getChild(Constants.OAUTH2_IMPLICIT_LOGIN_ENDPOINT) != null )
                    ig.setLoginEndpoint( implicitGrant.getChild(Constants.OAUTH2_IMPLICIT_LOGIN_ENDPOINT).getString(Constants.OAUTH2_IMPLICIT_LOGIN_ENDPOINT_URL));

                ig.setTokenName( implicitGrant.getString(Constants.OAUTH2_IMPLICIT_TOKEN_NAME));
            }

            SwaggerParser ac = grants.getChild(Constants.OAUTH2_AUTHORIZATION_CODE_GRANT);
            if( ac != null )
            {
                Authorizations.OAuth2Authorization.AuthorizationCodeGrant acg = oauth.getAuthorizationCodeGrant();
                SwaggerParser tre = ac.getChild(Constants.OAUTH2_AUTHORIZATION_GRANT_TOKEN_REQUEST_ENDPOINT);

                if( tre != null )
                {
                    acg.setTokenRequestEndpoint( tre.getString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_URL));
                    acg.setClientIdName(tre.getString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_CLIENT_ID_NAME));
                    acg.setClientSecretName( tre.getString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_CLIENT_SECRET_NAME));

                }

                SwaggerParser te = ac.getChild(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT);
                if( te != null )
                {
                    acg.setTokenEndpoint( te.getString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT_URL));
                    acg.setTokenName( te.getString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT_TOKEN_NAME));
                }
            }
        }
    }

    private void readResourceListingInfo(Constants constants, ResourceListingImpl resourceListing, SwaggerParser child) {
        Info info = resourceListing.getInfo();
        info.setContact( child.getString( constants.INFO_CONTACT));
        info.setDescription( child.getString( constants.INFO_DESCRIPTION ));
        info.setLicense( child.getString( constants.INFO_LICENSE ));
        info.setLicenseUrl( child.getString( constants.INFO_LICENSE_URL ));
        info.setTermsOfServiceUrl( child.getString(constants.INFO_TERMSOFSERVICEURL));
        info.setTitle( child.getString( constants.INFO_TITLE));
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
            Api api = apiDeclaration.getApi( apiPath );
            if( api != null )
            {
                logger.log( Level.INFO, "Adding to existing API at path [" + apiPath +
                        "] in ApiDeclaration at [" + basePath + resourcePath + "]");
            }
            else {
                api = apiDeclaration.addApi(apiPath);
                api.setDescription(apiNode.getString(constants.DESCRIPTION));
            }

            for( SwaggerParser opNode : apiNode.getChildren( constants.OPERATIONS ))
            {
                String nickName = opNode.getString(constants.NICKNAME);
                if( nickName == null ){
                    logger.log( Level.INFO, "Missing nickname in operation - using method instead");
                    nickName = getOperationMethod( constants, opNode );
                }

                if( api.getOperation(nickName ) != null )
                {
                    logger.log( Level.INFO, "Skipping duplicate Operation with nickName [" +
                            nickName + "] in API at path [" + apiPath +
                            "] in ApiDeclaration at [" + basePath + resourcePath + "]");
                }
                else
                {
                    readOperation(constants, api, opNode, nickName);
                }
            }
        }

        if (swaggerVersion == SwaggerVersion.V1_2) {
            SwaggerParser modelsNode = parser.getChild(constants.MODELS);
            if (modelsNode != null) {
                for (String modelKey : modelsNode.getChildNames()) {
                    SwaggerParser node = modelsNode.getChild(modelKey);
                    apiDeclaration.addModel(readModel(constants, node));
                }
            }
        } else {
            logger.log(Level.INFO, "skipping models parsing, not supported for version {0}", swaggerVersion.getIdentifier());
        }

        return apiDeclaration;
    }

    private void readOperation(Constants constants, Api api, SwaggerParser opNode, String nickName) {

        String method = getOperationMethod(constants, opNode);

        Operation operation = api.addOperation(nickName,
            Operation.Method.valueOf(method.toUpperCase()));

        operation.setSummary(opNode.getString(constants.SUMMARY));
        operation.setNotes(opNode.getString(constants.NOTES));
        operation.setResponseClass(opNode.getString(constants.OPERATION_TYPE));

        for (SwaggerParser parameterNode : opNode.getChildren(constants.PARAMETERS)) {

            try {
                Parameter parameter = operation.addParameter(parameterNode.getString(constants.NAME),
                    Parameter.ParamType.valueOf(parameterNode.getString(constants.PARAM_TYPE)));

                parameter.setAllowMultiple(parameterNode.getBoolean(constants.ALLOW_MULTIPLE));
                parameter.setDescription(parameterNode.getString(constants.DESCRIPTION));
                parameter.setRequired(parameterNode.getBoolean(constants.REQUIRED));
                parameter.setType(parameterNode.getString(constants.TYPE));
            }
            catch( Exception e )
            {
                Swagger4jExceptionHandler.get().onException( e );
        }
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

    private String getOperationMethod(Constants constants, SwaggerParser opNode) {
        String method = opNode.getString( constants.METHOD );
        if( method == null )
            method = opNode.getString( constants.HTTP_METHOD ) ;
        return method;
    }

    private Model readModel(Constants constants, SwaggerParser modelNode) {
        String id = modelNode.getString(constants.ID);
        String modelDescription = modelNode.getString(constants.DESCRIPTION);
        ModelImpl model = new ModelImpl(id, modelDescription);
        List<String> requiredProperties = modelNode.getArray(constants.REQUIRED);
        model.setRequiredProperties(requiredProperties);

        SwaggerParser propertiesNode = modelNode.getChild(constants.PROPERTIES);
        List<Property> properties = new ArrayList<Property>();
        for (String propertyName : propertiesNode.getChildNames()) {
            SwaggerParser propertyNode = propertiesNode.getChild(propertyName);

            DataType dataType = readDataType(constants, propertyNode);
            String propertyDescription = propertyNode.getString(constants.DESCRIPTION);
            boolean required = requiredProperties.contains(propertyName);
            PropertyImpl property = new PropertyImpl(propertyName, dataType, propertyDescription, required);

            if (dataType.isPrimitive() && dataType.isArray() == false) {
                PrimitiveType primitiveType = (PrimitiveType) dataType;
                property.setDefaultValue(
                    readDefaultValue(constants, propertyNode, primitiveType));
                if (primitiveType.isString()) {
                    property.setEnumValues(propertyNode.getArray(constants.ENUM));
                }
                if (primitiveType.isNumber()) {
                    property.setMaximum(propertyNode.getNumber(constants.MAXIMUM));
                    property.setMinimum(propertyNode.getNumber(constants.MINIMUM));
                }
            }
            properties.add(property);
        }
        model.setProperties(properties);
        return model;
    }

    private DataType readDataType(Constants constants, SwaggerParser node) {
        String type = node.getString(constants.TYPE);
        if (type == null) {
            return new RefDataType(node.getString(constants.$REF));
        }
        if ("array".equals(type)) {
            return readArrayDataType(constants, node.getChild(constants.ITEMS));
        }
        // TODO: handle complex data
        try {
            return PrimitiveType.get(type, node.getString(constants.FORMAT));
        } catch (IllegalArgumentException ex) {
            // not a primitive so a model's id
            return new NamedDataType(type);
        }
    }

    private DataType readArrayDataType(Constants constants, SwaggerParser itemsNode) {
        String itemsType = itemsNode.getString(constants.TYPE);
        if (itemsType == null) {
            return new RefArrayType(itemsNode.getString(constants.$REF));
        }
        try {
            return new PrimitiveArrayType(PrimitiveType.get(
                itemsType,
                itemsNode.getString(constants.FORMAT)));
        } catch (IllegalArgumentException ex) {
            // not a primitive so a model's id
            return new NamedDataArrayType(itemsType);
        }
    }

    private Object readDefaultValue(Constants constants, SwaggerParser node, PrimitiveType dataType) {
        Utils.SwaggerDataParser dataParser = new Utils.SwaggerDataParser(node);
        switch (dataType) {
            case INTEGER:
                return dataParser.getInteger(constants.DEFAULT_VALUE);
            case LONG:
                return dataParser.getLong(constants.DEFAULT_VALUE);
            case FLOAT:
                return dataParser.getFloat(constants.DEFAULT_VALUE);
            case DOUBLE:
                return dataParser.getDouble(constants.DEFAULT_VALUE);
            case BOOLEAN:
                return dataParser.getBoolean(constants.DEFAULT_VALUE);
            case BYTE:      // FALL THROUGH
            case DATE:      // FALL THROUGH
            case DATE_TIME: // FALL THROUGH
            case STRING:
                return dataParser.getString(constants.DEFAULT_VALUE);
            default:
                throw new AssertionError();
        }
    }

}
