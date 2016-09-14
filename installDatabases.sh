#!/bin/sh

# TODO:
#
# Should I have a "-force" option? When this is present, and the database exists, delete and recreate the database.
# Otherwise, should the install do nothing if the database exists? Or echo a warning and do nothing?


db_home=/Developer/Examples/JavaWebObjects/Databases
db_lib=$db_home/db-derby-10.3.2.1-bin/lib

if [ ! -d $db_lib ]; then
    echo "\nERROR: Cannot find Derby database files at \"$db_lib\".\n"
    echo $usage"\n"
    exit 1
fi

if [ ! -f $db_home/movies_create.sql ]; then
    echo "\nERROR: Cannot find example database import files at, for example, \"$db_home/movies_create.sql\".\n"
    echo $usage"\n"
    exit 1
fi

usage="Usage: installDatabases <username>"

if [ `id -u` != 0 ]; then
    echo "\nERROR: Please run this script as root.\n"
    echo $usage"\n"
    exit 1
fi

user=$1

if [ "$user" = "" ]; then
    echo "\nERROR: Please give a username as a parameter.\n"
    echo $usage"\n"
    exit 1
fi

data_home=/Users/$user/Library/Databases/derby-10.3.2.1

/bin/mkdir -p $data_home

if [ "$JAVA_HOME" = "" ]; then
    java="/usr/bin/java"
else
    java="$JAVA_HOME/bin/java"
fi

/bin/rm -rf $data_home/data

out=/tmp/derby_$$.log
/usr/bin/touch $out

opts="-classpath $db_lib/derby.jar:$db_lib/derbytools.jar -Dderby.system.home=$data_home/data"

$java $opts org.apache.derby.tools.ij < $db_home/bug_create.sql
$java $opts org.apache.derby.tools.ij < $db_home/bug_insert.sql

$java $opts org.apache.derby.tools.ij < $db_home/discussion_create.sql
$java $opts org.apache.derby.tools.ij < $db_home/discussion_insert.sql

$java $opts org.apache.derby.tools.ij < $db_home/inherit_horizontal_create.sql
$java $opts org.apache.derby.tools.ij < $db_home/inherit_horizontal_insert.sql

$java $opts org.apache.derby.tools.ij < $db_home/inherit_singletable_create.sql
$java $opts org.apache.derby.tools.ij < $db_home/inherit_singletable_insert.sql

$java $opts org.apache.derby.tools.ij < $db_home/inherit_vertical_create.sql
$java $opts org.apache.derby.tools.ij < $db_home/inherit_vertical_insert.sql

$java $opts org.apache.derby.tools.ij < $db_home/movies_create.sql
$java $opts org.apache.derby.tools.ij < $db_home/movies_insert.sql

$java $opts org.apache.derby.tools.ij < $db_home/petstore_create.sql
$java $opts org.apache.derby.tools.ij < $db_home/petstore_insert.sql

$java $opts org.apache.derby.tools.ij < $db_home/realestate_create.sql
$java $opts org.apache.derby.tools.ij < $db_home/realestate_insert.sql

$java $opts org.apache.derby.tools.ij < $db_home/school_create.sql
$java $opts org.apache.derby.tools.ij < $db_home/school_insert.sql

$java $opts org.apache.derby.tools.ij < $db_home/vendor_create.sql
$java $opts org.apache.derby.tools.ij < $db_home/vendor_insert.sql

chown -R $user $data_home

$java $opts org.apache.derby.tools.sysinfo

echo "Ok"
