package DesignPatternCodeGenerator;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.Document;

import javax.annotation.Nullable;

/**
 * The abstract class CodeGenerator represents individual
 * files in a particular design pattern. The files that
 * extend it have access to functions that help build
 * an AST from scratch.
 *
 * @author Harish Venkataraman
 */
public abstract class CodeGenerator {

    // each subclass needs to implement the below method to build the code
    public abstract Document buildCode();

    // fields
    // source string from which AST is constructed
    protected String source;
    // name of the file to write to
    protected String fileName;
    // represents the root node of an AST (a java file)
    protected CompilationUnit compilationUnit;
    // represents the top level class declaration in this file
    protected TypeDeclaration classDeclaration;
    // abstract syntax tree returned from the compilation unit
    protected AST abstractSyntaxTree;
    // used to store the string resulting from an AST
    public Document document;

    // static variables
    public static Modifier.ModifierKeyword publicKeyword = Modifier.ModifierKeyword.PUBLIC_KEYWORD;
    public static Modifier.ModifierKeyword privateKeyword = Modifier.ModifierKeyword.PRIVATE_KEYWORD;
    public static Modifier.ModifierKeyword protectedKeyword = Modifier.ModifierKeyword.PROTECTED_KEYWORD;
    public static Modifier.ModifierKeyword abstractKeyword = Modifier.ModifierKeyword.ABSTRACT_KEYWORD;
    public static Modifier.ModifierKeyword staticKeyword = Modifier.ModifierKeyword.STATIC_KEYWORD;
    public static PrimitiveType.Code intType = PrimitiveType.INT;
    public static PrimitiveType.Code floatType = PrimitiveType.FLOAT;
    public static PrimitiveType.Code doubleType = PrimitiveType.DOUBLE;
    public static PrimitiveType.Code booleanType = PrimitiveType.BOOLEAN;
    public static PrimitiveType.Code charType = PrimitiveType.CHAR;
    public static PrimitiveType.Code voidType = PrimitiveType.VOID;

    // constructor
    protected CodeGenerator(String fileName) {
        this.source = ""; // start building from an empty string
        this.fileName = fileName;

        // initializing parser from empty string
        ASTParser parser = ASTParser.newParser(AST.JLS13);
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        // initializing document
        this.document = new Document(this.source);
        // initializing compilation unit from empty source string
        this.compilationUnit = (CompilationUnit) parser.createAST(null);
        this.compilationUnit.recordModifications(); // enable changes to AST in-place
        // retrieve AST from empty CU
        this.abstractSyntaxTree = this.compilationUnit.getAST();

    }

    protected void createTypeDeclaration(String className, Boolean isInterface,
                                         @Nullable Modifier.ModifierKeyword accessModifierKeyword,
                                         Boolean isStatic, Boolean isAbstract,
                                         @Nullable String superClass, @Nullable String superInterface) {

        // create an AST node which is a type declaration
        this.classDeclaration = this.abstractSyntaxTree.newTypeDeclaration();
        // set name of class
        this.classDeclaration.setName(this.abstractSyntaxTree.newSimpleName(className));
        // set as interface if needed
        this.classDeclaration.setInterface(isInterface);
        // set modifiers
        if (accessModifierKeyword != null) {
            Modifier accessModifier = this.abstractSyntaxTree.newModifier(accessModifierKeyword);
            this.classDeclaration.modifiers().add(accessModifier);
        }
        if (isStatic) {
            Modifier staticModifier = this.abstractSyntaxTree.newModifier(CodeGenerator.staticKeyword);
            this.classDeclaration.modifiers().add(staticModifier);
        }
        if (isAbstract) {
            Modifier abstractModifier = this.abstractSyntaxTree.newModifier(CodeGenerator.abstractKeyword);
            this.classDeclaration.modifiers().add(abstractModifier);
        }
        // set super class and super interface
        if (superClass != null) {
            this.classDeclaration.setSuperclassType(createSimpleType(superClass));
        }
        if (superInterface != null) {
            this.classDeclaration.superInterfaceTypes().add(createSimpleType(superInterface));
        }

    }

    protected MethodDeclaration declareMethod(String methodName, Type returnType,
                                              Modifier.ModifierKeyword accessModifierKeyword,
                                              Boolean isStatic, Boolean isAbstract) {

        // create an AST node which is a method declaration
        MethodDeclaration methodDeclaration = this.abstractSyntaxTree.newMethodDeclaration();
        // set name of method
        methodDeclaration.setName(this.abstractSyntaxTree.newSimpleName(methodName));
        // set return type of method
        methodDeclaration.setReturnType2(returnType);
        // set access modifier
        Modifier accessModifier = this.abstractSyntaxTree.newModifier(accessModifierKeyword);
        methodDeclaration.modifiers().add(accessModifier);
        if (isStatic) {
            Modifier staticModifier = this.abstractSyntaxTree.newModifier(CodeGenerator.staticKeyword);
            methodDeclaration.modifiers().add(staticModifier);
        }
        if (isAbstract) {
            Modifier abstractModifier = this.abstractSyntaxTree.newModifier(CodeGenerator.abstractKeyword);
            methodDeclaration.modifiers().add(abstractKeyword);
        }

        return methodDeclaration;

    }

    protected MethodDeclaration createMainMethodDeclaration() {
        MethodDeclaration mainMethod = this.declareMethod("main", createPrimitiveType(CodeGenerator.voidType),
                CodeGenerator.publicKeyword, true, false);
        SingleVariableDeclaration mainParameter = this.abstractSyntaxTree.newSingleVariableDeclaration();
        mainParameter.setName(this.abstractSyntaxTree.newSimpleName("args"));
        mainParameter.setType(this.createArrayType(this.createSimpleType("String")));
        mainMethod.parameters().add(mainParameter);
        return mainMethod;
    }

    protected Block createBlock() {
        return this.abstractSyntaxTree.newBlock();
    }

    protected PrimitiveType createPrimitiveType(PrimitiveType.Code type) {
        return this.abstractSyntaxTree.newPrimitiveType(type);
    }

    protected SimpleType createSimpleType(String name) {
        return this.abstractSyntaxTree.newSimpleType(this.abstractSyntaxTree.newName(name));
    }

    protected ArrayType createArrayType(Type type) {
        return this.abstractSyntaxTree.newArrayType(type);
    }

    protected SingleVariableDeclaration createSingleVariableDeclaration(String name, Type type) {
        SingleVariableDeclaration singleVariableDeclaration = this.abstractSyntaxTree.newSingleVariableDeclaration();
        singleVariableDeclaration.setName(this.abstractSyntaxTree.newSimpleName(name));
        singleVariableDeclaration.setType(type);
        return singleVariableDeclaration;
    }

    protected VariableDeclarationExpression createVariableDeclarationExpression(String variableName,
                                                                                Type variableType) {
        VariableDeclarationFragment variableDeclarationFragment =
                this.abstractSyntaxTree.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(this.abstractSyntaxTree.newSimpleName(variableName));
        VariableDeclarationExpression variableDeclarationExpression =
                this.abstractSyntaxTree.newVariableDeclarationExpression(variableDeclarationFragment);
        variableDeclarationExpression.setType(variableType);
        return variableDeclarationExpression;
    }

    protected MethodInvocation createMethodInvocation(String methodName, Expression expression) {
        MethodInvocation methodInvocation = this.abstractSyntaxTree.newMethodInvocation();
        methodInvocation.setName(this.abstractSyntaxTree.newSimpleName(methodName));
        methodInvocation.setExpression(expression);
        return methodInvocation;
    }

    protected ClassInstanceCreation createInstanceCreationExpression(Type instanceType) {
        ClassInstanceCreation instanceCreation = this.abstractSyntaxTree.newClassInstanceCreation();
        instanceCreation.setType(instanceType);
        return instanceCreation;
    }

    protected Assignment createAssignmentExpression(Expression LHS, Expression RHS) {
        Assignment assignmentExpression = this.abstractSyntaxTree.newAssignment();
        assignmentExpression.setLeftHandSide(LHS);
        assignmentExpression.setRightHandSide(RHS);
        return assignmentExpression;
    }

    protected ThisExpression createThisExpression(String qualifier) {
        ThisExpression thisExpression = this.abstractSyntaxTree.newThisExpression();
        thisExpression.setQualifier(this.abstractSyntaxTree.newName(qualifier));
        return thisExpression;
    }

    protected ReturnStatement createReturnStatement(Expression expression) {
        ReturnStatement returnStatement = this.abstractSyntaxTree.newReturnStatement();
        returnStatement.setExpression(expression);
        return returnStatement;
    }

}