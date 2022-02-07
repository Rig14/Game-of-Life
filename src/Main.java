import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Point[] initialCells = {
                new Point(1, 1),
                new Point(2, 1),
                new Point(3, 1),
                new Point(3, 0),
                new Point(2, -1),
        };

        JFrame f = new JFrame("Game of life");
        Game g = new Game(10, initialCells);
        f.add(g);
        g.run();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}

class Game extends JComponent implements Runnable {
    private final int cellSize;
    private ArrayList<Point> cells = new ArrayList<Point>();

    public Game(int cellSize, Point[] initialCells) {
        this.cellSize = cellSize;

        this.cells.addAll(List.of(initialCells));
    }

    private void drawCells(Graphics g) {
        this.cells.forEach((cell) -> g.fillRect(
                this.cellSize * cell.x,
                this.cellSize * cell.y,
                this.cellSize,
                this.cellSize
        ));
    }

    private void nextGeneration() {
        ArrayList<Point> nextGenerationCells = new ArrayList<Point>();

        ArrayList<Point> cellsToTest = new ArrayList<Point>();

        // looks up what cells should be tested
        this.cells.forEach((cell) -> {
            for (int y = -1; y < 2; y++) {
                for (int x = -1; x < 2; x++) {
                    Point currentCell = new Point(cell.x + x, cell.y + y);

                    if(cellsToTest.contains(currentCell)) {
                        continue;
                    }
                    // if the cell to test hasn't been added already
                    cellsToTest.add(currentCell);
                }
            }
        });

        // actual testing begins here
        cellsToTest.forEach((cell) -> {
            int neighbours = 0;
            boolean isDead = !this.cells.contains(cell);

            for (int y = -1; y < 2; y++) {
                for (int x = -1; x < 2; x++) {
                    // no self test
                    if(x == 0 && y == 0) {
                        continue;
                    }

                    Point currentCell = new Point(cell.x + x, cell.y + y);

                    if(this.cells.contains(currentCell)) {
                        neighbours++;
                    }
                }
            }
            /*
            Any live cell with fewer than two live neighbours dies, as if by underpopulation.
            Any live cell with two or three live neighbours lives on to the next generation.
            Any live cell with more than three live neighbours dies, as if by overpopulation.
            Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
            */
            if(!isDead && (neighbours == 2 || neighbours == 3)) {
                nextGenerationCells.add(cell);
            }
            if(isDead && neighbours == 3) {
                nextGenerationCells.add(cell);
            }
        });
        this.cells = nextGenerationCells;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawCells(g);
    }

    @Override
    public void run() {
        final long timeInterval = 400;
        Runnable runnable = new Runnable() {
            public void run() {
                while (true) {
                    // ------- code for task to run
                    repaint();
                    nextGeneration();
                    // ------- ends here
                    try {
                        Thread.sleep(timeInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
