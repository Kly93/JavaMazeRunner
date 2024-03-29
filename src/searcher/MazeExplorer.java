package searcher;

import javafx.event.EventHandler;
import data.MazeData;
import searcher.event.MoveEvent;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MazeExplorer {
    private EventHandler<? super MoveEvent> moveEventHandler;

    private IMoveAlgorithm moveMethod;

    private Point start;
    private Point current;
    private Point destination;
    private MazeData mazeData;

    public MazeExplorer(IMoveAlgorithm moveMethod, Point current, Point destination, MazeData mazeData) {
        setMoveMethod(moveMethod);
        this.current = current;
        this.start = current;
        this.destination = destination;
        this.mazeData = mazeData;
    }

    public boolean move() {
        IMoveAlgorithm.Direction direction = moveMethod.getMoveDirection(new Point(current), new Point(start), new Point(destination), mazeData, getCanMoves());
        if(!canMove(direction)) {
            return false;
        }
        int nextX = current.x + direction.getDx();
        int nextY = current.y + direction.getDy();
        current.move(nextX, nextY);
        if(getOnSearcherMoved() != null) {
            MoveEvent event = new MoveEvent(this, null, direction);
            getOnSearcherMoved().handle(event);
        }
        return true;
    }

    public void setMoveMethod(IMoveAlgorithm moveMethod) {
        this.moveMethod = moveMethod;
    }

    public int getCurrentX() {
        return current.x;
    }

    public int getCurrentY() {
        return current.y;
    }

    public boolean canMove(IMoveAlgorithm.Direction direction) {
        if(direction == null) {
            return false;
        }
        int dx = direction.getDx();
        int dy = direction.getDy();
        int x = current.x;
        int y = current.y;
        return mazeData.isMazePath(x+dx, y+dy) && (mazeData.isMazePath(x+dx, y) || mazeData.isMazePath(x, y+dy));
    }

    public List<IMoveAlgorithm.Direction> getCanMoves() {
        List directions = new ArrayList<IMoveAlgorithm.Direction>();
        for(IMoveAlgorithm.Direction direction : IMoveAlgorithm.Direction.values()) {
            if(canMove(direction)) {
                directions.add(direction);
            }
        }
        return directions;
    }

    public final void setOnSearcherMoved(EventHandler<? super MoveEvent> value) {
        moveEventHandler = value;
    }

    public final EventHandler<? super MoveEvent> getOnSearcherMoved() {
        return moveEventHandler;
    }
}
