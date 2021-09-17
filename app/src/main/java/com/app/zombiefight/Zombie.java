package com.app.zombiefight;

public class Zombie extends Entity{
    private int id; // идентификатор зомби
    public Zombie(int row, int column)
    {
        super(row, column);
        id = (int)Math.round(Math.random()*(1.0e6+0.001 - 2.0)+2.0);
    }

    // идентификатор зомби
    public int getId() { return id;}

    // движение зомби с изменением статуса
    @Override
    public void move()
    {
            int direction = (int)Math.round(Math.random()*(4.001 - 1.0)+1.0);
            Zombie zfind;
            Creature creature = GameField.getCreature();
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
