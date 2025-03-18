
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

class Particle {

    VectorParticle position, velocity;
    double diameter, gravity, mass;
    Color color, temp;
    double elasticity, object;
    double spin;
    boolean highlight;

    public Particle(VectorParticle position, VectorParticle velocity, Color color, double diameter, double gravity,
            double elasticity,
            double object) {
        this.position = position;
        this.velocity = velocity;
        this.color = color;
        this.temp = color;
        this.diameter = diameter;
        this.gravity = gravity;
        this.elasticity = elasticity;
        this.object = object;
        Random random = new Random();
        this.spin = random.nextDouble();
    }

    public void show(Graphics2D g) {
        if (object == 0 || object == 2) {
            g.setColor(color);
            g.fillOval((int) position.x, (int) position.y, (int) diameter, (int) diameter);
            mass = Math.pow(diameter, 2) * .25 * Math.PI;
            highlight(g);
        }
        if (object == 1 || object == 3) {
            spin=0;
            g.setColor(color);
            int size = (int) diameter; // Assuming diameter is the square's side length
            // spin +=1;
            double angle = Math.toRadians(spin); // Rotation angle

            // Calculate rotated corners relative to the center
            double halfSize = size / 2.0;

            double cosA = Math.cos(angle);
            double sinA = Math.sin(angle);

            int[] xPoints = new int[4];
            int[] yPoints = new int[4];

            xPoints[0] = (int) (position.x + halfSize + (-halfSize * cosA - (-halfSize) * sinA));
            yPoints[0] = (int) (position.y + halfSize + (-halfSize * sinA + (-halfSize) * cosA));

            xPoints[1] = (int) (position.x + halfSize + (halfSize * cosA - (-halfSize) * sinA));
            yPoints[1] = (int) (position.y + halfSize + (halfSize * sinA + (-halfSize) * cosA));

            xPoints[2] = (int) (position.x + halfSize + (halfSize * cosA - halfSize * sinA));
            yPoints[2] = (int) (position.y + halfSize + (halfSize * sinA + halfSize * cosA));

            xPoints[3] = (int) (position.x + halfSize + (-halfSize * cosA - halfSize * sinA));
            yPoints[3] = (int) (position.y + halfSize + (-halfSize * sinA + halfSize * cosA));

            // Draw the rotated square
            g.fillPolygon(xPoints, yPoints, 4);

            mass = Math.pow(diameter, 2);

        }
        mass /= 4;
    }

    public void gravityShift() {
        velocity.y += gravity;
        position = position.add(velocity);
    }

    public void collide(Particle p) {
        if (object == 0 && p.object == 0 || object == 2 && p.object == 2 || object == 0 && p.object == 2
                || p.object == 0 && object == 2) {
            VectorParticle pCenter = position.add((new VectorParticle(diameter / 2, diameter / 2)));
            VectorParticle ppCenter = p.position.add((new VectorParticle(p.diameter / 2, p.diameter / 2)));
            double distance = pCenter.distance(ppCenter);
            if (distance < (diameter + p.diameter) / 2) {
                // Resolve collision
                VectorParticle normal = position.subtract(p.position).normalize();
                VectorParticle relativeVelocity = velocity.subtract(p.velocity);
                double speed = relativeVelocity.dot(normal);
                if (speed > 0) {
                    return;
                }

                double impulse = (2 * speed) / (mass + p.mass);
                velocity = velocity.subtract(normal.scale(impulse * p.mass));
                p.velocity = p.velocity.add(normal.scale(impulse * mass));

                // Separate overlapping particles
                double overlap = (diameter + p.diameter) / 2 - distance;
                position = position.add(normal.scale(overlap / 2));
                p.position = p.position.subtract(normal.scale(overlap / 2));
            }
        } else if (object == 1 || p.object == 3) {
            VectorParticle pCenter = position.add((new VectorParticle(diameter / 2, diameter / 2)));
            pCenter = pCenter.axis(spin);
            VectorParticle ppCenter = p.position.add((new VectorParticle(p.diameter / 2, p.diameter / 2)));
            ppCenter = ppCenter.axis(spin);
            VectorParticle distance = pCenter.subtract(ppCenter);
            if (Math.abs(distance.y) < (diameter + p.diameter) / 2
                    && Math.abs(distance.x) < (diameter + p.diameter) / 2) {
                // Resolve collision
                VectorParticle normal = position.subtract(p.position).normalize();
                VectorParticle relativeVelocity = velocity.subtract(p.velocity);
                double speed = relativeVelocity.dot(normal);
                if (speed > 0) {
                    return;
                }

                double impulse = (2 * speed) / (mass + p.mass);
                velocity = velocity.subtract(normal.scale(impulse * p.mass));
                p.velocity = p.velocity.add(normal.scale(impulse * mass));
                // Separate overlapping particles
                // Separate overlapping particles
                double overlapX = (diameter + p.diameter) / 2 - Math.abs(distance.x);
                double overlapY = (diameter + p.diameter) / 2 - Math.abs(distance.y);

                if (Math.abs(distance.y) > Math.abs(distance.x)) {
                    double correctionY = overlapY / 2;
                    if (distance.y > 0) {
                        position = position.add(new VectorParticle(0, correctionY));
                        p.position = p.position.subtract(new VectorParticle(0, correctionY));
                    } else {
                        position = position.subtract(new VectorParticle(0, correctionY));
                        p.position = p.position.add(new VectorParticle(0, correctionY));
                    }
                } else if (Math.abs(distance.x) > Math.abs(distance.y)) {
                    double correctionX = overlapX / 2;
                    if (distance.x > 0) {
                        position = position.add(new VectorParticle(correctionX, 0));
                        p.position = p.position.subtract(new VectorParticle(correctionX, 0));
                    } else {
                        position = position.subtract(new VectorParticle(correctionX, 0));
                        p.position = p.position.add(new VectorParticle(correctionX, 0));
                    }
                }

            }
        }
    }

    public void update(int width, int height, ArrayList<Particle> particles) {
        gravityShift();
        if (highlight) {
            if (ParticleCanvas.arrow == 1) {
                    this.velocity.y = -5;
                
            }
            if (ParticleCanvas.arrow == 2) {
                    this.velocity.y = 5;
                
            }
            if (ParticleCanvas.arrow == 3) {
                    this.velocity.x = -5;
                
            }
            if (ParticleCanvas.arrow == 4) {
                    this.velocity.x = 5;
                
            }
        }
        // Bounce off walls

        if (object == 0 || object == 2 || object == 1 || object == 3) {

            if (position.x < 0 || position.x + diameter > width) {
                velocity.x *= -elasticity;
                position.x = Math.max(0, Math.min(position.x, width - diameter));
            }

            if (position.y < 0 || position.y + diameter > height) {
                velocity.y *= -elasticity;
                position.y = Math.max(0, Math.min(position.y, height - diameter));
            }

            // Check for collisions with other particles
            for (Particle p : particles) {
                if (p != this) {
                    this.collide(p);
                }
            }
        }
    }

    public void highlight(Graphics2D g) {
        if (highlight) {
            g.setColor(Color.BLACK);
            g.drawOval((int) position.x, (int) position.y, (int) diameter, (int) diameter);
            color = Color.BLACK;

        } else {
            color = temp;
        }
    }

    public void selected(VectorParticle s1, VectorParticle s2) {
        if(ParticleCanvas.shift){

            VectorParticle pCenter = position.add((new VectorParticle(diameter / 2, diameter / 2)));
            // pCenter=pCenter.axis(spin);
            VectorParticle sCenter = new VectorParticle(s1.x + .5 * (s2.x - s1.x), s1.y + .5 * (s2.y - s1.y));
            
            // sCenter=sCenter.axis(1);
        double sdiametery = Math.abs(s1.y - s2.y);
        double sdiameterx = Math.abs(s1.x - s2.x);
        VectorParticle distance = pCenter.subtract(sCenter);
        if (Math.abs(distance.y) < (diameter + sdiametery) / 2
                && Math.abs(distance.x) < (diameter + sdiameterx) / 2) {
                    highlight = true;
        }
                }

    }

}

class Keysss extends JPanel implements KeyListener {

    public static int B = 0;
    public static int S = 1;
    public static int C = 2;
    public static int L = 3;
    public static int temp;

    public Keysss() {
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_B) {
            ParticleCanvas.object = B;
            System.out.println("Ball");
        }
        if (code == KeyEvent.VK_S) {
            ParticleCanvas.object = S;
            System.out.println("Square");
        }
        if (code == KeyEvent.VK_C) {
            ParticleCanvas.object = C;
            System.out.println("C");

        }
        if (code == KeyEvent.VK_L) {
            ParticleCanvas.object = L;
            System.out.println("L");
        }
        if (code == KeyEvent.VK_UP) {
            ParticleCanvas.arrow = 1;
            System.out.println("UP");
        }
        if (code == KeyEvent.VK_DOWN) {
            ParticleCanvas.arrow = 2;
            System.out.println("DOWN");
        }
        if (code == KeyEvent.VK_LEFT) {
            ParticleCanvas.arrow = 3;
            System.out.println("LEFT");
        }
        if (code == KeyEvent.VK_RIGHT) {
            ParticleCanvas.arrow = 4;
            System.out.println("RIGHT");
        }
        if (code == KeyEvent.VK_SHIFT) {
            ParticleCanvas.shift = true;
        }
        if (code == KeyEvent.VK_BACK_SPACE) {
            ParticleCanvas.delete=true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_UP) {
            ParticleCanvas.arrow = 0;
            System.out.println("UP");
        }
        if (code == KeyEvent.VK_DOWN) {
            ParticleCanvas.arrow = 0;
            System.out.println("DOWN");
        }
        if (code == KeyEvent.VK_LEFT) {
            ParticleCanvas.arrow = 0;
            System.out.println("LEFT");
        }
        if (code == KeyEvent.VK_RIGHT) {
            ParticleCanvas.arrow = 0;
            System.out.println("RIGHT");
        }
        if (code == KeyEvent.VK_SHIFT) {
            System.out.println("Shift");
            ParticleCanvas.shift = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nothing
    }
}

class VectorParticle {

    double x, y;

    public VectorParticle(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public VectorParticle add(VectorParticle v) {
        return new VectorParticle(x + v.x, y + v.y);
    }

    public VectorParticle add(double d) {
        return new VectorParticle(x + d, y + d);
    }

    public VectorParticle subtract(VectorParticle v) {
        return new VectorParticle(x - v.x, y - v.y);
    }

    public VectorParticle scale(double scalar) {
        return new VectorParticle(x * scalar, y * scalar);
    }

    public VectorParticle mult(VectorParticle v) {
        return new VectorParticle(x * v.x, y * v.y);
    }

    public double dot(VectorParticle v) {
        return x * v.x + y * v.y;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public VectorParticle normalize() {
        double mag = magnitude();
        return new VectorParticle(x / mag, y / mag);
    }

    public VectorParticle axis(double a) {
        a = -a;
        return new VectorParticle(x * Math.cos(a) + y * Math.sin(a), y * Math.cos(a) - x * Math.sin(a));
    }

    public double distance(VectorParticle v) {
        return Math.sqrt(Math.pow(x - v.x, 2) + Math.pow(y - v.y, 2));
    }
}

class ParticleCanvas extends JPanel {

    ArrayList<Particle> particles = new ArrayList<>();
    Random random = new Random();
    double gravity = 0.0098 * 5;
    double elasticity = .81;
    double vx = 0;
    double vy = -1;
    static double object = 0;
    static int arrow;
    VectorParticle positionm = new VectorParticle(0, 0);
    VectorParticle positions = new VectorParticle(0, 0);
    Color colorm;
    int diameter = 20;
    VectorParticle select1 = new VectorParticle(0, 0);
    VectorParticle select2 = new VectorParticle(0, 0);
    static boolean shift,delete;

    public ParticleCanvas() {
        Timer timer = new Timer(1, e -> {
            for (Particle p : particles) {
                p.update(getWidth(), getHeight(), particles);
            }
            repaint();
        });
        timer.start();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                positionm = new VectorParticle(e.getX(), e.getY());
                positions = new VectorParticle(e.getX(), e.getY());
                select1 = new VectorParticle(e.getX(), e.getY());
                colorm = Color.BLACK;

            }
            @Override
            public void mouseDragged(MouseEvent e) {
                positions = new VectorParticle(e.getX(), e.getY());

            }

        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (shift) {
                    System.out.println("NOOOOOOOOOOOO");
                    select1 = new VectorParticle(e.getX(), e.getY());
                    for (Particle p : particles) {
                        p.highlight = false;
                    }

                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                VectorParticle position = new VectorParticle(e.getX(), e.getY());
                VectorParticle velocity = new VectorParticle(2 * Math.cos(vx), 2 * Math.sin(vy));

                if (shift) {

                } else {
                    if (object == 0) {
                        // VectorParticle velocity = new VectorParticle(random.nextDouble() * 8 - 4,
                        // random.nextDouble()
                        // * 8 - 4);

                        Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                        particles.add(new Particle(position, velocity, color, diameter, gravity, elasticity, object));
                        vx += 0.1;
                        vy += 0.1;
                    }
                    if (object == 1) {
                        // VectorParticle velocity = new VectorParticle(random.nextDouble() * 8 - 4,
                        // random.nextDouble()
                        // * 8 - 4);

                        Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                        particles.add(new Particle(position, velocity, color, diameter, gravity, elasticity, object));
                        vx -= 0.1;
                        vy -= 0.1;

                    }
                    if (object == 2) {
                        // VectorParticle velocity = new VectorParticle(random.nextDouble() * 8 - 4,
                        // random.nextDouble()
                        // * 8 - 4);

                        Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                        particles.add(
                                new Particle(position, velocity, color, 2 * diameter, gravity, elasticity, object));
                        vx += 0.1;
                        vy += 0.1;
                    }
                    if (object == 3) {
                        // VectorParticle velocity = new VectorParticle(random.nextDouble() * 8 - 4,
                        // random.nextDouble()
                        // * 8 - 4);

                        Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                        particles.add(
                                new Particle(position, velocity, color, 2 * diameter, gravity, elasticity, object));
                        vx += 0.1;
                        vy += 0.1;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (shift) {
                    select2 = new VectorParticle(e.getX(), e.getY());
                }
                for (Particle p : particles) {
                    p.selected(select1, select2);
                }
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (Particle p : particles) {
            p.show(g2d);
        }
        if(delete){
            for (int index = 0; index < particles.size(); index++) {
                if(particles.get(index).highlight){
                    particles.remove(particles.get(index));
                    index--;
                }
            }
            delete=false;
        }
        if(shift){
            g2d.setColor(Color.BLACK);
            g2d.drawRect((int) (select1.x), (int) (select1.y), (int) (positions.x-select1.x), (int) (positions.y-select1.y));
            }else{
            if (object == 0) {
                g2d.setColor(colorm);
                g2d.drawOval((int) positionm.x, (int) positionm.y, (int) diameter, (int) diameter);
            }
            if (object == 2) {
                g2d.setColor(colorm);
                g2d.drawOval((int) positionm.x, (int) positionm.y, (int) diameter * 2, (int) diameter * 2);
            }
            if (object == 1) {
                g2d.setColor(colorm);
                g2d.drawRect((int) (positionm.x), (int) (positionm.y), (int) ((diameter)), (int) ((diameter)));
            }
            if (object == 3) {
                g2d.setColor(colorm);
                g2d.drawRect((int) (positionm.x), (int) (positionm.y), (int) ((2 * diameter)), (int) ((2 * diameter)));
            }
            }
    }
}

public class ParticleSimulation {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Particle Simulation");
        ParticleCanvas canvas = new ParticleCanvas();
        frame.add(canvas);
        Keysss panel = new Keysss();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addKeyListener(panel);
    }
}
