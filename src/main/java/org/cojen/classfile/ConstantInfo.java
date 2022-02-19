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

package org.cojen.classfile;

import java.io.DataOutput;
import java.io.IOException;

/**
 * This class corresponds to the cp_info structure as defined in <i>The Java
 * Virtual Machine Specification</i>.  Subclasses should override the default
 * hashCode and equals methods so that the ConstantPool will only contain one
 * instance of this constant.
 * 
 * @author Brian S O'Neill
 */
public abstract class ConstantInfo {

    public static final int TAG_UTF8 = 1;
    public static final int TAG_INTEGER = 3;
    public static final int TAG_FLOAT = 4;
    public static final int TAG_LONG = 5;
    public static final int TAG_DOUBLE = 6;
    public static final int TAG_CLASS = 7;
    public static final int TAG_STRING = 8;
    public static final int TAG_FIELD = 9;
    public static final int TAG_METHOD = 10;
    public static final int TAG_INTERFACE_METHOD = 11;
    public static final int TAG_NAME_AND_TYPE = 12;
    public static final int TAG_METHOD_HANDLE = 15;
    public static final int TAG_METHOD_TYPE = 16;
    public static final int TAG_DYNAMIC_CONSTANT = 17;
    public static final int TAG_INVOKE_DYNAMIC = 18;
    public static final int TAG_MODULE = 19;
    public static final int TAG_PACKAGE = 20;

    // mIndex is manipulated by ConstantPool
    int mIndex = -1;
    private final int mTag;

    protected ConstantInfo(int tag) {
        mTag = tag;
    }

    /**
     * The index of this constant in the constant pool. Is -1 if the
     * index has not yet been resolved. Constant pool indexes are resolved
     * when the constant pool is written out.
     */
    public int getIndex() {
        return mIndex;
    }

    /**
     * Returns a new instance of this ConstantInfo, except stored in a
     * different ConstantPool.
     */
    public abstract ConstantInfo copyTo(ConstantPool cp);

    protected boolean hasPriority() {
        return false;
    }
    
    protected int getEntryCount() {
        return 1;
    }

    public void writeTo(DataOutput dout) throws IOException {
        dout.writeByte(mTag);
    }
}
