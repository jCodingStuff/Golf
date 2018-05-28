package com.group.golf.Physics;

/**
 * Created by alex_ on 23-May-18.
 */
public class node<E> {
    E element;
    node<E> next;
    node<E> prev;

    public node(E element) {
        this.element = element;
        next = null;
    }
}
