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

    // направление движения
    final static int LEFT = 1;
    final static int UP = 2;
    final static int RIGHT = 3;
    final static int DOWN = 4;

    // статус сущности
    final static int MOVING = 1;
    final static int STAYING = 2;
    final static int KILLED = 3;

    private static char [][] gameField;
    private static int rows;
    private static int columns;
    private static int zombieBeaten;

    public static void setGameField(Size fieldSize)
    {
        rows = fieldSize.getWidth();
        columns = fieldSize.getHeight();
        gameField = new char[rows][columns];
        for(int i=0;i<rows;i++)
            for(int j=0; j<columns;j++)
                gameField[i][j] = FREE;
           zombies =  new ArrayList<>();
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
    public static int seedZombie(boolean fromRow)
    {
        int col = getRandomColumn();
        int row = getRandomRow();
        Zombie zombie = fromRow ? new Zombie(0, col) :
                new Zombie(row, 0);
        gameField[fromRow ? 0 : row][fromRow ? col : 0] = ZOMBIE;
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