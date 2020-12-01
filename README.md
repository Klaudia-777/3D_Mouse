# 3D-Mouse
The application which enables phone to act as a mouse whose cursor is moved by changing the position of the phone in space. 
Track changes in the orientation of the phone in 3D space and visualize the intersection point of the phone axis with the selected plane.

To provide communication between devices we use pybluez repository 
https://github.com/karulis/pybluez
and we extend the code of already prepared rfcomm-server.py script (examples/simple/rfcomm-server.py).


Setting up bluetooth server:
1. Download and install windows 10 build tools: https://www.visualstudio.com/pl/thank-you-downloading-visual-studio/?sku=BuildTools&rel=15
2. Clone pybluez repo https://github.com/karulis/pybluez
3. cd to directory with repo
4. run python setup.py install
5. cd examples/simple/
6. run python rfcomm-server.py
7. run application

Sensors rotational movement:
https://www.mathworks.com/matlabcentral/mlc-downloads/downloads/submissions/40876/versions/8/previews/sensorgroup/Examples/html/CapturingAzimuthRollPitchExample.html



