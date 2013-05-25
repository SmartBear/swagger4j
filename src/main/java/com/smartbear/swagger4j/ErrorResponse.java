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

package com.smartbear.swagger4j;

/**
 * Holds a Swagger Error Responses that can be defined for an API Operation - see
 * <a href="https://github.com/wordnik/swagger-core/wiki/Errors" target="_new">https://github.com/wordnik/swagger-core/wiki/Errors</a>
 */

public interface ErrorResponse {

    public int getCode();

    public void setCode(int code);

    public String getReason();

    public void setReason(String reason);
}
