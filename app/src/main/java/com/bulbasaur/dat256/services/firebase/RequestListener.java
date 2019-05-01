package com.bulbasaur.dat256.services.firebase;

public class RequestListener<E> {

    private boolean isBusyWait;
    private boolean done;
    private E object;

    public RequestListener(boolean isBusyWait) {
        this.isBusyWait = isBusyWait;
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
        String objectString = object == null ? null : object.toString();
        System.out.println("Request finished with status '" + status + "' for object: " + objectString);
        done = true;
    }

    void finish() {
        if (isBusyWait) {
            while (!done);
        }
    }

    public E getObject() {
        return object;
    }

    boolean isBusyWait() {
        return isBusyWait;
    }
}
