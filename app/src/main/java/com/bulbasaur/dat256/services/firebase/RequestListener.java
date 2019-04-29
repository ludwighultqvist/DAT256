package com.bulbasaur.dat256.services.firebase;

public abstract class RequestListener {

    abstract void onComplete();

    abstract void onSuccess();

    abstract void onFailure();
}
