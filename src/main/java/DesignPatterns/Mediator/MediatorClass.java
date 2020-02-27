package DesignPatterns.Mediator;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class MediatorClass extends CodeGenerator {

    String mediatorInterface, colleagueInterface, colleague1Class, colleague2Class;

    public MediatorClass(String filename, String mediatorInterface, String colleagueInterface,
                         String colleague1Class, String colleague2Class) {
        super(filename);
        this.mediatorInterface = mediatorInterface;
        this.colleagueInterface = colleagueInterface;
        this.colleague1Class = colleague1Class;
        this.colleague2Class = colleague2Class;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, this.mediatorInterface);

        // fields
        FieldDeclaration colleague1 = this.createFieldDeclaration("colleague1",
                this.createSimpleType(this.colleagueInterface), CodeGenerator.privateKeyword,
                false);
        FieldDeclaration colleague2 = this.createFieldDeclaration("colleague2",
                this.createSimpleType(this.colleagueInterface), CodeGenerator.privateKeyword,
                false);
        this.classDeclaration.bodyDeclarations().add(colleague1);
        this.classDeclaration.bodyDeclarations().add(colleague2);

        // constructor
        MethodDeclaration constructor = this.createConstructor(this.fileName,
                CodeGenerator.publicKeyword);
        Block constructorBlock = this.createBlock();
        FieldAccess colleague1This = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("colleague1")
        );
        ClassInstanceCreation colleague1Instance = this.createInstanceCreationExpression(
                this.createSimpleType(this.colleague1Class)
        );
        Assignment colleague1Assignment = this.createAssignmentExpression(colleague1This,
                colleague1Instance);
        FieldAccess colleague2This = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("colleague2")
        );
        ClassInstanceCreation colleague2Instance = this.createInstanceCreationExpression(
                this.createSimpleType(this.colleague2Class)
        );
        Assignment colleague2Assignment = this.createAssignmentExpression(colleague2This,
                colleague2Instance);
        constructorBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(colleague1Assignment));
        constructorBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(colleague2Assignment));
        constructor.setBody(constructorBlock);

        // overridden method
        MethodDeclaration sendMethod = this.declareMethod("send",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                false, false);
        SingleVariableDeclaration messageParameter = this.createSingleVariableDeclaration("message",
                this.createSimpleType("String"));
        SingleVariableDeclaration colleagueParameter = this.createSingleVariableDeclaration("colleague",
                this.createSimpleType(this.colleagueInterface));
        sendMethod.parameters().add(messageParameter);
        sendMethod.parameters().add(colleagueParameter);
        Block sendMethodBlock = this.createBlock();
        StringLiteral printValue = this.abstractSyntaxTree.newStringLiteral();
        printValue.setLiteralValue("Mediator : Mediating the interaction");
        Statement printStatement = this.createPrintStatement(printValue);
        sendMethodBlock.statements().add(printStatement);
        FieldAccess colleague1ThisAgain = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("colleague1")
        );
        InfixExpression infixExpression1 = this.createInfixExpression(
                this.abstractSyntaxTree.newSimpleName("colleague"),
                colleague1ThisAgain,
                CodeGenerator.equalsOperator
        );
        MethodInvocation receive1 = this.createMethodInvocation("receive",
                this.abstractSyntaxTree.newSimpleName("colleague1"));
        receive1.arguments().add(this.abstractSyntaxTree.newSimpleName("message"));
        IfStatement ifStatement1 = this.createIfStatement(
                infixExpression1,
                this.abstractSyntaxTree.newExpressionStatement(receive1),
                null);
        sendMethodBlock.statements().add(ifStatement1);
        FieldAccess colleague2ThisAgain = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("colleague2")
        );
        InfixExpression infixExpression2 = this.createInfixExpression(
                this.abstractSyntaxTree.newSimpleName("colleague"),
                colleague2ThisAgain,
                CodeGenerator.equalsOperator
        );
        MethodInvocation receive2 = this.createMethodInvocation("receive",
                this.abstractSyntaxTree.newSimpleName("colleague2"));
        receive2.arguments().add(this.abstractSyntaxTree.newSimpleName("message"));
        IfStatement ifStatement2 = this.createIfStatement(
                infixExpression2,
                this.abstractSyntaxTree.newExpressionStatement(receive2),
                null);
        sendMethodBlock.statements().add(ifStatement2);
        sendMethod.setBody(sendMethodBlock);

        // add methods to class
        this.classDeclaration.bodyDeclarations().add(constructor);
        this.classDeclaration.bodyDeclarations().add(sendMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
