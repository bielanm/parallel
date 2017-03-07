#include <iostream>
#include "MathUtil.h"
#include "IntVector.h"

IntVector MathUtil::multiplyCuncurrent(IntVector* v1, IntVector* v2, int size) {
    std::vector<int> result(size);
    #pragma omp parallel
    {
        #pragma omp for
        for (int i = 0; i < size; ++i) {
            result[i] = v1[i].multiply(v2[i]);
        }
    }

    return IntVector(result);
}

IntVector MathUtil::multiply(IntVector* v1, IntVector* v2, int size) {
    std::vector<int> result(size);
    for (int i = 0; i < size; ++i) {
        result[i] = v1[i].multiply(v2[i]);
    }
    return IntVector(result);
}