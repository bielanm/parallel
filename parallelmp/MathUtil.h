#ifndef MATHUTIL_H
#define MATHUTIL_H

#include "IntVector.h"

class MathUtil {

    public:
        static IntVector multiplyCuncurrent(IntVector* v1, IntVector* v2, int size);
        static IntVector multiply(IntVector* v1, IntVector* v2, int size);
};

#endif
