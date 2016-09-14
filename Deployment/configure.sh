#!/bin/sh

#
# This script installs configuration information for needed for deploying WebObjects applications
# using Apache 2 webserver and WebObjects Apache 2 module on a OS X Client system.  It will update
# the apache conf file and restart the webserver.  It also copies necessary launchd plists for JavaMonitor
# and wotaskd into place and starts them with launchd.
#


# assumes OSX and installs on boot drive
NEXT_ROOT=/


#
# First configure the httpd.conf file
#

# Set up some convenience variables 
woroot="${NEXT_ROOT}System/Library/WebObjects"
woapachedir=$woroot"/Adaptors/Apache2.2"
launchdroot="${NEXT_ROOT}System/Library/LaunchDaemons"

#canonical launchd plists
launchdplistroot="${NEXT_ROOT}/Developer/Examples/JavaWebObjects/Deployment/launchd"
womonitorplist="${launchdplistroot}/com.apple.womonitor.plist"
wotaskdplist="${launchdplistroot}/com.apple.wotaskd.plist"


# Make sure we have an apache2 config directory
mkdir -p "${NEXT_ROOT}/etc/apache2"

#
# Add the line to include the WebObjects module
#
if [ -f $woapachedir"/mod_WebObjects.so" ]; then
  # Append additional config info to the Apache setup file (httpd.conf)
  if [ -e "${NEXT_ROOT}etc/httpd/httpd.conf.new" ] ; then
    APACHECONFIG="${NEXT_ROOT}etc/apache2/httpd.conf.new"
  else
    APACHECONFIG="${NEXT_ROOT}etc/apache2/httpd.conf"
  fi

  if [ -f $APACHECONFIG ]; then
    if grep "# Including WebObjects Configs" $APACHECONFIG 2>&1 > /dev/null ; then
      echo "++++ Apache config file is already configured for WebObjects"
    else
        cp -p $APACHECONFIG $APACHECONFIG.saved
        echo "Adding WebObjects Apache module Include line to $APACHECONFIG ..."
        echo "" >> $APACHECONFIG
        echo "# Including WebObjects Configs" >> $APACHECONFIG
        echo "Include "$woapachedir"/apache.conf" >> $APACHECONFIG
        echo "" >> $APACHECONFIG
    fi

    # ... then kill and restart the WebServer.
	echo "Restarting Apache ..."
	apachectl graceful >/dev/null 2>&1
	echo "Apache module configuration and installation successful."
  else
    echo "*** No Apache configuration file found ($APACHECONFIG) ***" 
  fi

fi

#
# Copy the launchd plists into place
#
if [ -f $launchdroot/com.apple.womonitor.plist ]; then
    echo "++++ JavaMonitor launchd plist already exists"
else 
	echo "++++ Installing JavaMonitor launchd plist"
	cp -f "${womonitorplist}" "$launchdroot/com.apple.womonitor.plist"
	launchctl load "$launchdroot/com.apple.womonitor.plist"
	launchctl start com.webobjects.womonitor
fi

if [ -f $launchdroot/com.apple.wotaskd.plist ]; then
    echo "++++ wotaskd launchd plist already exists"
else 
	echo "++++ Installing wotaskd launchd plist"
	cp -f "${wotaskdplist}" "$launchdroot/com.apple.wotaskd.plist"
	launchctl load "$launchdroot/com.apple.wotaskd.plist"
	launchctl start com.webobjects.wotaskd
fi
