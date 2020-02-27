package DesignPatterns.Mediator;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Colleague extends CodeGenerator {

    String mediatorInterface;

    public Colleague(String filename, String mediatorInterface) {
        super(filename);
        this.mediatorInterface = mediatorInterface;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, true, null, null);

        // fields
        FieldDeclaration mediatorObject = this.createFieldDeclaration("mediator",
                this.createSimpleType(this.mediatorInterface),
                CodeGenerator.privateKeyword, false);
        this.classDeclaration.bodyDeclarations().add(mediatorObject);

        // constructor
        MethodDeclaration constructor = this.createConstructor(this.fileName,
                CodeGenerator.publicKeyword);
        SingleVariableDeclaration mediatorParameter = this.createSingleVariableDeclaration(
                "mediator",
                this.createSimpleType(this.mediatorInterface)
        );
        constructor.parameters().add(mediatorParameter);
        Block constructorBlock = this.createBlock();
        FieldAccess mediatorThis = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("mediator")
        );
        Assignment mediatorAssignment = this.createAssignmentExpression(
                mediatorThis,
                this.abstractSyntaxTree.newSimpleName("mediator")
        );
        constructorBlock.statements().add(
                this.abstractSyntaxTree.newExpressionStatement(mediatorAssignment)
        );
        constructor.setBody(constructorBlock);

        // send method
        MethodDeclaration sendMethod = this.declareMethod(
                "send",
                this.createPrimitiveType(CodeGenerator.voidType),
                CodeGenerator.publicKeyword, false, false
        );
        SingleVariableDeclaration messageParameter = this.createSingleVariableDeclaration(
                "message",
                this.createSimpleType("String")
        );
        sendMethod.parameters().add(messageParameter);
        Block sendMethodBlock = this.createBlock();
        FieldAccess mediatorThisAgain = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("mediator")
        );
        MethodInvocation sendInvocation = this.createMethodInvocation(
                "send",
                mediatorThisAgain
        );
        ThisExpression thisExpressionArgument = this.abstractSyntaxTree.newThisExpression();
        sendInvocation.arguments().add(this.abstractSyntaxTree.newSimpleName("message"));
        sendInvocation.arguments().add(thisExpressionArgument);
        sendMethodBlock.statements().add(
                this.abstractSyntaxTree.newExpressionStatement(sendInvocation));
        sendMethod.setBody(sendMethodBlock);

        // abstract method
        MethodDeclaration receiveMethod = this.declareMethod(
                "receive",
                this.createPrimitiveType(CodeGenerator.voidType),
                CodeGenerator.publicKeyword,
                false, true
        );
        SingleVariableDeclaration messageParameterAgain = this.createSingleVariableDeclaration(
                "message",
                this.createSimpleType("String")
        );
        receiveMethod.parameters().add(messageParameterAgain);

        // adding methods to class
        this.classDeclaration.bodyDeclarations().add(constructor);
        this.classDeclaration.bodyDeclarations().add(sendMethod);
        this.classDeclaration.bodyDeclarations().add(receiveMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
