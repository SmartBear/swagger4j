package com.smartbear.swagger4j.impl;

import com.smartbear.swagger4j.Authorizations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AuthorizationsImpl implements Authorizations {

    private List<Authorization> authorizations = new ArrayList<Authorization>();

    @Override
    public List<Authorization> getAuthorizations() {
        return Collections.unmodifiableList(authorizations);
    }

    @Override
    public List<Authorization> getAuthorizationsByType(AuthorizationType type) {

        List<Authorization> result = new ArrayList<Authorization>();

        for (Authorization a : authorizations)
            if (a.getType() == type)
                result.add(a);

        return result;
    }

    @Override
    public Authorization addAuthorization(String name, AuthorizationType type) {

        assert name != null : "Authorization name can not be null";
        assert type != null : "Authorization type can not be null";

        Authorization authorization = null;

        switch (type) {
            case API_KEY:
                authorization = new ApiKeyAuthorizationImpl(name);
                break;
            case BASIC:
                authorization = new BasicAuthenticationImpl(name);
                break;
            case OAUTH2:
                authorization = new OAuth2AuthenticationImpl(name);
                break;
        }

        if (authorization != null)
            authorizations.add(authorization);

        return authorization;
    }

    @Override
    public void removeAuthorization(Authorization authorization) {
        authorizations.remove(authorization);
    }

    private static abstract class AbstractAuthorization implements Authorization {
        private AuthorizationType type;
        private String name;

        private AbstractAuthorization(AuthorizationType type, String name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public AuthorizationType getType() {
            return type;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private static class OAuth2AuthenticationImpl extends AbstractAuthorization implements OAuth2Authorization {
        private List<ScopeImpl> scopes = new ArrayList<ScopeImpl>();
        private ImplicitGrant implicitGrant = new ImplicitGrantImpl();
        private AuthorizationCodeGrant authorizationCodeGrant = new AuthorizationCodeGrantImpl();

        private OAuth2AuthenticationImpl(String name) {
            super(AuthorizationType.OAUTH2, name);
        }

        @Override
        public Scope[] getScopes() {
            return scopes.toArray(new Scope[scopes.size()]);
        }

        @Override
        public Scope addScope(String name, String description) {
            ScopeImpl scope = new ScopeImpl(name, description);
            scopes.add(scope);
            return scope;
        }

        @Override
        public void removeScope(Scope scope) {
            scopes.remove(scope);
        }

        @Override
        public ImplicitGrant getImplicitGrant() {
            return implicitGrant;
        }

        @Override
        public AuthorizationCodeGrant getAuthorizationCodeGrant() {
            return authorizationCodeGrant;
        }

        private static class ImplicitGrantImpl implements ImplicitGrant {
            private String loginEndpoint;
            private String tokenName;

            public String getLoginEndpointUrl() {
                return loginEndpoint;
            }

            public void setLoginEndpoint(String loginEndpoint) {
                this.loginEndpoint = loginEndpoint;
            }

            public String getTokenName() {
                return tokenName;
            }

            public void setTokenName(String tokenName) {
                this.tokenName = tokenName;
            }
        }

        private static class AuthorizationCodeGrantImpl implements AuthorizationCodeGrant {
            private String tokenRequestEndpoint;
            private String clientIdName;
            private String clientSecretName;
            private String tokenEndpoint;
            private String tokenName;

            public String getTokenRequestEndpointUrl() {
                return tokenRequestEndpoint;
            }

            public void setTokenRequestEndpoint(String tokenRequestEndpoint) {
                this.tokenRequestEndpoint = tokenRequestEndpoint;
            }

            public String getClientIdName() {
                return clientIdName;
            }

            public void setClientIdName(String clientIdName) {
                this.clientIdName = clientIdName;
            }

            public String getClientSecretName() {
                return clientSecretName;
            }

            public void setClientSecretName(String clientSecretName) {
                this.clientSecretName = clientSecretName;
            }

            public String getTokenEndpointUrl() {
                return tokenEndpoint;
            }

            public void setTokenEndpoint(String tokenEndpoint) {
                this.tokenEndpoint = tokenEndpoint;
            }

            public String getTokenName() {
                return tokenName;
            }

            public void setTokenName(String tokenName) {
                this.tokenName = tokenName;
            }
        }
    }

    private static class BasicAuthenticationImpl extends AbstractAuthorization implements BasicAuthorization {
        private BasicAuthenticationImpl(String name) {
            super(AuthorizationType.BASIC, name);
        }
    }

    private static class ScopeImpl implements OAuth2Authorization.Scope {
        private String name;
        private String description;

        public ScopeImpl(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    private static class ApiKeyAuthorizationImpl extends AbstractAuthorization implements ApiKeyAuthorization {
        private String keyName;
        private String passAs;

        private ApiKeyAuthorizationImpl(String name) {
            super(AuthorizationType.API_KEY, name);
        }

        public String getKeyName() {
            return keyName;
        }

        public void setKeyName(String keyName) {
            this.keyName = keyName;
        }

        public String getPassAs() {
            return passAs;
        }

        public void setPassAs(String passAs) {
            this.passAs = passAs;
        }
    }
}
