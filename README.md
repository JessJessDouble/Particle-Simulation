Particle Simulation
A Java-based 2D particle simulation that models motion, gravity, and elastic collisions between different particle types (circles and squares). The simulation also supports user interaction for selecting and moving particles.

Features
Realistic gravity-based motion

Elastic collisions between particles (circle-circle and square-circle)

Square and circular particles

Particle selection and movement with arrow keys

Simple friction and boundary collision response

Custom rendering using Java AWT

How It Works
Physics Engine: Implements Newtonian mechanics using 2D vector math.

Collision Detection:

Circle-circle: Distance between centers is used to determine overlap.

Square-circle: Uses clamping to find the closest point and calculates penetration.

Collision Response: Applies impulse-based velocity changes using mass and elasticity.

Mass Calculation: Derived from shape area (proportional to diameter squared).

Graphics: Custom drawing with Graphics2D, including highlighting for selected particles.

Project Structure
cpp
Copy
Edit
ParticleSimulationFolder/
├── Particle.java          // Core class with physics and rendering
├── Vector.java            // 2D vector math operations
├── ParticleCanvas.java    // Handles rendering and input
├── Simulation.java        // Main JFrame application
Controls
Action	Input
Move selected particle	Arrow keys
Select particle(s)	Shift + Mouse drag
Run simulation	Run Simulation.java

Requirements
Java 8 or later

No external libraries (pure Java AWT)

Running the Simulation
Compile the code:

bash
Copy
Edit
javac ParticleSimulationFolder/*.java
Run the application:

bash
Copy
Edit
java ParticleSimulationFolder.Simulation
Notes
The simulation is designed for visualization and educational purposes.

Friction is only applied under certain velocity thresholds to simulate resting behavior.

Rotation for squares is static in the current implementation (spin is disabled).(Will be added later with angular velocity)
