package DesignPatterns.Factory;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Creator extends CodeGenerator {

    String productInterface;

    public Creator(String filename, String productInterface) {
        super(filename);
        this.productInterface = productInterface;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, true, null, null);

        // field declarations
        FieldDeclaration productField = this.createFieldDeclaration("product",
                this.createSimpleType(this.productInterface), CodeGenerator.privateKeyword, false);
        this.classDeclaration.bodyDeclarations().add(productField);

        // abstract method declaration
        MethodDeclaration factoryMethod = this.declareMethod("factoryMethod",
                this.createSimpleType(this.productInterface), CodeGenerator.publicKeyword, false, true);
        this.classDeclaration.bodyDeclarations().add(factoryMethod);

        // operation method
        MethodDeclaration operationMethod = this.declareMethod("operation",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                false, false);
        Block operationBlock = this.createBlock();
        MethodInvocation factoryInvocation = this.createMethodInvocation("factoryMethod", null);
        Assignment factoryAssignment = this.createAssignmentExpression(
                this.abstractSyntaxTree.newSimpleName("product"),
                factoryInvocation
        );
        operationBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(factoryAssignment));
        operationMethod.setBody(operationBlock);

        // add method to class
        this.classDeclaration.bodyDeclarations().add(operationMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;

    }

}
