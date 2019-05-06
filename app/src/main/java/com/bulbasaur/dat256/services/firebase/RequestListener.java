package com.bulbasaur.dat256.services.firebase;

public class RequestListener<E> {

    private boolean debug;
    private E object;

    public RequestListener(boolean debug) {
        this.debug = debug;
    }

    public RequestListener() {
        this(false);
    }

    public void onComplete(E object) {
        onFinish(object,"complete");
    }

    public void onSuccess(E object) {
        onFinish(object,"successful");
    }

    public void onFailure(E object) {
        onFinish(object,"failure");
    }

    private void onFinish(E object, String status) {
        this.object = object;
        if (debug) {
            System.out.println("Request finished with status '" + status + "' for object: " + object);
        }
    }

    public E getObject() {
        return object;
    }
}
