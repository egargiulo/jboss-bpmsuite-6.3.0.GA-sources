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

package org.jboss.errai.bus.client.tests.support;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * @author Mike Brock
 */
@Portable
public class EntityWithUnqualifiedFields {
  
  private Object field1;
  private Object field2;

  public EntityWithUnqualifiedFields() {
  }

  public Object getField1() {
    return field1;
  }

  public void setField1(Object field1) {
    this.field1 = field1;
  }

  public Object getField2() {
    return field2;
  }

  public void setField2(Object field2) {
    this.field2 = field2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EntityWithUnqualifiedFields)) return false;

    EntityWithUnqualifiedFields that = (EntityWithUnqualifiedFields) o;

    return !(field1 != null ? !field1.equals(that.field1) : that.field1 != null)
            && !(field2 != null ? !field2.equals(that.field2) : that.field2 != null);
  }
}
