package com.bielanm.net;

import com.bielanm.net.exceptions.HttpExeption;

public interface RequestDispatcher {
    void dispatch(HttpRequest httpRequest, HttpResponse httpResponse) throws HttpExeption;
}
