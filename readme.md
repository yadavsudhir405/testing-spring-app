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
     -  File Resolution
       - When nothing is passed, directory => Test file location and file name => Test file name
       - It can look for file from classpath directory. Like this `classpath: db.properties`
       - It can look for file from other location as well like this `file: ~/db.properties`
