package com.app.zombiefight;


import android.util.Size;

import java.util.ArrayList;

// игровое поле - содержит информацию о расположении
public class GameField {
    // список зомби
    private static ArrayList<Entity> zombies;
    // человек
    private static Entity creature;

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
    // занята ли ячейка игрового поля
    public static boolean isEmpty(int row, int column) { return gameField[row][column] == FREE; }
    public  static boolean isZombie(int row, int column) { return gameField[row][column] == ZOMBIE; }
    public  static boolean isPerson(int row, int column) { return gameField[row][column] == PERSON; }
    public static void setEmpty(int row, int column) { gameField[row][column] = FREE;}
    public static void setZombie(int row, int column) { gameField[row][column] = ZOMBIE;}
    public static void setPerson(int row, int column) { gameField[row][column] = PERSON;}

    // для определения случайной строки
    public static int getRandomRow()
    {
        double a = 0.0;
        double b = (double)rows -1.0 + 0.001;
        return (int)Math.round(Math.random()*(b-a)+a);
    }
    // для определения случайной колонки
    public static int getRandomColumn()
    {
        double a = 0.0;
        double b = (double)columns - 1.0 + 0.001;
        return (int)Math.round(Math.random()*(b-a)+a);
    }

    // появление зомби на поле
    public static void seedZombie(boolean fromRow)
    {
        int col = getRandomColumn();
        int row = getRandomRow();
        Entity zombie = fromRow ? new Entity(0, col, ZOMBIE) :
                new Entity(row, 0, ZOMBIE);
        gameField[fromRow ? 0 : col][fromRow ? row : 0] = ZOMBIE;
        zombies.add(zombie);

    }

    // появление персоны: с правого края, с середины
    public static void seedPerson()
    {
        int row = rows/2-1;
        int col = columns - 1;
        creature = new Entity(row, col, PERSON);
        gameField[row][col] = PERSON;
    }
    // найти замби по координате
    public static Entity findZombieByCoordinates(int row, int column)
    {
        Entity ent = null;
        for(Entity el : zombies)
        {
            if (el.getRow() == row && el.getColumn() == column) { ent = el; break;}
        }
        return ent;
    }
    public static Entity findZombieById(int id)
    {
        Entity ent = null;
        for(Entity el : zombies)
        {
            if (el.getId() == id) { ent = el; break;}
        }
        return ent;
    }
    public static Entity getCreature() { return creature;}
}