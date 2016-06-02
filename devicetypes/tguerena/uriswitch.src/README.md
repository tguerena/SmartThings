# SmartThings

URI Switch
Attached to a Virtual Switch, this kicks off a URL (External and/or Internal) when an On/Off command is sent

External calls triggered with On and Off commands
-------------------------------------------------

External On URI --- ie:  http://www.externalaccess.com/turn_lightOn	

External Off URI --- ie:  http://www.externalaccess.com/turn_lightOff

Internal calls triggered with On and Off commands
-------------------------------------------------

Internal IP --- 192.168.1.1

Internal Port (if not 80)        

Internal On Path (/blah?q=this) --- /api/HomeAutomation.X10/o1/Control.Off

Internal Off Path (/blah?q=this) --- /api/HomeAutomation.X10/o1/Control.On



For stateless URI Button see https://github.com/ruricu/SmartThings
