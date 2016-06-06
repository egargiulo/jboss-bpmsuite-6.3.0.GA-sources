/**
 * Copyright (C) 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formModeler.core.processing.fieldHandlers;

import org.apache.commons.lang3.StringUtils;
import org.jbpm.formModeler.api.model.DataHolder;
import org.jbpm.formModeler.api.model.Field;
import org.jbpm.formModeler.api.model.Form;
import org.jbpm.formModeler.core.processing.*;
import org.jbpm.formModeler.core.processing.fieldHandlers.subform.checkers.SubformChecker;
import org.jbpm.formModeler.core.processing.fieldHandlers.subform.utils.SubFormHelper;
import org.jbpm.formModeler.core.processing.formRendering.FormErrorMessageBuilder;
import org.jbpm.formModeler.core.processing.formStatus.FormStatus;
import org.jbpm.formModeler.core.rendering.SubformFinderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named("org.jbpm.formModeler.core.processing.fieldHandlers.SubformFieldHandler")
public class SubformFieldHandler extends PersistentFieldHandler {
    private static transient Logger log = LoggerFactory.getLogger(SubformFieldHandler.class);

    @Inject
    private Instance<SubformChecker> checkers;
    private Set<SubformChecker> subformCheckers;

    @Inject
    private SubformFinderService subformFinderService;

    @Inject
    private FormErrorMessageBuilder formErrorMessageBuilder;

    @Inject
    private FormProcessor formProcessor;

    @Inject
    private SubFormHelper helper;

    private static int maxDepth = 2;


    @PostConstruct
    public void prepare() {
        subformCheckers = new TreeSet<SubformChecker>(new Comparator<SubformChecker>() {
            @Override
            public int compare(SubformChecker o1, SubformChecker o2) {
                return o1.getPriority() - o2.getPriority();
            }
        });
        for (SubformChecker p : checkers) {
            subformCheckers.add(p);
        }
    }

    /**
     * Determine the list of class types this field can generate. That is, normally,
     * a field can generate multiple outputs (an input text can generate Strings,
     * Integers, ...)
     *
     * @return the set of class types that can be generated by this handler.
     */
    public String[] getCompatibleClassNames() {
        return new String[]{Object.class.getName()};
    }


    public Object getValue(Field field, String inputName, Map parametersMap, Map filesMap, String desiredClassName, Object previousValue) throws Exception {
        Form form = getEnterDataForm(inputName, field);
        if (!checkSubformDepthAllowed(form, inputName)) return null;
        formProcessor.setValues(form, inputName, parametersMap, filesMap);
        FormStatusData status = formProcessor.read(form, inputName);
        if (status.isValid()) {
            // Check if form status is empty & if the object already exists to avoid null objects creation.
            if (status.isEmpty()) return null;
            Map m = formProcessor.getMapRepresentationToPersist(form, inputName);
            return m;
        } else {
            throw new IllegalArgumentException("Subform status is invalid.");
        }
    }

    @Override
    public Object persist( Field field, String inputName, Object fieldValue ) throws Exception {
        Form form = getEnterDataForm(inputName, field);
        Map representation = ( Map ) fieldValue;

        DataHolder holder = form.getHolders().iterator().next();

        FormNamespaceData rootNamespaceData = getNamespaceManager().getRootNamespace( inputName );
        FormStatusData rootData = formProcessor.read(rootNamespaceData.getForm(), rootNamespaceData.getNamespace());

        Object locadedObject = rootData.getLoadedObject( inputName );

        return formProcessor.persistFormHolder(form, inputName, representation, holder, locadedObject);
    }

    @Override
    public Object getStatusValue( Field field, String inputName, Object value, Map rootLoadedObjects ) {
        if (value == null) return Collections.EMPTY_MAP;
        if (!rootLoadedObjects.containsKey( inputName )) rootLoadedObjects.put( inputName, value );

        Form form = getEnterDataForm(inputName, field);
        DataHolder holder = form.getHolders().iterator().next();

        Map<String, Object> inputData = new HashMap();

        if (!StringUtils.isEmpty(holder.getInputId())) inputData.put(holder.getInputId(), value);

        Map<String, Object> outputData = new HashMap();

        if (!StringUtils.isEmpty(holder.getInputId())) outputData.put(holder.getOuputId(), value);

        Map<String, Object> loadedObjects = new HashMap();

        Map result = null;
        try {
            result = formProcessor.readValuesToLoad(form, inputData, new HashMap(), loadedObjects, inputName);
        } catch (Exception e) {
            log.error("Error getting status value for field: " + inputName, e);
        }

        formProcessor.read(form, inputName, result, loadedObjects);

        return result;
    }

    @Override
    public Map getParamValue(Field field, String inputName, Object value) {
        if (value == null)
            return Collections.EMPTY_MAP;

        FormNamespaceData data = getNamespaceManager().getNamespace(inputName);
        Field parentField = data.getForm().getField(data.getFieldNameInParent());
        Form childForm = getEnterDataForm(inputName, parentField);

        Map result = new HashMap();
        DataHolder holder = childForm.getHolders().iterator().next();
        for (Iterator it = childForm.getFormFields().iterator(); it.hasNext();) {
            Field childField = (Field) it.next();
            String bindingExpression = StringUtils.defaultIfEmpty(childField.getInputBinding(), childField.getOutputBinding());
            if (!holder.isAssignableForField(childField)) continue;
            try {
                Object val = holder.readFromBindingExperssion(value, bindingExpression);
                FieldHandler fieldManager = getFieldHandlersManager().getHandler(childField.getFieldType());
                Map childrenMap = fieldManager.getParamValue(childField, childField.getFieldName(), val);
                if (childrenMap != null) result.putAll(childrenMap);
            } catch (Exception e) {
                log.warn("Error reading value from field '" + childField.getFieldName() + "': ", e);
            }
        }

        return result;
    }


    public void addWrongChildFieldErrors(String namespace, Field field, List errors) {
        formErrorMessageBuilder.getWrongFormErrors(namespace, getEnterDataForm(namespace, field), errors);
    }

    public boolean isEmpty(Object value) {
        return value == null || "".equals(value);
    }

    protected Form getEnterDataForm(String namespace, Field field) {
        String formName = field.getDefaultSubform();

        return getForm(formName, namespace);
    }

    private Form getForm(String formPath, String namespace) {
        return subformFinderService.getFormByPath(formPath, namespace);
    }

    public static boolean checkSubformDepthAllowed(Form form, String namesapce) {
        StringTokenizer token = new StringTokenizer(namesapce, FormProcessor.NAMESPACE_SEPARATOR, false);
        String _id = form.getId().toString();
        int count = 0;
        while (token.hasMoreElements()) {
            String idToCompare = (String) token.nextElement();
            if (idToCompare.equals(_id)) {
                count++;
                if (count >= maxDepth) {
                    return false;
                }
            }

        }
        return true;

    }

    public Set<SubformChecker> getSubformCheckers() {
        return subformCheckers;
    }
}
