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
import org.cojen.classfile.MethodDesc;

/**
 * 
 *
 * @author Brian S O'Neill
 */
public class ConstantMethodTypeInfo extends ConstantInfo {
    private final MethodDesc mDescriptor;

    public ConstantMethodTypeInfo(ConstantUTFInfo desc) {
        this(desc.getValue());
    }

    public ConstantMethodTypeInfo(String desc) {
        super(TAG_METHOD_TYPE);
        mDescriptor = MethodDesc.forDescriptor(desc);
    }

    public MethodDesc getDescriptor() {
        return mDescriptor;
    }

    @Override
    public ConstantMethodTypeInfo copyTo(ConstantPool cp) {
        // FIXME
        throw null;
    }

    @Override
    public int hashCode() {
        return mDescriptor.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConstantMethodTypeInfo) {
            ConstantMethodTypeInfo other = (ConstantMethodTypeInfo) obj;
            return mDescriptor.equals(other.mDescriptor);
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "CONSTANT_MethodType_info: " + getDescriptor();
    }
}
