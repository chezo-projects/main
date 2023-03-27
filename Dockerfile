FROM arm64v8/amazoncorretto:17
WORKDIR "myApp"
COPY build/libs/main-0.0.1-SNAPSHOT.jar ./app.jar
CMD java -jar app.jar


