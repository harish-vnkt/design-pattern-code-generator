package DesignPatternCodeGenerator;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public abstract Document buildCode() throws BadLocationException;

    // fields
    // source string from which AST is constructed
    protected String source;
    // name of the file to write to
    public String fileName;
    // represents the root node of an AST (a java file)
    protected CompilationUnit compilationUnit;
    // represents the top level class declaration in this file
    protected TypeDeclaration classDeclaration;
    // abstract syntax tree returned from the compilation unit
    protected AST abstractSyntaxTree;
    // used to store the string resulting from an AST
    protected Document document;
    // logger for this class
    private static Logger logger;

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
    public static InfixExpression.Operator multiplicationOperator = InfixExpression.Operator.TIMES;
    public static InfixExpression.Operator additionOperator = InfixExpression.Operator.PLUS;
    public static InfixExpression.Operator subtractionOperator = InfixExpression.Operator.MINUS;
    public static InfixExpression.Operator divisionOperator = InfixExpression.Operator.DIVIDE;
    public static InfixExpression.Operator greaterOperator = InfixExpression.Operator.GREATER;
    public static InfixExpression.Operator lesserOperator = InfixExpression.Operator.LESS;
    public static InfixExpression.Operator geqOperator = InfixExpression.Operator.GREATER_EQUALS;
    public static InfixExpression.Operator leqOperator = InfixExpression.Operator.LESS_EQUALS;
    public static InfixExpression.Operator equalsOperator = InfixExpression.Operator.EQUALS;
    public static InfixExpression.Operator notEqualsOperator = InfixExpression.Operator.NOT_EQUALS;
    public static InfixExpression.Operator andOperator = InfixExpression.Operator.CONDITIONAL_AND;
    public static InfixExpression.Operator orOperator = InfixExpression.Operator.CONDITIONAL_OR;

    /**
     * Used to instantiate important variables required by
     * each file. Includes setting up the compilation unit,
     * AST and the document object
     *
     * @param fileName name of the class and name of the file
     *                 where generated code is written
     */
    protected CodeGenerator(String fileName) {
        // instantiate logger
        logger = LoggerFactory.getLogger("DesignPatternCodeGenerator.CodeGenerator");

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

    /**
     * Used to create a top-level Type declaration in a file.
     * Stores the type in the compilation unit in place.
     * Assumes there is only one type per compilation unit.
     *
     * @param className name of the type
     * @param isInterface whether type is interface
     * @param accessModifierKeyword access modifier of the type declaration,
     *                              is of type Modifier.ModifierKeyword,
     *                              chosen from the static variables of this class
     * @param isStatic whether type is static
     * @param isAbstract whether type is abstract
     * @param superClass name of the class extended extended by type
     * @param superInterface name of the interface implemented by type
     */

    protected void createTypeDeclaration(String className, Boolean isInterface,
                                         Modifier.ModifierKeyword accessModifierKeyword,
                                         Boolean isStatic, Boolean isAbstract,
                                         @Nullable String superClass, @Nullable String superInterface) {

        logger.debug("Creating type {}", className);
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
            logger.info("Superclass is not null");
            this.classDeclaration.setSuperclassType(createSimpleType(superClass));
        }
        if (superInterface != null) {
            logger.info("Superinterface is not null");
            this.classDeclaration.superInterfaceTypes().add(createSimpleType(superInterface));
        }

    }

    /**
     * Used to create a method declaration. The method declaration
     * can have a body that is set using blocks.
     *
     * @param methodName name of the method
     * @param returnType return type of the method,
     *                   can be PrimitiveType or SimpleType
     * @param accessModifierKeyword access modifier of the type declaration,
     *                              is of type Modifier.ModifierKeyword,
     *                              chosen from the static variables of this class
     * @param isStatic whether method is static or not
     * @param isAbstract whether method is abstract
     * @return object of type MethodDeclaration that can be added to a type
     */
    protected MethodDeclaration declareMethod(String methodName, Type returnType,
                                              Modifier.ModifierKeyword accessModifierKeyword,
                                              Boolean isStatic, Boolean isAbstract) {

        logger.debug("Declaring method {}", methodName);
        // create an AST node which is a method declaration
        MethodDeclaration methodDeclaration = this.abstractSyntaxTree.newMethodDeclaration();
        // set name of method
        methodDeclaration.setName(this.abstractSyntaxTree.newSimpleName(methodName));
        // set return type of method
        methodDeclaration.setReturnType2(returnType);
        // set access modifier
        Modifier accessModifier = this.abstractSyntaxTree.newModifier(accessModifierKeyword);
        methodDeclaration.modifiers().add(accessModifier);
        // set static or abstract
        if (isStatic) {
            Modifier staticModifier = this.abstractSyntaxTree.newModifier(CodeGenerator.staticKeyword);
            methodDeclaration.modifiers().add(staticModifier);
        }
        if (isAbstract) {
            Modifier abstractModifier = this.abstractSyntaxTree.newModifier(CodeGenerator.abstractKeyword);
            methodDeclaration.modifiers().add(abstractModifier);
        }

        return methodDeclaration;

    }

    /**
     * Used to create a constructor in a type. User is required
     * to pass the same name as the enclosing type. Can be modified
     * similar to MethodDeclaration.
     *
     * @param methodName name of the constructor
     * @param accessModifierKeyword access modifier of the type declaration,
     *                              is of type Modifier.ModifierKeyword,
     *                              chosen from the static variables of this class
     * @return constructor of type MethodDeclaration that can be added to a type
     */
    protected MethodDeclaration createConstructor(String methodName,
                                                  Modifier.ModifierKeyword accessModifierKeyword) {
        logger.debug("Creating constructor");
        MethodDeclaration constructorDeclaration = this.abstractSyntaxTree.newMethodDeclaration();
        // set name of constructor
        constructorDeclaration.setName(this.abstractSyntaxTree.newSimpleName(methodName));
        // set access modifier of constructor
        Modifier accessModifier = this.abstractSyntaxTree.newModifier(accessModifierKeyword);
        constructorDeclaration.modifiers().add(accessModifier);
        // set MethodDeclaration as a constructor
        constructorDeclaration.setConstructor(true);
        return constructorDeclaration;
    }

    /**
     * Used to create a main method with the standard signature.
     *
     * @return object of type MethodDeclaration that can be added to a type
     */
    protected MethodDeclaration createMainMethodDeclaration() {
        logger.debug("Creating main method");
        MethodDeclaration mainMethod = this.declareMethod("main", createPrimitiveType(CodeGenerator.voidType),
                CodeGenerator.publicKeyword, true, false);
        // set args[] argument in main method
        SingleVariableDeclaration mainParameter = this.abstractSyntaxTree.newSingleVariableDeclaration();
        mainParameter.setName(this.abstractSyntaxTree.newSimpleName("args"));
        mainParameter.setType(this.createArrayType(this.createSimpleType("String")));
        mainMethod.parameters().add(mainParameter);
        return mainMethod;
    }

    /**
     * Used to create an empty block of code that can be added
     * to methods. A block can be added to a MethodDeclaration
     * using setBody(Block block) function of the MethodDeclaration
     *
     * @return object of type Block to which Statements can be added
     */
    protected Block createBlock() {
        return this.abstractSyntaxTree.newBlock();
    }

    /**
     * Used to create a PrimitiveType in an AST. The type can
     * be assigned to a variable or a function.
     *
     * @param type object of type Primitive.Code defined
     *             in the static fields of CodeGenerator
     * @return object of type PrimitiveType that extends Type
     */
    protected PrimitiveType createPrimitiveType(PrimitiveType.Code type) {
        logger.info("Creating primitive type {}", type);
        return this.abstractSyntaxTree.newPrimitiveType(type);
    }

    /**
     * Used to create a type that is user-defined. The type can
     * be assigned to a variable or a function.
     *
     * @param name name of the user-defined type
     * @return object of type SimpleType that extends Type
     */
    protected SimpleType createSimpleType(String name) {
        logger.info("Creating simple type {}", name);
        return this.abstractSyntaxTree.newSimpleType(this.abstractSyntaxTree.newName(name));
    }

    /**
     * Used to create an array type in an AST.
     *
     * @param type can be a PrimitiveType or SimpleType
     * @return object of type ArrayType that extends Type
     */
    protected ArrayType createArrayType(Type type) {
        return this.abstractSyntaxTree.newArrayType(type);
    }

    /**
     * Used to create parameterized types such as lists.
     *
     * @param outsideType types like List or Collection
     * @param insideType type of the elements
     * @return object of type ParameterizedType that extends Type
     */
    protected ParameterizedType createParameterizedType(Type outsideType, Type insideType) {
        ParameterizedType parameterizedType = this.abstractSyntaxTree.newParameterizedType(outsideType);
        parameterizedType.typeArguments().add(insideType);
        return parameterizedType;
    }

    /**
     * Used to create a variable declaration used in formal parameters.
     * These can be added to function arguments.
     *
     * @param name name of the variable
     * @param type type of the variable
     * @return object of type SingleVariableDeclaration
     */
    protected SingleVariableDeclaration createSingleVariableDeclaration(String name, Type type) {
        logger.info("Creating variable {}", name);
        SingleVariableDeclaration singleVariableDeclaration = this.abstractSyntaxTree.newSingleVariableDeclaration();
        singleVariableDeclaration.setName(this.abstractSyntaxTree.newSimpleName(name));
        singleVariableDeclaration.setType(type);
        return singleVariableDeclaration;
    }

    /**
     * Used to create a declaration of a variable in a code
     * block. A VariableDeclarationExpression contains a
     * VariableDeclarationFragment that allows us to set the name
     * of the variable. The type is set by the VariableDeclarationExpression.
     *
     * @param variableName name of the variable
     * @param variableType type of the variable
     * @return object of type VariableDeclarationExpression that extends Expression
     */
    protected VariableDeclarationExpression createVariableDeclarationExpression(String variableName,
                                                                                Type variableType) {
        logger.info("Creating variable declaration {}", variableName);
        // variable declaration fragment
        VariableDeclarationFragment variableDeclarationFragment =
                this.abstractSyntaxTree.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(this.abstractSyntaxTree.newSimpleName(variableName));
        // variable declaration expression
        VariableDeclarationExpression variableDeclarationExpression =
                this.abstractSyntaxTree.newVariableDeclarationExpression(variableDeclarationFragment);
        variableDeclarationExpression.setType(variableType);
        return variableDeclarationExpression;
    }

    /**
     * Used to create a field declaration inside a type. This can be added
     * to a type using bodyDeclarations() of a type. Like VariableDeclarationExpression,
     * FieldDeclaration also takes a VariableDeclarationFragment as input that contains
     * the name of the variable.
     *
     * @param variableName name of the field
     * @param variableType type of the field
     * @param accessModifierKeyword access modifier of the field,
     *                              set using the static fields of CodeGenerator
     * @param isStatic whether field is static
     * @return object of type FieldDeclaration
     */
    protected FieldDeclaration createFieldDeclaration(String variableName, Type variableType,
                                                      Modifier.ModifierKeyword accessModifierKeyword,
                                                      Boolean isStatic) {
        logger.info("Creating field declaration {}", variableName);
        // variable declaration fragment
        VariableDeclarationFragment variableDeclarationFragment =
                this.abstractSyntaxTree.newVariableDeclarationFragment();
        variableDeclarationFragment.setName(this.abstractSyntaxTree.newSimpleName(variableName));
        // field declaration
        FieldDeclaration fieldDeclaration = this.abstractSyntaxTree.newFieldDeclaration(variableDeclarationFragment);
        fieldDeclaration.setType(variableType);
        // set modifiers
        Modifier accessModifier = this.abstractSyntaxTree.newModifier(accessModifierKeyword);
        fieldDeclaration.modifiers().add(accessModifier);
        if (isStatic) {
            Modifier staticModifier = this.abstractSyntaxTree.newModifier(CodeGenerator.staticKeyword);
            fieldDeclaration.modifiers().add(staticModifier);
        }
        return fieldDeclaration;
    }

    /**
     * Used to create an invocation of a method. Takes an Expression
     * as an input to specify the entity that can invoke the method.
     *
     * @param methodName name of the method
     * @param expression expression that invokes the method (can be null)
     * @return object of type MethodInvocation that can be added
     * to method body
     */
    protected MethodInvocation createMethodInvocation(String methodName, @Nullable Expression expression) {
        logger.info("Creating method invocation {}", methodName);
        MethodInvocation methodInvocation = this.abstractSyntaxTree.newMethodInvocation();
        methodInvocation.setName(this.abstractSyntaxTree.newSimpleName(methodName));
        if (expression != null) {
            methodInvocation.setExpression(expression);
        }
        return methodInvocation;
    }

    /**
     * Creates a print statement
     *
     * @param argument argument of type expression
     * @return object of type Statement
     */
    protected Statement createPrintStatement(Expression argument) {
        logger.info("Creating print statement");
        // expression
        QualifiedName qName = this.abstractSyntaxTree.newQualifiedName(
                this.abstractSyntaxTree.newSimpleName("System"),
                this.abstractSyntaxTree.newSimpleName("out"));
        MethodInvocation printMethod = this.createMethodInvocation("println", qName);
        printMethod.arguments().add(argument);
        return this.abstractSyntaxTree.newExpressionStatement(printMethod);
    }

    /**
     * Used to create an expression that creates an instance
     * of a class
     *
     * @param instanceType type being instantiated
     * @return object of type ClassInstanceCreation that extends Expression
     */
    protected ClassInstanceCreation createInstanceCreationExpression(Type instanceType) {
        ClassInstanceCreation instanceCreation = this.abstractSyntaxTree.newClassInstanceCreation();
        instanceCreation.setType(instanceType);
        return instanceCreation;
    }

    /**
     * Creates and Assignment expression
     *
     * @param LHS Expression object
     * @param RHS Expression object
     * @return Assignment object which extends Assignment
     */
    protected Assignment createAssignmentExpression(Expression LHS, Expression RHS) {
        logger.info("Creating assignment expression");
        Assignment assignmentExpression = this.abstractSyntaxTree.newAssignment();
        assignmentExpression.setLeftHandSide(LHS);
        assignmentExpression.setRightHandSide(RHS);
        return assignmentExpression;
    }

    /**
     * Creates an expression that uses an operator between two
     * operands
     *
     * @param LHS Left operand
     * @param RHS Right operand
     * @param operator operator used, defined in the static fields of
     *                 CodeGenerator
     * @return object of type InfixExpression which extends Expression
     */
    protected InfixExpression createInfixExpression(Expression LHS, Expression RHS,
                                                    InfixExpression.Operator operator) {
        logger.info("Creating infix expression");
        InfixExpression infixExpression = this.abstractSyntaxTree.newInfixExpression();
        infixExpression.setLeftOperand(LHS);
        infixExpression.setRightOperand(RHS);
        infixExpression.setOperator(operator);
        return infixExpression;
    }

    /**
     * Used to create an access to a field in a class using this
     * object.
     *
     * @param simpleName name of variable of type SimpleName
     * @return object of FieldAccess which extends Expression
     */
    protected FieldAccess createFieldAccessExpression(SimpleName simpleName) {
        FieldAccess fieldAccess = this.abstractSyntaxTree.newFieldAccess();
        // this expression for field access
        ThisExpression thisExpression = this.abstractSyntaxTree.newThisExpression();
        // setting this expression
        fieldAccess.setExpression(thisExpression);
        fieldAccess.setName(simpleName);
        return fieldAccess;
    }

    /**
     * Used to create an if-else block.
     *
     * @param expression if condition
     * @param thenStatement what to do if condition gives true
     * @param elseStatement what to do if condition gives false (can be null)
     * @return object of type IfStatement that extends Statement
     */
    protected IfStatement createIfStatement(Expression expression, Statement thenStatement,
                                            @Nullable Statement elseStatement) {
        IfStatement ifStatement = this.abstractSyntaxTree.newIfStatement();
        ifStatement.setExpression(expression);
        ifStatement.setThenStatement(thenStatement);
        if (elseStatement != null) {
            ifStatement.setElseStatement(elseStatement);
        }
        return ifStatement;
    }

    /**
     * Used to create a return statement
     *
     * @param expression expression to be returned
     * @return object of type ReturnStatement which extends Statement
     */
    protected ReturnStatement createReturnStatement(Expression expression) {
        ReturnStatement returnStatement = this.abstractSyntaxTree.newReturnStatement();
        returnStatement.setExpression(expression);
        return returnStatement;
    }

    /**
     * Used to invoke a super class constructor. The resulting object
     * also accepts parameters like MethodDeclaration
     *
     * @param expression the expression that invokes super constructor
     *                   (can be null)
     * @return object of type SuperConstructorInvocation
     */
    protected SuperConstructorInvocation createSuperConstructorInvocation(@Nullable Expression expression) {
        SuperConstructorInvocation superConstructorInvocation =
                this.abstractSyntaxTree.newSuperConstructorInvocation();
        if (expression != null) {
            superConstructorInvocation.setExpression(expression);
        }
        return superConstructorInvocation;
    }

    /**
     * Creates an invocation of a super class method
     *
     * @param name name of the method
     * @return object of type SuperMethodInvocation
     */
    protected SuperMethodInvocation createSuperMethodInvocation(SimpleName name) {
        SuperMethodInvocation superMethodInvocation = this.abstractSyntaxTree.newSuperMethodInvocation();
        superMethodInvocation.setName(name);
        return superMethodInvocation;
    }

    /**
     * Used to create an import declaration
     *
     * @param name name of the import
     */
    protected void addImportDeclaration(Name name) {
        ImportDeclaration importDeclaration = this.abstractSyntaxTree.newImportDeclaration();
        importDeclaration.setName(name);
        this.compilationUnit.imports().add(importDeclaration);
    }

    /**
     * applies edits to the compilation unit to the document
     *
     * @throws BadLocationException
     */
    protected void applyEdits() throws BadLocationException {
        TextEdit edits = this.compilationUnit.rewrite(this.document, null);
        edits.apply(this.document);
    }

}
