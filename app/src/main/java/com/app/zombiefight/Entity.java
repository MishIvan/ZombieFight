package com.app.zombiefight;

public class Entity {
    private int currentRow;
    private int curerntColumn;
    private char type;
    public Entity(int row, int column, char type)
    {
        curerntColumn = row;
        curerntColumn = column;
        this.type = type;
    }

    public void setRow(int row) { currentRow = row;}
    public void setColumn(int column) { curerntColumn = column;}
    public int getRow(int row) { return currentRow;}
    public int getColumn(int column) { return curerntColumn;}

    public boolean isZombie() { return type == GameField.ZOMBIE;}
    public boolean isPerson() { return type == GameField.PERSON;}
    public void move(int directions) { return;}
    public void kill() { return;}
}
