package DesignPatterns.Mediator;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class MediatorInterface extends CodeGenerator {

    String colleagueInteface;

    public MediatorInterface(String filename, String colleagueInterface) {
        super(filename);
        this.colleagueInteface = colleagueInterface;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, null);

        // send method
        MethodDeclaration sendMethod = this.declareMethod("send",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                false, false);
        SingleVariableDeclaration messageParameter = this.createSingleVariableDeclaration("message",
                this.createSimpleType("String"));
        SingleVariableDeclaration colleagueParameter = this.createSingleVariableDeclaration("colleague",
                this.createSimpleType("Colleague"));
        sendMethod.parameters().add(messageParameter);
        sendMethod.parameters().add(colleagueParameter);

        // adding method to class
        this.classDeclaration.bodyDeclarations().add(sendMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
