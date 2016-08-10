#!/bin/bash
. ./Exactitude-tools/setenv.sh
export EXACTITUDE_TESTRUN="${EXACTITUDE_RUN} -jar ${EXACTITUDE_BUILD}/lib/Exactitude-test-${EXACTITUDE_VERSION}.jar"
export EXACTITUDE_TESTDATA=${EXACTITUDE_HOME}/Exactitude-test/testData

cd ${EXACTITUDE_HOME}
rm -rf Exactitude-build/build
rm -rf Exactitude-Dependencies/src
rm -rf Exactitude-Dependencies/archives

cd Exactitude-Dependencies
../Exactitude-tools/phase-1-get.sh
../Exactitude-tools/phase-2-extract.sh
../Exactitude-tools/phase-3-compile.sh

cd ${EXACTITUDE_HOME}/Exactitude-build
ant clean
ant runtests

#${EXACTITUDE_TESTRUN} -f jpg -t png -i ${EXACTITUDE_TESTDATA}/filea.jpg -o ${EXACTITUDE_TESTDATA}/filea.png
