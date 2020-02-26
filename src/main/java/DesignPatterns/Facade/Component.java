package DesignPatterns.Facade;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Component extends CodeGenerator {

    String methodName;

    public Component(String filename, String methodName) {
        super(filename);
        this.methodName = methodName;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, null);

        // operation method
        MethodDeclaration operationMethod = this.declareMethod(this.methodName,
                this.createSimpleType("String"), CodeGenerator.publicKeyword, false, false);
        Block operationBlock = this.createBlock();
        StringLiteral returnString = this.abstractSyntaxTree.newStringLiteral();
        returnString.setLiteralValue(this.fileName);
        ReturnStatement returnStatement = this.createReturnStatement(returnString);
        operationBlock.statements().add(returnStatement);
        operationMethod.setBody(operationBlock);

        // add method to class
        this.classDeclaration.bodyDeclarations().add(operationMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
