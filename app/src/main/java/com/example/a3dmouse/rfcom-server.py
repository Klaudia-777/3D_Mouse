# file: rfcomm-server.py
# auth: Albert Huang <albert@csail.mit.edu>
# desc: simple demonstration of a server application that uses RFCOMM sockets
#
# $Id: rfcomm-server.py 518 2007-08-10 07:20:07Z albert $

import ctypes
from ctypes import c_long, c_wchar_p, c_ulong, c_void_p
from bluetooth import *

#==== GLOBAL VARIABLES ======================

gHandle = ctypes.windll.kernel32.GetStdHandle(c_long(-11))

#============================================

server_sock=BluetoothSocket( RFCOMM )
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

uuid = "94f39d29-7d6d-437d-973b-fba39e49d4ee"

advertise_service( server_sock, "SampleServer",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ],
#                   protocols = [ OBEX_UUID ]
                    )

print("Waiting for connection on RFCOMM channel %d" % port)

client_sock, client_info = server_sock.accept()
print("Accepted connection from ", client_info)

try:
    while True:
        data = client_sock.recv(1024)
        if len(data) > 2:
            coordinates = data.decode('utf8').strip().replace(",",".").split(";")
            if len(coordinates) == 2:
                print("received [%s]" % coordinates[0])
                print("received [%s]" % coordinates[1])
                if '' not in coordinates:
                    value = int(float(coordinates[0])) + (int(float(coordinates[1])) << 16)
                    ctypes.windll.kernel32.SetConsoleCursorPosition(gHandle, c_ulong(value))
except IOError:
    pass

print("disconnected")

client_sock.close()
server_sock.close()
print("all done")