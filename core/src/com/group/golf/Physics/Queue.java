package com.group.golf.Physics;

/**
 * Created by alex_ on 23-May-18.
 */
public class Queue<E> {
    protected node<E> head;
    protected node<E> tail;
    private int size;
    private double x;
    private double y;
    private E defaultE;

    public Queue() {
        head = null;
        tail = null;
        size = 0;
    }

    public void enqueue(E element) {
        if (head == null) {
            head = new node<E>(element);
            tail = head;
            System.out.println("First element");
        } else {
            node<E> newTail = new node<E>(element);
            node<E> temp = tail;
            temp.next = newTail;
            newTail.prev = temp;
            tail = newTail;
            System.out.println(size + " element added");
        }
//        System.out.println("Enqueue works");
        size++;
    }

    public E dequeue() {
        E temp = head.element;
        head = head.next;
//        head.prev = null;
        size--;
        return temp;
    }

    public E last() {
        if (tail == null) {
//            System.out.println("in here");
            return defaultE;
        } else
        return tail.element;
    }

    public E peak() {
        if (head == null) {
            return defaultE;
        } else
            return head.element;
    }

    public int getSize() {
        return size;
    }

    public void setE(E element) {
        defaultE = element;
    }

    public E getE() {
        return defaultE;
    }

//    public void setX(double x) {
//        this.x = x;
//    }
//
//    public void setY(double y) {
//        this.y = y;
//    }
//
//    public double getX() {
//        return x;
//    }
//
//    public double getY() {
//        return y;
//    }
}
