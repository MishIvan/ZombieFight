package com.app.zombiefight;

public class Entity {
    // координаты сущности
    protected int currentRow;
    protected int currentColumn;
    protected int status; // статус сущности

    public Entity(int row, int column)
    {
        currentRow = row;
        currentColumn = column;
        status = GameField.STAYING;
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
