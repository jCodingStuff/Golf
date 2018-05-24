package com.group.golf.Physics;

/**
 * Created by alex_ on 23-May-18.
 */

public class Queue<E> {
    protected node<E> head;
    protected node<E> tail;
    private int size;

    public Queue() {
        head = null;
        tail = null;
        size = 0;
    }

    public void enqueue(E element) {
        if (head == null) {
            head = new node<E>(element);
            tail = head;
        } else {
            node<E> newTail = new node<E>(element);
            node<E> temp = tail;
            temp.next = newTail;
            newTail.prev = temp;
            tail = newTail;
        }
        size++;
    }

    public E dequeue() {
        E temp = head.element;
        head = head.next;
        size--;
        return temp;
    }

    public E last() {
        return tail.element;
    }


    public int getSize() {
        return size;
    }
}
