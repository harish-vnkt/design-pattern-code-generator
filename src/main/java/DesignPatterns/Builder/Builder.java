package DesignPatterns.Builder;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Builder extends CodeGenerator {

    String complexObjectClass, productAClass, productBClass;

    public Builder(String filename, String complexObjectClass, String productAClass, String productBClass) {
        super(filename);
        this.complexObjectClass = complexObjectClass;
        this.productAClass = productAClass;
        this.productBClass = productBClass;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, null);

        // fields of the class
        FieldDeclaration complexObjectField = this.createFieldDeclaration("co",
                this.createSimpleType(this.complexObjectClass), CodeGenerator.privateKeyword, false);

        // constructor
        MethodDeclaration constructor = this.createConstructor(this.fileName, CodeGenerator.publicKeyword);
        Block constructorBlock = this.createBlock();
        ClassInstanceCreation complexObjectInstanceCreation = this.createInstanceCreationExpression(
                this.createSimpleType(this.complexObjectClass)
        );
        FieldAccess coThis = this.createFieldAccessExpression(this.abstractSyntaxTree.newSimpleName("co"));
        Assignment coAssignment = this.createAssignmentExpression(coThis, complexObjectInstanceCreation);
        constructorBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(coAssignment));
        constructor.setBody(constructorBlock);

        // buildPartA
        MethodDeclaration partA = this.declareMethod("buildPartA",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                false, false);
        Block partABlock = this.createBlock();
        StringLiteral partAPrintString = this.abstractSyntaxTree.newStringLiteral();
        partAPrintString.setLiteralValue("Creating product 1");
        Statement partAPrint = this.createPrintStatement(partAPrintString);
        partABlock.statements().add(partAPrint);
        ClassInstanceCreation productA = this.createInstanceCreationExpression(
                this.createSimpleType(this.productAClass));
        MethodInvocation addPartA = this.createMethodInvocation("add",
                this.abstractSyntaxTree.newSimpleName("co"));
        addPartA.arguments().add(productA);
        partABlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(addPartA));
        partA.setBody(partABlock);

        // buildPartB
        MethodDeclaration partB = this.declareMethod("buildPartB",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                false, false);
        Block partBBlock = this.createBlock();
        StringLiteral partBPrintString = this.abstractSyntaxTree.newStringLiteral();
        partBPrintString.setLiteralValue("Creating product 2");
        Statement partBPrint = this.createPrintStatement(partBPrintString);
        partBBlock.statements().add(partBPrint);
        ClassInstanceCreation productB = this.createInstanceCreationExpression(
                this.createSimpleType(this.productBClass));
        MethodInvocation addPartB = this.createMethodInvocation("add",
                this.abstractSyntaxTree.newSimpleName("co"));
        addPartB.arguments().add(productB);
        partBBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(addPartB));
        partB.setBody(partBBlock);

        // getResult
        MethodDeclaration getResult = this.declareMethod("getResult",
                this.createSimpleType(this.complexObjectClass), CodeGenerator.publicKeyword,
                false, false);
        Block getResultBlock = this.createBlock();
        ReturnStatement returnStatement = this.createReturnStatement(this.abstractSyntaxTree.newSimpleName("co"));
        getResultBlock.statements().add(returnStatement);
        getResult.setBody(getResultBlock);

        // add methods to class
        this.classDeclaration.bodyDeclarations().add(partA);
        this.classDeclaration.bodyDeclarations().add(partB);
        this.classDeclaration.bodyDeclarations().add(getResult);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
