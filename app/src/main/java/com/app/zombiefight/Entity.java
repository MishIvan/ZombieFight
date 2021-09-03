package com.app.zombiefight;

public class Entity {
    // координаты сущности
    private int currentRow;
    private int currentColumn;
    private char type; // тип сущности - зомби или человек
    private boolean alive; // уничтожена сущность или нет
    private int id; // идентификатор сущности

    public Entity(int row, int column, char type)
    {
        currentRow = row;
        currentColumn = column;
        this.type = type;
        if(this.type == GameField.PERSON) id = 1;
        else id = (int)Math.round(Math.random()*(1.0e6+0.001 - 2.0)+2.0);
        alive = true;
    }


    // координаты сущности на игровом поле
    public void setRow(int row) { currentRow = row;}
    public void setColumn(int column) { currentColumn = column;}
    public int getRow() { return currentRow;}
    public int getColumn() { return currentColumn;}

    // идентификатор сущности (0 - для человека)
    public int getId() { return id;}

    // тип сущности
    public boolean isZombie() { return type == GameField.ZOMBIE;}
    public boolean isPerson() { return type == GameField.PERSON;}
    // движение сущности, возврат true если переместилась, false - если стоит на место
    public boolean move(int directions)
    {
        Entity zombie = null;
        if(type == GameField.PERSON)
        {
            switch (directions)
            {
                case GameField.UP:


            }
        }
        return false;

    }
    public void kill() { return;}
}
