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

import com.smartbear.swagger4j.ErrorResponse;
import com.smartbear.swagger4j.Operation;
import com.smartbear.swagger4j.Parameter;

import java.util.*;

/**
 * Default implementation of the Operation interface
 *
 * @see Operation
 */

public class OperationImpl implements Operation {
    private String nickName;
    private Method method;
    private String responseClass;
    private String summary;
    private String notes;
    private HashSet<String> produces = new HashSet<String>();
    private HashSet<String> consumes = new HashSet<String>();
    private List<Parameter> parameterList = new ArrayList<Parameter>();
    private List<ErrorResponse> errorResponses = new ArrayList<ErrorResponse>();

    OperationImpl(String nickName, Method method ) {

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
    public String getResponseClass() {
        return responseClass;
    }

    @Override
    public void setResponseClass(String responseClass) {
        this.responseClass = responseClass;
    }

    @Override
    public String getSummary() {
        return summary;
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
    public List<ErrorResponse> getErrorResponses() {
        return Collections.unmodifiableList(errorResponses);
    }

    @Override
    public ErrorResponse getErrorResponse(int code) {
        assert code > 0 : "code can not be 0";

        synchronized (errorResponses) {
            for (ErrorResponse errorResponse : errorResponses)
                if (errorResponse.getCode() == code)
                    return errorResponse;

            return null;
        }
    }

    @Override
    public void removeErrorResponse(ErrorResponse errorResponse) {
        assert errorResponse != null;

        synchronized (errorResponses) {
            errorResponses.remove(errorResponse);
        }
    }

    @Override
    public ErrorResponse addErrorResponse(int errorCode, String reason) {
        assert errorCode > 0 : "errorCode must have a value";
        assert getErrorResponse(errorCode) == null : "Error Response for already exists for code [" + errorCode + "]";

        synchronized (errorResponses) {
            ErrorResponse errorResponse = new ErrorResponseImpl(errorCode, reason);
            errorResponses.add(errorResponse);
            return errorResponse;
        }
    }
}
