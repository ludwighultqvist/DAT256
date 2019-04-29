package com.bulbasaur.dat256.services.firebase;

public abstract class RequestListener<E> {

    public abstract void onComplete();

    public abstract void onSuccess();

    public abstract void onFailure();

    public void onComplete(E object) {
    }

    public void onSuccess(E object) {
    }

    public void onFailure(E object) {

    }
}
