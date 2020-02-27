package DesignPatterns.Visitor;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Visitor extends CodeGenerator {

    String element1Class, element2Class;

    public Visitor(String filename, String element1Class, String element2Class) {
        super(filename);
        this.element1Class = element1Class;
        this.element2Class = element2Class;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, true, null, null);

        // visit methods
        MethodDeclaration visitElement1 = this.declareMethod(
                "visit" + this.element1Class,
                this.createPrimitiveType(CodeGenerator.voidType),
                CodeGenerator.publicKeyword,
                false, true
        );
        SingleVariableDeclaration element1 = this.createSingleVariableDeclaration(
                "element",
                this.createSimpleType(this.element1Class)
        );
        visitElement1.parameters().add(element1);
        MethodDeclaration visitElement2 = this.declareMethod(
                "visit" + this.element2Class,
                this.createPrimitiveType(CodeGenerator.voidType),
                CodeGenerator.publicKeyword,
                false, true
        );
        SingleVariableDeclaration element2 = this.createSingleVariableDeclaration(
                "element",
                this.createSimpleType(this.element2Class)
        );
        visitElement2.parameters().add(element2);

        // add methods to class
        this.classDeclaration.bodyDeclarations().add(visitElement1);
        this.classDeclaration.bodyDeclarations().add(visitElement2);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
