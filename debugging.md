# Enable Logger Debugging
Sometimes when developing with the APU sources you need a closer look at what the bot is doing\
This file will guide you on how to enable logger debugging 
# How To Enable
Go to the logback.xml under src/main/resources/\
Where it says `<root level="info">` (Line 9) change the level, so line 9 says `<root level="debug">`\
From now on, all debug messages will be printed to the logger. This contains debug messages from JDA and some other classes in the bot