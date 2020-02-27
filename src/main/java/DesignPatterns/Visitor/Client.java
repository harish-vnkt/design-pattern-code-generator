package DesignPatterns.Visitor;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Client extends CodeGenerator {

    String visitorClass, visitorConcreteClass, element1Class, element2Class;

    public Client(String filename, String visitorClass, String visitorConcreteClass,
                  String element1Class, String element2Class) {
        super(filename);
        this.visitorClass = visitorClass;
        this.visitorConcreteClass = visitorConcreteClass;
        this.element1Class = element1Class;
        this.element2Class = element2Class;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, null, null);

        // main method
        MethodDeclaration mainMethod = this.createMainMethodDeclaration();
        Block mainMethodBlock = this.createBlock();
        VariableDeclarationExpression element1Variable = this.createVariableDeclarationExpression(
                "element1",
                this.createSimpleType(this.element1Class)
        );
        ClassInstanceCreation element1Instance = this.createInstanceCreationExpression(
                this.createSimpleType(this.element1Class)
        );
        Assignment element1Assignment = this.createAssignmentExpression(
                element1Variable,
                element1Instance
        );
        mainMethodBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(element1Assignment));
        VariableDeclarationExpression element2Variable = this.createVariableDeclarationExpression(
                "element2",
                this.createSimpleType(this.element2Class)
        );
        ClassInstanceCreation element2Instance = this.createInstanceCreationExpression(
                this.createSimpleType(this.element2Class)
        );
        Assignment element2Assignment = this.createAssignmentExpression(
                element2Variable,
                element2Instance
        );
        mainMethodBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(element2Assignment));
        VariableDeclarationExpression visitorVariable = this.createVariableDeclarationExpression(
                "visitor",
                this.createSimpleType(this.visitorClass)
        );
        ClassInstanceCreation visitorInstance = this.createInstanceCreationExpression(
                this.createSimpleType(this.visitorConcreteClass)
        );
        Assignment visitorAssignment = this.createAssignmentExpression(
                visitorVariable,
                visitorInstance
        );
        mainMethodBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(visitorAssignment));
        MethodInvocation element1Visit = this.createMethodInvocation(
                "accept",
                this.abstractSyntaxTree.newSimpleName("element1")
        );
        element1Visit.arguments().add(this.abstractSyntaxTree.newSimpleName("visitor"));
        MethodInvocation element2Visit = this.createMethodInvocation(
                "accept",
                this.abstractSyntaxTree.newSimpleName("element2")
        );
        element2Visit.arguments().add(this.abstractSyntaxTree.newSimpleName("visitor"));
        mainMethodBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(element1Visit));
        mainMethodBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(element2Visit));
        mainMethod.setBody(mainMethodBlock);

        // adding main method to class
        this.classDeclaration.bodyDeclarations().add(mainMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
