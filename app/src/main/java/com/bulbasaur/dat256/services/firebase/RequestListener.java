package com.bulbasaur.dat256.services.firebase;

public abstract class RequestListener {

    public abstract void onComplete();

    public abstract void onSuccess();

    public abstract void onFailure();
}
