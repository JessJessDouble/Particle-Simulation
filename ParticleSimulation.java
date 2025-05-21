package ParticleSimulationFolder;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

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
            ParticleCanvas.delete = true;
        }
        if (code == KeyEvent.VK_ESCAPE) {
            for (Particle p : ParticleCanvas.particles) {
                p.highlight = false;
            }
        }
        if (e.isControlDown() && code == KeyEvent.VK_A) {
            for (Particle p : ParticleCanvas.particles) {
                p.highlight = true;
            }
        }
        if (code == KeyEvent.VK_SPACE) {
            ParticleCanvas.space = true;
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

class ParticleCanvas extends JPanel {

    static ArrayList<Particle> particles = new ArrayList<>();
    Random random = new Random();
    double gravity = 0.0098 * 5;
    double elasticity = .81;
    double vx = 0;
    double vy = -1;
    static double object = 0;
    static int arrow;
    Vector position = new Vector(0, 0);
    Vector positionm = new Vector(0, 0);
    Vector positions = new Vector(0, 0);
    Color colorm;
    int diameter = 20;
    Vector select1 = new Vector(0, 0);
    Vector select2 = new Vector(0, 0);
    static boolean shift, delete, space;

    public ParticleCanvas() {
        Timer timer = new Timer(10, e -> {
            for (Particle p : particles) {
                p.update(getWidth(), getHeight(), particles);
            }
            repaint();
        });
        timer.start();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                    if (space) {
                        
                        positionm = new Vector(e.getX(), e.getY());
                        positions = new Vector(e.getX(), e.getY());
                        select1 = new Vector(e.getX(), e.getY());
                        colorm = Color.BLACK;
                    }

                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (space) {

                    positions = new Vector(e.getX(), e.getY());
                    }
                }

            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (space) {

                    position = new Vector(e.getX(), e.getY());
                    if (shift) {
                        select1 = new Vector(e.getX(), e.getY());

                    }
                }}

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (space) {

                    if (!shift) {
                        position = new Vector(e.getX(), e.getY());
                    }
                }}

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (space) {

                    select2 = new Vector(e.getX(), e.getY());
                    Vector velocity = select2.subtract(position);
                    velocity = new Vector(velocity.x * 0.1, velocity.y * 0.1);
                    if (!shift) {
                        for (Particle p : particles) {
                            p.highlight = false;
                        }

                        if (object == 0) {
                            // Vector velocity = new Vector(random.nextDouble() * 8 - 4,
                            // random.nextDouble()
                            // * 8 - 4);

                            Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                            particles.add(
                                    new Particle(position.add(new Vector(random.nextInt(2),random.nextInt(1))), velocity, color, diameter, gravity, elasticity, object));
                            vx += 0.1;
                            vy += 0.1;
                        }
                        if (object == 1) {
                            // Vector velocity = new Vector(random.nextDouble() * 8 - 4,
                            // random.nextDouble()
                            // * 8 - 4);

                            Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                            particles.add(
                                    new Particle(position.add(new Vector(random.nextInt(2),random.nextInt(1))), velocity, color, diameter, gravity, elasticity, object));
                            vx -= 0.1;
                            vy -= 0.1;

                        }
                        if (object == 2) {
                            // Vector velocity = new Vector(random.nextDouble() * 8 - 4,
                            // random.nextDouble()
                            // * 8 - 4);

                            Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                            particles.add( 
                                    new Particle(position.add(new Vector(random.nextInt(2),random.nextInt(1))), velocity, color, 2 * diameter, gravity, elasticity, object));
                            vx += 0.1;
                            vy += 0.1;
                        }
                        if (object == 3) {
                            // Vector velocity = new Vector(random.nextDouble() * 8 - 4,
                            // random.nextDouble()
                            // * 8 - 4);

                            Color color = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                            particles.add(
                                    new Particle(position.add(new Vector(random.nextInt(2),random.nextInt(1))), velocity, color, 2 * diameter, gravity, elasticity, object));
                            vx += 0.1;
                            vy += 0.1;
                        }
                    }
                    for (Particle p : particles) {
                        p.selected(select1, select2);
                    }

                }}
            });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (!space) {
            // Draw Welcome Screen
            GradientPaint gradient = new GradientPaint(0, 0, Color.DARK_GRAY, getWidth(), getHeight(), Color.BLACK);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 25));
            drawCenteredString(g2d, "WELCOME TO A PHYSICS SIM", getWidth(), 50);

            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            int startY = 120;
            int lineHeight = 30;

            String[] message = {
                    "Controls:",
                    "- Press 'S' or 'L' to create squares.",
                    "- Press 'C' or 'B' to create circles.",
                    "- Circles have built-in physics.",
                    "",
                    "Selection & Movement:",
                    "- Shift + Click or Drag: Select and move shapes.",
                    "- Ctrl + A: Select all shapes.",
                    "",
                    "Press SPACE to start."
            };

            for (String line : message) {
                drawCenteredString(g2d, line, getWidth(), startY);
                startY += lineHeight;
            }
            return;
        } else {

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            for (Particle p : particles) {
                p.show(g2d);
            }
            if (delete) {
                for (int index = 0; index < particles.size(); index++) {
                    if (particles.get(index).highlight) {
                        particles.remove(particles.get(index));
                        index--;
                    }
                }
                delete = false;
            }
            if (shift) {
                g2d.setColor(Color.BLACK);
                g2d.drawRect((int) (select1.x), (int) (select1.y), (int) (positions.x - select1.x),
                        (int) (positions.y - select1.y));
            } else {
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
                    g2d.drawRect((int) (positionm.x), (int) (positionm.y), (int) ((2 * diameter)),
                            (int) ((2 * diameter)));
                }
            }
        }
    }

    private void drawCenteredString(Graphics2D g2d, String text, int width, int y) {
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int x = (width - metrics.stringWidth(text)) / 2;
        g2d.drawString(text, x, y);
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
