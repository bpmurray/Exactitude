#!/bin/sh
##############################################################################
# This is the first phase - retrieve the files
. ../Exactitude-tools/phase-0-settings.sh

##############################################################################
# Get the archives of the dependencies
echo "Retrieving archives (about 30MB)"
cd $ARCHIVEDIR

# Get JAI library
wget -c "http://download.java.net/media/jai/builds/release/${JAI_VERSION}/jai-${JAI_VERSION}-lib-${PLATFORMOS}-${PLATFORMVER}.tar.gz"

# Get the phash dependencies - update these filenames as needed
# CImg - image + video
wget -c http://cimg.eu/files/CImg_latest.zip
# jpegsrc - image
wget -c http://www.ijg.org/files/jpegsrc.v${JPEGSRC_VERSION}.tar.gz
# ffmpeg - video
wget -c http://ffmpeg.org/releases/ffmpeg-${FFMPEG_VERSION}.tar.gz
# libsndfile - audio
wget -c http://www.mega-nerd.com/libsndfile/files/libsndfile-${LIBSNDFILE_VERSION}.tar.gz
# libsamplerate - audio
wget -c http://www.mega-nerd.com/SRC/libsamplerate-${LIBSAMPLERATE_VERSION}.tar.gz
# libmpg123 - audio
wget -cO mpg123-${MPG123_VERSION}.tar.bz2 http://netassist.dl.sourceforge.net/project/mpg123/mpg123/${MPG123_VERSION}/mpg123-${MPG123_VERSION}.tar.bz2

# Finally get pHash itself
### PHASH SITE IS OFFLINE!!!!
wget -c http://www.phash.org/releases/pHash-${PHASH_VERSION}.tar.gz
# cp ../../../../Exactitude-old/Exactitude-Dependencies/archives/pHash-${PHASH_VERSION}.tar.gz .
chmod 644 *

###############################################################################
echo "Phase 1 (retrieve) complete"
