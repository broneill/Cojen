/*
 *  Copyright 2010 Brian S O'Neill
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

import org.cojen.classfile.attribute.CodeAttr;

/**
 * 
 *
 * @author Brian S O'Neill
 */
public class ClassFileRewriter {
    public static void main(String[] args) throws Exception {
        ClassFileRewriter rewriter = new ClassFileRewriter();
        rewriter.rewrite(new File(args[0]), new File(args[1]));
    }

    public ClassFileRewriter() {
    }

    public void rewrite(File sourceFile, File targetFile) throws IOException {
        if (sourceFile.isDirectory()) {
            for (File f : sourceFile.listFiles()) {
                if (f.isDirectory() || f.getName().endsWith(".class")) {
                    rewrite(f, targetFile);
                }
            }
            return;
        }

        InputStream in = new BufferedInputStream(new FileInputStream(sourceFile));
        ClassFile source;
        try {
            source = ClassFile.readFrom(in);
        } finally {
            in.close();
        }

        System.out.println(source);
        ClassFile target = rewrite(source);

        if (targetFile.isDirectory()) {
            targetFile = new File(targetFile, target.getClassName().replace(".", "/") + ".class");
            targetFile.getParentFile().mkdirs();
        }

        OutputStream out = new BufferedOutputStream(new FileOutputStream(targetFile));
        try {
            target.writeTo(out);
        } finally {
            out.close();
        }
    }

    public ClassFile rewrite(ClassFile source) {
        ClassFile target = new ClassFile(source.getClassName(), source.getSuperClassName());
        rewrite(source, target);
        return target;
    }

    private void rewrite(ClassFile source, ClassFile target) {
        target.setTarget(source.getTarget());
        target.setModifiers(source.getModifiers());

        for (String iface : source.getInterfaces()) {
            target.addInterface(iface);
        }

        for (Attribute attr : source.getAttributes()) {
            target.addAttribute(attr);
        }

        for (FieldInfo field : source.getFields()) {
            field.copyTo(target);
        }

        for (MethodInfo method : source.getAllMethods()) {
            //method.copyTo(target);
            rewrite(method, target.addMethod(method));
        }
    }

    private void rewrite(MethodInfo sourceMethod, MethodInfo targetMethod) {
        for (Attribute attr : sourceMethod.getAttributes()) {
            if (!(attr instanceof CodeAttr)) {
                targetMethod.addAttribute(attr);
            }
        }

        if (sourceMethod.getCodeAttr() == null) {
            return;
        }

        // FIXME: save local variables (if debug info exists)

        CodeBuilder b = targetMethod.newCodeBuilder();
        new CodeDisassembler(sourceMethod).disassemble(b);
    }
}
