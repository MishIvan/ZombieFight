package com.app.zombiefight;

public class Creature extends Entity{
    public Creature(int row, int column)
    {
        super(row,column);
    }

    @Override
    public boolean move(int row, int column)
    {
        if(
                (currentRow == row && (column == currentColumn +1 || column == currentColumn -1))
                        || (currentColumn == column && (row == currentRow - 1 || row == currentRow + 1))
        )
        {
            GameField.setEmpty(currentRow, currentColumn);
            Zombie zombie = GameField.findZombieByCoordinates(row, column);
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
