package ParticleSimulationFolder;


public class Vector {

    double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y);
    }

    public Vector subtract(Vector v) {
        return new Vector(x - v.x, y - v.y);
    }

    public Vector scale(double scalar) {
        return new Vector(x * scalar, y * scalar);
    }

    public double dot(Vector v) {
        return x * v.x + y * v.y;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector normalize() {
        double mag = magnitude();
        return new Vector(x / mag, y / mag);
    }

    public double distance(Vector v) {
        return Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2));
    }
    
    public Vector axis(double a) {
        a = -a;
        return new Vector(x * Math.cos(a) + y * Math.sin(a), y * Math.cos(a) - x * Math.sin(a));
    }
}
