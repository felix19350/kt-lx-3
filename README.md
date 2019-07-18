# Support materials for Kotlin Lisboa #3

This repository contains the full code examples that were presented on Kotlin Lisboa #3, for the "Testing with Kotlin - Practices and practices from the trenches".

Although these examples don't contain a fully working application, the main focus is to showcase several interesting usages of Kotlin for testing purposes. 

## Structure ##

The code is structured in the standard maven/gradle folder structure, so hopefully there will be no surpises there.

The presentation itself is located in the *docs* folder 

## Running the tests ##

### Terminal ###
* Open a terminal on this project's root
* Type: *./gradlew test*
* Open the */build/reports/tests/test/index.html* report to view the test results on the browser


### Intellij ###
* Just import the project :)
* Run the tests individually or through the gradle task


## Playing around with the code ##
* Do yourself a favour and use Intellij
* Install ktlint (https://github.com/pinterest/ktlint)
* Integrate ktlint with intellij, so that the IDE's code formatting is compliant with the linter: *ktlint --apply-to-idea-project*  