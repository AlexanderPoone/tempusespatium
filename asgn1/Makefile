arch := $(shell uname -m)
kernel := $(shell uname -s)

server := myftpserver-$(arch)-$(kernel)
client := myftpclient-$(arch)-$(kernel)

cflags = -std=gnu99 -lpthread

ifeq ($(kernel),SunOS)
	cflags += -lsocket -lnsl -m64
endif

all: $(server) $(client)

$(server): myftp.c myftpserver.c
	gcc $(cflags) -o $@ $^

$(client): myftp.c myftpclient.c
	gcc $(cflags) -o $@ $^

myftp.c: myftp.h

myftpserver.c: myftp.h

myftpclient.c: myftp.h

clean:
	rm -rf *.o *.dSYM $(server) $(client)

