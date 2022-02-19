/*
 *  Copyright 2004-2013 Brian S O'Neill
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

import java.io.*;
import java.util.*;

import org.cojen.classfile.attribute.*;
import org.cojen.classfile.constant.*;

/**
 *
 * @author Brian S O'Neill
 */
class OpcodeCounter {
    public static void main(String[] args) throws Exception {
        OpcodeCounter counter = new OpcodeCounter();
        counter.disassemble(new File(args[0]));
        counter.printResults();
    }

    private ClassFile mClassFile;
    private ConstantPool mCp;

    private byte[] mByteCodes;
    // Current address being decompiled.
    private int mAddress;

    private int[] mCounts;

    OpcodeCounter() {
        mCounts = new int[256 * 2];
    }

    public void printResults() {
        for (int i=0; i<mCounts.length; i++) {
            int count = mCounts[i];
            if (count > 0) {
                String mnemonic = Opcode.getMnemonic((byte) i);
                if (i >= 256) {
                    System.out.print("wide ");
                }
                System.out.println(mnemonic + "\t" + count);
            }
        }
    }

    public void disassemble(File f) throws IOException {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) for (File file : files) {
                disassemble(file);
            }
            return;
        }

        if (!f.getName().endsWith(".class")) {
            return;
        }

        InputStream in = new BufferedInputStream(new FileInputStream(f));
        ClassFile cf;
        try {
            cf = ClassFile.readFrom(in);
        } finally {
            in.close();
        }

        disassemble(cf);
    }

    public void disassemble(ClassFile cf) {
        mClassFile = cf;
        mCp = cf.getConstantPool();

        MethodInfo[] methods = mClassFile.getMethods();
        MethodInfo[] ctors = mClassFile.getConstructors();
        MethodInfo init = mClassFile.getInitializer();

        Object[] members = new Object[methods.length + ctors.length + ((init == null) ? 0 : 1)];

        int m = 0;

        for (int i=0; i<methods.length; i++) {
            members[m++] = methods[i];
        }

        for (int i=0; i<ctors.length; i++) {
            members[m++] = ctors[i];
        }

        if (init != null) {
            members[m++] = init;
        }

        for (int i=0; i<members.length; i++) {
            disassemble((MethodInfo) members[i]);
        }
    }

    private void disassemble(MethodInfo method) {
        CodeAttr code = method.getCodeAttr();
        if (code != null) {
            disassemble(code);
        }
    }

    private void disassemble(CodeAttr code) {
        CodeBuffer buffer = code.getCodeBuffer();
        mByteCodes = buffer.getByteCodes();

        for (mAddress = 0; mAddress < mByteCodes.length; mAddress++) {
            byte opcode = mByteCodes[mAddress];

            mCounts[opcode & 0xff]++;

            switch (opcode) {
                
            default:
                break;

                // Opcodes with no operands...

            case Opcode.NOP:
            case Opcode.BREAKPOINT:
            case Opcode.ACONST_NULL:
            case Opcode.ICONST_M1:
            case Opcode.ICONST_0:
            case Opcode.ICONST_1:
            case Opcode.ICONST_2:
            case Opcode.ICONST_3:
            case Opcode.ICONST_4:
            case Opcode.ICONST_5:
            case Opcode.LCONST_0:
            case Opcode.LCONST_1:
            case Opcode.FCONST_0:
            case Opcode.FCONST_1:
            case Opcode.FCONST_2:
            case Opcode.DCONST_0:
            case Opcode.DCONST_1:
            case Opcode.POP:
            case Opcode.POP2:
            case Opcode.DUP:
            case Opcode.DUP_X1:
            case Opcode.DUP_X2:
            case Opcode.DUP2:
            case Opcode.DUP2_X1:
            case Opcode.DUP2_X2:
            case Opcode.SWAP:
            case Opcode.IADD:  case Opcode.LADD: 
            case Opcode.FADD:  case Opcode.DADD:
            case Opcode.ISUB:  case Opcode.LSUB:
            case Opcode.FSUB:  case Opcode.DSUB:
            case Opcode.IMUL:  case Opcode.LMUL:
            case Opcode.FMUL:  case Opcode.DMUL:
            case Opcode.IDIV:  case Opcode.LDIV:
            case Opcode.FDIV:  case Opcode.DDIV:
            case Opcode.IREM:  case Opcode.LREM:
            case Opcode.FREM:  case Opcode.DREM:
            case Opcode.INEG:  case Opcode.LNEG:
            case Opcode.FNEG:  case Opcode.DNEG:
            case Opcode.ISHL:  case Opcode.LSHL:
            case Opcode.ISHR:  case Opcode.LSHR:
            case Opcode.IUSHR: case Opcode.LUSHR:
            case Opcode.IAND:  case Opcode.LAND:
            case Opcode.IOR:   case Opcode.LOR:
            case Opcode.IXOR:  case Opcode.LXOR:
            case Opcode.FCMPL: case Opcode.DCMPL:
            case Opcode.FCMPG: case Opcode.DCMPG:
            case Opcode.LCMP: 
            case Opcode.I2L:
            case Opcode.I2F:
            case Opcode.I2D:
            case Opcode.L2I:
            case Opcode.L2F:
            case Opcode.L2D:
            case Opcode.F2I:
            case Opcode.F2L:
            case Opcode.F2D:
            case Opcode.D2I:
            case Opcode.D2L:
            case Opcode.D2F:
            case Opcode.I2B:
            case Opcode.I2C:
            case Opcode.I2S:
            case Opcode.IRETURN:
            case Opcode.LRETURN:
            case Opcode.FRETURN:
            case Opcode.DRETURN:
            case Opcode.ARETURN:
            case Opcode.RETURN:
            case Opcode.IALOAD:
            case Opcode.LALOAD:
            case Opcode.FALOAD:
            case Opcode.DALOAD:
            case Opcode.AALOAD:
            case Opcode.BALOAD:
            case Opcode.CALOAD:
            case Opcode.SALOAD:
            case Opcode.IASTORE:
            case Opcode.LASTORE:
            case Opcode.FASTORE:
            case Opcode.DASTORE:
            case Opcode.AASTORE:
            case Opcode.BASTORE:
            case Opcode.CASTORE:
            case Opcode.SASTORE:
            case Opcode.ARRAYLENGTH:
            case Opcode.ATHROW:
            case Opcode.MONITORENTER:
            case Opcode.MONITOREXIT:
            case Opcode.ILOAD_0:
            case Opcode.LLOAD_0:
            case Opcode.FLOAD_0:
            case Opcode.DLOAD_0:
            case Opcode.ALOAD_0:
            case Opcode.ISTORE_0:
            case Opcode.LSTORE_0:
            case Opcode.FSTORE_0:
            case Opcode.DSTORE_0:
            case Opcode.ASTORE_0:
            case Opcode.ILOAD_1:
            case Opcode.LLOAD_1:
            case Opcode.FLOAD_1:
            case Opcode.DLOAD_1:
            case Opcode.ALOAD_1:
            case Opcode.ISTORE_1:
            case Opcode.LSTORE_1:
            case Opcode.FSTORE_1:
            case Opcode.DSTORE_1:
            case Opcode.ASTORE_1:
            case Opcode.ILOAD_2:
            case Opcode.LLOAD_2:
            case Opcode.FLOAD_2:
            case Opcode.DLOAD_2:
            case Opcode.ALOAD_2:
            case Opcode.ISTORE_2:
            case Opcode.LSTORE_2:
            case Opcode.FSTORE_2:
            case Opcode.DSTORE_2:
            case Opcode.ASTORE_2:
            case Opcode.ILOAD_3:
            case Opcode.LLOAD_3:
            case Opcode.FLOAD_3:
            case Opcode.DLOAD_3:
            case Opcode.ALOAD_3:
            case Opcode.ISTORE_3:
            case Opcode.LSTORE_3:
            case Opcode.FSTORE_3:
            case Opcode.DSTORE_3:
            case Opcode.ASTORE_3:
                // End opcodes with no operands.
                break;

                // Opcodes that load a constant from the constant pool...
            case Opcode.LDC:
            case Opcode.LDC_W:
            case Opcode.LDC2_W:
                int index;
                ConstantInfo constant;

                switch (opcode) {
                case Opcode.LDC:
                    index = readUnsignedByte();
                    break;
                case Opcode.LDC_W:
                case Opcode.LDC2_W:
                    index = readUnsignedShort();
                    break;
                default:
                    index = 0;
                    break;
                }

                break;

            case Opcode.NEW:
            case Opcode.ANEWARRAY:
            case Opcode.CHECKCAST:
            case Opcode.INSTANCEOF:
                constant = getConstant(readUnsignedShort());
                break;
            case Opcode.MULTIANEWARRAY:
                constant = getConstant(readUnsignedShort());
                int dims = readUnsignedByte();
                break;

            case Opcode.GETSTATIC:
            case Opcode.PUTSTATIC:
            case Opcode.GETFIELD:
            case Opcode.PUTFIELD:
                constant = getConstant(readUnsignedShort());
                break;

            case Opcode.INVOKEVIRTUAL:
            case Opcode.INVOKESPECIAL:
            case Opcode.INVOKESTATIC:
            case Opcode.INVOKEINTERFACE:
            case Opcode.INVOKEDYNAMIC:
                constant = getConstant(readUnsignedShort());

                if (opcode == Opcode.INVOKEINTERFACE) {
                    readShort();
                } else if (opcode == Opcode.INVOKEDYNAMIC) {
                    readShort();
                }
                break;

                // End opcodes that load a constant from the constant pool.

                // Opcodes that load or store local variables...

            case Opcode.ILOAD:
            case Opcode.LLOAD:
            case Opcode.FLOAD:
            case Opcode.DLOAD:
            case Opcode.ALOAD:
            case Opcode.RET:
                int varNum = readUnsignedByte();
                break;
            case Opcode.ISTORE:
            case Opcode.LSTORE:
            case Opcode.FSTORE:
            case Opcode.DSTORE:
            case Opcode.ASTORE:
                varNum = readUnsignedByte();
                break;
            case Opcode.IINC:
                varNum = readUnsignedByte();
                int incValue = readByte();
                break;

                // End opcodes that load or store local variables.

                // Opcodes that branch to another address.
            case Opcode.GOTO:
            case Opcode.JSR:
            case Opcode.IFNULL:
            case Opcode.IFNONNULL:
            case Opcode.IF_ACMPEQ:
            case Opcode.IF_ACMPNE:
            case Opcode.IFEQ:
            case Opcode.IFNE:
            case Opcode.IFLT:
            case Opcode.IFGE:
            case Opcode.IFGT:
            case Opcode.IFLE:
            case Opcode.IF_ICMPEQ:
            case Opcode.IF_ICMPNE:
            case Opcode.IF_ICMPLT:
            case Opcode.IF_ICMPGE:
            case Opcode.IF_ICMPGT:
            case Opcode.IF_ICMPLE:
                readShort();
                break;
            case Opcode.GOTO_W:
            case Opcode.JSR_W:
                readInt();
                break;

                // End opcodes that branch to another address.

                // Miscellaneous opcodes...
            case Opcode.BIPUSH:
                int value = readByte();
                break;
            case Opcode.SIPUSH:
                value = readShort();
                break;

            case Opcode.NEWARRAY:
                int atype = readByte();
                break;

            case Opcode.TABLESWITCH:
            case Opcode.LOOKUPSWITCH:
                int opcodeAddress = mAddress;
                // Read padding until address is 32 bit word aligned.
                while (((mAddress + 1) & 3) != 0) {
                    ++mAddress;
                }
                readInt();
                int[] cases;
                String[] locations;
                
                if (opcode == Opcode.TABLESWITCH) {
                    int lowValue = readInt();
                    int highValue = readInt();
                    int caseCount = highValue - lowValue + 1;
                    for (int i=0; i<caseCount; i++) {
                        readInt();
                    }
                } else {
                    int caseCount = readInt();
                    for (int i=0; i<caseCount; i++) {
                        readInt();
                        readInt();
                    }
                }
                break;

            case Opcode.WIDE:
                opcode = mByteCodes[++mAddress];

                mCounts[256 + (opcode & 0xff)]++;

                switch (opcode) {

                default:
                    break;

                case Opcode.ILOAD: case Opcode.ISTORE:
                case Opcode.LLOAD: case Opcode.LSTORE:
                case Opcode.FLOAD: case Opcode.FSTORE:
                case Opcode.DLOAD: case Opcode.DSTORE:
                case Opcode.ALOAD: case Opcode.ASTORE:
                case Opcode.RET:
                    readUnsignedShort();
                    break;
                case Opcode.IINC:
                    readUnsignedShort();
                    readShort();
                    break;
                }

                break;
            } // end huge switch
        } // end for loop
    }

    private ConstantInfo getConstant(int index) {
        try {
            return mCp.getConstant(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private int readByte() {
        return mByteCodes[++mAddress];
    }

    private int readUnsignedByte() {
        return mByteCodes[++mAddress] & 0xff;
    }

    private int readShort() {
        return (mByteCodes[++mAddress] << 8) | (mByteCodes[++mAddress] & 0xff);
    }

    private int readUnsignedShort() {
        return 
            ((mByteCodes[++mAddress] & 0xff) << 8) | 
            ((mByteCodes[++mAddress] & 0xff) << 0);
    }

    private int readInt() {
        return
            (mByteCodes[++mAddress] << 24) | 
            ((mByteCodes[++mAddress] & 0xff) << 16) |
            ((mByteCodes[++mAddress] & 0xff) << 8) |
            ((mByteCodes[++mAddress] & 0xff) << 0);
    }
}
