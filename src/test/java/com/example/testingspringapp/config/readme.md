## Unit test class support by Spring TestingFramework
Spring Testing Framework provides few helpful mocked class that can be used in unit testing.
* `MockEnvironment
  * If a class has dependency to `Environment` then `MockEnvironment` class can easily be used in Unit testing. `MockEnvironment` class's set method can be used to set the property.
  * See `DbConfigTest` for example.

