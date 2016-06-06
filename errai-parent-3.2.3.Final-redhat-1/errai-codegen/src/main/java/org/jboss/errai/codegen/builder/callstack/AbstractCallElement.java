/*
 * Copyright 2011 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.errai.codegen.builder.callstack;

import org.jboss.errai.codegen.Context;
import org.jboss.errai.codegen.Statement;
import org.jboss.errai.codegen.exception.GenerationException;
import org.jboss.errai.codegen.meta.MetaClass;
import org.jboss.errai.codegen.util.GenUtil;

/**
 * @author Mike Brock <cbrock@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public abstract class AbstractCallElement implements CallElement {
  protected CallElement next;
  protected MetaClass resultType = null;

  protected final RuntimeException blame = new RuntimeException("Problem was caused by this call");

  public void nextOrReturn(CallWriter writer, Context ctx, Statement statement) {
    try {
      if (statement != null) {
        if (!writer.getCallString().isEmpty()) {
          writer.append(".");
        }
        writer.append(statement.generate(ctx));
        resultType = statement.getType();
      }

      if (next != null) {
        getNext().handleCall(writer, ctx, statement);
      }
    }
    catch (GenerationException e) {
      blameAndRethrow(e);
    }
  }

  @Override
  public CallElement setNext(CallElement next) {
    return this.next = next;
  }

  @Override
  public CallElement getNext() {
    return next;
  }

  public static void append(CallElement start, CallElement last) {
    CallElement el = start;
    while (el.getNext() != null)
      el = el.getNext();

    el.setNext(last);
  }

  @Override
  public MetaClass getResultType() {
    return resultType;
  }

  @Override
  public String toString() {
    return "[CallElement<" + next + ">]";
  }

  protected void blameAndRethrow(GenerationException e) {
    if (e.getCause() == null) {
      GenUtil.rewriteBlameStackTrace(blame);

      e.initCause(blame);
    }
    throw e;
  }
}
