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
public class ConstantMethodHandleInfo extends ConstantInfo {
    private final int mKind;
    private final ConstantInfo mConstant;

    public ConstantMethodHandleInfo(int kind, ConstantInfo constant) {
        super(TAG_METHOD_HANDLE);
        mKind = kind;
        mConstant = constant;
    }

    public int getKind() {
        return mKind;
    }

    public ConstantInfo getConstant() {
        return mConstant;
    }

    @Override
    public ConstantMethodHandleInfo copyTo(ConstantPool cp) {
        // FIXME
        throw null;
    }

    @Override
    public int hashCode() {
        return mKind + mConstant.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConstantMethodHandleInfo) {
            ConstantMethodHandleInfo other = (ConstantMethodHandleInfo) obj;
            return mKind == other.mKind && mConstant.equals(other.mConstant);
        }
        
        return false;
    }

    @Override
    public String toString() {
        return "CONSTANT_MethodHandle_info: " + getKind() + ", " + getConstant();
    }
}
