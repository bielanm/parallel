cmake_minimum_required(VERSION 3.6)
project(parallelmp)

set(CMAKE_CXX_STANDARD 14)

set(SOURCE_FILES main.cpp IntVector.h IntVector.cpp MathUtil.cpp MathUtil.h)
add_executable(parallelmp ${SOURCE_FILES})