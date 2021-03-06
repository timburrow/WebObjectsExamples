echo
echo The frameworks scripts have been deprecated. Please use the new scripts in
echo the /bin directory. See the file frameworks.DEPRECATED.txt for more
echo information
echo

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to you under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# ---------------------------------------------------------
# -- This simple script is an example of how to start dblook in 
# -- an embedded environment.
# --
#-- REQUIREMENTS: 
# -- You must have the Derby libraries in your classpath
# -- 
# -- See the setEmbeddedCP.ksh for an example of
# -- how to do this.
# --
# -- This file for use on Unix ksh systems
# ---------------------------------------------------------

# DERBY_INSTALL=

DERBY_HOME=${DERBY_HOME:-$DERBY_INSTALL}

[ -z "$DERBY_HOME" ] && {
  echo "\$DERBY_HOME or \$DERBY_INSTALL not set. Please set one of these variables"
  echo "to the location of your Derby installation."
  exit 1
}

[ -z "$JAVA_HOME" ] && {
  [ -x /usr/java/bin/java ] && {
    JAVA_HOME=/usr/java
  }
  [ -z "$JAVA_HOME" ] && {
    echo "JAVA_HOME not set. Please set JAVA_HOME to the location of your Java"
    echo "installation."
    exit 1
  }
}

# ---------------------------------------------------------
# -- start dblook
# ---------------------------------------------------------
"$JAVA_HOME/bin/java" -cp "${DERBY_HOME}/lib/derby.jar:${DERBY_HOME}/lib/derbytools.jar:${CLASSPATH}" org.apache.derby.tools.dblook $@

# ---------------------------------------------------------
# -- To use a different JVM with a different syntax, simply edit
# -- this file
# ---------------------------------------------------------

