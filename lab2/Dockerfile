FROM openjdk:8

ENV SPRING_ROOT /spring

WORKDIR $SPRING_ROOT

RUN wget https://bootstrap.pypa.io/get-pip.py && \
    python get-pip.py && \
    pip install boto3 && \
    pip install ec2_metadata 
    #wget https://raw.githubusercontent.com/soldierxue/spring-ecs/master/ecs-port-mapping.py -P /spring

RUN rm -rf $SPRING_ROOT/*
#RUN wget https://raw.githubusercontent.com/soldierxue/spring-ecs/master/ecs-port-mapping.py
COPY ./*.jar $SPRING_ROOT/
COPY ./ecs-port-mapping.py $SPRING_ROOT/

ENTRYPOINT ["python","./ecs-port-mapping.py"]
CMD ["java","-jar","`ls *.jar | head -n 1`"]
EXPOSE 8080
