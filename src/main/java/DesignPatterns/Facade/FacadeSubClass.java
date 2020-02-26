package DesignPatterns.Facade;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class FacadeSubClass extends CodeGenerator {

    String facadeClass, class1, class2, class3;

    public FacadeSubClass(String filename, String facadeClass, String class1, String class2, String class3) {
        super(filename);
        this.facadeClass = facadeClass;
        this.class1 = class1;
        this.class2 = class2;
        this.class3 = class3;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, this.facadeClass, null);

        // constructor
        MethodDeclaration constructor = this.createConstructor(this.fileName, CodeGenerator.publicKeyword);
        SingleVariableDeclaration parameter1 = this.createSingleVariableDeclaration("object1",
                this.createSimpleType(this.class1));
        SingleVariableDeclaration parameter2 = this.createSingleVariableDeclaration("object2",
                this.createSimpleType(this.class2));
        SingleVariableDeclaration parameter3 = this.createSingleVariableDeclaration("object3",
                this.createSimpleType(this.class3));
        constructor.parameters().add(parameter1);
        constructor.parameters().add(parameter2);
        constructor.parameters().add(parameter3);
        Block constructorBlock = this.createBlock();
        FieldAccess object1This = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("object1")
        );
        Assignment object1Assignment = this.createAssignmentExpression(object1This,
                this.abstractSyntaxTree.newSimpleName("object1"));
        constructorBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(object1Assignment));
        FieldAccess object2This = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("object2")
        );
        Assignment object2Assignment = this.createAssignmentExpression(object2This,
                this.abstractSyntaxTree.newSimpleName("object2"));
        constructorBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(object2Assignment));
        FieldAccess object3This = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("object3")
        );
        Assignment object3Assignment = this.createAssignmentExpression(object3This,
                this.abstractSyntaxTree.newSimpleName("object3"));
        constructorBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(object3Assignment));
        constructor.setBody(constructorBlock);

        // operation method
        MethodDeclaration operationMethod = this.declareMethod("operation",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                false, false);
        Block operationBlock = this.createBlock();
        MethodInvocation operation1 = this.createMethodInvocation("operation1",
                this.abstractSyntaxTree.newSimpleName("object1"));
        Statement printStatment1 = this.createPrintStatement(operation1);
        MethodInvocation operation2 = this.createMethodInvocation("operation2",
                this.abstractSyntaxTree.newSimpleName("object2"));
        Statement printStatment2 = this.createPrintStatement(operation2);
        MethodInvocation operation3 = this.createMethodInvocation("operation3",
                this.abstractSyntaxTree.newSimpleName("object3"));
        Statement printStatment3 = this.createPrintStatement(operation3);
        operationBlock.statements().add(printStatment1);
        operationBlock.statements().add(printStatment2);
        operationBlock.statements().add(printStatment3);
        operationMethod.setBody(operationBlock);

        // add methods to class
        this.classDeclaration.bodyDeclarations().add(constructor);
        this.classDeclaration.bodyDeclarations().add(operationMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();
        ;

        // return document
        return this.document;
    }
}
