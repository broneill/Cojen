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
public class ConstantPackageInfo extends ConstantInfo {
    private final ConstantUTFInfo mStringConstant;
    
    public ConstantPackageInfo(ConstantUTFInfo constant) {
        super(TAG_PACKAGE);
        mStringConstant = constant;
    }

    public ConstantPackageInfo(ConstantPool cp, String str) {
        super(TAG_PACKAGE);
        mStringConstant = cp.addConstantUTF(str);
    }
    
    public String getValue() {
        return mStringConstant.getValue();
    }

    @Override
    public ConstantPackageInfo copyTo(ConstantPool cp) {
        // FIXME
        throw null;
    }

    @Override
    public int hashCode() {
        return mStringConstant.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ConstantPackageInfo) {
            ConstantPackageInfo other = (ConstantPackageInfo)obj;
            return mStringConstant.equals(other.mStringConstant);
        }
        return false;
    }
    
    @Override
    public void writeTo(DataOutput dout) throws IOException {
        super.writeTo(dout);
        dout.writeShort(mStringConstant.getIndex());
    }

    @Override
    public String toString() {
        return "CONSTANT_Package_info: " + getValue();
    }
}
