/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.core.impl.phase.custom;

import java.util.Map;

import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.impl.phase.Phase;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaplanner.core.impl.solver.ProblemFactChange;
import org.optaplanner.core.impl.solver.termination.AbstractTermination;

/**
 * Runs a custom algorithm as a {@link Phase} of the {@link Solver} that changes the planning variables.
 * Do not abuse to change the problems facts,
 * instead use {@link Solver#addProblemFactChange(ProblemFactChange)} for that.
 * <p>
 * An implementation must extend {@link AbstractCustomPhaseCommand} to ensure backwards compatibility in future versions.
 * @see AbstractCustomPhaseCommand
 */
public interface CustomPhaseCommand {

    /**
     * Called during {@link SolverFactory#buildSolver()}.
     * @param customPropertyMap never null
     * @throws IllegalArgumentException if any of the properties are not supported or don't parse correctly
     */
    void applyCustomProperties(Map<String, String> customPropertyMap);

    /**
     * Changes {@link Solution workingSolution} of {@link ScoreDirector#getWorkingSolution()}.
     * When the {@link Solution workingSolution} is modified, the {@link ScoreDirector} must be correctly notified
     * (through {@link ScoreDirector#beforeVariableChanged(Object, String)},
     * {@link ScoreDirector#afterProblemFactChanged(Object)}, etc),
     * otherwise calculated {@link Score}s will be corrupted.
     * <p>
     * Don't forget to call {@link ScoreDirector#triggerVariableListeners()} after each set of changes
     * (especially before every {@link ScoreDirector#calculateScore()} call)
     * to ensure all shadow variables are updated.
     * @param scoreDirector never null, the {@link ScoreDirector} that needs to get notified of the changes.
     */
    void changeWorkingSolution(ScoreDirector scoreDirector);

}
