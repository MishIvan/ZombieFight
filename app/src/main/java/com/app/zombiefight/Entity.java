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
        id = this.type == GameField.PERSON ? 1 : (int)Math.round(Math.random()*(1.0e6+0.001 - 2.0)+2.0);
        status = GameField.STAYING;
    }


    // координаты сущности на игровом поле
    public int getRow() { return currentRow;}
    public int getColumn() { return currentColumn;}

    // идентификатор сущности (0 - для человека)
    public int getId() { return id;}
    // статус сущности
    public int getStatus() { return status;}
    public void setStatus(int status) { this.status = status;}

    // сущность - это зомби
    public boolean isZombie() { return type == GameField.ZOMBIE;}

    // движение сущности, возврат статуса сущности
    public void move()
    {
        if(isZombie())
        {
            int direction = (int)Math.round(Math.random()*(4.001 - 1.0)+1.0);
            Entity zfind;
            Entity creature = GameField.getCreature();
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
            zfind = GameField.findZombieByCoordinates(newRow, newColumn);
            if (zfind == null) {
                int colCreature = creature == null ? -1 :creature.getColumn();
                int rowCreature = creature == null ? -1 :creature.getRow();
                if(newRow == rowCreature && newColumn == colCreature )
                {
                    GameField.getCreature().setStatus(GameField.KILLED);
                    status = GameField.STAYING;
                }
                else {
                    GameField.setEmpty(oldRow, oldColumn);
                    currentRow = newRow;
                    currentColumn = newColumn;
                    GameField.setZombie(currentRow, currentColumn);
                    status = GameField.MOVING;
                }
            }
            else
                status = GameField.STAYING;
        }
    }
    public boolean move(int row, int column)
    {
        if(isZombie()) { status = GameField.STAYING; return false;}
        if(
             (currentRow == row && (column == currentColumn +1 || column == currentColumn -1))
          || (currentColumn == column && (row == currentRow - 1 || row == currentRow + 1))
        )
        {
            GameField.setEmpty(currentRow, currentColumn);
            Entity zombie = GameField.findZombieByCoordinates(row, column);
            if(zombie != null)
            {
                GameField.getZombies().remove(zombie);
                GameField.increaseZombieBeaten();
                return true;
            }
            currentRow = row;
            currentColumn = column;
            GameField.setPerson(currentRow, currentColumn);
            status = GameField.MOVING;
        }
        else
            status = GameField.STAYING;
        return false;
    }
}
