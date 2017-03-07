#include <cstdlib>
#include <vector>
#include "IntVector.h"

int IntVector::size() {
    return this->vector.size();
}

int IntVector::getElem(int n) {
    return this->vector[n];
}

int IntVector::multiply(IntVector other) {
    int result = 0;
    for (int i = 0; i < size(); ++i) {
        result += getElem(i)*other.getElem(i);
    }
    return result;
}

IntVector IntVector::newInstance(int n, int maxValue) {
    std::vector<int> newInstnc = std::vector<int>();
    for (int i = 0; i < n; ++i) {
        newInstnc.push_back(rand() % maxValue);
    }
    return IntVector(newInstnc);
}

std::ostream& operator<<(std::ostream &os, IntVector const &vector) {
    IntVector v = vector;
    for(int i=0; i < v.size(); ++i) {
        os << v.getElem(i) << ' ';
    }
    return os;
}

IntVector::IntVector(std::vector<int> v) {
    this->vector = v;
}

IntVector::IntVector() {}
