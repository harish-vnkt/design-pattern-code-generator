package DesignPatterns.Builder;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class ProductB extends CodeGenerator {

    String productInterface;

    public ProductB(String filename, String productInterface) {
        super(filename);
        this.productInterface = productInterface;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, this.productInterface);
        MethodDeclaration getName = this.declareMethod("getName",
                this.createSimpleType("String"), CodeGenerator.publicKeyword,
                false, false);
        Block getNameBody = this.createBlock();
        StringLiteral returnString = this.abstractSyntaxTree.newStringLiteral();
        returnString.setLiteralValue(this.fileName);
        ReturnStatement returnStatement = this.createReturnStatement(returnString);
        getNameBody.statements().add(returnStatement);
        getName.setBody(getNameBody);

        // add method to class
        this.classDeclaration.bodyDeclarations().add(getName);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
