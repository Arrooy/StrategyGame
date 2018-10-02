package Model.UI;

import java.awt.*;
import java.util.LinkedList;

public class Organizer {

    private class OPoint {
        int x, y;
        public OPoint (int x,int y) {
            this.x = x;
            this.y = y;
        }
    }

    private int mode;
    private LinkedList<OPoint> points;

    public Organizer() {
        mode = 0;
        points = new LinkedList<>();
    }

    public void resetOrganization() {
        if (points.size() != 0) for (int i = 0; i < points.size(); i++) points.remove(i--);
    }

    public void addPoint(int x, int y) {
        points.add(new OPoint(x, y));
    }

    private double getSelectionLenght() {
        double lenght = 0;
        for (int i = 1; i < points.size(); i++)
            lenght += (Math.sqrt(Math.pow(points.get(i).x - points.get(i - 1).x, 2) +
                    Math.pow(points.get(i).y - points.get(i - 1).y, 2)));
        return lenght;
    }

    public int[][] getPositions(int n) throws Exception{
        double acum = 0, total = getSelectionLenght();
        double gap = total / (n - 1);
        int[][] pos = new int[n][2];

        if (points.size() < 1) throw new Exception("S'ha intentat organitzar sense selecció");

        pos[0][0] = points.getFirst().x;
        pos[0][1] = points.getFirst().y;
        for (int i = 1; i < n; i++) {
            acum += gap;

            double diff = acum, aux = 0;
            int j = 1;
            while (diff > 0 && j < points.size() - 1) {
                aux = (Math.sqrt(Math.pow(points.get(j).x - points.get(j - 1).x, 2) + Math.pow(points.get(j).y - points.get(j - 1).y, 2)));
                diff -= aux;
                j++;
            }
            diff += aux;

            double alpha = Math.atan2(points.get(j).y - points.get(j - 1).y, points.get(j).x - points.get(j - 1).x);
            pos[i][0] = (int) (points.get(j - 1).x + diff * Math.cos(alpha));
            pos[i][1] = (int) (points.get(j - 1).y + diff * Math.sin(alpha));
        }

        return pos;
    }

    public void render(Graphics2D g) {
        g.setColor(new Color(86, 112, 123));
        g.setStroke(new BasicStroke(7));
        for (int i = 1; i < points.size(); i++) g.drawLine(points.get(i).x, points.get(i).y, points.get(i - 1).x, points.get(i - 1).y);
        g.setStroke(new BasicStroke(1));
    }
}
