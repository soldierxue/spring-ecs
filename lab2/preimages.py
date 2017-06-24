#!/bin/python
import argparse
import json
import logging
import os
import time
import uuid
from os.path import expanduser
from random import randint

import boto3

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def cleanup(project_name='spring-cloud-demo', service_list={'spring-netflix-eureka-ha':'8761','spring-config-server':'7001','spring-petclinic-rest-owner':'8080','spring-petclinic-rest-vet':'8080','spring-petclinic-rest-visit':'8080'}, region='ap-northeast-1'):
    ecr_client = boto3.client('ecr')

    for service in service_list:
       try:
            ecr_client.delete_repository(repositoryName=service, force=True)
       except Exception as e:
            logger.error(e)	

def docker_login_config():
    ecr_client = boto3.client('ecr')
    # Get latest authorization token and put it in ~/.docker/config.json
    ecr_login_token = ecr_client.get_authorization_token().get('authorizationData')[0].get('authorizationToken')
    hostname = ecr_client.get_authorization_token().get('authorizationData')[0]['proxyEndpoint'][8:]

    home = expanduser("~")
    filename = home + '/.docker/config.json'
    with open(filename, 'r+') as f:
        data = json.load(f)
        data['auths'] = {
            "https://"+hostname: {
                "auth": ecr_login_token
            }
        }
        #data['auths'][hostname]['auth'] = ecr_login_token
        # logger.info('Writing docker configuration as '+str(data)
        f.seek(0)
        f.write(json.dumps(data))
        f.truncate()

def setup(project_name='spring-cloud-demo', service_list={'spring-netflix-eureka-ha':'8761','spring-config-server':'7001','spring-petclinic-rest-owner':'8080','spring-petclinic-rest-vet':'8080','spring-petclinic-rest-visit':'8080'}, region='ap-northeast-1'):
    ecr_client = boto3.client('ecr')

    docker_login_config()
    repository_uri = []

    for service in service_list:
        logger.info("Create resources for service: " + service)

        # Create repository ignore repository exists error
        create_repository_response = ecr_client.create_repository(repositoryName=service)
        logger.info("Create ECR repository")
        uri = create_repository_response['repository']['repositoryUri']
        repository_uri.append({service: uri})

        # Set repository host URL in pom.xml
        os.environ["docker_registry_host"] = uri.split('/')[0]
        logger.info('Compile project, package, bake image, and push to registry for ' + service)
        # Compile project, package, bake image, and push to registry
        os.chdir(service)
        os.system('mvn clean package docker:build -DpushImage -Dmaven.test.skip=true')	
        os.chdir("..")


def main():
    parser = argparse.ArgumentParser(description="Execute input file. Supports only python or sh file.")
    parser.add_argument('-m', '--mode', required=True, help="execution mode -m cleanup or -m setup")
    parser.add_argument('-p', '--project_name', required=False, default='spring-cloud-demo',
                        help="Name of the project")
    parser.add_argument('-r', '--region', required=True, default='ap-northeast-1', help="Region. Default 'ap-northeast-1'")
    parser.add_argument('-s', '--service_list', required=False, default={'spring-netflix-eureka-ha':'8761','spring-config-server':'7001','spring-petclinic-rest-owner':'8080','spring-petclinic-rest-vet':'8080','spring-petclinic-rest-visit':'8080'},
                        help="Service list. The artificat id of the Spring Application")
    args = parser.parse_args()

    project_name = args.project_name
    service_list = args.service_list
    region = args.region
    mode = args.mode
    os.environ["AWS_DEFAULT_REGION"] = region

    logger.info("Mode: " + mode)

    if mode == 'setup':
        setup_results = setup(project_name=project_name, service_list=service_list, region=region)
        logger.info("Setup is complete")
    elif mode == 'cleanup':
        cleanup_results = cleanup(project_name=project_name, service_list=service_list, region=region)
    else:
        parser.print_help()
        raise Exception("Not supported mode")


if __name__ == "__main__":
    main()
