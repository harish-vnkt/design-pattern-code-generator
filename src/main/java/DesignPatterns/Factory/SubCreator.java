package DesignPatterns.Factory;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class SubCreator extends CodeGenerator {

    String creatorClass, productInterface, productClass;

    public SubCreator(String filename, String creatorClass, String productInterface, String productClass) {
        super(filename);
        this.creatorClass = creatorClass;
        this.productInterface = productInterface;
        this.productClass = productClass;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, this.creatorClass, null);

        // overridden method
        MethodDeclaration factoryMethod = this.declareMethod("factoryMethod",
                this.createSimpleType(this.productInterface), CodeGenerator.publicKeyword, false, false);
        Block factoryBlock = this.createBlock();
        ClassInstanceCreation productA = this.createInstanceCreationExpression(
                this.createSimpleType(this.productClass)
        );
        ReturnStatement returnStatement = this.createReturnStatement(productA);
        factoryBlock.statements().add(returnStatement);
        factoryMethod.setBody(factoryBlock);

        // add method to class
        this.classDeclaration.bodyDeclarations().add(factoryMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
