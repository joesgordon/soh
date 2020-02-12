#!/bin/bash

g++ -o krelay.exe -I. -l pthread ../win10/krelay/krelay.cpp ./libftd2xx.a

