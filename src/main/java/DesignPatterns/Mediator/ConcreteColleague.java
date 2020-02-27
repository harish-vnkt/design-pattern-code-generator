package DesignPatterns.Mediator;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class ConcreteColleague extends CodeGenerator {

    String colleagueInterface;

    public ConcreteColleague(String filename, String colleagueInterface) {
        super(filename);
        this.colleagueInterface = colleagueInterface;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, this.colleagueInterface, null);

        // receive method
        MethodDeclaration receiveMethod = this.declareMethod(
                "receive",
                this.createPrimitiveType(CodeGenerator.voidType),
                CodeGenerator.publicKeyword,
                false, false
        );
        SingleVariableDeclaration messageParameter = this.createSingleVariableDeclaration(
                "message",
                this.createSimpleType("String")
        );
        receiveMethod.parameters().add(messageParameter);
        Block receiveBlock = this.createBlock();
        StringLiteral printValue = this.abstractSyntaxTree.newStringLiteral();
        printValue.setLiteralValue(this.fileName + "Received");
        InfixExpression infixExpression = this.createInfixExpression(
                printValue,
                this.abstractSyntaxTree.newSimpleName("message"),
                CodeGenerator.additionOperator
        );
        Statement printStatement = this.createPrintStatement(infixExpression);
        receiveBlock.statements().add(printStatement);
        receiveMethod.setBody(receiveBlock);

        // adding method to class
        this.classDeclaration.bodyDeclarations().add(receiveMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }
}
