/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.core.impl.testdata.domain.reflect.accessmodifier;

import java.util.Collection;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.impl.domain.solution.descriptor.SolutionDescriptor;
import org.optaplanner.core.impl.testdata.domain.TestdataEntity;
import org.optaplanner.core.impl.testdata.domain.TestdataObject;
import org.optaplanner.core.impl.testdata.domain.TestdataValue;

@PlanningSolution
public class TestdataAccessModifierSolution extends TestdataObject implements Solution<SimpleScore> {

    private static final String STATIC_FINAL_FIELD = "staticFinalFieldValue";

    private static Object staticField;

    public static String getStaticFinalField() {
        return STATIC_FINAL_FIELD;
    }

    public static Object getStaticField() {
        return staticField;
    }

    public static void setStaticField(Object staticField) {
        TestdataAccessModifierSolution.staticField = staticField;
    }

    public static SolutionDescriptor buildSolutionDescriptor() {
        return SolutionDescriptor.buildSolutionDescriptor(TestdataAccessModifierSolution.class, TestdataEntity.class);
    }

    private final String finalField;
    private String readWriteOnlyField;

    private List<TestdataValue> valueList;
    private List<TestdataEntity> entityList;

    private SimpleScore score;

    private TestdataAccessModifierSolution() {
        finalField = "No-argument constructor";
    }

    public TestdataAccessModifierSolution(String code) {
        super(code);
        finalField = "Constructor with argument code (" + code + ")";
    }

    public String getFinalField() {
        return finalField;
    }

    public String getReadOnlyField() {
        return "read" + readWriteOnlyField;
    }

    public void setWriteOnlyField(String writeOnlyField) {
        if (!writeOnlyField.startsWith("write")) {
            throw new IllegalArgumentException("The writeOnlyField (" + writeOnlyField + ") should start with write.");
        }
        readWriteOnlyField = writeOnlyField.substring("write".length());
    }

    @ValueRangeProvider(id = "valueRange")
    public List<TestdataValue> getValueList() {
        return valueList;
    }

    public void setValueList(List<TestdataValue> valueList) {
        this.valueList = valueList;
    }

    @PlanningEntityCollectionProperty
    public List<TestdataEntity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<TestdataEntity> entityList) {
        this.entityList = entityList;
    }

    public SimpleScore getScore() {
        return score;
    }

    public void setScore(SimpleScore score) {
        this.score = score;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public Collection<? extends Object> getProblemFacts() {
        return valueList;
    }

}