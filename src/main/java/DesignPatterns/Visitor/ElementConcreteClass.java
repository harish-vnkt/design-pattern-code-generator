package DesignPatterns.Visitor;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class ElementConcreteClass extends CodeGenerator {

    String visitorClass, elementClass;

    public ElementConcreteClass(String filename, String visitorClass, String elementClass) {
        super(filename);
        this.visitorClass = visitorClass;
        this.elementClass = elementClass;
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, false, this.elementClass, null);

        // accept method
        MethodDeclaration acceptMethod = this.declareMethod(
                "accept",
                this.createPrimitiveType(CodeGenerator.voidType),
                CodeGenerator.publicKeyword,
                false, false
        );
        SingleVariableDeclaration visitorParameter = this.createSingleVariableDeclaration(
                "visitor",
                this.createSimpleType(this.visitorClass)
        );
        acceptMethod.parameters().add(visitorParameter);
        Block acceptMethodBlock = this.createBlock();
        MethodInvocation visitElement = this.createMethodInvocation(
                "visit" + this.fileName,
                this.abstractSyntaxTree.newSimpleName("visitor")
        );
        ThisExpression thisExpression = this.abstractSyntaxTree.newThisExpression();
        visitElement.arguments().add(thisExpression);
        acceptMethodBlock.statements().add(this.abstractSyntaxTree.newExpressionStatement(visitElement));
        acceptMethod.setBody(acceptMethodBlock);

        // operation method
        MethodDeclaration operationMethod = this.declareMethod(
                "operation",
                this.createSimpleType("String"),
                CodeGenerator.publicKeyword,
                false, false
        );
        Block operationMethodBlock = this.createBlock();
        StringLiteral returnValue = this.abstractSyntaxTree.newStringLiteral();
        returnValue.setLiteralValue("Hello from " + this.fileName);
        ReturnStatement returnStatement = this.createReturnStatement(returnValue);
        operationMethodBlock.statements().add(returnStatement);
        operationMethod.setBody(operationMethodBlock);

        // add methods to class
        this.classDeclaration.bodyDeclarations().add(acceptMethod);
        this.classDeclaration.bodyDeclarations().add(operationMethod);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
