#ifndef INTVECTOR_H
#define INTVECTOR_H

#include <vector>
#include <ostream>

class IntVector {

    private:
        std::vector<int> vector;

    public:
        IntVector(std::vector<int> vector);
        IntVector();

        friend std::ostream& operator<<(std::ostream& os, IntVector const &elem);
        static IntVector newInstance(int n, int maxValue);
        int multiply(IntVector other);
        int getElem(int n);
        int size();
};

#endif
