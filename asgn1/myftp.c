#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <stdarg.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <netdb.h>
#include <netinet/in.h>
#include "myftp.h"


void fatal_error( int argc, ... )
{
  if( argc > 0 ) fprintf( stderr, "Error: " );

  va_list args;
  va_start( args, argc );
  for(int i = 0; i < argc; i++) {
    fprintf( stderr, "%s ", va_arg( args, char* ) );
  }
  va_end( args );

  if( argc > 0 ) fprintf( stderr, "\n" );
  exit(1);
}

int open_socket( char *hostname, char *port, int flags, int action )
{
  int err;

  struct protoent *tcp_proto = getprotobyname("tcp");
  if( tcp_proto == NULL ) fatal_error( 1, "getprotobyname(\"tcp\")" );
  endprotoent();

  struct addrinfo *host;
  struct addrinfo hints = {
    .ai_family    = AF_UNSPEC,
    .ai_socktype  = SOCK_STREAM,
    .ai_protocol  = tcp_proto->p_proto,
    .ai_flags     = flags
  };
  err = getaddrinfo( hostname, port, &hints, &host );
  if( err != 0 ) fatal_error( 2, "getaddrinfo", (char*) gai_strerror(err) );

  int sock_fd = socket(
      host->ai_family,
      host->ai_socktype,
      host->ai_protocol);
  if( sock_fd == -1 ) fatal_error( 2, "socket", strerror(errno) );

  switch(action) {
    case MYFTP_BIND:
      err = bind( sock_fd, host->ai_addr, host->ai_addrlen );
      if( err != 0 ) fatal_error( 2, "bind", strerror(errno) );
      break;

    case MYFTP_CONNECT:
      err = connect( sock_fd, host->ai_addr, host->ai_addrlen );
      if( err != 0 ) fatal_error( 2, "connect", strerror(errno) );
      break;
  }

  freeaddrinfo(host);

  return sock_fd;
}

bool filename_valid( char *filename )
{
  for( int i = 0; filename[i] != '\0'; i++ ) {
    switch( filename[i] ) {
      case '/':
      case ':':
        return false;
    }
  }
  return true;
}

bool myftp_msg_ok( struct myftp_msg msg )
{
  return memcmp( msg.protocol, "myftp", 5 ) == 0;
}

struct myftp_msg new_myftp_msg( unsigned char type )
{
  struct myftp_msg msg = {
    .protocol = "myftp",
    .type     = type,
    .length   = sizeof (struct myftp_msg)
  };
  return msg;
}

int recv_myftp_msg( int sock_fd, struct myftp_msg *msg )
{
  int err = read_all( sock_fd, (char*) msg, sizeof (struct myftp_msg) );
  if( err == -1 ) return -1;
  msg->length = ntohl( msg->length );
  return 0;
}

int send_myftp_msg( int sock_fd, struct myftp_msg *msg )
{
  msg->length = htonl( msg->length );
  int err = write_all( sock_fd, (char*) msg, sizeof (struct myftp_msg) );
  if( err == -1 ) return -1;
  return 0;
}

int recv_myftp_file( int sock_fd, char *filename )
{
  int return_val = 0;

  int file_fd = open(
      filename, O_WRONLY | O_TRUNC | O_CREAT,
      S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH);
  if( file_fd == -1 ) {
    fprintf( stderr, "Error: open: %s\n", strerror(errno) );
    return_val = -1;
    goto fail;
  }

  struct myftp_msg resp;
  if( recv_myftp_msg( sock_fd, &resp ) == -1 ) {
    fprintf( stderr, "Error: recv: %s\n", strerror(errno) );
    return_val = -1;
    goto fail;
  }
  if( !( myftp_msg_ok( resp ) && resp.type == MYFTP_FILE_DATA ) ) {
    fprintf( stderr, "Error: Malformed response\n" );
    return_val = -1;
    goto fail;
  }

  int file_size = resp.length - (sizeof resp);
  if( file_size < 0 ) {
    fprintf( stderr, "Error: Invalid file size\n" );
    return_val = -1;
    goto fail;
  }

  fprintf( stderr, "Recv: %s (%d bytes)\n", filename, file_size );

  int page_size = getpagesize();
  char *buf = malloc( page_size );
  if( buf == NULL ) {
    fprintf( stderr, "Error: malloc: %s\n", strerror(errno) );
    return_val = -1;
    goto fail;
  }

  int err = copy_file( sock_fd, file_fd, file_size, buf, page_size );
  if( err == -1 ) {
    fprintf( stderr, "Error: copy_file: %s\n", strerror(errno) );
    return_val = -1;
    goto fail;
  }

fail:
  close( file_fd );
  free( buf );
  return return_val;
}

int send_myftp_file( int sock_fd, char *filename, size_t file_size )
{
  int return_val = 0;

  int file_fd = open( filename, O_RDONLY );
  if( file_fd == -1 ) {
    fprintf( stderr, "Error: open: %s\n", strerror(errno) );
    return_val = -1;
    goto fail;
  }  

  fprintf( stderr, "Send: %s (%lu bytes)\n", filename, file_size );

  struct myftp_msg resp = new_myftp_msg( MYFTP_FILE_DATA );
  resp.length += file_size;
  if( send_myftp_msg( sock_fd, &resp ) == -1 ) {
    fprintf( stderr, "Error: send: %s\n", strerror(errno) );
    return_val = -1;
    goto fail;
  }

  int page_size = getpagesize();
  char *buf = malloc( page_size );
  if( buf == NULL ) {
    fprintf( stderr, "Error: malloc: %s\n", strerror(errno) );
    return_val = -1;
    goto fail;
  }

  int err = copy_file( file_fd, sock_fd, file_size, buf, page_size );
  if( err == -1 ) {
    fprintf( stderr, "Error: copy_file: %s\n", strerror(errno) );
    return_val = -1;
    goto fail;
  }

fail:
  close( file_fd );
  free( buf );
  return return_val;
}

int read_all( int fd, char *buf, size_t length )
{
  int size;
  size_t offset = 0;
  while(1) {
    size = read( fd, buf + offset, length - offset );
    if( size == -1 ) return -1;
    offset += size;
    if( offset >= length ) return offset;
  }
}

int write_all( int fd, char *buf, size_t length )
{
  int size;
  size_t offset = 0;
  while(1) {
    size = write( fd, buf + offset, length - offset );
    if( size == -1 ) return -1;
    offset += size;
    if( offset >= length ) return offset;
  }
}

int copy_file( int src, int dst, size_t length, char *buf, size_t buf_size )
{
  size_t size;
  int copied_len = 0;
  while(1) {
    int remain_len = length - copied_len;
    int copy_size = (remain_len < buf_size)? remain_len: buf_size;
    size = read( src, buf, copy_size );
    if( size == -1 ) return -1;
    size = write_all( dst, buf, size );
    if( size == -1 ) return -1;
    copied_len += size;
    if( copied_len >= length ) return copied_len;
  }
  return 0;
}

