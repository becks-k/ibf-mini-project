# Build angular
FROM node:22 AS ngbuild

# directory INSIDE image
WORKDIR /frontend-mystery

# Install angular
RUN npm i -g @angular/cli@17.3.8

# Copy the required files for building
# from directory FRONTEND on local machine
COPY frontend-mystery/angular.json .
COPY frontend-mystery/package*.json .
COPY frontend-mystery/tsconfig*.json .
COPY frontend-mystery/ngsw-config.json .
COPY frontend-mystery/src src

# Install modules - ci installs packages from package-lock.json
# npm ci stands for "npm clean install." It removes the node_modules folder and installs dependencies from scratch based exactly on the package-lock.json file.
RUN npm ci
RUN ng build

# Build Spring boot
FROM openjdk:21 AS javabuild

WORKDIR /backend-mystery

# . refers to destination in docker container /backend
# copies mvn files in backend-mystery/.mvn into /backend-mystery/.mvn
COPY backend-mystery/mvnw .
COPY backend-mystery/pom.xml .
COPY backend-mystery/.mvn .mvn
COPY backend-mystery/src src


# Copy angular files to Spring Boot
COPY --from=ngbuild /frontend-mystery/dist/frontend-mystery/browser/ src/main/resources/static

# Compile SpringBoot
# produce target/mysterygame-0.0.1-SNAPSHOT.jar
# skip tests
# ./mvnw is a maven wrapper script that ensures the correct maven is used, regardless of version installed on host machine
RUN chmod a+x mvnw
RUN ./mvnw package -Dmaven.test.skip=true

# Run container
FROM openjdk:21

WORKDIR /app

COPY --from=javabuild /backend-mystery/target/mysterygame-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080 

EXPOSE ${PORT}

# starts container
ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar .