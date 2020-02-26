package DesignPatterns.ChainOfResponsibility;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class Handler extends CodeGenerator {

    public Handler(String filename) {
        super(filename);
    }

    public Document buildCode() throws BadLocationException {
        this.createTypeDeclaration(this.fileName, false, CodeGenerator.publicKeyword,
                false, true, null, null);

        // field
        FieldDeclaration successorObject = this.createFieldDeclaration("successor",
                this.createSimpleType(this.fileName), CodeGenerator.privateKeyword, false);
        this.classDeclaration.bodyDeclarations().add(successorObject);

        // constructor 1
        MethodDeclaration constructor1 = this.createConstructor(this.fileName, CodeGenerator.publicKeyword);
        Block constructor1Block = this.createBlock();
        constructor1.setBody(constructor1Block);

        // constructor 2
        MethodDeclaration constructor2 = this.createConstructor(this.fileName, CodeGenerator.publicKeyword);
        Block constructor2Block = this.createBlock();
        FieldAccess successorThis = this.createFieldAccessExpression(
                this.abstractSyntaxTree.newSimpleName("successor"));
        Assignment successorAssignment = this.createAssignmentExpression(successorThis,
                this.abstractSyntaxTree.newSimpleName("successor"));
        constructor2Block.statements().add(this.abstractSyntaxTree.newExpressionStatement(successorAssignment));
        constructor2.setBody(constructor2Block);

        // handleRequest method
        MethodDeclaration handleRequest = this.declareMethod("handleRequest",
                this.createPrimitiveType(CodeGenerator.voidType), CodeGenerator.publicKeyword,
                false, false);
        Block handleRequestBlock = this.createBlock();
        InfixExpression ifCondition = this.createInfixExpression(
                this.abstractSyntaxTree.newSimpleName("successor"),
                this.abstractSyntaxTree.newNullLiteral(),
                CodeGenerator.notEqualsOperator
        );
        MethodInvocation ifBlock = this.createMethodInvocation("handleRequest",
                this.abstractSyntaxTree.newSimpleName("successor"));
        IfStatement ifStatement = this.createIfStatement(ifCondition,
                this.abstractSyntaxTree.newExpressionStatement(ifBlock), null);
        handleRequestBlock.statements().add(ifStatement);
        handleRequest.setBody(handleRequestBlock);

        // adding methods to class
        this.classDeclaration.bodyDeclarations().add(constructor1);
        this.classDeclaration.bodyDeclarations().add(constructor2);
        this.classDeclaration.bodyDeclarations().add(handleRequest);

        // add class to CU
        this.compilationUnit.types().add(this.classDeclaration);
        // apply edits to the document
        this.applyEdits();;

        // return document
        return this.document;
    }

}
