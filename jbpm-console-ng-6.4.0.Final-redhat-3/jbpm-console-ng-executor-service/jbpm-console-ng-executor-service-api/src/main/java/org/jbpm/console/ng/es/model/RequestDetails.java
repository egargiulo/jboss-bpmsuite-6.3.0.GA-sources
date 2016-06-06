/*
 * Copyright 2013 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jbpm.console.ng.es.model;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RequestDetails implements Serializable {

    private RequestSummary request;
    private List<ErrorSummary> errors;
    private List<RequestParameterSummary> params;

    public RequestDetails() {
    }

    public RequestDetails(RequestSummary request, List<ErrorSummary> errors, List<RequestParameterSummary> params) {
        this();
        this.request = request;
        this.errors = new Vector<ErrorSummary>(errors);
        this.params = new Vector<RequestParameterSummary>(params);
    }

    public RequestSummary getRequest() {
        return request;
    }

    public void setRequest(RequestSummary request) {
        this.request = request;
    }

    public List<ErrorSummary> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorSummary> errors) {
        this.errors = new Vector<ErrorSummary>(errors);
    }

    public List<RequestParameterSummary> getParams() {
        return params;
    }

    public void setParams(List<RequestParameterSummary> params) {
        this.params = new Vector<RequestParameterSummary>(params);
    }

}
