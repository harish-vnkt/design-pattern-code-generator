package DesignPatternCodeGenerator;

import DesignPatterns.Facade.Facade;
import com.typesafe.config.*;
import DesignPatternBuilders.*;
import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;

public class DesignPatternFactory {

    private int patternNumber;
    private Config configObject;

    public DesignPatternFactory(int patternNumber) throws IOException, BadLocationException {
        this.patternNumber = patternNumber;
        this.configObject = ConfigFactory.load();
        callBuilder();;
    }

    private void callBuilder() throws IOException, BadLocationException {

        if (patternNumber == 1) {
            AbstractFactoryBuilder abstractFactoryBuilder = new AbstractFactoryBuilder(
                    this.configObject.getString("AbstractFactory.entryClass"),
                    this.configObject.getString("AbstractFactory.clientClass"),
                    this.configObject.getString("AbstractFactory.factoryClass"),
                    this.configObject.getString("AbstractFactory.factoryInterface"),
                    this.configObject.getString("AbstractFactory.productAClass"),
                    this.configObject.getString("AbstractFactory.productBClass")
            );
            abstractFactoryBuilder.writeFile();
        }

        else if (patternNumber == 2) {
            BuilderBuilder builderBuilder = new BuilderBuilder(
                    this.configObject.getString("Builder.directorClass"),
                    this.configObject.getString("Builder.builderClass"),
                    this.configObject.getString("Builder.complexObjectClass"),
                    this.configObject.getString("Builder.productInterface"),
                    this.configObject.getString("Builder.productAClass"),
                    this.configObject.getString("Builder.productBClass")
            );
            builderBuilder.writeFile();
        }

        else if (patternNumber == 3) {
            FactoryBuilder factoryBuilder = new FactoryBuilder(
                    this.configObject.getString("Factory.creatorClass"),
                    this.configObject.getString("Factory.subCreatorClass"),
                    this.configObject.getString("Factory.productInterface"),
                    this.configObject.getString("Factory.productAClass")
            );
            factoryBuilder.writeFile();
        }

        else if (patternNumber == 4) {
            FacadeBuilder facadeBuilder = new FacadeBuilder(
                    this.configObject.getString("Facade.facadeClass"),
                    this.configObject.getString("Facade.facadeSubClass"),
                    this.configObject.getString("Facade.class1"),
                    this.configObject.getString("Facade.class2"),
                    this.configObject.getString("Facade.class3"),
                    this.configObject.getString("Facade.method1"),
                    this.configObject.getString("Facade.method2"),
                    this.configObject.getString("Facade.method3")
            );
            facadeBuilder.writeFile();;
        }

        else if (patternNumber == 5) {
            ChainOfResponsibilityBuilder corBuilder = new ChainOfResponsibilityBuilder(
                    this.configObject.getString("ChainOfResponsibility.senderClass"),
                    this.configObject.getString("ChainOfResponsibility.handlerClass"),
                    this.configObject.getString("ChainOfResponsibility.receiver1Class"),
                    this.configObject.getString("ChainOfResponsibility.receiver2Class")
            );
            corBuilder.writeFile();
        }

        else if (patternNumber == 6) {
            MediatorBuilder mediatorBuilder = new MediatorBuilder(
                    this.configObject.getString("Mediator.mediatorInterface"),
                    this.configObject.getString("Mediator.mediatorClass"),
                    this.configObject.getString("Mediator.colleagueAbstractClass"),
                    this.configObject.getString("Mediator.colleague1Class"),
                    this.configObject.getString("Mediator.colleague2Class")
            );
            mediatorBuilder.writeFile();
        }

        else if (patternNumber == 7) {
            VisitorBuilder visitorBuilder = new VisitorBuilder(
                    this.configObject.getString("Visitor.clientClass"),
                    this.configObject.getString("Visitor.visitorAbstractClass"),
                    this.configObject.getString("Visitor.visitorConcreteClass"),
                    this.configObject.getString("Visitor.elementAbstractClass"),
                    this.configObject.getString("Visitor.element1Class"),
                    this.configObject.getString("Visitor.element2Class")
            );
            visitorBuilder.writeFile();
        }
        
    }

}
