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

    private static final Random random = new Random();

    public Particle(Vector position, Vector velocity, Color color, double diameter, double gravity,
                    double elasticity, double object) {
        this.position = position;
        this.velocity = velocity;
        this.color = color;
        this.temp = color;
        this.diameter = diameter;
        this.gravity = gravity;
        this.elasticity = elasticity;
        this.object = object;
        this.spin = random.nextDouble();

        // Estimate mass based on shape type
        if (object == 0 || object == 2) {
            this.mass = Math.pow(diameter, 2) * 0.25 * Math.PI;
        } else {
            this.mass = Math.pow(diameter, 2);
        }
        
    }

    public void show(Graphics2D g) {
        g.setColor(color);
        int size = (int) diameter;

        if (object == 0 || object == 2) {
            g.fillOval((int) position.x, (int) position.y, size, size);
            highlight(g);
        } else if (object == 1 || object == 3) {
            // Rotated square using basic rotation matrix
            double angle = Math.toRadians(spin);
            double halfSize = size / 2.0;
            double cosA = Math.cos(angle);
            double sinA = Math.sin(angle);

            int[] xPoints = new int[4];
            int[] yPoints = new int[4];

            for (int i = 0; i < 4; i++) {
                double dx = (i == 1 || i == 2) ? halfSize : -halfSize;
                double dy = (i >= 2) ? halfSize : -halfSize;

                xPoints[i] = (int) (position.x + halfSize + dx * cosA - dy * sinA);
                yPoints[i] = (int) (position.y + halfSize + dx * sinA + dy * cosA);
            }

            g.fillPolygon(xPoints, yPoints, 4);
        }
    }

    public void gravityShift() {
        velocity.y += gravity;
        position = position.add(velocity);
    }

    public void collide(Particle p) {
        // Circle-circle collision
        if (object % 2 == 0 && p.object % 2 == 0 && object <= p.object) {
            Vector center1 = position.add(new Vector(diameter / 2, diameter / 2));
            Vector center2 = p.position.add(new Vector(p.diameter / 2, p.diameter / 2));
            double distance = center1.distance(center2);
            double minDistance = (diameter + p.diameter) / 2;

            if (distance < minDistance) {
                Vector normal = center1.subtract(center2).normalize();
                Vector relativeVel = velocity.subtract(p.velocity);

                // Apply impulse only if particles are moving toward each other
                if (relativeVel.dot(normal) < 0) {
                    double impulse = (2 * relativeVel.dot(normal)) / (mass + p.mass);
                    velocity = velocity.subtract(normal.scale(elasticity * impulse * p.mass));
                    p.velocity = p.velocity.add(normal.scale(elasticity * impulse * mass));
                }

                // Overlap correction: push particles apart equally
                double overlap = minDistance - distance;
                Vector correction = normal.scale(overlap * 0.5);
                position = position.add(correction);
                p.position = p.position.subtract(correction);

                // Basic friction when resting vertically
                if (Math.abs(velocity.y) < 0.1) velocity.x *= 0.9;
                if (Math.abs(p.velocity.y) < 0.1) p.velocity.x *= 0.9;
            }

        // Square-circle collision
        } else if (object % 2 == 1 && p.object % 2 == 0) {
            Vector circleCenter = p.position.add(new Vector(p.diameter / 2, p.diameter / 2));

            // Clamp circle center to square bounds
            double clampedX = Math.max(position.x, Math.min(circleCenter.x, position.x + diameter));
            double clampedY = Math.max(position.y, Math.min(circleCenter.y, position.y + diameter));
            Vector closest = new Vector(clampedX, clampedY);

            Vector penetration = circleCenter.subtract(closest);
            double penetrationDist = penetration.magnitude();

            if (penetrationDist < p.diameter / 2) {
                Vector normal = penetration.normalize();

                // Simple vertical bounce for top surface
                if (Math.abs(normal.x) < 0.5 && p.velocity.dot(normal) < 0) {
                    p.velocity = new Vector(p.velocity.x, -p.velocity.y * elasticity);

                    double overlap = (p.diameter / 2) - penetrationDist;
                    p.position = p.position.add(normal.scale(normal.y > 0 ? overlap : -overlap));
                }
            }
        }
    }

    public void update(int width, int height, ArrayList<Particle> particles) {
        if (highlight) {
            switch (ParticleCanvas.arrow) {
                case 1 -> this.position.y -= 5;
                case 2 -> this.position.y += 5;
                case 3 -> this.position.x -= 5;
                case 4 -> this.position.x += 5;
            }
        } else if (object == 0 || object == 2) {
            gravityShift();
        }

        // Wall collisions
        if (object >= 0 && object <= 3) {
            if (position.x < 0 || position.x + diameter > width) {
                velocity.x *= -elasticity;
                position.x = Math.max(0, Math.min(position.x, width - diameter));
            }

            if (position.y < 0 || position.y + diameter > height) {
                velocity.y *= -elasticity;
                position.y = Math.max(0, Math.min(position.y, height - diameter));
            }

            // Check particle-particle collisions
            for (Particle p : particles) {
                if (p != this) collide(p);
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
            Vector pCenter = position.add(new Vector(diameter / 2, diameter / 2));
            Vector sCenter = s1.add(s2.subtract(s1).scale(0.5));
            double dx = Math.abs(s1.x - s2.x) / 2 + diameter / 2;
            double dy = Math.abs(s1.y - s2.y) / 2 + diameter / 2;

            Vector diff = pCenter.subtract(sCenter);
            if (Math.abs(diff.x) < dx && Math.abs(diff.y) < dy) {
                highlight = true;
            }
        }
    }
}
