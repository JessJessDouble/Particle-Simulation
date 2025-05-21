package ParticleSimulationFolder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Particle {

    Vector position, velocity;
    double diameter, gravity, mass;
    Color color, temp;
    double elasticity, object;
    double spin;
    boolean highlight;

    public Particle(Vector position, Vector velocity, Color color, double diameter, double gravity,
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
            spin = 0;
            g.setColor(color);
            int size = (int) diameter; // Assuming diameter is the square's side length
            // spin +=1;
            double angle = Math.toRadians(spin); // Rotation angle

            // Calculate rotated corners relative to the center
            double halfSize = size / 2.0;
            double quarterSize = size / 2.0;

            double cosA = Math.cos(angle);
            double sinA = Math.sin(angle);

            int[] xPoints = new int[4];
            int[] yPoints = new int[4];

            xPoints[0] = (int) (position.x + halfSize + (-halfSize * cosA - (-halfSize) * sinA));
            yPoints[0] = (int) (position.y + halfSize + (-halfSize * sinA + (-halfSize) * cosA));

            xPoints[1] = (int) (position.x + quarterSize + (quarterSize * cosA - (-quarterSize) * sinA));
            yPoints[1] = (int) (position.y + quarterSize + (quarterSize * sinA + (-quarterSize) * cosA));

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
            Vector pCenter = position.add((new Vector(diameter / 2, diameter / 2)));
            Vector ppCenter = p.position.add((new Vector(p.diameter / 2, p.diameter / 2)));
            double distance = pCenter.distance(ppCenter);
            if (distance < (diameter + p.diameter) / 2) {
                // Resolve collision
                Vector normal = position.subtract(p.position).normalize();
                Vector relativeVelocity = velocity.subtract(p.velocity);
                double speed = relativeVelocity.dot(normal);

                double impulse = (2 * speed) / (mass + p.mass);
                velocity = velocity.subtract(normal.scale(elasticity * impulse * p.mass));
                p.velocity = p.velocity.add(normal.scale(elasticity * impulse * mass));

                // Separate overlapping particles
                double overlap = (diameter + p.diameter) / 2 - distance*.99;
                position = position.add(normal.scale(overlap / 2));
                p.position = p.position.subtract(normal.scale(overlap / 2));
            }
        } else if (object == 1 && p.object == 0 || p.object == 1 && p.object == 2 || object == 3 && p.object == 2 || p.object == 3 && p.object == 4) {
            Vector pCenter = position.add((new Vector(diameter / 2, diameter / 2)));
            pCenter = pCenter.axis(spin);
            Vector ppCenter = p.position.add((new Vector(p.diameter / 2, p.diameter / 2)));
            ppCenter = ppCenter.axis(spin);
            Vector distance = pCenter.subtract(ppCenter);
            if (Math.abs(distance.y) < (diameter + p.diameter) / 2
                    && Math.abs(distance.x) < (diameter + p.diameter) / 2) {
                // Resolve collision
                Vector normal = position.subtract(p.position).normalize();
                Vector relativeVelocity = velocity.subtract(p.velocity);
                double speed = relativeVelocity.dot(normal);

                double impulse = (2 * speed) / (mass + p.mass);
                // velocity = velocity.subtract(normal.scale(elasticity*impulse * p.mass));
                p.velocity = p.velocity.add(normal.scale(elasticity * impulse * mass));
                // Separate overlapping particles
                // Separate overlapping particles
                double overlapX = (diameter + p.diameter) / 2 - Math.abs(distance.x);
                double overlapY = (diameter + p.diameter) / 2 - Math.abs(distance.y);

                if (Math.abs(distance.y) > Math.abs(distance.x)) {
                    // double correctionY = overlapY / 2;
                    double correctionY = overlapY;
                    if (distance.y > 0) {
                        // position = position.add(new Vector(0, correctionY));
                        p.position = p.position.subtract(new Vector(0, correctionY));
                    } else {
                        // position = position.subtract(new Vector(0, correctionY));
                        p.position = p.position.add(new Vector(0, correctionY));
                    }
                } else if (Math.abs(distance.x) > Math.abs(distance.y)) {
                    // double correctionX = overlapX / 2;
                    double correctionX = overlapX * 1.1;
                    if (distance.x > 0) {
                        // position = position.add(new Vector(correctionX, 0));
                        p.position = p.position.subtract(new Vector(correctionX, 0));
                    } else {
                        // position = position.subtract(new Vector(correctionX, 0));
                        p.position = p.position.add(new Vector(correctionX, 0));
                    }
                }

            }
        }
    }

    public void update(int width, int height, ArrayList<Particle> particles) {
        if (highlight) {
            if (ParticleCanvas.arrow == 1) {
                this.position.y += -5;
                
            }
            else if (ParticleCanvas.arrow == 2) {
                this.position.y += 5;
                
            }
            else if (ParticleCanvas.arrow == 3) {
                this.position.x += -5;
                
            }
            else if (ParticleCanvas.arrow == 4) {
                this.position.x -= -5;
                
            }
        }
        else if (object == 0 || object == 2) {
            gravityShift();
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

    public void selected(Vector s1, Vector s2) {
        if (ParticleCanvas.shift) {

            Vector pCenter = position.add((new Vector(diameter / 2, diameter / 2)));
            // pCenter=pCenter.axis(spin);
            Vector sCenter = new Vector(s1.x + .5 * (s2.x - s1.x), s1.y + .5 * (s2.y - s1.y));

            // sCenter=sCenter.axis(1);
            double sdiametery = Math.abs(s1.y - s2.y);
            double sdiameterx = Math.abs(s1.x - s2.x);
            Vector distance = pCenter.subtract(sCenter);
            if (Math.abs(distance.y) < (diameter + sdiametery) / 2
                    && Math.abs(distance.x) < (diameter + sdiameterx) / 2) {
                highlight = true;
            }
        }

    }

}
