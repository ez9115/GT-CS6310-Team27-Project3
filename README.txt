#Use the following command to compile the program:

javac -d bin src/

#In order launch the GUI and interact with the GUI to supply program parameters,
perform the following:

cd bin

java EarthSim.Demo

#In order launch the GUI and pass parameters from the command line pass the
following arguments:

java EarthSim.Demo [-s] [-p] [-r | -t] [-b #]

#Options:
-s  set the Simulation to run in its own thread
-p  set the Presentation to run in its own thread
-r  set the Presentation to have the initiative 
-t  set the Simulation to have the initiative
-b # set the buffer length to an integer of value #

