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

package org.cojen.classfile.attribute;

import java.io.DataInput;
import java.io.IOException;

import org.cojen.classfile.Attribute;
import org.cojen.classfile.ConstantInfo;
import org.cojen.classfile.ConstantPool;

import org.cojen.classfile.constant.ConstantMethodHandleInfo;

/**
 * 
 *
 * @author Brian S O'Neill
 */
public class BootstrapMethodsAttr extends Attribute {
    private final Method[] mMethods;

    public BootstrapMethodsAttr(ConstantPool cp, String name, int length, DataInput din)
        throws IOException
    {
        super(cp, name);

        mMethods = new Method[din.readUnsignedShort()];

        for (int i=0; i<mMethods.length; i++) {
            ConstantMethodHandleInfo info =
                (ConstantMethodHandleInfo) cp.getConstant(din.readUnsignedShort());

            ConstantInfo[] args = new ConstantInfo[din.readUnsignedShort()];

            for (int j=0; j<args.length; j++) {
                int argRef = din.readUnsignedShort();
                args[j] = cp.getConstant(argRef);
            }

            mMethods[i] = new Method(info, args);
        }
    }

    public int getMethodCount() {
        return mMethods.length;
    }

    public Method getMethod(int i) {
        return mMethods[i];
    }

    @Override
    public BootstrapMethodsAttr copyTo(ConstantPool cp) {
        // FIXME
        throw null;
    }

    @Override
    public int getLength() {
        int length = 2;
        for (Method m : mMethods) {
            length += 4;
            length += 2 * m.getArgCount();
        }
        return length;
    }

    public static final class Method {
        private final ConstantMethodHandleInfo mInfo;
        private final ConstantInfo[] mArgs;

        Method(ConstantMethodHandleInfo info, ConstantInfo[] args) {
            mInfo = info;
            mArgs = args;
        }

        public ConstantMethodHandleInfo getInfo() {
            return mInfo;
        }

        public int getArgCount() {
            return mArgs.length;
        }

        public ConstantInfo getArg(int i) {
            return mArgs[i];
        }
    }
}
