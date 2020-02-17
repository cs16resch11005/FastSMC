Fast-SMC is statistical model checker for agent based systems on 2D_grid_graphs/Torus, which uses sampling technique. 

System Requirements.
JDK 1.8 or plus

Steps to run the Simulation:

1. Run src/testSMC.java 
   cd src/
   javac testSMC.java 
   java testSMC


Important: To run the simulation for either different agents or different grid size, first you have to set these parameters in Config/config.properties, and then follow the above steps. 

Note: To compare exaustive-simulation with semicone-shape-sampling and cylinder-shape-sampling methods, you have to run sampling.sh. You can see the sampling/time_sample.png, and sampling/prob_sample.png for sample output of these stragies.

2. Run sampling.sh    
   ./sampling.sh
# FastSMC
# FastSMC
