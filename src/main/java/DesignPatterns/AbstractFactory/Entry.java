package DesignPatterns.AbstractFactory;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;


public class Entry extends CodeGenerator {

    private String clientName, factoryName;

    public Entry(String fileName, String clientName, String factoryName) {
        super(fileName);
        this.clientName = clientName;
        this.factoryName = factoryName;
    }

    public Document buildCode() throws BadLocationException {

        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, null);

        // main method
        MethodDeclaration mainMethod = this.createMainMethodDeclaration();
        Block mainBlock = this.createBlock();
        // client object instantiation
        VariableDeclarationExpression clientObject = this.createVariableDeclarationExpression("client",
                this.createSimpleType(this.clientName));
        ClassInstanceCreation clientInstantiation = this.
                createInstanceCreationExpression(this.createSimpleType(this.clientName));
        ClassInstanceCreation factoryInstantiation = this.
                createInstanceCreationExpression(this.createSimpleType(this.factoryName));
        clientInstantiation.arguments().add(factoryInstantiation);
        Assignment assignment = this.createAssignmentExpression(clientObject, clientInstantiation);
        mainBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(assignment));
        // print statement
        MethodInvocation clientCall = this.createMethodInvocation("operation",
                this.abstractSyntaxTree.newSimpleName("client"));
        Statement printStatement = this.createPrintStatement(clientCall);
        mainBlock.statements().add(printStatement);
        // add mainBlock to main method
        mainMethod.setBody(mainBlock);

        // add mainMethod to class
        this.classDeclaration.bodyDeclarations().add(mainMethod);
        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();
        // return document
        return this.document;

    }

}
