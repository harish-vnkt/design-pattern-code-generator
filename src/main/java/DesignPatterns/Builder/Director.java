package DesignPatterns.Builder;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Director extends CodeGenerator {

    String complexObjectClass, builderClass;

    public Director(String filename, String complexObjectClass, String builderClass) {
        super(filename);
        this.complexObjectClass = complexObjectClass;
        this.builderClass = builderClass;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, null);

        // fields of the class
        FieldDeclaration complexObjectField = this.createFieldDeclaration("co",
                this.createSimpleType(this.complexObjectClass), CodeGenerator.privateKeyword, false);
        FieldDeclaration builderObjectField = this.createFieldDeclaration("builder",
                this.createSimpleType(this.builderClass), CodeGenerator.privateKeyword, false);
        this.classDeclaration.bodyDeclarations().add(complexObjectField);
        this.classDeclaration.bodyDeclarations().add(builderObjectField);

        // constructor
        MethodDeclaration constructor = this.createConstructor(this.fileName, CodeGenerator.publicKeyword);
        SingleVariableDeclaration constructorArgument = this.createSingleVariableDeclaration("builder",
                this.createSimpleType(this.builderClass));
        constructor.parameters().add(constructorArgument);
        Block constructorBlock = this.createBlock();
        FieldAccess builderThis = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("builder"));
        Assignment constructorAssignment = this.createAssignmentExpression(builderThis,
                this.abstractSyntaxTree.newSimpleName("builder"));
        constructorBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(constructorAssignment));
        constructor.setBody(constructorBlock);

        // construct method
        MethodDeclaration constructMethod = this.declareMethod("construct",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                null, null);
        Block constructBlock = this.createBlock();
        StringLiteral printArgument = this.abstractSyntaxTree.newStringLiteral();
        printArgument.setLiteralValue("Constructing a complex object using the builder");
        Statement printStatement = this.createPrintStatement(printArgument);
        constructBlock.statements().add(printArgument);
        MethodInvocation partA = this.createMethodInvocation("buildPartA",
                this.abstractSyntaxTree.newSimpleName("builder"));
        constructBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(partA));
        MethodInvocation partB = this.createMethodInvocation("buildPartB",
                this.abstractSyntaxTree.newSimpleName("builder"));
        constructBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(partB));
        MethodInvocation getResult = this.createMethodInvocation("getResult",
                this.abstractSyntaxTree.newSimpleName("builder"));
        Assignment getCO = this.createAssignmentExpression(this.abstractSyntaxTree.newSimpleName("co"),
                getResult);
        constructBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(getCO));
        constructMethod.setBody(constructBlock);

        // adding method bodies to class
        this.classDeclaration.bodyDeclarations().add(constructor);
        this.classDeclaration.bodyDeclarations().add(constructMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
