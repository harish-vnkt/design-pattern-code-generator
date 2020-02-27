package DesignPatterns.ChainOfResponsibility;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Receiver2 extends CodeGenerator {

    String handlerClass;

    public Receiver2(String filename, String handlerClass) {
        super(filename);
        this.handlerClass = handlerClass;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, this.handlerClass, null);

        // overridden method
        MethodDeclaration handleRequest = this.declareMethod("handleRequest",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                false, false);
        Block handleBlock = this.createBlock();
        StringLiteral printString = this.abstractSyntaxTree.newStringLiteral();
        printString.setLiteralValue("Receiver 2 : Handling the request");
        Statement printStatement = this.createPrintStatement(printString);
        handleBlock.statements().add(printStatement);
        handleRequest.setBody(handleBlock);

        // adding method to class
        this.classDeclaration.bodyDeclarations().add(handleRequest);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
