#include <iostream>
#include <cstdlib>
#include <ctime>
#include "IntVector.h"
#include "MathUtil.h"

#define MAX_ELEMENT 100
#define VECTOR_SIZE 500
#define VECTORS_COUNT 10

int main() {
    srand(time(NULL));
    IntVector arrOne[VECTORS_COUNT];
    IntVector arrTwo[VECTORS_COUNT];
    for(int i = 0; i < VECTORS_COUNT; i++) {
        arrOne[i] = IntVector::newInstance(VECTOR_SIZE, MAX_ELEMENT);
        std::cout<<arrOne[i]<<std::endl;
        arrTwo[i] = IntVector::newInstance(VECTOR_SIZE, MAX_ELEMENT);
        std::cout<<arrTwo[i]<<std::endl;
    }
    const clock_t begin_time_cunc = clock();
    IntVector resultCuncurrent = MathUtil::multiplyCuncurrent(arrOne, arrTwo, VECTORS_COUNT);
    const clock_t end_time_cunc = clock();
    const clock_t begin_time = clock();
    IntVector result = MathUtil::multiply(arrOne, arrTwo, VECTORS_COUNT);
    const clock_t end_time = clock();
    std::cout<<"Cuncurrent time result: "<<float(end_time_cunc - begin_time_cunc)<<std::endl;
    std::cout<<"Time result: "<<float(end_time - begin_time)<<std::endl;
    std::cout<<result<<std::endl;
    return 0;
}