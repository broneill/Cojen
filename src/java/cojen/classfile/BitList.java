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

package cojen.classfile;

/**
 * A specialized, faster BitSet used by InstructionList.
 *
 * @author Brian S O'Neill
 */
final class BitList implements Cloneable {
    // Bits are stored little endian.
    private int[] mData;

    /**
     * @param capacity initial amount of bits to store
     */    
    public BitList(int capacity) {
        mData = new int[(capacity + 31) / 32];
    }

    public boolean get(int index) {
        return (mData[index / 32] & (0x80000000 >>> index)) != 0;
    }

    /**
     * @return true if any change made
     */
    public boolean set(int index) {
        int i = index / 32;
        int v = mData[i];
        return v != (mData[i] = v | (0x80000000 >>> index));
    }

    /**
     * @return true if any changes made
     */
    public boolean or(BitList list) {
        boolean changes = ensureCapacity(list.capacity());
        for (int i=list.mData.length; --i >= 0; ) {
            int v = mData[i];
            changes |= (v != (mData[i] = v | list.mData[i]));
        }
        return changes;
    }

    public boolean isAllClear() {
        for (int i=mData.length; --i >= 0; ) {
            if (mData[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllSet() {
        for (int i=mData.length; --i >= 0; ) {
            if (mData[i] != 0xffffffff) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if the bitwise or of the two lists is different than the
     * bitwise xor.
     */
    public boolean intersects(BitList list) {
        if (list != null) {
            for (int i=Math.min(mData.length, list.mData.length); --i >= 0; ) {
                int v1 = mData[i];
                int v2 = list.mData[i];
                if ((v1 | v2) != (v1 ^ v2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int hashCode() {
        int hash = 0;
        for (int i=mData.length; --i >= 0; ) {
            hash = hash * 31 + mData[i];
        }
        return hash;
    }

    public int capacity() {
        return mData.length * 32;
    }

    public boolean equals(Object obj) {
        if (obj instanceof BitList) {
            return java.util.Arrays.equals(mData, ((BitList)obj).mData);
        }
        return false;
    }

    public BitList copy() {
        return (BitList)clone();
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(mData.length + 2);
        buf.append('[');
        for (int i=0; i<mData.length; i++) {
            String binary = Integer.toBinaryString(mData[i]);
            for (int j=binary.length(); j<32; j++) {
                buf.append('0');
            }
            buf.append(binary);
        }
        buf.append(']');
        return buf.toString();
    }

    private boolean ensureCapacity(int capacity) {
        int len = (capacity + 31) / 32;
        if (len > mData.length) {
            int[] newData = new int[len];
            System.arraycopy(mData, 0, newData, 0, mData.length);
            mData = newData;
            return true;
        }
        return false;
    }
}