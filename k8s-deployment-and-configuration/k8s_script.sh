#! /usr/bin/bash

kubectl apply -f namespace.yml
kubectl apply -f configmap.yml
kubectl apply -f secret.yml