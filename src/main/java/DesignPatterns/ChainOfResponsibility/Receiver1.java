package DesignPatterns.ChainOfResponsibility;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentInformationMapping;

import javax.management.StandardEmitterMBean;
import java.lang.reflect.Method;

public class Receiver1 extends CodeGenerator {

    String handlerClass;

    public Receiver1(String filename, String handlerClass) {
        super(filename);
        this.handlerClass = handlerClass;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, this.handlerClass, null);

        // constructor
        MethodDeclaration constructor = this.createConstructor(this.fileName, CodeGenerator.publicKeyword);
        SingleVariableDeclaration parameter = this.createSingleVariableDeclaration("successor",
                this.createSimpleType(this.handlerClass));
        constructor.parameters().add(parameter);
        Block constructorBlock = this.createBlock();
        SuperConstructorInvocation superConstructorInvocation = this.createSuperConstructorInvocation(null);
        superConstructorInvocation.arguments().add(this.abstractSyntaxTree.newSimpleName("successor"));
        constructorBlock.statements().add(superConstructorInvocation);
        constructor.setBody(constructorBlock);

        // overriden method
        MethodDeclaration handleRequest = this.declareMethod("handleRequest",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                false, false);
        Block handleRequestBody = this.createBlock();
        StringLiteral printValue = this.abstractSyntaxTree.newStringLiteral();
        printValue.setLiteralValue("Receiver : Passing the request along the chain");
        Statement printStatement = this.createPrintStatement(printValue);
        handleRequestBody.statements().add(printStatement);
        SuperMethodInvocation superMethodInvocation = this.createSuperMethodInvocation(
                this.abstractSyntaxTree.newSimpleName("handleRequest")
        );
        handleRequestBody.statements().add(this.abstractSyntaxTree.newExpressionStatement(superMethodInvocation));
        handleRequest.setBody(handleRequestBody);

        // adding methods to class
        this.classDeclaration.bodyDeclarations().add(constructor);
        this.classDeclaration.bodyDeclarations().add(handleRequest);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
