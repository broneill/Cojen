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
 * This class corresponds to the CONSTANT_Utf8_info structure as defined in
 * <i>The Java Virtual Machine Specification</i>.
 * 
 * @author Brian S O'Neill
 */
public class ConstantUTFInfo extends ConstantInfo {
    private final String mStr;
    
    public ConstantUTFInfo(String str) {
        super(TAG_UTF8);
        mStr = str;
    }
    
    public String getValue() {
        return mStr;
    }

    public ConstantUTFInfo copyTo(ConstantPool cp) {
        return cp.addConstantUTF(mStr);
    }

    public int hashCode() {
        return mStr.hashCode();
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof ConstantUTFInfo) {
            ConstantUTFInfo other = (ConstantUTFInfo)obj;
            return mStr.equals(other.mStr);
        }
        
        return false;
    }
    
    public void writeTo(DataOutput dout) throws IOException {
        super.writeTo(dout);
        dout.writeUTF(mStr);
    }

    public String toString() {
        return "CONSTANT_Utf8_info: " + getValue();
    }
}
