package com.bulbasaur.dat256.services.firebase;

public abstract class RequestListener<E> {

    public void onComplete() {

    };

    public void onSuccess() {

    };

    public void onFailure() {

    };

    public void onComplete(E object) {
    }

    public void onSuccess(E object) {
    }

    public void onFailure(E object) {

    }
}
