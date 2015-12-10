package com.xtremelabs.robolectric.bytecode;

import javassist.ClassPool;
import javassist.CtClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AndroidTranslatorUnitTest {
    private ClassPool classPool;
    private AndroidTranslator androidTranslator;

    @Before public void setUp() throws Exception {
        classPool = new ClassPool(true);
        androidTranslator = new AndroidTranslator(null, null);
    }

    @Test
    public void whenMethodReturnsObject_shouldGenerateMethodBody() throws Exception {
        CtClass ctClass = classPool.get("java.lang.String");
        String methodBody = androidTranslator.generateMethodBody(
                ctClass, ctClass.getDeclaredMethod("substring", new CtClass[]{CtClass.intType}),
                ctClass, Type.OBJECT, false, false);
        assertEquals("if (!com.xtremelabs.robolectric.bytecode.RobolectricInternals.shouldCallDirectly(this)) {\n" +
                "Object x = com.xtremelabs.robolectric.bytecode.RobolectricInternals.methodInvoked(\n" +
                "  java.lang.String.class, \"substring\", this, new String[] {\"int\"}, new Object[] {com.xtremelabs.robolectric.bytecode.RobolectricInternals.autobox($1)});\n" +
                "if (x != null) return ((java.lang.String) x);\n" +
                "return null;\n" +
                "}\n", methodBody);
    }

    @Test
    public void whenMethodReturnsPrimitive_shouldGenerateMethodBody() throws Exception {
        CtClass ctClass = classPool.get("java.lang.String");
        String methodBody = androidTranslator.generateMethodBody(
                ctClass, ctClass.getDeclaredMethod("length"),
                ctClass, Type.OBJECT, false, false);
        assertEquals("if (!com.xtremelabs.robolectric.bytecode.RobolectricInternals.shouldCallDirectly(this)) {\n" +
                "Object x = com.xtremelabs.robolectric.bytecode.RobolectricInternals.methodInvoked(\n" +
                "  java.lang.String.class, \"length\", this, new String[0], new Object[0]);\n" +
                "if (x != null) return ((java.lang.String) x);\n" +
                "return null;\n" +
                "}\n", methodBody);
    }

    @Test
    public void whenMethodReturnsVoid_shouldGenerateMethodBody() throws Exception {
        CtClass ctClass = classPool.get("java.lang.Object");
        String methodBody = androidTranslator.generateMethodBody(
                ctClass, ctClass.getDeclaredMethod("wait"),
                ctClass, Type.VOID, false, false);
        assertEquals("if (!com.xtremelabs.robolectric.bytecode.RobolectricInternals.shouldCallDirectly(this)) {\n" +
                "com.xtremelabs.robolectric.bytecode.RobolectricInternals.methodInvoked(\n" +
                "  java.lang.Object.class, \"wait\", this, new String[] {\"long\"}, new Object[] {com.xtremelabs.robolectric.bytecode.RobolectricInternals.autobox($1)});\n" +
                "return;\n" +
                "}\n", methodBody);
    }

    @Test
    public void whenMethodIsStatic_shouldGenerateMethodBody() throws Exception {
        CtClass ctClass = classPool.get("java.lang.String");
        String methodBody = androidTranslator.generateMethodBody(
                ctClass, ctClass.getDeclaredMethod("valueOf", new CtClass[]{CtClass.intType}),
                ctClass, Type.OBJECT, true, false);
        assertEquals("if (!com.xtremelabs.robolectric.bytecode.RobolectricInternals.shouldCallDirectly(java.lang.String.class)) {\n" +
                "Object x = com.xtremelabs.robolectric.bytecode.RobolectricInternals.methodInvoked(\n" +
                "  java.lang.String.class, \"valueOf\", null, new String[] {\"int\"}, new Object[] {com.xtremelabs.robolectric.bytecode.RobolectricInternals.autobox($1)});\n" +
                "if (x != null) return ((java.lang.String) x);\n" +
                "return null;\n" +
                "}\n", methodBody);
    }

    @Test
    public void shouldGenerateParameterList() throws Exception {
        assertEquals(androidTranslator.makeParameterReplacementList(0), "");
        assertEquals(androidTranslator.makeParameterReplacementList(1), "$1");
        assertEquals(androidTranslator.makeParameterReplacementList(2), "$1, $2");
    }

    @Test
    public void shouldGenerateMethodBodyForEquals() throws Exception {
        CtClass ctClass = classPool.get("java.lang.Object");
        String methodBody = androidTranslator.generateMethodBody(
                ctClass, ctClass.getDeclaredMethod("equals", new CtClass[]{ctClass}),
                ctClass, Type.BOOLEAN, false, true);
        assertEquals("if (!com.xtremelabs.robolectric.bytecode.RobolectricInternals.shouldCallDirectly(this)) {\n" +
                "Object x = com.xtremelabs.robolectric.bytecode.RobolectricInternals.methodInvoked(\n" +
                "  java.lang.Object.class, \"equals\", this, new String[] {\"java.lang.Object\"}, new Object[] {com.xtremelabs.robolectric.bytecode.RobolectricInternals.autobox($1)});\n" +
                "if (x != null) return ((java.lang.Boolean) x).booleanValue();\n" +
                "return super.equals($1);}\n", methodBody);
    }
}
