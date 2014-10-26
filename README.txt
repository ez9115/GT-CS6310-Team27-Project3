#In order to compile the code you must be in the src directory:

cd src

#Use the following command to compile the program:

javac EarthSim/Demo.java

#In order launch the GUI and interact with the GUI to supply program parameters,
perform the following:

java EarthSim.Demo

#In order launch the GUI and pass parameters from the command line use the
#following arguments:

java EarthSim.Demo [-s] [-p] [-r | -t] [-b #]

#Options:
-s  set the Simulation to run in its own thread
-p  set the Presentation to run in its own thread
-r  set the Presentation to have the initiative 
-t  set the Simulation to have the initiative
-b # set the buffer length to a positive integer of value #

