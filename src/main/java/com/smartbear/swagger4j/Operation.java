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

package com.smartbear.swagger4j;

import java.util.Collection;
import java.util.List;

/**
 * Holds a Swagger operation - see <a href="https://github.com/wordnik/swagger-core/wiki/API-Declaration"
 * target="_new">https://github.com/wordnik/swagger-core/wiki/API-Declaration</a>
 */

public interface Operation {

    public Api getApi();

    public Method getMethod();

    public void setMethod(Method method);

    public String getNickName();

    public void setNickName(String nickName);

    public String getResponseClass();

    public void setResponseClass(String responseClass);

    public String getSummary();

    public void setSummary(String summary);

    public String getNotes();

    public void setNotes(String notes);

    public Collection<String> getProduces();

    public void removeProduces(String produces);

    public void addProduces(String produces);

    public Collection<String> getConsumes();

    public void removeConsumes(String consumes);

    public void addConsumes(String consumes);

    public List<Parameter> getParameters();

    public Parameter getParameter(String name);

    public void removeParameter(Parameter parameter);

    public Parameter addParameter(String name, Parameter.ParamType type);

    public List<ResponseMessage> getResponseMessages();

    public ResponseMessage getResponseMessage(int code);

    public void removeResponseMessage(ResponseMessage responseMessage);

    public ResponseMessage addResponseMessage(int code, String message);

    /**
     * These are the methods supported by Swagger 1.2 - more to come
     */

    public enum Method {
        GET, POST, PUT, DELETE, HEAD, OPTIONS, PATCH
    }
}
