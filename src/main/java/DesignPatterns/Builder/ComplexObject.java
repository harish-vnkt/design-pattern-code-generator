package DesignPatterns.Builder;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class ComplexObject extends CodeGenerator {

    String productInterface;

    public ComplexObject(String filename, String productInterface) {
        super(filename);
        this.productInterface = productInterface;
    }

    public Document buildCode() throws BadLocationException {
        // import declaration
        this.addImportDeclaration(this.abstractSyntaxTree.newQualifiedName(
                this.abstractSyntaxTree.newName("java"),
                this.abstractSyntaxTree.newSimpleName("util")
        ));

        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, null);

        // fields of the class
        FieldDeclaration children = this.createFieldDeclaration(
                "children",
                this.createParameterizedType(
                        this.createSimpleType("List"),
                        this.createSimpleType(this.productInterface)
                ),
                CodeGenerator.privateKeyword,
                false);
        this.classDeclaration.bodyDeclarations().add(children);

        // constructor
        MethodDeclaration constructor = this.createConstructor(this.fileName, CodeGenerator.publicKeyword);
        Block constructorBlock = this.createBlock();
        FieldAccess childrenThis = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("children"));
        ClassInstanceCreation childrenListInstance = this.createInstanceCreationExpression(
                this.createParameterizedType(
                        this.createSimpleType("ArrayList"),
                        this.createSimpleType(this.productInterface)
                )
        );
        Assignment constructorAssignment = this.createAssignmentExpression(childrenThis, childrenListInstance);
        constructorBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(constructorAssignment));
        constructor.setBody(constructorBlock);

        // add
        MethodDeclaration addMethod = this.declareMethod("add",
                this.createPrimitiveType(CodeGenerator.booleanType), CodeGenerator.publicKeyword,
                false, false);
        SingleVariableDeclaration parameter = this.createSingleVariableDeclaration("child",
                this.createSimpleType(this.productInterface));
        addMethod.parameters().add(parameter);
        Block addBlock = this.createBlock();
        MethodInvocation add = this.createMethodInvocation("add",
                this.abstractSyntaxTree.newSimpleName("children"));
        add.arguments().add(this.abstractSyntaxTree.newSimpleName("child"));
        ReturnStatement returnStatement = this.createReturnStatement(add);
        addBlock.statements().add(returnStatement);
        addMethod.setBody(addBlock);

        // add methods to the class
        this.classDeclaration.bodyDeclarations().add(constructor);
        this.classDeclaration.bodyDeclarations().add(addMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
