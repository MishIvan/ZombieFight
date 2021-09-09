package com.app.zombiefight;

public class Entity {
    // координаты сущности
    private int currentRow;
    private int currentColumn;
    private char type; // тип сущности - зомби или человек
    private int id; // идентификатор сущности
    private int status; // статус сущности

    public Entity(int row, int column, char type)
    {
        currentRow = row;
        currentColumn = column;
        this.type = type;
        if(this.type == GameField.PERSON) id = 1;
        else id = (int)Math.round(Math.random()*(1.0e6+0.001 - 2.0)+2.0);
        status = GameField.MOVING;
    }


    // координаты сущности на игровом поле
    public void setRow(int row) { currentRow = row;}
    public void setColumn(int column) { currentColumn = column;}
    public int getRow() { return currentRow;}
    public int getColumn() { return currentColumn;}

    // идентификатор сущности (0 - для человека)
    public int getId() { return id;}
    // статус сущности
    public int getStatus() { return status;}

    // тип сущности
    public boolean isZombie() { return type == GameField.ZOMBIE;}
    public boolean isPerson() { return type == GameField.PERSON;}
    // движение сущности, возврат статуса сущности
    public void move()
    {
        if(isZombie())
        {
            int direction = (int)Math.round(Math.random()*(4.001 - 1.0)+1.0);
            Entity zfind = null;
            int rows = GameField.getRows() - 1;
            int columns = GameField.getColumns() - 1;
            int oldRow = currentRow;
            int oldColumn = currentColumn;
            int newRow = -1;
            int newColumn = -1;
            switch (direction)
            {
                // вверх
                case GameField.UP:
                    newRow = oldRow == 0 ? rows : oldRow - 1;
                    newColumn = oldColumn;
                    break;
                // вниз
                case GameField.DOWN:
                    newRow = oldRow == rows ? 0 : oldRow + 1;
                    newColumn = oldColumn;
                    break;
                // влево
                case GameField.LEFT:
                    newColumn = oldColumn == 0 ? columns : oldColumn - 1;
                    newRow = oldRow;
                    break;
                case GameField.RIGHT:
                    newColumn = oldColumn == columns ? 0 : oldColumn + 1;
                    newRow = oldRow;
                    break;
            }
            zfind = GameField.findZombieByCoords(newRow, newColumn);
            if (zfind == null) {
                currentRow = newRow;
                currentColumn = newColumn;
                status = GameField.MOVING;
            }
            else
                status = GameField.STAYING;
        }
    }

    public void move(int direction)
    {
        int rows = GameField.getRows() - 1;
        int columns = GameField.getColumns() - 1;
        int oldRow = currentRow;
        int oldColumn = currentColumn;
        int newRow;
        int newColumn;

        if(isPerson())
        {
            switch (direction)
            {
                // вверх
                case GameField.UP:
                    newRow = oldRow == 0 ? rows : oldRow - 1;
                    newColumn = oldColumn;
                    break;
                // вниз
                case GameField.DOWN:
                    newRow = oldRow == rows ? 0 : oldRow + 1;
                    newColumn = oldColumn;
                    break;
                // влево
                case GameField.LEFT:
                    newColumn = oldColumn == 0 ? columns : oldColumn - 1;
                    newRow = oldRow;
                    break;
                case GameField.RIGHT:
                    newColumn = oldColumn == columns ? 0 : oldColumn + 1;
                    newRow = oldRow;
                    break;
            }

        }
    }
    public void kill() { return;}
}
