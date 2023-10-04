# Spring Testing Framework

## Unit test class support by Spring TestingFramework
Spring Testing Framework provides few helpful mocked class(General Purpose Test Util class) that can be used in unit testing.
* MockEnvironment
  * If a class has dependency to `Environment` then `MockEnvironment` class can easily be used in Unit testing. `MockEnvironment` class's set method can be used to set the property.
  * See `DbConfigTest` for example.
* `ReflectionTestUtils` can be used to set non public field of object, set the dependency as well.
  *  See `DbConfigTestUsingReflectionTestUtils`

Apart from General Purpose Test Util classes Spring provides MVC related general purpose Test util classes. We will see that in while.

## Spring Test Context Initialization

* Using Component Classes Refer: `DatasourceProviderTestContextLoadWithClass`
* Using `TestPropertySource`
  TestPropertySource can be configured by following ways:
  - Inline, Key and values pair can be passed inline like below 
    ```java 
    @TestPropertySource(properties = {"db.username=childUsername", "db.password=childPassword"}) 
    ```
  -  From property file like below 
     ```java
            @TestPropertySource(locations="");
      ``` 
  * File Resolution
    - When nothing is passed, directory => Test file location and file name => Test file name
    - It can look for file from classpath directory. Like this `classpath: db.properties`
    - It can look for file from other location as well like this `file: ~/db.properties`
* Using `Profile`
    Create a configuration class annotate that with `@Profile` and then to use it in Testing just annotate test file with `@ActiveProfile`
    Whenever Active Profile changes, Spring reloads the context with respective configuration specified in Profile. For Example, If a Test class
    having ActiveProfile for dev then Test loads dev configuration and run the test. If a there is Nested test inside the same test class, then
    previously loaded configuration gets unloaded and Spring adds new profile related configuration.

   `@NestedTestConfiguration` accepts argument either inherit(default) or override mode. Override mode clears all configuration
    coming from outer test class. Nested test class having`@NestedTestConfiguration` with override mode must specify new configuration
    with which Test will run. If no configuration is passed then this is like running test without any spring context initialized.

  Refer `LoadConfigWithProfileTest` to see example
* Using `contextInitializer` 
    Refer `DataSourceProviderWithContextInitializer`
* Using Inheritance
