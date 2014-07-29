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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.smartbear.swagger4j.Api;
import com.smartbear.swagger4j.Operation;
import com.smartbear.swagger4j.Parameter;
import com.smartbear.swagger4j.ResponseMessage;

/**
 * Default implementation of the Operation interface
 *
 * @see Operation
 */

public class OperationImpl extends DataTypeImpl implements Operation {
    private String nickName;
    private Method method;
    private String summary;
    private String notes;
    private final Set<String> produces = new LinkedHashSet<String>();
    private final Set<String> consumes = new LinkedHashSet<String>();
    private final List<Parameter> parameterList = new ArrayList<Parameter>();
    private final List<ResponseMessage> responseMessages = new ArrayList<ResponseMessage>();
    private Api api;
    private boolean deprecated;

    OperationImpl(Api api, String nickName, Method method ) {
        this.api = api;
        this.nickName = nickName;
        this.method = method;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public void setMethod(Method method) {
        assert method != null : "method can not be null";
        this.method = method;
    }

    @Override
    public String getNickName() {
        return nickName;
    }

    @Override
    public void setNickName(String nickName) {
        assert nickName != null : "nickName can not be null";
        this.nickName = nickName;
    }

    @Override
    public String getType() {
        return super.getType() == null ? "void" : super.getType();
    }

    @Override
    public String getSummary() {
        return summary == null ? "" : summary;
    }

    @Override
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public Collection<String> getProduces() {
        if (produces.isEmpty() && getApi() != null && getApi().getApiDeclaration() != null )
            return getApi().getApiDeclaration().getProduces();

        return Collections.unmodifiableCollection(produces);
    }

    @Override
    public void removeProduces(String produces) {
        this.produces.remove(produces);
    }

    @Override
    public void addProduces(String produces) {
        assert produces != null : "produces can not be null";

        this.produces.add(produces);
    }

    @Override
    public Collection<String> getConsumes() {
        if( consumes.isEmpty() && getApi() != null && getApi().getApiDeclaration() != null )
            return getApi().getApiDeclaration().getConsumes();

        return Collections.unmodifiableCollection(consumes);
    }

    @Override
    public void removeConsumes(String consumes) {
        this.produces.remove(consumes);
    }

    @Override
    public void addConsumes(String consumes) {
        assert consumes != null : "consumes can not be null";
        this.consumes.add(consumes);
    }

    @Override
    public List<Parameter> getParameters() {
        return Collections.unmodifiableList(parameterList);
    }

    @Override
    public Parameter getParameter(String name) {
        assert name != null : "parameter name can not be null";

        synchronized (parameterList) {
            for (Parameter parameter : parameterList)
                if (parameter.getName().equals(name))
                    return parameter;

            return null;
        }
    }

    @Override
    public void removeParameter(Parameter parameter) {
        assert parameter != null : "parameter can not be null";

        synchronized (parameterList) {
            parameterList.remove(parameter);
        }
    }

    @Override
    public Parameter addParameter(String name, Parameter.ParamType type) {
        assert type != null : "parameter must be created with type";

        if (type != Parameter.ParamType.body) {
            assert name != null : "parameter that is not a body must have a name";
            assert getParameter(name) == null : "Parameter already exists with name [" + name + "]";
        }

        synchronized (parameterList) {
            ParameterImpl parameter = new ParameterImpl(name, type);
            parameterList.add(parameter);

            return parameter;
        }
    }

    @Override
    public List<ResponseMessage> getResponseMessages() {
        return Collections.unmodifiableList(responseMessages);
    }

    @Override
    public ResponseMessage getResponseMessage(int code) {
        assert code > 0 : "code can not be 0";

        synchronized (responseMessages) {
            for (ResponseMessage responseMessage : responseMessages)
                if (responseMessage.getCode() == code)
                    return responseMessage;

            return null;
        }
    }

    @Override
    public void removeResponseMessage(ResponseMessage responseMessage) {
        assert responseMessage != null;

        synchronized (responseMessages) {
            responseMessages.remove(responseMessage);
        }
    }

    @Override
    public ResponseMessage addResponseMessage(int code, String message) {
        assert code > 0 : "code must have a value";
        assert getResponseMessage(code) == null : "Response for already exists for code [" + code + "]";

        synchronized (responseMessages) {
            ResponseMessage responseMessage = new ResponseMessageImpl(code, message);
            responseMessages.add(responseMessage);
            return responseMessage;
        }
    }

    @Override
	public Api getApi() {
        return api;
    }

    @Override
	public void setDeprecated(boolean value) {
    	this.deprecated = value;
    }

    @Override
	public boolean isDeprecated() {
    	return deprecated;
    }
}
