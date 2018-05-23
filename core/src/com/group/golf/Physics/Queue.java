package com.group.golf.Physics;

/**
 * Created by alex_ on 23-May-18.
 */
public class Queue {
    private Coordinates head;
    private Coordinates tail;
    private int size;
    private double x;
    private double y;

    public Queue() {
        head = null;
        tail = null;
        size = 0;
    }

    public void enqueue(double[] coord) {
        if (head == null) {
            head = new Coordinates(coord);
            tail = head;
            System.out.println("First element");
        } else {
            Coordinates newTail = new Coordinates(coord);
            Coordinates temp = tail;
            temp.next = newTail;
            newTail.prev = temp;
            tail = newTail;
            System.out.println(coord[0]);
        }
//        System.out.println("Enqueue works");
        size++;
    }

    public double[] dequeue() {
        double[] temp = head.coord;
        head = head.next;
//        head.prev = null;
        size--;
        return temp;
    }

    public double[] last() {
        if (tail == null) {
//            System.out.println("in here");
            return new double[]{x,y};
        } else
        return tail.coord;
    }

    public double[] peak() {
        if (head == null) {
            return new double[]{x,y};
        } else
            return head.coord;
    }

    public int getSize() {
        return size;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
