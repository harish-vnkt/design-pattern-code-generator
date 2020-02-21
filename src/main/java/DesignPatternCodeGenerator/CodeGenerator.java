package DesignPatternCodeGenerator;

import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;

/**
 * The abstract class CodeGenerator is a type that represents
 * individual files in a particular design pattern. The files
 * that implement it have access to functions that help build
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

}
