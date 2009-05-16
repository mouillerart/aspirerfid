OW2 :: AspireRFID ::  iBuddy

This bundle provides a service and a command to control one or more iBuddies connected to the host

The i-Buddy is a small blinking and moving USB figurine initially designed as a MSN live avantar.
This actuator can be used in SOHO (Small Office Home Office) usecases.




Installation Windows
--------------------
Copy the file IBUDDY.INF in the system

copy libusb0.dll C:\windows\system32\
copy libusb0.sys C:\windows\system32\drivers\

Linux
--------------------
??




Command
-------
The command can execute in parallel several actions on a set of connected iBuddies

ibuddy 0 rotate 1000 left right left right left right flap 75 30 head 1000 red yellow none cyan yellow heart 1000 on off on off on off on off  ibuddy 1 rotate 1000 left right left right left right flap 75 30 head 1000 red yellow none cyan yellow heart 1000 on off on off on off on

is interpreted as
 
>>iBuddy 0 will excute all those actions in parallel :
        ROTATE : [1000, LEFT, RIGHT, LEFT, RIGHT, LEFT, RIGHT]
        FLAP : [75, 30]
        HEAD : [1000, RED, YELLOW, NONE, CYAN, YELLOW]
        HEART : [1000, ON, OFF, ON, OFF, ON, OFF, ON, OFF]

>>iBuddy 1 will excute all those actions in parallel :
        ROTATE : [1000, LEFT, RIGHT, LEFT, RIGHT, LEFT, RIGHT]
        FLAP : [75, 30]
        HEAD : [1000, RED, YELLOW, NONE, CYAN, YELLOW]
        HEART : [1000, ON, OFF, ON, OFF, ON, OFF, ON]


The first parameter is the duration in milliseconds of the action
The second parameter of the FLAP action is the number of times of the wing flapping with a duration indicated in the first argument


Other links
-----------
"hacking the USB i-buddy" http://cuntography.com/blog/?p=17

http://www.jraf.org/static/maven/sites/jlibibuddy

http://sourceforge.net/projects/usbsnoop


