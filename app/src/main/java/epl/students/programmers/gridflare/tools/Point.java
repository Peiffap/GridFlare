package epl.students.programmers.gridflare.tools;

public class Point {

    private int x, y;
    private int strength;
    private double ping, dl;

    public Point(int x, int y, int s, double p, double d){
        this.x = x;
        this.y = y;
        strength = s;
        ping = p;
        dl = d;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getStrength() {
        return strength;
    }

    public double getPing() {
        return ping;
    }

    public double getDl() {
        return dl;
    }
}
