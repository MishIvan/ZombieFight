package com.app.zombiefight;

import android.content.Context;

import java.util.ArrayList;

// игровое поле - содержит информацию о расположении
public class GameField {
    private static ArrayList<Entity> zombies;
    private static Entity creatue;
    final static char ZOMBIE = 'z';
    final static char PERSON = 'p';
    final static char FREE = ' ';

    private static char [][] gameField;
    private static int rows;
    private static int columns;

    public static void setGameField(String fieldSize)
    {
        String [] size = fieldSize.split("x");
        rows = Integer.parseInt(size[0]);
        columns = Integer.parseInt(size[1]);
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
    public static boolean isEmpty(int row, int column) { return gameField[row][column] != FREE; }
    public static void setEmpty(int row, int column) { gameField[row][column] = FREE;}
    public static void setZombie(int row, int column) { gameField[row][column] = ZOMBIE;}
    public static void setPerson(int row, int column) { gameField[row][column] = PERSON;}

    // для определения случайной строки
    public static int getRandomRow()
    {
        double a = (double)rows;
        double b = (double)rows+0.001;
        return (int)Math.round(Math.random()*(b-a)+a);
    }
    // для определения случайной колонки
    public static int getRandomСщдгьт()
    {
        double a = (double)columns;
        double b = (double)columns+0.001;
        return (int)Math.round(Math.random()*(b-a)+a);
    }

    public static void seedZombie()
    {
        Entity zombie = new Entity();
    }
}