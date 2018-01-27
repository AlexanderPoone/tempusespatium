#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/socket.h>
#include <netdb.h>
#include <dirent.h>
#include <fcntl.h>
#include <pthread.h>
#include "myftp.h"

#define DATA_DIR "data"

struct request {
  int sock_fd;
  struct sockaddr_storage addr;
  socklen_t addrlen;
};

void myftp_server_list( int sock_fd )
{
  struct str_list {
    size_t length;
    char *data;
    struct str_list *next;
  };
  struct str_list *filelist = NULL;
  struct str_list *filename = NULL;
  DIR *data_dir = opendir( DATA_DIR );
  if( data_dir == NULL ) {
    fprintf( stderr, "Error: opendir: %s\n", strerror(errno) );
    return;
  }

  while(1) {
    errno = 0;
    struct dirent *dir = readdir( data_dir );
    if( dir == NULL ) {
      if( errno != 0 ) {
        fprintf( stderr, "Error: readdir: %s\n", strerror(errno) );
      }
      break;
    }

#if defined(__sun)
    char *file_path = (char*) malloc( strlen(DATA_DIR"/") + strlen(dir->d_name) + 2 );
    if( file_path == NULL ) {
      fprintf( stderr, "Error: malloc: %s\n", strerror(errno) );
      continue;
    }
    strcpy( file_path, DATA_DIR"/" );
    strcat( file_path, dir->d_name );

    struct stat s;
    if( stat( file_path, &s ) == -1 ) {
      fprintf( stderr, "Error: stat %s: %s\n", dir->d_name, strerror(errno) );
      free( file_path );
      continue;
    }
    free( file_path );

    if( (s.st_mode & S_IFMT) == S_IFREG ) {
#else
    if( dir->d_type == DT_REG ) {
#endif
      if( filelist == NULL ) {
        filename = malloc( sizeof (struct str_list) );
        filelist = filename;
      } else {
        filename->next = malloc( sizeof (struct str_list) );
        filename = filename->next;
      }
      filename->length = strlen(dir->d_name) + 1;
      filename->data = malloc( filename->length );
      strcpy( filename->data, dir->d_name );
      filename->next = NULL;
    }
  }
  closedir(data_dir);

  struct myftp_msg msg = new_myftp_msg( MYFTP_LIST_REPLY );
  for( filename = filelist; filename; filename = filename->next ) {
    msg.length += filename->length;
  }
  if( send_myftp_msg( sock_fd, &msg ) == -1 ){
    fprintf( stderr, "Error: send: %s\n", strerror(errno) );
    return;
  }

  struct str_list *next_filename;
  for( filename = filelist; filename; filename = next_filename ) {
    if( write_all( sock_fd, filename->data, filename->length ) == -1 ) {
      fprintf( stderr, "Error: send: %s\n", strerror(errno) );
      return;
    }
    next_filename = filename->next;
    free( filename->data );
    free( filename );
  }
}

void myftp_server_get( int sock_fd, unsigned int msg_len )
{
  if( msg_len < 1 ) {
    fprintf( stderr, "Client Error: Invalid payload length\n" );
    goto error;
  }

  char *filename = (char*) malloc( msg_len );
  if( filename == NULL ) {
    fprintf( stderr, "Error: malloc: %s\n", strerror(errno) );
    goto error;
  }
  if( read_all( sock_fd, filename, msg_len ) == -1 ) {
    fprintf( stderr, "Error: recv: %s\n", strerror(errno) );
    goto error;
  }
  if( filename[msg_len - 1] != '\0' ) {
    fprintf( stderr, "Error: Invalid payload\n" );
    goto error;
  }
  if( !filename_valid( filename ) ) {
    fprintf( stderr, "Client Error: Invalid filename\n" );
    goto failure;
  }

  char *file_path = (char*) malloc( strlen(DATA_DIR"/") + strlen(filename) + 2 );
  if( file_path == NULL ) {
    fprintf( stderr, "Error: malloc: %s\n", strerror(errno) );
    goto error;
  }
  strcpy( file_path, DATA_DIR"/" );
  strcat( file_path, filename );

  struct stat file_stat;
#if defined(__sun)
  errno = ENOENT;
#endif
  if( stat( file_path, &file_stat ) == -1 ) {
    switch( errno ) {
      case EACCES:
      case ENAMETOOLONG:
      case ENOENT:
        fprintf( stderr, "Client Error: %s\n", strerror(errno) );
        goto failure;
      default:
        fprintf( stderr, "Error: stat: %s\n", strerror(errno) );
        goto error;
    }
  }
  if( (file_stat.st_mode & S_IFMT) != S_IFREG ) {
    fprintf( stderr, "Client Error: Not a regular file\n" );
    goto failure;
  }

  struct myftp_msg resp = new_myftp_msg( MYFTP_GET_REPLY_SUCCESS );
  resp.length += file_stat.st_size;
  if( send_myftp_msg( sock_fd, &resp ) == -1 ) {
    fprintf( stderr, "Error: send: %s\n", strerror(errno) );
    goto error;
  }

  if( send_myftp_file( sock_fd, file_path, file_stat.st_size ) == -1 ) {
    fprintf( stderr, "Error: send: %s\n", strerror(errno) );
    goto error;
  }

error:
  free( filename );
  free( file_path );
  return;

failure:;
  struct myftp_msg fail_resp = new_myftp_msg( MYFTP_GET_REPLY_FAILURE );
  if( send_myftp_msg( sock_fd, &fail_resp ) == -1 ) {
    fprintf( stderr, "Error: send: %s\n", strerror(errno) );
  }
  goto error;
}

void myftp_server_put( int sock_fd, unsigned int msg_len )
{
  if( msg_len < 1 ) {
    fprintf( stderr, "Client Error: Invalid payload length\n" );
    goto error;
  }

  char *filename = (char*) malloc( msg_len );
  if( filename == NULL ) {
    fprintf( stderr, "Error: malloc: %s\n", strerror(errno) );
    goto error;
  }
  if( read_all( sock_fd, filename, msg_len ) == -1 ) {
    fprintf( stderr, "Error: recv: %s\n", strerror(errno) );
    goto error;
  }
  if( filename[msg_len - 1] != '\0' ) {
    fprintf( stderr, "Error: Invalid payload\n" );
    goto error;
  }
  if( !filename_valid( filename ) ) {
    fprintf( stderr, "Client Error: Invalid filename\n" );
    goto error;
  }

  char *file_path = (char*) malloc( strlen(DATA_DIR"/") + strlen(filename) + 2 );
  if( file_path == NULL ) {
    fprintf( stderr, "Error: malloc: %s\n", strerror(errno) );
    goto error;
  }
  strcpy( file_path, DATA_DIR"/" );
  strcat( file_path, filename );

  struct myftp_msg resp = new_myftp_msg( MYFTP_PUT_REPLY );
  if( send_myftp_msg( sock_fd, &resp ) == -1 ) {
    fprintf( stderr, "Error: send: %s\n", strerror(errno) );
  }

  if( recv_myftp_file( sock_fd, file_path ) == -1 ){
    fprintf( stderr, "Error: recv: %s\n", strerror(errno) );
  }

error:
  free( filename );
  free( file_path );
  return;
}

void *handle_request( void *arg )
{
  struct request *req = (struct request*) arg;

  char hostname[256] = {0};
  int err = getnameinfo(
      (struct sockaddr*) &(req->addr), req->addrlen,
      hostname, 256, NULL, 0, NI_NUMERICHOST );
  if( err != 0 ) {
    fprintf( stderr, "Error: getnameinfo: %s\n", gai_strerror(err) );
    goto fail;
  }

  struct myftp_msg msg;
  if( recv_myftp_msg( req->sock_fd, &msg ) == -1 ){
    fprintf( stderr, "Error: recv: %s\n", strerror(errno) );
    goto fail;
  }

  fprintf(
      stderr, "Msg: 0x%X %s (%lu + %lu bytes)\n",
      msg.type, hostname, sizeof msg, msg.length - (sizeof msg));

  if( !myftp_msg_ok( msg ) ) {
    fprintf( stderr, "Client Error: Malformed message\n" );
    goto fail;
  }

  switch( msg.type ) {
    case MYFTP_LIST_REQUEST:
      myftp_server_list( req->sock_fd );
      break;
    case MYFTP_GET_REQUEST:
      myftp_server_get( req->sock_fd, msg.length - (sizeof msg) );
      break;
    case MYFTP_PUT_REQUEST:
      myftp_server_put( req->sock_fd, msg.length - (sizeof msg) );
      break;
    default:
      fprintf( stderr, "Client Error: Invalid message type\n" );
      goto fail;
  }

fail:
  close( req->sock_fd );
  free( req );
  return NULL;
}

int main( int argc, char *argv[] )
{
  if( argc < 2 ) fatal_error( 1, "Too few arguments" );

  DIR *data_dir = opendir( DATA_DIR );
  if( data_dir == NULL ) {
    fatal_error( 1, "Cannot open data directory" );
  }
  closedir(data_dir);
  fprintf( stderr, "Using data dir: %s\n", DATA_DIR );

  int sock_fd = open_socket( NULL, argv[1], AI_PASSIVE, MYFTP_BIND );
  int err = listen( sock_fd, 128 );
  if( err != 0 ) fatal_error( 2, "listen", strerror(errno) );

  fprintf( stderr, "Listening on port %s...\n", argv[1] );
  while(1) {
    struct request *req = malloc( sizeof (struct request) );
    req->addrlen = sizeof req->addr;

    req->sock_fd = accept( sock_fd, (struct sockaddr*) &(req->addr), &(req->addrlen) );
    if( req->sock_fd == -1 ) {
      switch( errno ) {
        case ECONNABORTED:
        case EINTR:
        case EMFILE:
        case ENFILE:
        case ENOBUFS:
        case ENOMEM:
        case EPROTO:
        case EPERM:
        case ENOSR:
        case ETIMEDOUT:
          perror( "Error: accept" );
          free( req );
          continue;
        default:
          fatal_error( 2, "accept", strerror(errno) );
      }
    }

    //handle_request(req);
    pthread_t handler_thread;
    pthread_attr_t attr;
    pthread_attr_init( &attr );
    pthread_attr_setdetachstate( &attr, PTHREAD_CREATE_DETACHED );
    err = pthread_create( &handler_thread, &attr, handle_request, req );
    if( err != 0 ) {
      switch( errno ) {
        case EAGAIN:
          perror( "Error: pthread_create" );
          free( req );
          continue;
        default:
          fatal_error( 2, "pthread_create", strerror(errno) );
      }
    }
    pthread_attr_destroy( &attr );
  }

  return 0;
}
