package DesignPatterns.Factory;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Product extends CodeGenerator {

    public Product(String filename) {
        super(filename);
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, true, CodeGenerator.publicKeyword,
                false, false, null, null);

        // method declaration
        MethodDeclaration getName = this.declareMethod("getName",
                this.createSimpleType("String"), CodeGenerator.publicKeyword, false, false);
        this.classDeclaration.bodyDeclarations().add(getName);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
