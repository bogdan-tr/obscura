#!/bin/bash

javac GUI.java

if [ $? -eq 0 ]; then
    java GUI
fi
