package DesignPatterns.AbstractFactory;

import DesignPatternCodeGenerator.CodeGenerator;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class AbstractFactory extends CodeGenerator {

        String productAClass, productBClass;

        public AbstractFactory(String filename, String productAClass, String productBClass) {
            super(filename);
            this.productAClass = productAClass;
            this.productBClass = productBClass;
        }

        public Document buildCode() throws BadLocationException {
            this.createTypeDeclaration(this.fileName, true, CodeGenerator.publicKeyword,
                    false, false, null, null);
            MethodDeclaration productAMethod = this.declareMethod("create" + this.productAClass,
                    this.createSimpleType(this.productAClass),
                    CodeGenerator.publicKeyword, false, false);
            MethodDeclaration productBMethod = this.declareMethod("create" + this.productBClass,
                    this.createSimpleType(this.productBClass),
                    CodeGenerator.publicKeyword, false, false);
            this.classDeclaration.bodyDeclarations().add(productAMethod);
            this.classDeclaration.bodyDeclarations().add(productBMethod);

            // add class to CU
            this.compilationUnit.types().add(this.classDeclaration);
            // apply edits to document
            this.applyEdits();
            // return documents
            return this.document;
        }

}
