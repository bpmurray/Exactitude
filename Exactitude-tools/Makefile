##############################################################################
# Starting point
PATH		=		/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

##############################################################################
# Dependencies version numbers
PLATFORMOS		=	$(shell uname -s | tr "[A-Z]" "[a-z]")
PLATFORMVER		=	$(if $(shell uname -m | grep 64), "amd64", "i586")
JAI_VERSION		=	1_1_3
JAI_MAINVERSION		=	1_1
JPEGSRC_VERSION		=	9b
FFMPEG_VERSION		=	0.11.5
LIBSNDFILE_VERSION	=	1.0.27
LIBSAMPLERATE_VERSION	=	0.1.8
MPG123_VERSION		=	1.23.6
PHASH_VERSION		=	0.9.6

# Commands
UNZIP	=	"jar -xf"
COPY	=	"cp"
COPYALL	=	"cp -R"

##############################################################################
# Where should stuff go?
#if [ -z "$(JAVA_HOME)" ]
#then
#   export JAVA_HOME	=	/usr/lib/jvm/java-6-sun
#   if [ ! -d $(JAVA_HOME) ]
#   then
#      export JAVA_HOME	=	/usr/lib/jvm/java-1.6.0-openjdk
#   fi
#fi
PREFIX		=	$(CURDIR)/../Exactitude-build/build
INCDIR		=	$(PREFIX)/include
BINDIR		=	$(PREFIX)/bin
LIBDIR		=	$(PREFIX)/lib
ARCHIVEDIR	=	$(CURDIR)/archives
SRCDIR		=	$(CURDIR)/src
DOCDIR		=	$(CURDIR)/../Exactitude-Doc
HTMLDOCDIR	=	$(DOCDIR)
BUILDLOG	=	$(CURDIR)/../Exactitude-build/bld.log

PKG_CONFIG_PATH	=	$(LIBDIR)/pkgconfig
LDFLAGS		=	-L$(LIBDIR) $(LDFLAGS)
CPPFLAGS	=	$(CPPFLAGS) -I$(INCDIR) $(LDFLAGS)
CXXFLAGS	=	$(CXXFLAGS) -I$(INCDIR) $(LDFLAGS)
CFLAGS		=	$(CFLAGS) -I$(INCDIR) $(LDFLAGS)
LD_LIBRARY_PATH	=	$(LD_LIBRARY_PATH):$(LIBDIR)
LD_RUN_PATH	=	$(LD_RUN_PATH):$(LIBDIR)

##############################################################################
# Configuration settings
CONFIGFLAGS	=	"--bindir=$(BINDIR) --sbindir=$(BINDIR) --libexecdir=$(LIBDIR) --sysconfdir=$(LIBDIR) --sharedstatedir=$(LIBDIR) --localstatedir=$(LIBDIR) --libdir=$(LIBDIR) --includedir=$(INCDIR) --oldincludedir=$(INCDIR) --datarootdir=$(LIBDIR) --datadir=$(LIBDIR) --infodir=$(DOCDIR) --localedir=$(LIBDIR) --mandir=$(DOCDIR) --docdir=$(DOCDIR) --htmldir=$(DOCDIR) --dvidir=$(LIBDIR) --pdfdir=$(DOCDIR) --psdir=$(DOCDIR)"
CONFIGFLAGSA	=	"--bindir=$(BINDIR) --datadir=$(LIBDIR) --libdir=$(LIBDIR) --shlibdir=$(LIBDIR) --incdir=$(INCDIR) --mandir=$(DOCDIR)"


# Create any required directories
dirs :
	mkdir -p $(ARCHIVEDIR) $(SRCDIR) $(DOCDIR)/jai $(DOCDIR)/CImg $(LIBDIR) $(INCDIR) $(BINDIR)


# JAI library
$(ARCHIVEDIR)/jai-$(JAI_VERSION)-lib-$(PLATFORMOS)-$(PLATFORMVER).tar.gz :
	cd $(ARCHIVEDIR)
	wget -c "http://download.java.net/media/jai/builds/release/$(JAI_VERSION)/jai-$(JAI_VERSION)-lib-$(PLATFORMOS)-$(PLATFORMVER).tar.gz"

# CImg - image + video
$(ARCHIVEDIR)/CImg_latest.zip:
	cd $(ARCHIVEDIR)
	wget -c http://cimg.eu/files/CImg_latest.zip

# jpegsrc - image
$(ARCHIVEDIR)/jpegsrc.v$(JPEGSRC_VERSION).tar.gz :
	cd $(ARCHIVEDIR)
	wget -c http://www.ijg.org/files/jpegsrc.v$(JPEGSRC_VERSION).tar.gz

# ffmpeg - video
$(ARCHIVEDIR)/ffmpeg-$(FFMPEG_VERSION).tar.gz :
	cd $(ARCHIVEDIR)
	wget -c http://ffmpeg.org/releases/ffmpeg-$(FFMPEG_VERSION).tar.gz

# libsndfile - audio
$(ARCHIVEDIR)/libsndfile-$(LIBSNDFILE_VERSION).tar.gz :
	cd $(ARCHIVEDIR)
	wget -c http://www.mega-nerd.com/libsndfile/files/libsndfile-$(LIBSNDFILE_VERSION).tar.gz

# libsamplerate - audio
$(ARCHIVEDIR)/libsamplerate-$(LIBSAMPLERATE_VERSION).tar.gz :
	cd $(ARCHIVEDIR)
	wget -c http://www.mega-nerd.com/SRC/libsamplerate-$(LIBSAMPLERATE_VERSION).tar.gz

# libmpg123 - audio
$(ARCHIVEDIR)/mpg123-$(MPG123_VERSION).tar.bz2 :
	cd $(ARCHIVEDIR)
	wget -cO mpg123-$(MPG123_VERSION).tar.bz2 http://netassist.dl.sourceforge.net/project/mpg123/mpg123/$(MPG123_VERSION)/mpg123-$(MPG123_VERSION).tar.bz2

# Finally get pHash itself
$(ARCHIVEDIR)/pHash-$(PHASH_VERSION).tar.gz :
	cd $(ARCHIVEDIR)
	wget -c http://www.phash.org/releases/pHash-$(PHASH_VERSION).tar.gz

# All archives
archives : dirs $(ARCHIVEDIR)/jai-$(JAI_VERSION)-lib-$(PLATFORMOS)-$(PLATFORMVER).tar.gz $(ARCHIVEDIR)/jpegsrc.v$(JPEGSRC_VERSION).tar.gz $(ARCHIVEDIR)/ffmpeg-$(FFMPEG_VERSION).tar.gz $(ARCHIVEDIR)/libsndfile-$(LIBSNDFILE_VERSION).tar.gz $(ARCHIVEDIR)/libsamplerate-$(LIBSAMPLERATE_VERSION).tar.gz $(ARCHIVEDIR)/mpg123-$(MPG123_VERSION).tar.bz2 $(ARCHIVEDIR)/pHash-$(PHASH_VERSION).tar.gz	
	cd $(ARCHIVEDIR)
	chmod 644 *
