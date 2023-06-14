setup:
	gradle wrapper --gradle-version 7.5

clean:
	./gradlew clean

build:
	./gradlew clean build

run:
	./gradlew bootRun --args='--spring.profiles.active=dev'

run-prod:
	./gradlew bootRun --args='--spring.profiles.active=prod'

install:
	./gradlew installDist

start-dist:
	./build/install/app/bin/app

lint:
	./gradlew checkstyleMain checkstyleTest

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

check-updates:
	./gradlew dependencyUpdates

generate-migrations:
	gradle diffChangeLog

db-migrate:
	./gradlew update


.PHONY: build
