/*
 *  Copyright 2005-2010 Brian S O'Neill
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

package org.cojen.classfile.attribute;

import java.io.DataInput;
import java.io.IOException;
import org.cojen.classfile.ConstantPool;

/**
 * The class corresponds to the RuntimeVisibleAnnotations_attribute structure
 * defined for Java 5.
 *
 * @author Brian S O'Neill
 */
public class RuntimeVisibleAnnotationsAttr extends AnnotationsAttr {

    public RuntimeVisibleAnnotationsAttr(ConstantPool cp) {
        super(cp, RUNTIME_VISIBLE_ANNOTATIONS);
    }

    public RuntimeVisibleAnnotationsAttr(ConstantPool cp, String name) {
        super(cp, name);
    }
    
    public RuntimeVisibleAnnotationsAttr(ConstantPool cp, String name, int length, DataInput din)
        throws IOException
    {
        super(cp, name, length, din);
    }

    RuntimeVisibleAnnotationsAttr(ConstantPool cp, String name,
                                  RuntimeVisibleAnnotationsAttr source)
    {
        super(cp, name, source);
    }

    public RuntimeVisibleAnnotationsAttr copyTo(ConstantPool cp) {
        return new RuntimeVisibleAnnotationsAttr(cp, getName(), this);
    }
}
