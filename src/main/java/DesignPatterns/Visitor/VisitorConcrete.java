package DesignPatterns.Visitor;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class VisitorConcrete extends CodeGenerator {

    String visitorClass, element1Class, element2Class;

    public VisitorConcrete(String filename, String visitorClass,
                           String element1Class, String element2Class) {
        super(filename);
        this.visitorClass = visitorClass;
        this.element1Class = element1Class;
        this.element2Class = element2Class;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, this.visitorClass, null);

        // visit method for first element
        MethodDeclaration visitElement1 = this.declareMethod(
                "visit" + this.element1Class,
                this.createPrimitiveType(CodeGenerator.voidType),
                CodeGenerator.publicKeyword,
                false, false
        );
        SingleVariableDeclaration element1 = this.createSingleVariableDeclaration(
                "element",
                this.createSimpleType(this.element1Class)
        );
        visitElement1.parameters().add(element1);
        Block visit1Block = this.createBlock();
        MethodInvocation operation1 = this.createMethodInvocation(
                "operation",
                this.abstractSyntaxTree.newSimpleName("element")
        );
        Statement printStatement1 = this.createPrintStatement(operation1);
        visit1Block.statements().add(printStatement1);
        visitElement1.setBody(visit1Block);

        // visit method for second element
        MethodDeclaration visitElement2 = this.declareMethod(
                "visit" + this.element2Class,
                this.createPrimitiveType(CodeGenerator.voidType),
                CodeGenerator.publicKeyword,
                false, false
        );
        SingleVariableDeclaration element2 = this.createSingleVariableDeclaration(
                "element",
                this.createSimpleType(this.element2Class)
        );
        visitElement2.parameters().add(element2);
        Block visit2Block = this.createBlock();
        MethodInvocation operation2 = this.createMethodInvocation(
                "operation",
                this.abstractSyntaxTree.newSimpleName("element")
        );
        Statement printStatement2 = this.createPrintStatement(operation2);
        visit2Block.statements().add(printStatement2);
        visitElement2.setBody(visit2Block);

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
