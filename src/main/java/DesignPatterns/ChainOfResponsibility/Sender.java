package DesignPatterns.ChainOfResponsibility;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Sender extends CodeGenerator {

    String handlerClass, receiver1, receiver2;

    public Sender(String filename, String handlerClass, String receiver1, String receiver2) {
        super(filename);
        this.handlerClass = handlerClass;
        this.receiver1 = receiver1;
        this.receiver2 = receiver2;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, null);

        MethodDeclaration mainMethod = this.createMainMethodDeclaration();
        Block mainMethodBlock = this.createBlock();
        VariableDeclarationExpression handlerObject = this.createVariableDeclarationExpression("handler",
                this.createSimpleType(this.handlerClass));
        ClassInstanceCreation receiver1Instance = this.createInstanceCreationExpression(
                this.createSimpleType(this.receiver1)
        );
        ClassInstanceCreation receiver2Instance = this.createInstanceCreationExpression(
                this.createSimpleType(this.receiver2)
        );
        receiver1Instance.arguments().add(receiver2Instance);
        Assignment handlerAssignment = this.createAssignmentExpression(handlerObject, receiver1Instance);
        mainMethodBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(handlerAssignment));
        StringLiteral string1 = this.abstractSyntaxTree.newStringLiteral();
        string1.setLiteralValue("Issuing a request to handler object");
        Statement printStatement1 = this.createPrintStatement(string1);
        mainMethodBlock.statements().add(printStatement1);
        MethodInvocation handleRequest = this.createMethodInvocation("handleRequest",
                this.abstractSyntaxTree.newSimpleName("handler"));
        Statement printStatement2 = this.createPrintStatement(handleRequest);
        mainMethodBlock.statements().add(printStatement2);
        mainMethod.setBody(mainMethodBlock);

        // adding main method to class
        this.classDeclaration.bodyDeclarations().add(mainMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
