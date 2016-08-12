##############################################################################
# Starting point
STARTDIR=`pwd`
export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

##############################################################################
# Set the version numbers
PLATFORMOS=`uname -s | tr "[A-Z]" "[a-z]"`
PLATFORMVER=`uname -m | grep 64`
if [ -z "$PLATFORMVER" ]
then
   PLATFORMVER="i586"
else
   PLATFORMVER="amd64"
fi
JAI_VERSION=1_1_3
JAI_MAINVERSION=1_1
JPEGSRC_VERSION=9b
#CIMG_VERSION=1.5.1
FFMPEG_VERSION=0.11.5
LIBSNDFILE_VERSION=1.0.27
LIBSAMPLERATE_VERSION=0.1.8
MPG123_VERSION=1.23.6
PHASH_VERSION=0.9.6

# Commands
UNZIP="jar -xf"
COPY="cp"
COPYALL="cp -R"

##############################################################################
# Where should stuff go?
if [ -z "$JAVA_HOME" ]
then
   export JAVA_HOME=/usr/lib/jvm/java-6-sun
   if [ ! -d ${JAVA_HOME} ]
   then
      export JAVA_HOME=/usr/lib/jvm/java-1.6.0-openjdk
   fi
fi
PREFIX=${STARTDIR}/../Exactitude-build/build
INCDIR=${STARTDIR}/../Exactitude-build/build/include
BINDIR=${STARTDIR}/../Exactitude-build/build/bin
LIBDIR=${STARTDIR}/../Exactitude-build/build/lib
ARCHIVEDIR=${STARTDIR}/archives
SRCDIR=${STARTDIR}/src
DOCDIR=${STARTDIR}/../Exactitude-Doc
HTMLDOCDIR=${DOCDIR}
BUILDLOG=${STARTDIR}/../Exactitude-build/bld.log

PKG_CONFIG_PATH=${LIBDIR}/pkgconfig
LDFLAGS="-L${LIBDIR} ${LDFLAGS}"
CPPFLAGS="${CPPFLAGS} -I${INCDIR} ${LDFLAGS}"
CXXFLAGS="${CXXFLAGS} -I${INCDIR} ${LDFLAGS}"
if [ -z "${CFLAGS}" ]
then
   CFLAGS="-I${INCDIR} ${LDFLAGS}"
else
   CFLAGS="${CFLAGS} -I${INCDIR} ${LDFLAGS}"
fi
LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:${LIBDIR}"
LD_RUN_PATH="${LD_RUN_PATH}:${LIBDIR}"

##############################################################################
# Configuration settings
CONFIGFLAGS="--bindir=${BINDIR} --sbindir=${BINDIR} --libexecdir=${LIBDIR} --sysconfdir=${LIBDIR} --sharedstatedir=${LIBDIR} --localstatedir=${LIBDIR} --libdir=${LIBDIR} --includedir=${INCDIR} --oldincludedir=${INCDIR} --datarootdir=${LIBDIR} --datadir=${LIBDIR} --infodir=${DOCDIR} --localedir=${LIBDIR} --mandir=${DOCDIR} --docdir=${DOCDIR} --htmldir=${DOCDIR} --dvidir=${LIBDIR} --pdfdir=${DOCDIR} --psdir=${DOCDIR}"
CONFIGFLAGSA="--bindir=${BINDIR} --datadir=${LIBDIR} --libdir=${LIBDIR} --shlibdir=${LIBDIR} --incdir=${INCDIR} --mandir=${DOCDIR}"


###############################################################################
# Create any required directories
mkdir -p ${ARCHIVEDIR} ${SRCDIR} ${DOCDIR}/jai ${DOCDIR}/CImg ${LIBDIR} ${INCDIR} ${BINDIR}

###############################################################################
# Check for required tools
if [ -z "`which javac`" -o -z "`which jar`" ]
then
   echo "** WARNING: Java does not appear to be installed correctly"
fi
if [ -z "`which yasm`" ]
then
   echo "** WARNING: yasm does not appear to be installed correctly"
fi
if [ -z "`which g++`" ]
then
   echo "** WARNING: C++ does not appear to be installed correctly"
fi
