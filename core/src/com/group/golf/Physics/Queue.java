package com.group.golf.Physics;

/**
 * Created by alex_ on 23-May-18.
 */
public class Queue {
    private Coordinates head;
    private Coordinates tail;
    private int size;

    public Queue() {
        head = null;
        tail = null;
        size = 0;
    }

    public void enqueue(double[] coord) {
        if (head == null) {
            head = new Coordinates(coord);
            tail = head;
        } else {
            Coordinates newTail = new Coordinates(coord);
            Coordinates temp = tail;
            tail.next = newTail;
            tail = temp;
        }
//        System.out.println("Enqueue works");
        size++;
    }

    public double[] dequeue() {
        double[] temp = head.coord;
        head = head.next;
        size--;
        return temp;
    }

    public int getSize() {
        return size;
    }

}
