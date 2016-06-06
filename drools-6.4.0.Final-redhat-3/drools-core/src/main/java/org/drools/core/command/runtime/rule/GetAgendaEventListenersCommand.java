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

package org.drools.core.command.runtime.rule;

import java.util.Collection;

import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.impl.KnowledgeCommandContext;
import org.kie.internal.command.Context;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.runtime.KieSession;

public class GetAgendaEventListenersCommand
    implements
    GenericCommand<Collection<AgendaEventListener>> {

    public Collection<AgendaEventListener> execute(Context context) {
        KieSession ksession = ((KnowledgeCommandContext) context).getKieSession();
        return ksession.getAgendaEventListeners();
    }

    public String toString() {
        return "session.getAgendaEventListeners();";
    }
}