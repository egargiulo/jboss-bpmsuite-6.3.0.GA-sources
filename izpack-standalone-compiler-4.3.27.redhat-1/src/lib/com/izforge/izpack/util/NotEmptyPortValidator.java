/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 * 
 * http://izpack.org/
 * http://izpack.codehaus.org/
 * 
 * Copyright 2003 Tino Schwarze
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izforge.izpack.util;

import com.izforge.izpack.panels.ProcessingClient;
import com.izforge.izpack.panels.Validator;

/**
 * A validator to enforce non-empty fields for ports but allow System Property to be empty
 * <p/>
 * This validator can be used for rule input fields in the UserInputPanel to make sure that the user
 * entered something.
 *
 * @author Dustin Kut Moy Cheung
 */
public class NotEmptyPortValidator implements Validator
{

    public boolean validate(ProcessingClient client)
    {
        int numfields = client.getNumFields();

        // Do not check if field(0) is empty 
        // field(0) will be occupied by the System Property, which is optional
        if (numfields > 1) {
            for (int i = 1; i < numfields; i++)
            {
                String value = client.getFieldContents(i);

                if ((value == null) || (value.length() == 0))
                {
                    return false;
                }
            }
        }
        return true;
    }

}
