package DesignPatternCodeGenerator;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mock class to test the functioning of methods of CodeGenerator
 *
 * @author Harish Venkataraman
 */
class CodeGeneratorMock extends CodeGenerator {

    public CodeGeneratorMock(String filename) {
        super(filename);
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, null);

        // main method
        MethodDeclaration mainMethod = this.createMainMethodDeclaration();

        // field declaration
        FieldDeclaration fieldDeclaration = this.createFieldDeclaration(
                "classField",
                this.createPrimitiveType(CodeGenerator.intType),
                CodeGenerator.privateKeyword,
                false
        );

        // operation method
        MethodDeclaration operationMethod = this.declareMethod("operation",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                false, false);
        Block operationBlock = this.createBlock();
        StringLiteral argument = this.abstractSyntaxTree.newStringLiteral();
        argument.setLiteralValue("Hello world");
        Statement printStatement = this.createPrintStatement(argument);
        operationBlock.statements().add(printStatement);
        FieldAccess fieldAccess = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("classField"));
        operationBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(fieldAccess));
        operationMethod.setBody(operationBlock);

        // add field to class
        this.classDeclaration.bodyDeclarations().add(fieldDeclaration);

        // add method to class
        this.classDeclaration.bodyDeclarations().add(mainMethod);
        this.classDeclaration.bodyDeclarations().add(operationMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to document
        this.applyEdits();
        // return this document
        return this.document;
    }

}

/**
 * Unit test cases for testing specific components of CodeGenerator
 */
public class CodeGeneratorTest {

    private CodeGeneratorMock mockCodeGenerator;
    private Document mockDocumentObject;


    public CodeGeneratorTest() throws BadLocationException {
        mockCodeGenerator = new CodeGeneratorMock("MockClassName");
        mockDocumentObject = mockCodeGenerator.buildCode();
    }

    /**
     * Tests createTypeDeclaration() method of CodeGenerator
     */
    @Test
    public void shouldCreateCorrectClass() {
        TypeDeclaration retrievedType = mockCodeGenerator.classDeclaration;
        String retrievedTypeName = retrievedType.getName().toString();
        String retrievedModifier = retrievedType.modifiers().get(0).toString();
        assertEquals("name should match", retrievedTypeName, "MockClassName");
        assertEquals("should be public", retrievedModifier, "public");
    }

    /**
     * Tests createMainMethod() method of CodeGenerator
     */
    @Test
    public void shouldAddMainMethod() {
        MethodDeclaration retrievedMainMethodDeclaration =
                mockCodeGenerator.classDeclaration.getMethods()[0];
        String retrievedMethodName = retrievedMainMethodDeclaration.getName().toString();
        String retrievedMethodReturnType = retrievedMainMethodDeclaration.getReturnType2().toString();
        String retrievedModifier = retrievedMainMethodDeclaration.modifiers().get(0).toString();
        assertEquals("should be main", retrievedMethodName, "main");
        assertEquals("should be void", retrievedMethodReturnType, "void");
        assertEquals("should be public", retrievedModifier, "public");
    }

    /**
     * Tests createFieldDeclaration() method of CodeGenerator
     */
    @Test
    public void shouldAddField() {
        FieldDeclaration retrievedFieldDeclaration =
                mockCodeGenerator.classDeclaration.getFields()[0];
        String retrievedFieldName = retrievedFieldDeclaration.fragments().get(0).toString();
        String retrievedType = retrievedFieldDeclaration.getType().toString();
        String retrievedModifier = retrievedFieldDeclaration.modifiers().get(0).toString();
        assertEquals("should be classField", retrievedFieldName, "classField");
        assertEquals("should be int", retrievedType, "int");
        assertEquals("should be private", retrievedModifier, "private");
    }

    /**
     * Tests createBlock() and createPrintStatement() method of CodeGenerator
     */
    @Test
    public void shouldAddBlock() {
        MethodDeclaration operationMethodDeclaration =
                mockCodeGenerator.classDeclaration.getMethods()[1];
        Block operationMethodBlock = operationMethodDeclaration.getBody();
        ExpressionStatement retrievedStatement = (ExpressionStatement) operationMethodBlock.statements().get(0);
        MethodInvocation retrievedFunctionCall = (MethodInvocation) retrievedStatement.getExpression();
        String retrievedFunctionName = retrievedFunctionCall.getName().toString();
        assertEquals("should be println", retrievedFunctionName, "println");
    }

    /**
     * Tests FieldAccess creation of CodeGenerator
     */
    @Test
    public void shouldAddFieldAcess() {
        MethodDeclaration operationMethodDeclaration =
                mockCodeGenerator.classDeclaration.getMethods()[1];
        Block operationMethodBlock = operationMethodDeclaration.getBody();
        ExpressionStatement retrievedStatement = (ExpressionStatement) operationMethodBlock.statements().get(1);
        FieldAccess retrievedFieldAccess = (FieldAccess) retrievedStatement.getExpression();
        String retrievedFieldName = retrievedFieldAccess.getName().toString();
        assertEquals("should be classField", retrievedFieldName, "classField");
    }

    @AfterClass
    public static void displayLogMessage() {
        Logger logger = LoggerFactory.getLogger("DesignPatternCodeGenerator.CodeGeneratorTest");
        logger.debug("Finished testing");
    }

}
