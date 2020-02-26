package DesignPatterns.Facade;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Facade extends CodeGenerator {

    public Facade(String filename) {
        super(filename);
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, true, null, null);

        // add method
        MethodDeclaration operationMethod = this.declareMethod("operation",
                this.createSimpleType("String"), CodeGenerator.publicKeyword, false, true);

        // add method to class
        this.classDeclaration.bodyDeclarations().add(operationMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();
        
        // return document
        return this.document;
    }
}
