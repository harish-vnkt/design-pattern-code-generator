package DesignPatterns.Factory;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class ProductA extends CodeGenerator {

    String productInterface;

    public ProductA(String filename, String productInterface) {
        super(filename);
        this.productInterface = productInterface;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, this.productInterface);

        // overriden method
        MethodDeclaration getName = this.declareMethod("getName",
                this.createSimpleType("String"), CodeGenerator.publicKeyword, false, false);
        Block getNameBlock = this.createBlock();
        StringLiteral returnValue = this.abstractSyntaxTree.newStringLiteral();
        returnValue.setLiteralValue(this.fileName);
        ReturnStatement returnStatement = this.createReturnStatement(returnValue);
        getNameBlock.statements().add(returnStatement);
        getName.setBody(getNameBlock);

        // adding methods to class
        this.classDeclaration.bodyDeclarations().add(getName);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
