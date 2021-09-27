package com.app.zombiefight;

public class Entity {
    // направление движения
    final static int LEFT = 1;
    final static int UP = 2;
    final static int RIGHT = 3;
    final static int DOWN = 4;

    // статус сущности
    final static int MOVING = 1;
    final static int STAYING = 2;
    final static int KILLED = 3;

    // координаты сущности
    protected int currentRow;
    protected int currentColumn;
    protected int status; // статус сущности

    public Entity(int row, int column)
    {
        currentRow = row;
        currentColumn = column;
        status = STAYING;
    }


    // координаты сущности на игровом поле
    public int getRow() { return currentRow;}
    public int getColumn() { return currentColumn;}

    // статус сущности
    public int getStatus() { return status;}
    public void setStatus(int status) { this.status = status;}
    public boolean move(int row, int column) { return false;}
    public  void move() { }

}
