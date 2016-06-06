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

package org.jboss.errai.marshalling.client.marshallers;

import org.jboss.errai.common.client.protocols.SerializationParts;
import org.jboss.errai.marshalling.client.api.MarshallingSession;
import org.jboss.errai.marshalling.client.api.annotations.ClientMarshaller;
import org.jboss.errai.marshalling.client.api.annotations.ServerMarshaller;
import org.jboss.errai.marshalling.client.api.json.EJValue;
import org.jboss.errai.marshalling.client.util.NumbersUtils;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
@ClientMarshaller(Long.class)
@ServerMarshaller(Long.class)
public class LongMarshaller extends AbstractNumberMarshaller<Long> {
  @Override
  public Long[] getEmptyArray() {
    return new Long[0];
  }

  @Override
  public Long doNotNullDemarshall(final EJValue o, final MarshallingSession ctx) {
    if (o.isObject() != null) {
      final EJValue numValue = o.isObject().get(SerializationParts.NUMERIC_VALUE);

      if (numValue.isNumber() != null) {
        return new Double(numValue.isNumber().doubleValue()).longValue();
      }
      return Long.parseLong(numValue.isString().stringValue());
    }
    else {
      if (o.isNumber() != null) {
        return new Double(o.isNumber().doubleValue()).longValue();
      }
      return Long.parseLong(o.isString().stringValue());
    }
  }

  @Override
  public String doNotNullMarshall(final Long o, final MarshallingSession ctx) {
    return NumbersUtils.qualifiedNumericEncoding(o);
  }
}