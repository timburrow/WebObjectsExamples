#!/bin/sh

#
# Platform name
#
PLATFORM_NAME=`uname -s`

if [ "$PLATFORM_NAME" = "" ]
then
    echo "Unable to access "uname" executable!  Terminating."
    exit 1
fi

#
# Platform type
#
case "$PLATFORM_NAME" in
    "Darwin")   PLATFORM_TYPE=Darwin
                ;;
    "Mac OS")   PLATFORM_TYPE=Darwin
                ;;
    *Windows*)  PLATFORM_TYPE=Dos
                ;;
    *winnt*)    PLATFORM_TYPE=Dos
                ;;
    *)          echo "Unsupported platform ($PLATFORM_NAME)!  Terminating."
                exit 1
                ;;
esac

#
# System directories
#
if [ "$PLATFORM_TYPE" = "Darwin" ]
then
    SYSTEM_LIBRARY_DIR=/System/Library
    LOCAL_LIBRARY_DIR=/Library
    SYSTEM_DEVELOPER_DIR=/Developer
else

    if [ "$NEXT_ROOT" = "" ]
    then
        echo "NEXT_ROOT environment variable is not set!  Terminating."
        exit 1
    fi

    SYSTEM_LIBRARY_DIR=$NEXT_ROOT/Library
    LOCAL_LIBRARY_DIR=$NEXT_ROOT/Local/Library
    SYSTEM_DEVELOPER_DIR=$NEXT_ROOT/Developer
fi

#
# Essential directories
#
SLWJ=$SYSTEM_LIBRARY_DIR/WebObjects/JavaApplications
LFJR=$LOCAL_LIBRARY_DIR/Frameworks/JavaBusinessLogic.framework/Resources
DEJD=$SYSTEM_DEVELOPER_DIR/Examples/JavaWebObjects/DatabaseSetup

#
# Location of executable, pause disabled
#
if [ "$PLATFORM_TYPE" = "Dos" ]
then
    eoutil=$SLWJ/javaeoutil.woa/javaeoutil.cmd
    nonstop="-WONoPause YES"
else
    eoutil=$SLWJ/javaeoutil.woa/javaeoutil
    nonstop=""
fi

#
# Schema drop disabled for new databases
#
#drop="-schemaDrop tables primaryKeySupport"
drop=""

#
# List of names
#
names="Movies Rentals"

for name in $names
do

    #
    # Locations of model and plist
    #
    model=$LFJR/$name.eomodeld
    plist=$DEJD/$name.plist

    #
    # Locations of models for model groups
    #
    if [ "$name" = "Movies" ]
    then
        models=$model
    fi
    if [ "$name" = "Rentals" ]
    then
        models="$LFJR/Movies.eomodeld $model"
    fi

    #
    # Run
    #
    echo Loading schema and data for $model, please wait...
    $eoutil dump $model -source plist $plist -dest database $drop -schemaCreate tables primaryKeySupport foreignKeyConstraints -force -modelGroup $models $nonstop -WODebuggingEnabled TRUE -EOAdaptorDebugEnabled FALSE

done
