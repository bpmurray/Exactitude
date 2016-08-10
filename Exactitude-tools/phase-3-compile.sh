#!/bin/sh
##############################################################################
# This is the third phase - compile the files
. ../Exactitude-tools/phase-0-settings.sh

##############################################################################
# Start the process
echo "Phase 3: Configure and compile"

###############################################################################
# Build the files

# JPEG
echo -n "Building JPEG ..."
echo "===================== JPEG-${JPEGSRC_VERSION} ======================" >${BUILDLOG}
cd ${SRCDIR}/jpeg-${JPEGSRC_VERSION}
echo -n " configure ..."
./configure CFLAGS='-O2' ${CONFIGFLAGS} >>${BUILDLOG} 2>&1
echo -n " build ..."
make >>${BUILDLOG} 2>&1
make test >>${BUILDLOG} 2>&1
echo -n " install ..."
make install >>${BUILDLOG} 2>&1
echo "done!"

# ffmpeg
echo -n "Building FFMPEG ..."
echo "===================== ffmpeg-${FFMPEG_VERSION} ======================" >>${BUILDLOG}
cd ${SRCDIR}/ffmpeg-${FFMPEG_VERSION}
echo -n " configure ..."
./configure --prefix=${PREFIX} --enable-shared --enable-swscale --enable-pthreads ${CONFIGFLAGSA} >>${BUILDLOG} 2>&1
echo -n " build ..."
make >>${BUILDLOG} 2>&1
echo -n " install ..."
make install >>${BUILDLOG} 2>&1
echo "done!"

# libsndfile
echo -n "Building libsndfile ..."
echo "===================== libsndfile-${LIBSNDFILE_VERSION} ======================" >>${BUILDLOG}
cd ${SRCDIR}/libsndfile-${LIBSNDFILE_VERSION}
## We may need these (note: they require a load of extra stuff!)
# sudo apt-get install libflac* libogg* libvorbis*
echo -n " configure ..."
./configure ${CONFIGFLAGS} --prefix=${PREFIX} >>${BUILDLOG} 2>&1
echo -n " build ..."
make >>${BUILDLOG} 2>&1
echo -n " install ..."
make install >>${BUILDLOG} 2>&1
echo "done!"

# libsamplerate
echo -n "Building libsamplerate ..."
echo "===================== libsamplerate-${LIBSAMPLERATE_VERSION} ======================" >>${BUILDLOG}
cd ${SRCDIR}/libsamplerate-${LIBSAMPLERATE_VERSION}
echo -n " configure ..."
./configure ${CONFIGFLAGS} --prefix=${PREFIX} >>${BUILDLOG} 2>&1
echo -n " build ..."
make >>${BUILDLOG} 2>&1
echo -n " install ..."
make install >>${BUILDLOG} 2>&1
echo "done!"

# mpg123
echo -n "Building mpg123 ..."
echo "===================== mpg123-${MPG123_VERSION} ======================" >>${BUILDLOG}
cd ${SRCDIR}/mpg123-${MPG123_VERSION}
echo -n " configure ..."
./configure ${CONFIGFLAGS} --prefix=${PREFIX} --exec-prefix=${PREFIX} >>${BUILDLOG} 2>&1
echo -n " build ..."
make >>${BUILDLOG} 2>&1
echo -n " install ..."
make install >>${BUILDLOG} 2>&1
echo "done!"

# pHash
echo -n "Building pHash ..."
echo "===================== phash-${PHASH_VERSION} ======================" >>${BUILDLOG}
cd ${SRCDIR}/pHash-${PHASH_VERSION}
${COPY} ../CImg-${CIMG_VERSION}/CImg.h ${INCDIR}
${COPY} ../CImg-${CIMG_VERSION}/CImg.h .
echo -n " configure ..."
echo ./configure --enable-java --enable-video-hash ${CONFIGFLAGS} LDFLAGS=${LDFLAGS} CFLAGS=${CFLAGS} >>${BUILDLOG} 2>&1
./configure --enable-java --enable-video-hash ${CONFIGFLAGS} LDFLAGS=${LDFLAGS} CFLAGS="${CFLAGS}" >>${BUILDLOG} 2>&1
echo -n " build ..."
make clean
make >>${BUILDLOG} 2>&1
cd bindings/java
# Copy  the updated file(s)
${COPYALL} ${STARTDIR}/../Exactitude-pHash/pHash-updates/* .
rm -rf ./org/.svn ./org/phash/.svn
mkdir -p bin
javac -d bin org/phash/*.java >>${BUILDLOG} 2>&1
cd bin
jar -cf ${LIBDIR}/pHash-${PHASH_VERSION}.jar *
cd ${SRCDIR}/pHash-${PHASH_VERSION}
echo -n " install ..."
make install >>${BUILDLOG} 2>&1
echo "done!"

###############################################################################
echo "Phase 3 (compile) complete"

