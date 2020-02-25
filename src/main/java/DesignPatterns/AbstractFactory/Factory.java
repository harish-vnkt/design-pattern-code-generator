package DesignPatterns.AbstractFactory;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Factory extends CodeGenerator {

    String abstractFactoryInterface, productAClass, productBClass;

    public Factory(String filename, String abstractFactoryInterface, String productAClass, String productBClass) {
        super(filename);
        this.abstractFactoryInterface = abstractFactoryInterface;
        this.productAClass = productAClass;
        this.productBClass = productBClass;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, this.abstractFactoryInterface);

        // method for creating product A
        MethodDeclaration createProductA = this.declareMethod("create" + this.productAClass,
                this.createSimpleType(this.productAClass), CodeGenerator.publicKeyword, false, false);
        Block productABlock = this.createBlock();
        StringLiteral stringA = this.abstractSyntaxTree.newStringLiteral();
        stringA.setLiteralValue("Creating product 1");
        Statement printStatementA = this.createPrintStatement(stringA);
        productABlock.statements().add(printStatementA);
        ClassInstanceCreation instanceA =
                this.createInstanceCreationExpression(this.createSimpleType(this.productAClass));
        ReturnStatement returnA = this.createReturnStatement(instanceA);
        productABlock.statements().add(returnA);
        createProductA.setBody(productABlock);

        // method for creating product B
        MethodDeclaration createProductB = this.declareMethod("create" + this.productBClass,
                this.createSimpleType(this.productBClass), CodeGenerator.publicKeyword, false, false);
        Block productBBlock = this.createBlock();
        StringLiteral stringB = this.abstractSyntaxTree.newStringLiteral();
        stringB.setLiteralValue("Creating product 2");
        Statement printStatementB = this.createPrintStatement(stringB);
        productBBlock.statements().add(printStatementB);
        ClassInstanceCreation instanceB =
                this.createInstanceCreationExpression(this.createSimpleType(this.productBClass));
        ReturnStatement returnB = this.createReturnStatement(instanceB);
        productBBlock.statements().add(returnB);
        createProductB.setBody(productBBlock);

        // adding above methods to class
        this.classDeclaration.bodyDeclarations().add(createProductA);
        this.classDeclaration.bodyDeclarations().add(createProductB);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;
        // return the document
        return this.document;
    }

}
