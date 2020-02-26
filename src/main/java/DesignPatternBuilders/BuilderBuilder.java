package DesignPatternBuilders;

import DesignPatternCodeGenerator.CodeBuilder;
import DesignPatterns.Builder.*;
import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.io.File;

public class BuilderBuilder implements CodeBuilder {

    String directorClass, builderClass, complexObjectClass, productInterface, productAClass, productBClass;
    String directoryPath = "generated_patterns/Builder/";

    public BuilderBuilder(String directorClass, String builderClass, String complexObjectClass,
                                  String productInterface, String productAClass, String productBClass) {
        this.directorClass = directorClass;
        this.builderClass = builderClass;
        this.complexObjectClass = complexObjectClass;
        this.productInterface = productInterface;
        this.productAClass = productAClass;
        this.productBClass = productBClass;
    }

    public void writeFile() throws BadLocationException, IOException {

        // create file objects
        Director directorObject = new Director(this.directorClass, this.complexObjectClass, this.builderClass);
        Builder builderObject = new Builder(this.builderClass, this.complexObjectClass, this.productAClass,
                this.productBClass);
        ComplexObject complexObjectObject = new ComplexObject(this.complexObjectClass, this.productInterface);
        Product productObject = new Product(this.productInterface);
        ProductA productAObject = new ProductA(this.productAClass, this.productInterface);
        ProductB productBObject = new ProductB(this.productBClass, this.productInterface);

        // write the generated code
        String directorFilename = this.directoryPath + directorObject.fileName + ".java";
        String builderFilename = this.directoryPath + builderObject.fileName + ".java";
        String complexObjectFilename = this.directoryPath + complexObjectObject.fileName + ".java";
        String productFilename = this.directoryPath + productObject.fileName + ".java";
        String productAFilename = this.directoryPath + productAObject.fileName + ".java";
        String productBFilename = this.directoryPath + productBObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(directorFilename),
                directorObject.buildCode().get(), StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(builderFilename), builderObject.buildCode().get(), StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(complexObjectFilename),
                complexObjectObject.buildCode().get(), StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(productFilename), productObject.buildCode().get(), StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(productAFilename),
                productAObject.buildCode().get(), StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(productBFilename),
                productBObject.buildCode().get(), StandardCharsets.UTF_8);
    }

}
