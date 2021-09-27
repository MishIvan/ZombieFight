package com.app.zombiefight;


import android.util.Size;

import java.util.ArrayList;

// игровое поле - содержит информацию о расположении
public class GameField {
    // список зомби
    private static ArrayList<Zombie> zombies;
    // человек
    private static Creature creature;

    // флаг занятия клетки игрового поля
    final static char ZOMBIE = 'z';
    final static char PERSON = 'p';
    final static char FREE = ' ';

    private static char [][] gameField;
    private static int rows;
    private static int columns;
    private static int zombieBeaten;

    public static void setGameField(Size fieldSize)
    {
        rows = fieldSize.getHeight();
        columns = fieldSize.getWidth();
        gameField = new char[rows][columns];
        for(int i=0;i<rows;i++)
            for(int j=0; j<columns;j++)
                gameField[i][j] = FREE;
           zombies =  new ArrayList<>();
    }
    // очистка игрового поля
    public static void clearGameField()
    {
        for(int i=0;i<rows;i++)
            for(int j=0; j<columns;j++)
                gameField[i][j] = FREE;
            zombieBeaten = 0;

    }
    // ряды игрового поля
    public static int getRows() { return rows;}
    // колонки игрового поля
    public static int getColumns() { return columns;}

    public  static boolean isZombie(int row, int column) { return gameField[row][column] == ZOMBIE; }
    public  static boolean isPerson(int row, int column) { return gameField[row][column] == PERSON; }
    public static void setEmpty(int row, int column) { gameField[row][column] = FREE;}
    public static void setZombie(int row, int column) { gameField[row][column] = ZOMBIE;}
    public static void setPerson(int row, int column) { gameField[row][column] = PERSON;}
    public static Creature getCreature() { return creature;}
    public static int getZombieBeaten() {return zombieBeaten;}
    public  static void increaseZombieBeaten() { zombieBeaten++;}

    public static void killCreature()
    {
        int row = creature.getRow();
        int col = creature.getColumn();
        Zombie zombie = new Zombie(row, col);
        gameField[row][col] = ZOMBIE;
        zombies.add(zombie);
        creature = null;
    }
    public static ArrayList<Zombie> getZombies() { return zombies;}


    // для определения случайной строки
    private static int getRandomRow()
    {
        double a = 0.0;
        double b = (double)rows -1.0 + 0.001;
        return (int)Math.round(Math.random()*(b-a)+a);
    }
    // для определения случайной колонки
    private static int getRandomColumn()
    {
        double a = 0.0;
        double b = (double)columns - 1.0 + 0.001;
        return (int)Math.round(Math.random()*(b-a)+a);
    }

    // появление зомби на поле
    public static int seedZombie(int direction)
    {
        int col = getRandomColumn();
        int row = getRandomRow();
        switch(direction)
        {
            case Entity.LEFT:
                col = 0; break;
            case Entity.RIGHT:
                col = columns - 1; break;
            case Entity.UP:
                row = 0; break;
            case Entity.DOWN:
                row = rows - 1; break;
        }
        Zombie zombie =  new Zombie(row, col);
        gameField[row][col] = ZOMBIE;
        zombies.add(zombie);
        return zombie.getId();

    }

    // появление персоны: с правого края, с середины
    public static void seedPerson()
    {
        int row = rows/2-1;
        int col = columns - 1;
        creature = new Creature(row, col);
        gameField[row][col] = PERSON;
    }
    // найти замби по координате
    public static Zombie findZombieByCoordinates(int row, int column)
    {
        Zombie ent = null;
        for(Zombie el : zombies)
        {
            if (el.getRow() == row && el.getColumn() == column) { ent = el; break;}
        }
        return ent;
    }
    public static Zombie findZombieById(int id)
    {
        Zombie ent = null;
        for(Zombie el : zombies)
        {
            if (el.getId() == id) { ent = el; break;}
        }
        return ent;
    }
}