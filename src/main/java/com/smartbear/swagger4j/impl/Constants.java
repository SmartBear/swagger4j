package com.smartbear.swagger4j.impl;

import com.smartbear.swagger4j.SwaggerVersion;

/**
 * Construct to handle change of property/element names in different Swagger versions
 */

public class Constants {

    public static final String SWAGGER_VERSION = "swaggerVersion";
    public static final String SWAGGER_V2_VERSION = "swagger";

    public static final String API_DOCUMENTATION = "ApiDocumentation";
    public static final String AUTHORIZATIONS = "authorizations";
    public static final String AUTHORIZATION_TYPE = "type";
    public static final String OAUTH_2_TYPE = "oauth2";
    public static final String API_KEY_TYPE = "apiKey";
    public static final String API_KEY_KEY_NAME = "keyName";
    public static final String API_KEY_PASS_AS = "passAs";
    public static final String BASIC_AUTH_TYPE = "basicAuth";
    public static final String OAUTH2_SCOPES = "scopes";
    public static final String OAUTH2_GRANT_TYPES = "grantTypes";
    public static final String OAUTH2_IMPLICIT_GRANT = "implicit";
    public static final String OAUTH2_IMPLICIT_LOGIN_ENDPOINT = "loginEndpoint";
    public static final String OAUTH2_IMPLICIT_LOGIN_ENDPOINT_URL = "url";
    public static final String OAUTH2_IMPLICIT_TOKEN_NAME = "tokenName";
    public static final String OAUTH2_AUTHORIZATION_CODE_GRANT = "authorization_code";
    public static final String OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_URL = "url";
    public static final String OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_CLIENT_ID_NAME = "clientIdName";
    public static final String OAUTH2_AUTHORIZATION_CODE_TOKEN_REQUEST_ENDPOINT_CLIENT_SECRET_NAME = "clientSecretName";
    public static final String OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT = "tokenEndpoint";
    public static final String OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT_URL = "url";
    public static final String OAUTH2_AUTHORIZATION_CODE_TOKEN_ENDPOINT_TOKEN_NAME = "tokenName";
    public static final String OAUTH2_AUTHORIZATION_GRANT_TOKEN_REQUEST_ENDPOINT = "tokenRequestEndpoint";

    /**
     * Constants for the names used in Swagger definitions - these are not final static since they can be overridden
     * for specific versions
     */

    public static final String API_VERSION = "apiVersion";
    public static final String BASE_PATH = "basePath";
    public static final String PATH = "path";
    public static final String APIS = "apis";
    public static final String DESCRIPTION = "description";
    public static final String RESOURCE_PATH = "resourcePath";
    public static final String OPERATIONS = "operations";
    public static final String NICKNAME = "nickname";
    public static final String SUMMARY = "summary";
    public static final String NOTES = "notes";
    public static final String PARAMETERS = "parameters";
    public static final String NAME = "name";
    public static final String PARAM_TYPE = "paramType";
    public static final String ALLOW_MULTIPLE = "allowMultiple";
    public static final String REQUIRED = "required";
    public static final String CODE = "code";
    public static final String PRODUCES = "produces";
    public static final String CONSUMES = "consumes";
    public static final String INFO = "info";
    public static final String INFO_CONTACT = "title";
    public static final String INFO_DESCRIPTION = "description";
    public static final String INFO_LICENSE = "license";
    public static final String INFO_LICENSE_URL = "licenseUrl";
    public static final String INFO_TERMSOFSERVICEURL = "termsOfServiceUrl";
    public static final String INFO_TITLE = "title";
    public static final String RESPONSE_MODEL = "responseModel";
    public static final String OAUTH2_SCOPE = "scope";
    public static final String OAUTH2_SCOPE_DESCRIPTION = "description";
    public static final String HTTP_METHOD = "httpMethod";
    public static final String MODELS = "models";
    public static final String ID = "id";
    public static final String PROPERTIES = "properties";
    public static final String $REF = "$ref";
    public static final String FORMAT = "format";
    public static final String ITEMS = "items";
    public static final String ENUM = "enum";
    public static final String MINIMUM = "minimum";
    public static final String MAXIMUM = "maximum";
    public static final String DEFAULT_VALUE = "defaultValue";

    public String METHOD = "method";
    public String TYPE = "type";
    public String MESSAGE = "message";
    public String RESPONSE_MESSAGES = "responseMessages";
    public String OPERATION_TYPE = "type";

    public static final Constants V1_1 = new V1_1Constants();
    public static final Constants V1_2 = new Constants();
    public static final Constants V2_0 = new Constants();

    public static Constants get(SwaggerVersion version) {
        switch (version) {
            case V1_0:
                return V1_1;
            case V1_1:
                return V1_1;
            case V1_2:
                return V1_2;
            case V2_0:
                return V2_0;
        }

        throw new RuntimeException("Unknown Swagger version: " + version);
    }

    private static class V1_1Constants extends Constants {
        public V1_1Constants() {
            METHOD = "httpMethod";
            MESSAGE = "reason";
            RESPONSE_MESSAGES = "errorResponses";
            TYPE = "dataType";
            OPERATION_TYPE = "responseClass";
        }
    }
}
