#!/bin/bash
export EXACTITUDE_HOME=`dirname "$(cd "${0%/*}" 2>/dev/null; echo "$PWD"/"${0##*/}")"`/..
export EXACTITUDE_VERSION=0.2
export EXACTITUDE_BUILD=${EXACTITUDE_HOME}/Exactitude-build/build
export PATH=${EXACTITUDE_BUILD}/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
export LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:${EXACTITUDE_BUILD}/lib
export EXACTITUDE_CLASSPATH=${EXACTITUDE_BUILD}/
export EXACTITUDE_RUN="java -Djava.library.path=${EXACTITUDE_BUILD}/lib -classpath ${EXACTITUDE_CLASSPATH}"


if [ -f /usr/lib/jvm/java-6-sun/jre/bin/java ]
then
   export JAVA_HOME=/usr/lib/jvm/java-6-sun/
elif [ -f usr/lib/j2sdk1.6-ibm/jre/bin/java ]
then
   export JAVA_HOME=/usr/lib/j2sdk1.6-ibm
else
   export JAVA_HOME=`update-alternatives --list java | head -1 | sed -e "s/\/bin\/java$//"`
fi
PATH="${JAVA_HOME}/bin:$PATH"
