# Kubernetes overview

## Description
In this part the infrastructure for k8s cluster will be created and deploy the microservices applications there.

## Getting started

The system can be deployed locally on kubernetes cluster. To use Kubernetes go to Docker Desktop settings, choose Kubernetes and click checkbox 'Enable Kubernetes'. You will need to wait for the installation and restart docker.
To verify installation run the next command: ```kubectl version```.

## Deploy system on local k8s cluster

In order to deploy system on local k8s cluster you need to go to the [folder](#https://git.epam.com/grzegorz_chabiera/k8s-course/-/tree/master/k8s-overview/manifest) and run the following command

- deploy user-service with local storage

      kubectl apply -f ./sub-task-3

- deploy the system (both microservices with databases)

      kubectl apply -f ./sub-task-4

To shut down system, run respectively ```kubectl delete -f ./sub-task-3``` or ```kubectl delete -f ./sub-task-4```

## Useful Command
- view all object in _**k8s-course**_ namespace

      kubectl get all -n k8s-course
    
- list all pods/deployments/services in _**k8s-course**_ namespace, with more details
  
      kubectl get pods -n k8s-course -o wide
      kubectl get deployments -n k8s-course -o wide
      kubectl get services -n k8s-course -o wide
- view particular resource in _**k8s-course**_ namespace

      kubectl get pod <pod-name> -n k8s-course
      kubectl get deployment <deployment-name> -n k8s-course
      kubectl get service <service-name> -n k8s-course
- view details of resource in _**k8s-course**_ namespace

      kubectl describe <resource-type> <resource-name> -n k8s-course
