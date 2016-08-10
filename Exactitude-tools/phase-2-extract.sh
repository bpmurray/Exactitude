#!/bin/sh
##############################################################################
# This phase extracts the files from the archives
. ../Exactitude-tools/phase-0-settings.sh

###############################################################################
# Start the process
echo "Phase 2: Extracting the files"

###############################################################################
# Extract the source files
cd ${SRCDIR}
${UNZIP} ${ARCHIVEDIR}/CImg-${CIMG_VERSION}.zip
tar xzf ${ARCHIVEDIR}/jpegsrc.v${JPEGSRC_VERSION}.tar.gz
tar xzf ${ARCHIVEDIR}/ffmpeg-${FFMPEG_VERSION}.tar.gz
tar xzf ${ARCHIVEDIR}/libsndfile-${LIBSNDFILE_VERSION}.tar.gz
tar xzf ${ARCHIVEDIR}/libsamplerate-${LIBSAMPLERATE_VERSION}.tar.gz
tar xzf ${ARCHIVEDIR}/pHash-${PHASH_VERSION}.tar.gz

tar xjf ${ARCHIVEDIR}/mpg123-${MPG123_VERSION}.tar.bz2

# Set up JAI
cd ${LIBDIR}
#${ARCHIVEDIR}/jai-${JAI_VERSION}-lib-${PLATFORMOS}-${PLATFORMVER}-jdk.bin
tar xzf "${ARCHIVEDIR}/jai-${JAI_VERSION}-lib-${PLATFORMOS}-${PLATFORMVER}.tar.gz"

###############################################################################
# Set up documentation
cd ${DOCDIR}
${COPY} ${ARCHIVEDIR}/CImg_slides.pdf CImg
${COPYALL} ${SRCDIR}/CImg-${CIMG_VERSION}/html CImg
cd jai
${UNZIP} ${ARCHIVEDIR}/jai-${JAI_MAINVERSION}-mr-doc.zip


# Environment
echo "#!/bin/bash" >${BINDIR}/setpaths.sh
echo "# Change LIBDIR/BINDIR to suit your installation" >>${BINDIR}/setpaths.sh
echo "LIBDIR=${LIBDIR}" >>${BINDIR}/setpaths.sh
echo "BINDIR=${BINDIR}" >>${BINDIR}/setpaths.sh
echo "export JAIHOME=\${LIBDIR}/jai-1_1_3/lib" >>${BINDIR}/setpaths.sh
echo "export CLASSPATH=\${JAIHOME}/jai_core.jar:\${JAIHOME}/jai_codec.jar:\${JAIHOME}/mlibwrapper_jai.jar:${CLASSPATH}" >>${BINDIR}/setpaths.sh
echo "export LD_LIBRARY_PATH=.:\${JAIHOME}:${LIBDIR}:${CLASSPATH}" >>${BINDIR}/setpaths.sh
echo "export PATH=\${BINDIR}:${PATH}" >>${BINDIR}/setpaths.sh
chmod +x ${BINDIR}/setpaths.sh


###############################################################################
echo "Phase 2 (extract) complete"

