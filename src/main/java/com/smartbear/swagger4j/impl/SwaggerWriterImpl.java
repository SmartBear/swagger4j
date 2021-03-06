/**
 * Copyright 2013 SmartBear Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartbear.swagger4j.impl;

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

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;

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

    public void writeApiDeclaration(ApiDeclaration declaration, Writer writer) throws IOException {
        SwaggerGenerator w = SwaggerGenerator.newGenerator(writer, format);

        SwaggerVersion swaggerVersion = declaration.getSwaggerVersion();
        Constants constants = Constants.get(swaggerVersion);
        w.addString(Constants.SWAGGER_VERSION, swaggerVersion.getIdentifier());
        w.addString(constants.API_VERSION, declaration.getApiVersion());
        w.addString(constants.BASE_PATH, declaration.getBasePath());
        w.addString(constants.RESOURCE_PATH, declaration.getResourcePath());

        if (swaggerVersion.isGreaterThan(SwaggerVersion.V1_1)) {
            Collection<String> produces = declaration.getProduces();
            if (!produces.isEmpty()) {
                w.addArray(constants.PRODUCES, produces.toArray(new String[produces.size()]));
            }

            Collection<String> consumes = declaration.getConsumes();
            if (!consumes.isEmpty()) {
                w.addArray(constants.CONSUMES, consumes.toArray(new String[consumes.size()]));
            }
        }

        for (Api api : declaration.getApis()) {
            SwaggerGenerator aw = w.addArrayObject(constants.APIS);
            aw.addString(constants.PATH, api.getPath());
            aw.addString(constants.DESCRIPTION, api.getDescription());

            for (Operation operation : api.getOperations()) {
                SwaggerGenerator ow = aw.addArrayObject(constants.OPERATIONS);
                ow.addString(constants.NICKNAME, operation.getNickName());
                ow.addString(constants.METHOD, operation.getMethod().name());
                ow.addString(constants.SUMMARY, operation.getSummary());
                ow.addString(constants.NOTES, operation.getNotes());
                ow.addString(constants.OPERATION_TYPE, operation.getResponseClass());

                for (Parameter parameter : operation.getParameters()) {
                    SwaggerGenerator pw = ow.addArrayObject(constants.PARAMETERS);
                    pw.addString(constants.NAME, parameter.getName());
                    pw.addString(constants.PARAM_TYPE, parameter.getParamType().name());
                    pw.addBoolean(constants.ALLOW_MULTIPLE, parameter.isAllowMultiple());
                    pw.addString(constants.DESCRIPTION, parameter.getDescription());
                    pw.addBoolean(constants.REQUIRED, parameter.isRequired());
                    pw.addString(constants.TYPE, parameter.getType());
                }

                for (ResponseMessage responseMessage : operation.getResponseMessages()) {
                    SwaggerGenerator ew = ow.addArrayObject(constants.RESPONSE_MESSAGES);
                    ew.addInt(constants.CODE, responseMessage.getCode());
                    ew.addString(constants.MESSAGE, responseMessage.getMessage());

                    if (swaggerVersion.isGreaterThan(SwaggerVersion.V1_1)) {
                        ew.addString(constants.RESPONSE_MODEL, responseMessage.getResponseModel());
                    }
                }

                Collection<String> produces = operation.getProduces();
                if (!produces.isEmpty()) {
                    ow.addArray(constants.PRODUCES, produces.toArray(new String[produces.size()]));
                }

                Collection<String> consumes = operation.getConsumes();
                if (!consumes.isEmpty()) {
                    ow.addArray(constants.CONSUMES, consumes.toArray(new String[consumes.size()]));
                }
            }
        }

        SwaggerGenerator models = w.addObject(Constants.MODELS);

        for (Model model : declaration.getModels()) {
            SwaggerGenerator mw = models.addObject(model.getName());

            mw.addString(constants.ID, model.getId());
            mw.addString(constants.DESCRIPTION, model.getDescription());

            List<String> requiredProperties = model.getRequiredProperties();

            if (requiredProperties != null) {
                mw.addArray(constants.REQUIRED, requiredProperties.toArray(
                    new String[]{}));
            }

            if (model.getRequiredProperties() != null) {
                mw.addArray(constants.REQUIRED,
                    model.getRequiredProperties().toArray(new String[model.getRequiredProperties().size()]));
            }

            List<Property> properties = model.getProperties();

            if (properties != null) {
                SwaggerGenerator props = mw.addObject(constants.PROPERTIES);

                for (Property property : properties) {
                    SwaggerGenerator prop = props.addObject(property.getName());

                    if (property.getDescription() != null && property.getDescription().trim().length() > 0) {
                        prop.addString(constants.DESCRIPTION,
                            property.getDescription());
                    }

                    DataType dataType = property.getDataType();

                    if (dataType == null) {
                        continue;
                    }

                    if (dataType.isArray()) {
                        prop.addString(constants.TYPE, "array");
                        prop = prop.addObject(constants.ITEMS);

                    }

                    if (dataType.isRef()) {
                        prop.addString("$ref", dataType.getRef());
                    } else {
                        prop.addString(constants.TYPE, dataType.getType());
                    }

                    if (dataType.getFormat() != null && dataType.getFormat().trim().length() > 0) {
                        prop.addString(constants.FORMAT, dataType.getFormat());
                    }

                    if (property.getMinimum() != null) {
                        prop.addString(constants.MINIMUM, property.getMinimum()
                            .toString());
                    }

                    if (property.getMaximum() != null) {
                        prop.addString(constants.MAXIMUM, property.getMaximum()
                            .toString());
                    }

                    if (property.getEnumValues() != null && !property.getEnumValues().isEmpty()) {
                        prop.addArray(constants.ENUM, property.getEnumValues()
                            .toArray(
                                new String[property.getEnumValues()
                                    .size()]));
                    }

                    if (property.getDefaultValue() != null) {
                        prop.addString(constants.DEFAULT_VALUE, property.getDefaultValue().toString());
                    }
                }
            }
        }

        w.finish();
    }

    public void writeResourceListing(ResourceListing listing, Writer writer) throws IOException {
        SwaggerGenerator w = SwaggerGenerator.newGenerator(writer, format);

        Constants constants = Constants.get(listing.getSwaggerVersion());
        w.addString(constants.API_VERSION, listing.getApiVersion());
        w.addString(Constants.SWAGGER_VERSION, listing.getSwaggerVersion().getIdentifier());
        if (listing.getSwaggerVersion().isLessThan(SwaggerVersion.V1_2)) {
            w.addString(constants.BASE_PATH, listing.getBasePath());
        }

        for (ResourceListing.ResourceListingApi api : listing.getApis()) {
            SwaggerGenerator sw = w.addArrayObject(constants.APIS);
            sw.addString(constants.DESCRIPTION, api.getDescription());
            sw.addString(constants.PATH, api.getPath());
        }

        if (listing.getSwaggerVersion().isGreaterThan(SwaggerVersion.V1_1)) {
            Info info = listing.getInfo();
            SwaggerGenerator sw = w.addObject(constants.INFO);
            sw.addString(constants.INFO_TITLE, info.getTitle());
            sw.addString(constants.INFO_DESCRIPTION, info.getDescription());
            sw.addString(constants.INFO_TERMSOFSERVICEURL, info.getTermsOfServiceUrl());
            sw.addString(constants.INFO_CONTACT, info.getContact());
            sw.addString(constants.INFO_LICENSE, info.getLicense());
            sw.addString(constants.INFO_LICENSE_URL, info.getLicenseUrl());

            Authorizations authorizations = listing.getAuthorizations();
            if (authorizations != null && authorizations.getAuthorizations() != null && !authorizations.getAuthorizations().isEmpty()) {
                writeAuthorizations(w, constants, authorizations);
            }
        }

        w.finish();
    }

    private void writeAuthorizations(SwaggerGenerator w, Constants constants, Authorizations authorizations) {
        SwaggerGenerator sg = w.addObject(constants.AUTHORIZATIONS);
        for (Authorizations.Authorization aut : authorizations.getAuthorizations()) {
            if (aut.getType() == Authorizations.AuthorizationType.BASIC) {
                sg.addObject(aut.getName()).addString(constants.AUTHORIZATION_TYPE, constants.BASIC_AUTH_TYPE);
            } else if (aut.getType() == Authorizations.AuthorizationType.API_KEY) {
                Authorizations.ApiKeyAuthorization aka = (Authorizations.ApiKeyAuthorization) aut;
                if (hasContent(aka.getKeyName()) && hasContent(aka.getPassAs())) {
                    sg.addObject(aut.getName()).addString(constants.AUTHORIZATION_TYPE, constants.API_KEY_TYPE).
                        addString(constants.API_KEY_KEY_NAME, aka.getKeyName()).
                        addString(constants.API_KEY_PASS_AS, aka.getPassAs());
                }
            } else if (aut.getType() == Authorizations.AuthorizationType.OAUTH2) {
                Authorizations.OAuth2Authorization oaa = (Authorizations.OAuth2Authorization) aut;
                if (oaa.getAuthorizationCodeGrant() != null || oaa.getImplicitGrant() != null) {
                    sg = sg.addObject(aut.getName()).addString(constants.AUTHORIZATION_TYPE, constants.API_KEY_TYPE);

                    final Authorizations.OAuth2Authorization.Scope[] scopes = oaa.getScopes();
                    if (scopes.length > 0) {
                        for (Authorizations.OAuth2Authorization.Scope s : scopes) {
                            SwaggerGenerator so = sg.addArrayObject(constants.OAUTH2_SCOPES);
                            so.addString(constants.OAUTH2_SCOPE, s.getName());
                            so.addString(constants.OAUTH2_SCOPE_DESCRIPTION, s.getDescription());
                        }
                    }

                    sg = sg.addObject(Constants.OAUTH2_GRANT_TYPES);
                    Authorizations.OAuth2Authorization.ImplicitGrant ig = oaa.getImplicitGrant();
                    if (ig != null) {
                        sg.addObject(Constants.OAUTH2_IMPLICIT_GRANT).
                            addString(Constants.OAUTH2_IMPLICIT_TOKEN_NAME, ig.getTokenName()).
                            addObject(Constants.OAUTH2_IMPLICIT_LOGIN_ENDPOINT).
                            addString(Constants.OAUTH2_IMPLICIT_LOGIN_ENDPOINT_URL, ig.getLoginEndpointUrl());
                    }

                    Authorizations.OAuth2Authorization.AuthorizationCodeGrant acg = oaa.getAuthorizationCodeGrant();
                    if (acg != null) {
                        sg = sg.addObject(Constants.OAUTH2_AUTHORIZATION_CODE_GRANT);

                        sg.addObject(Constants.OAUTH2_AUTHORIZATION_GRANT_TOKEN_REQUEST_ENDPOINT).
                            addString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_URL, acg.getTokenRequestEndpointUrl()).
                            addString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_CLIENT_ID_NAME, acg.getClientIdName()).
                            addString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_CLIENT_SECRET_NAME, acg.getClientSecretName());

                        sg.addObject(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT).
                            addString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT_URL, acg.getTokenEndpointUrl()).
                            addString(Constants.OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT_TOKEN_NAME, acg.getTokenName());
                    }
                }
            }
        }
    }

    public SwaggerFormat getFormat() {
        return format;
    }

    public void writeSwagger(SwaggerStore store, ResourceListing resourceListing) throws IOException {
        Writer writer = store.createResource("api-docs." + format.getExtension());
        writeResourceListing(resourceListing, writer);

        for (ResourceListing.ResourceListingApi api : resourceListing.getApis()) {
            ApiDeclaration declaration = api.getDeclaration();
            String path = Utils.createFileNameFromPath("api-docs" + api.getPath(), format);

            writer = store.createResource(path);
            writeApiDeclaration(declaration, writer);
        }
    }

    private static boolean hasContent(String str) {
        return str != null && str.trim().length() > 0;
    }
}
