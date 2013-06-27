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

import com.smartbear.swagger4j.Api;
import com.smartbear.swagger4j.Operation;

import java.util.*;

/**
 * Default implementation of the Api interface
 *
 * @see Api
 */

public class ApiImpl implements Api {
    private String path;
    private String description;
    private List<Operation> operations = new ArrayList<Operation>();

    ApiImpl(String path) {
        setPath( path );
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        assert path != null && path.trim().length() > 0 : "path can not be null or empty";
        this.path = path;
    }

    @Override
    public String getDescription() {
        return description == null ? "" : description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Operation getOperation(String nickName) {
        assert nickName != null : "nickName can not be null";

        synchronized (operations)
        {
            for( Operation operation : operations )
                if( operation.getNickName().equals(nickName))
                    return operation;
        }

        return null;
    }

    @Override
    public List<Operation> getOperations() {
        return Collections.unmodifiableList(operations);
    }

    @Override
    public void removeOperation(Operation operation) {
        assert operation != null && operation.getNickName() != null : "operation can not be null and must have a nickname";
        synchronized (operations) {
            operations.remove(operation.getNickName());
        }
    }

    @Override
    public Operation addOperation(String nickName, Operation.Method method) {
        assert nickName != null && method != null : "operation can not be null and must have a nickname";
        assert getOperation(nickName ) == null : "operation with nickName [" + nickName + "] already exists";

        synchronized (operations) {
            OperationImpl result = new OperationImpl(nickName, method);
            operations.add( result );
            return result;
        }
    }
}
