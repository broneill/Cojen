/*
 *  Copyright 2004 Brian S O'Neill
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

package cojen.classfile.attr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import cojen.classfile.Attribute;
import cojen.classfile.ConstantPool;
import cojen.classfile.ConstantUTFInfo;
import cojen.classfile.FixedLocation;
import cojen.classfile.LocalVariable;
import cojen.classfile.Location;
import cojen.classfile.LocationRange;
import cojen.classfile.LocationRangeImpl;
import cojen.classfile.TypeDesc;

/**
 * This class corresponds to the LocalVariableTable_attribute structure as 
 * defined in section 4.7.7 of <i>The Java Virtual Machine Specification</i>.
 * 
 * @author Brian S O'Neill
 */
public class LocalVariableTableAttr extends Attribute {

    private List mEntries = new ArrayList(10);
    private List mCleanEntries;
    private int mRangeCount;
    
    public LocalVariableTableAttr(ConstantPool cp) {
        super(cp, LOCAL_VARIABLE_TABLE);
    }
    
    public LocalVariableTableAttr(ConstantPool cp, String name) {
        super(cp, name);
    }

    public LocalVariableTableAttr(ConstantPool cp, String name, int length, DataInput din)
        throws IOException
    {
        super(cp, name);

        int size = din.readUnsignedShort();
        for (int i=0; i<size; i++) {
            int start_pc = din.readUnsignedShort();
            int end_pc = start_pc + din.readUnsignedShort() + 1;
            int name_index = din.readUnsignedShort();
            int descriptor_index = din.readUnsignedShort();
            final int index = din.readUnsignedShort();

            final ConstantUTFInfo varName = 
                (ConstantUTFInfo)cp.getConstant(name_index);
            final ConstantUTFInfo varDesc = 
                (ConstantUTFInfo)cp.getConstant(descriptor_index);

            final Location startLocation = new FixedLocation(start_pc);
            final Location endLocation = new FixedLocation(end_pc);

            Set ranges = new HashSet();
            ranges.add(new LocationRangeImpl(startLocation, endLocation));
            final Set fRanges = Collections.unmodifiableSet(ranges);

            LocalVariable localVar = new LocalVariable() {
                private String mName;
                private TypeDesc mType;

                {
                    mName = varName.getValue();
                    mType = TypeDesc.forDescriptor(varDesc.getValue());
                }

                public String getName() {
                    return mName;
                }

                public void setName(String name) {
                    mName = name;
                }

                public TypeDesc getType() {
                    return mType;
                }
                
                public boolean isDoubleWord() {
                    return mType.isDoubleWord();
                }
                
                public int getNumber() {
                    return index;
                }

                public Set getLocationRangeSet() {
                    return fRanges;
                }
            };

            mEntries.add(new Entry(localVar, varName, varDesc));
        }
    }

    /**
     * Add an entry into the LocalVariableTableAttr.
     */
    public void addEntry(LocalVariable localVar) {
        String varName = localVar.getName();
        if (varName != null) {
            ConstantUTFInfo name = getConstantPool().addConstantUTF(varName);
            ConstantUTFInfo descriptor = 
                getConstantPool().addConstantUTF(localVar.getType().getDescriptor());
            mEntries.add(new Entry(localVar, name, descriptor));
        }

        mCleanEntries = null;
    }
    
    public int getLength() {
        clean();
        return 2 + 10 * mRangeCount;
    }
    
    public void writeDataTo(DataOutput dout) throws IOException {
        dout.writeShort(mRangeCount);

        int size = mCleanEntries.size();
        for (int i=0; i<size; i++) {
            Entry entry = (Entry)mEntries.get(i);
            LocalVariable localVar = entry.mLocalVar;

            Set ranges = localVar.getLocationRangeSet();
            if (ranges == null) {
                continue;
            }

            int name_index = entry.mName.getIndex();
            int descriptor_index = entry.mDescriptor.getIndex();
            int index = localVar.getNumber();

            check("local variable table entry name index", name_index);
            check("local variable table entry descriptor index", descriptor_index);
            check("local variable table entry index", index);

            Iterator it = ranges.iterator();
            while (it.hasNext()) {
                LocationRange range = (LocationRange)it.next();

                Location startLocation = range.getStartLocation();
                Location endLocation = range.getEndLocation();

                int start_pc = startLocation.getLocation();
                int length = endLocation.getLocation() - start_pc - 1;

                check("local variable table entry start PC", start_pc);

                dout.writeShort(start_pc);
                dout.writeShort(length);
                dout.writeShort(name_index);
                dout.writeShort(descriptor_index);
                dout.writeShort(index);
            }
        }
    }

    private void check(String type, int addr) throws IllegalStateException {
        if (addr < 0 || addr > 65535) {
            throw new IllegalStateException("Value for " + type + " out of " +
                                            "valid range: " + addr);
        }
    }

    private void clean() {
        if (mCleanEntries != null) {
            return;
        }

        // Clean out entries that are incomplete or bogus.

        int size = mEntries.size();
        mCleanEntries = new ArrayList(size);
        mRangeCount = 0;

    outer:
        for (int i=0; i<size; i++) {
            Entry entry = (Entry)mEntries.get(i);
            LocalVariable localVar = entry.mLocalVar;

            Set ranges = localVar.getLocationRangeSet();
            if (ranges == null || ranges.size() == 0) {
                continue;
            }

            Iterator it = ranges.iterator();
            while (it.hasNext()) {
                LocationRange range = (LocationRange)it.next();

                Location startLocation = range.getStartLocation();
                Location endLocation = range.getEndLocation();

                if (startLocation == null || endLocation == null) {
                    continue outer;
                }

                int start_pc = startLocation.getLocation();
                int length = endLocation.getLocation() - start_pc - 1;

                if (length < 0) {
                    continue outer;
                }
            }
            
            mCleanEntries.add(entry);
            mRangeCount += entry.getRangeCount();
        }
    }

    private static class Entry {
        public LocalVariable mLocalVar;
        public ConstantUTFInfo mName;
        public ConstantUTFInfo mDescriptor;

        public Entry(LocalVariable localVar,
                     ConstantUTFInfo name, ConstantUTFInfo descriptor) {

            mLocalVar = localVar;
            mName = name;
            mDescriptor = descriptor;
        }

        public int getRangeCount() {
            return mLocalVar.getLocationRangeSet().size();
        }
    }
}
