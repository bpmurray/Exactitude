#!/bin/sh
##############################################################################
# This is the first phase - retrieve the files
. ../Exactitude-tools/phase-0-settings.sh

##############################################################################
# Get the archives of the dependencies
echo "Retrieving archives (about 30MB)"
cd $ARCHIVEDIR

# Get JAI library
#wget -qc "http://download.java.net/media/jai/builds/release/${JAI_VERSION}/jai-${JAI_VERSION}-lib-${PLATFORMOS}-${PLATFORMVER}-jdk.bin"
wget -c "http://download.java.net/media/jai/builds/release/${JAI_VERSION}/jai-${JAI_VERSION}-lib-${PLATFORMOS}-${PLATFORMVER}.tar.gz"
wget -c "http://download.java.net/media/jai/builds/release/${JAI_VERSION}/jai-${JAI_MAINVERSION}-mr-doc.zip"

# Get the phash dependencies - update these filenames as needed
wget -cO CImg-${CIMG_VERSION}.zip http://sourceforge.net/projects/cimg/files/CImg-${CIMG_VERSION}.zip/download/
wget -c http://cimg.sourceforge.net/CImg_slides.pdf
wget -c http://www.ijg.org/files/jpegsrc.v${JPEGSRC_VERSION}.tar.gz
wget -c http://ffmpeg.org/releases/ffmpeg-${FFMPEG_VERSION}.tar.gz
wget -c http://www.mega-nerd.com/libsndfile/files/libsndfile-${LIBSNDFILE_VERSION}.tar.gz
wget -c http://www.mega-nerd.com/SRC/libsamplerate-${LIBSAMPLERATE_VERSION}.tar.gz
wget -cO mpg123-${MPG123_VERSION}.tar.bz2 https://sourceforge.net/projects/mpg123/files/mpg123/${MPG123_VERSION}/mpg123-${MPG123_VERSION}.tar.bz2/download/

# Finally get pHash itself
### PHASH SITE IS OFFLINE!!!!
# wget -c http://www.phash.org/releases/pHash-${PHASH_VERSION}.tar.gz
cp ../../../../Exactitude-old/Exactitude-Dependencies/archives/pHash-${PHASH_VERSION}.tar.gz .
chmod 644 *

###############################################################################
echo "Phase 1 (retrieve) complete"
