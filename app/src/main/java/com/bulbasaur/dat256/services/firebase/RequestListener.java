package com.bulbasaur.dat256.services.firebase;


public class RequestListener<E> {

    private boolean isBusyWait;
    private boolean done = false;
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
        String print = "Request finished with status '" + status + "' for object: " + objectString;
        System.out.println(print);
        //Log.d("REQUEST", print);
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
