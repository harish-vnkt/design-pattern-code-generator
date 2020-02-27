package DesignPatterns.Visitor;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Element extends CodeGenerator {

    String visitorClass;

    public Element(String filename, String visitorClass) {
        super(filename);
        this.visitorClass = visitorClass;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, true, null, null);

        // abstract method
        MethodDeclaration acceptMethod = this.declareMethod(
                "accept",
                this.createPrimitiveType(CodeGenerator.voidType),
                CodeGenerator.publicKeyword,
                false, true
        );
        SingleVariableDeclaration visitorParameter = this.createSingleVariableDeclaration(
                "visitor",
                this.createSimpleType(this.visitorClass)
        );
        acceptMethod.parameters().add(visitorParameter);

        // adding method to class
        this.classDeclaration.bodyDeclarations().add(acceptMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
