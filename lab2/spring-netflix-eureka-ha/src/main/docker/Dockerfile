FROM openjdk:8

ENV SPRING_ROOT /spring

WORKDIR $SPRING_ROOT

RUN rm -rf $SPRING_ROOT/*
COPY ./*.jar $SPRING_ROOT/

CMD java -jar `ls *.jar | head -n 1`

EXPOSE 8080