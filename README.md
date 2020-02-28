# Design Pattern Code Generator
##### Harish Venkataraman | hvenka8@uic.edu | 668426171
---
### Build tools
* Gradle
* IntelliJ

### Build instructions

To build the project - ```gradle clean build```

To run the executable in the project - ```gradle run```

To build without executing test cases - ```gradle clean build -x test```

### User inputs

An ```application.conf``` file is provided in ```src/main/java/resources/``` which can be used to modify the input values for any design pattern. Running the project asks the user for the design pattern to be generated and then selectively reads ```application.conf``` file for the desired inputs based on the design pattern chosen. Default inputs are provided in the configuration file, so the project can run without any modifications, too. The logs are displayed on the console as well as stored in a file named ```logs.log```.

### System design

The design chosen for the Design Pattern Code Generator is a combination of the Abstract Factory and Builder design patterns. The base classes for the implementation of the code generator is present in the ```src/main/java/DesignPatternCodeGenerator``` directory. Each design pattern has a Builder class assigned to it having the suffix *Builder* in the filename. The Builder classes are all present in the ```src/main/java/DesignPatternBuilders``` directory. The generation code for each of the design patterns are present in the appropriate directories in ```src/main/java/DesignPatterns```. 

For the code generation, I have used the Eclipse AST Parser to build the code. For file I/O, apache-commons along with Java FileIO is used. JUnit4 is used for testing. TypeSafe is used for input configurations and Logback is used for logging.

### Components

![alt text][uml]

[uml]: readme_resources/uml.png

In my abstraction, the design patterns are realized through their component files. Each design pattern is made of a collection of classes and interfaces (top-level types) that make up the pattern. The following are the main components of the abstraction. 

#### CodeGenerator

```CodeGenerator``` is an abstract class that represents a component of a design pattern. Each class that makes up a particular design pattern extends ```CodeGenerator```.

For a class of a design pattern, ```CodeGenerator``` provides a set of fields and methods that help generate the code for that particular class. Using these fields and methods, it is the responsibility of the individual classes to provide implementation to the abstract ```buildCode()``` method of ```CodeGenerator```. 

All of the functionality to build an **abstract syntax tree** is provided in the ```CodeGenerator``` that a class of a design pattern inherits. Since all of the code generation needs to modify an individual abstract syntax tree, the ```CodeGenerator``` was not implemented as a utility class, but instead as a set of inherited functions that allow a class in the design pattern to change the abstract syntax that it represents.

A class in a design pattern that extends ```CodeGenerator``` returns a ```Document``` object from the ```buildCode()``` method. This ```Document``` object is collected along with other ```Document``` objects from the other component classes of a design pattern by a Builder class, which has the responsibility of writing the generated code to the corresponding files.

#### CodeBuilder

```CodeBuilder``` is an interface that is implemented by the Builders of all the design patterns. This interface is an implementation of the builder design pattern and forces all the Builders to implement a function called ```writeFile()```. Each Builder uses the ```writeFile()``` function to get the generated code from each component of its design pattern and writes the generated code to files. It is also the responsibility of the Builder to pass the appropriate user inputs for a design pattern to the appropriate component classes.

#### DesignPatternFactory

```DesignPatternFactory``` is an implementation of the abstract factory design pattern whose job is to instantiate the correct Builder to build the code for the design pattern that the user has chosen. The factory also selectively reads user inputs from the config file based on the design pattern chosen to be generated.

#### DesignPatterns

The implementation of the code generation for each of the design patterns are present in appropriate subdirectories of ```DesignPatterns```. Each design pattern consists of component classes that generate the code for that component of the pattern. Each component extends the ```CodeGenerator``` class as mentioned in the above sections.

### Results

The design patterns implemented here are - 

* Abstract factory
* Builder
* Facade
* Factory
* Chain of Responsibility
* Mediator
* Visitor

There are five unit tests and integration tests for all the implemented design patterns. The generated patterns are written to a folder named ```generated_patterns``` in the project directory under the appropriate folders.

### Areas of improvement

* **Coverage** - Could have implemented more design patterns if I had managed time better. I spent more time than necessary trying to design my abstraction instead of implementing design patterns.
* **Design** - Since any code that needs to be generated needs to be added to the abstract syntax tree under construction, the ```CodeGenerator``` class was implemented as an abstract class to inherit rather than a utility class, which seems more reasonable to me. It makes more sense as a utility class because of the presence of methods in the class that generate pieces of code that I manually piece together later in the subclasses.
* **Testing and Exceptions** - The testing on all the methods for manipulating the abstract syntax was not exhaustive. A lot of the parts of the tree are added to list objects and I have not done type-checking before adding them.
* **Scope for further abstraction** - I ended up repeating code generating procedures for multiple files that needed to be generated, for example, the steps to create a method block, or the steps to add formal parameters, or the steps create an assignment expression. This could have been avoided by more careful abstraction. I also repeated code for creating the Builder classes which could have been avoided in retrospect.
* **Limited input freedom** - The design patterns have been chosen with a basic structure that is to be generated giving the user less number of inputs to manipulate. The code generation should ideally cover a broader scope.
* **Logging** - Logging could have been better and more structured.