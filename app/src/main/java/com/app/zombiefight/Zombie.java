package com.app.zombiefight;

public class Zombie extends Entity{
    final private int id; // идентификатор зомби
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
            Zombie zfind;
            Creature creature = GameField.getCreature();
            int direction = findDirection(creature);
            if(direction == 0)
                    direction = (int)Math.round(Math.random()*(4.001 - 1.0)+1.0);
            int rows = GameField.getRows() - 1;
            int columns = GameField.getColumns() - 1;
            int oldRow = currentRow;
            int oldColumn = currentColumn;
            int newRow = -1;
            int newColumn = -1;
            switch (direction)
            {
                // вверх
                case Entity.UP:
                    newRow = oldRow == 0 ? rows : oldRow - 1;
                    newColumn = oldColumn;
                    break;
                // вниз
                case Entity.DOWN:
                    newRow = oldRow == rows ? 0 : oldRow + 1;
                    newColumn = oldColumn;
                    break;
                // влево
                case Entity.LEFT:
                    newColumn = oldColumn == 0 ? columns : oldColumn - 1;
                    newRow = oldRow;
                    break;
                case Entity.RIGHT:
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
                    GameField.getCreature().setStatus(Entity.KILLED);
                    status = Entity.STAYING;
                }
                else {
                    GameField.setEmpty(oldRow, oldColumn);
                    currentRow = newRow;
                    currentColumn = newColumn;
                    GameField.setZombie(currentRow, currentColumn);
                    status = Entity.MOVING;
                }
            }
            else
                status = Entity.STAYING;

    }
    // определить направление зомби к человечку
    private int findDirection(Creature creature)
    {
        if(creature == null) return 0;
        if(creature.getStatus() == Entity.KILLED) return 0;
        int cx = creature.getRow();
        int cy = creature.getColumn();
        int dy = cx - currentRow;
        int dx = cy - currentColumn;
        if(dx >= 0 && dy >= 0)
        {
            return dy <= dx ? Entity.RIGHT : Entity.DOWN;

        }
        else if( dx >= 0 && dy <= 0)
        {
            return Math.abs(dy) <= dx ? Entity.RIGHT : Entity.UP;
        }
        else if( dx <=0 && dy <= 0)
        {
            return Math.abs(dy) <= Math.abs(dx) ? Entity.LEFT : Entity.UP;
        }
        else if(dx <= 0 && dy >=0)
        {
            return dy <= Math.abs(dx) ? Entity.LEFT : Entity.UP;
        }
        else return 0;
    }


}
