package DesignPatterns.AbstractFactory;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class ProductA extends CodeGenerator {

    public ProductA(String filename) {
        super(filename);
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, null);

        // constructor
        MethodDeclaration constructor = this.createConstructor(this.fileName, CodeGenerator.publicKeyword);
        Block block = this.createBlock();
        StringLiteral argument = this.abstractSyntaxTree.newStringLiteral();
        argument.setLiteralValue(this.fileName + " object has been instantiated.");
        Statement printStatement = this.createPrintStatement(argument);
        block.statements().add(printStatement);
        constructor.setBody(block);

        // add constructor to class
        this.classDeclaration.bodyDeclarations().add(constructor);
        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;
        // return document
        return this.document;
    }
}
