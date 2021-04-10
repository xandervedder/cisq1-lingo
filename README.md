# CISQ1: Lingo Trainer
Lingo trainer built with Java

[![Java CI](https://github.com/xandervedder/cisq1-lingo/actions/workflows/build.yml/badge.svg)](https://github.com/xandervedder/cisq1-lingo/actions/workflows/build.yml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=xandervedder_cisq1-lingo&metric=coverage)](https://sonarcloud.io/dashboard?id=xandervedder_cisq1-lingo)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=xandervedder_cisq1-lingo&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=xandervedder_cisq1-lingo)

## Domain diagram
Initial thoughts about the classes that will be needed in the exercise (concept).

![domain_diagram](/documentation/domain/domain.png)

<sub><sup>*Ended up using a much simpler implementation*</sub></sup>

## Features
- Working implementation of Lingo (as specified by the exercise)
- Tests on multiple levels:
  - Integration tests
  - Unit tests
  - Architectural tests (archunit) - tests if certain packages don't communicate use other package (e.g. domain layer --> application layer)
- 100% test coverage in pretty much everything:
  - Line Coverage
  - Branch Coverage
  - Mutation Coverage
