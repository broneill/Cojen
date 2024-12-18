/*
 *  Copyright 2004-2010 Brian S O'Neill
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

package org.cojen.classfile.constant;

import java.io.DataOutput;
import java.io.IOException;
import org.cojen.classfile.ConstantInfo;
import org.cojen.classfile.ConstantPool;

/**
 * This class corresponds to the CONSTANT_InterfaceMethodRef_info structure as
 * defined in <i>The Java Virtual Machine Specification</i>.
 * 
 * @author Brian S O'Neill
 */
public class ConstantInterfaceMethodInfo extends ConstantInfo {
    private final ConstantClassInfo mParentClass;
    private final ConstantNameAndTypeInfo mNameAndType;
    
    public ConstantInterfaceMethodInfo(ConstantClassInfo parentClass,
                                       ConstantNameAndTypeInfo nameAndType) {
        super(TAG_INTERFACE_METHOD);
        mParentClass = parentClass;
        mNameAndType = nameAndType;
    }

    public ConstantClassInfo getParentClass() {
        return mParentClass;
    }
    
    public ConstantNameAndTypeInfo getNameAndType() {
        return mNameAndType;
    }
    
    public ConstantInterfaceMethodInfo copyTo(ConstantPool cp) {
        return (ConstantInterfaceMethodInfo)cp
            .addConstant(new ConstantInterfaceMethodInfo
                         (mParentClass.copyTo(cp), mNameAndType.copyTo(cp)));
    }

    public int hashCode() {
        return mNameAndType.hashCode();
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof ConstantInterfaceMethodInfo) {
            ConstantInterfaceMethodInfo other = 
                (ConstantInterfaceMethodInfo)obj;
            return (mParentClass.equals(other.mParentClass) && 
                    mNameAndType.equals(other.mNameAndType));
        }
        
        return false;
    }
    
    public void writeTo(DataOutput dout) throws IOException {
        super.writeTo(dout);
        dout.writeShort(mParentClass.getIndex());
        dout.writeShort(mNameAndType.getIndex());
    }

    public String toString() {
        StringBuffer buf = 
            new StringBuffer("CONSTANT_InterfaceMethodref_info: ");
        buf.append(getParentClass().getType().getFullName());

        ConstantNameAndTypeInfo cnati = getNameAndType();

        buf.append(' ');
        buf.append(cnati.getName());
        buf.append(' ');
        buf.append(cnati.getType());

        return buf.toString();
    }
}
