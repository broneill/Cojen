/*
 *  Copyright 2018 Cojen.org
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
 * 
 *
 * @author Brian S O'Neill
 */
public class ConstantInvokeDynamicInfo extends ConstantInfo {
    private final int mBootstrapIndex;
    private final ConstantNameAndTypeInfo mNameAndType;

    public ConstantInvokeDynamicInfo(int bootstrapIndex, ConstantNameAndTypeInfo nameAndType) {
        super(TAG_INVOKE_DYNAMIC);
        mBootstrapIndex = bootstrapIndex;
        mNameAndType = nameAndType;
    }

    /**
     * Returns the index into the BootstrapMethodsAttr of the ClassFile.
     */
    public int getBootstrapIndex() {
        return mBootstrapIndex;
    }

    public ConstantNameAndTypeInfo getNameAndType() {
        return mNameAndType;
    }

    @Override
    public ConstantInvokeDynamicInfo copyTo(ConstantPool cp) {
        // FIXME
        throw null;
    }

    @Override
    public int hashCode() {
        return mBootstrapIndex + mNameAndType.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConstantInvokeDynamicInfo) {
            ConstantInvokeDynamicInfo other = (ConstantInvokeDynamicInfo) obj;
            return mBootstrapIndex == other.mBootstrapIndex
                && mNameAndType.equals(other.mNameAndType);
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "CONSTANT_InvokeDynamic_info: " + getBootstrapIndex() + ", " + getNameAndType();
    }
}
