package DesignPatterns.AbstractFactory;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Client extends CodeGenerator {

    String productAClass, productBClass, factoryClass;

    public Client(String filename, String productAClass, String productBClass, String factoryClass) {
        super(filename);
        this.productAClass = productAClass;
        this.productBClass = productBClass;
        this.factoryClass = factoryClass;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, null);

        // fields of the class
        FieldDeclaration productA = this.createFieldDeclaration("product1",
                this.createSimpleType(this.productAClass), CodeGenerator.privateKeyword, false);
        FieldDeclaration productB = this.createFieldDeclaration("product2",
                this.createSimpleType(this.productBClass), CodeGenerator.privateKeyword, false);
        FieldDeclaration factory = this.createFieldDeclaration("factory",
                this.createSimpleType(this.factoryClass), CodeGenerator.privateKeyword, false);
        this.classDeclaration.bodyDeclarations().add(productA);
        this.classDeclaration.bodyDeclarations().add(productB);
        this.classDeclaration.bodyDeclarations().add(factory);

        // constructor
        MethodDeclaration constructor = this.createConstructor(this.fileName, CodeGenerator.publicKeyword);
        Block constructorBlock = this.createBlock();
        FieldAccess factoryThis = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("factory"));
        SimpleName factoryVariable = this.abstractSyntaxTree.newSimpleName("factory");
        Assignment constructorAssignment = this.createAssignmentExpression(factoryThis, factoryVariable);
        constructorBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(constructorAssignment));
        constructor.setBody(constructorBlock);

        // operation method
        MethodDeclaration operationMethod = this.declareMethod("operation",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                false, false);
        Block operationBlock = this.createBlock();
        StringLiteral argument = this.abstractSyntaxTree.newStringLiteral();
        argument.setLiteralValue("Client : Delegating object creation to factory");
        Statement printStatement = this.createPrintStatement(argument);
        operationBlock.statements().add(printStatement);
        SimpleName productAVariable = this.abstractSyntaxTree.newSimpleName("product1");
        SimpleName productBVariable = this.abstractSyntaxTree.newSimpleName("product2");
        MethodInvocation productAMethod = this.createMethodInvocation("create" + this.productAClass,
                this.abstractSyntaxTree.newSimpleName("factory"));
        MethodInvocation productBMethod = this.createMethodInvocation("create" + this.productBClass,
                this.abstractSyntaxTree.newSimpleName("factory"));
        Assignment productAAssignment = this.createAssignmentExpression(productAVariable, productAMethod);
        Assignment productBAssignment = this.createAssignmentExpression(productBVariable, productBMethod);
        operationBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(productAAssignment));
        operationBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(productBAssignment));
        operationMethod.setBody(operationBlock);

        // adding method bodies to class
        this.classDeclaration.bodyDeclarations().add(constructor);
        this.classDeclaration.bodyDeclarations().add(operationMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;
        // return the document
        return this.document;
    }

}
