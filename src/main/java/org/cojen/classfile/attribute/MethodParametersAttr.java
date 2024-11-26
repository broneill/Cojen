/*
 *  Copyright 2021 Cojen.org
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
import java.io.DataOutput;
import java.io.IOException;

import org.cojen.classfile.Attribute;
import org.cojen.classfile.ConstantPool;

import org.cojen.classfile.constant.ConstantUTFInfo;

/**
 * 
 *
 * @author Brian S O'Neill
 */
public class MethodParametersAttr extends Attribute {
    public MethodParametersAttr(ConstantPool cp, String name, int length, DataInput din)
        throws IOException
    {
        super(cp, name);
        //System.out.println("MethodParameters");
        int paramCount = din.readUnsignedByte();
        for (int i=0; i<paramCount; i++) {
            int index = din.readUnsignedShort();
            ConstantUTFInfo paramName = (ConstantUTFInfo) cp.getConstant(index);
            int flags = din.readUnsignedShort();
            String paramNameStr = paramName == null ? "null" : paramName.getValue();
            /*
            System.out.println("  param: " + paramNameStr + ", flags: " +
                               Integer.toUnsignedString(flags, 16));
            */
        }
    }

    public MethodParametersAttr copyTo(ConstantPool cp) {
        // FIXME
        throw null;
    }

    public int getLength() {
        // FIXME
        throw null;
    }
    
    public void writeDataTo(DataOutput dout) throws IOException {
        // FIXME
        throw null;
    }
}
