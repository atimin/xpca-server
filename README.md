XPCA-Server is experimental server for [XPCA](http://www.xpca.org/).

Required
-------------------------------------
Maven 3 
JDK 1.5

[See](https://github.com/torquebox/jruby-maven-plugins) for setting maven for work with jruby and rubygems.

Architecture
-------------------------------------

XPCA-Server writen in Java and JRuby

The core of application is the Runtime Engine that use database for storing configuration and communicate with other devices (PLCA, OPC servers and etc) with using drivers.
Web application is used for data access and configuration  on HTML, JSON, etc protocols.      

	+--------------------------------+		+-----------------------------+
	|     Databse configuration      |		|							  |
	|							     |<---->+							  |
	|		+-----------------+      |		|                             |
	+-------|   Hibernate     |------+		|		REST Server           |
			+--------+--------+				|		Sinatra + jruby       |
		    	     ^         				|		                      |
		        	 |						|                             |
		        	 v						|                             |
	+----------------+---------------+		|		                      |
	|                                |		|	                          |
	|	   Runtime Engine(Java)		 |<---->+                             |
	|		+--------+--------+      |		|                             |
	+-------|     Drivers     |------+		+-----------------------------+
			+--------+--------+
				     ^
					 |
			+--------+--------+
			|     	PLC  	  |
			+--------+--------+		

Working
-------------------------------------
1. Web application for access configuration data (readonly)
2. Basic class for organization configuration tree.

Usage
-------------------------------------

Run in root directory

$mvn gem:exec

And open in browser http://127.0.0.1:4567/xpca/

Authors
-------------------------------------
Aleksey Timin <atimin@gmail.com>