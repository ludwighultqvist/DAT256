package com.bulbasaur.dat256.services.firebase;

public interface RequestListener {

    void onComplete();

    void onSuccess();

    void onFailure();
}
