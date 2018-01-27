#include <stdbool.h>

#define MYFTP_BIND 0
#define MYFTP_CONNECT 1

#define MYFTP_LIST_REQUEST      0xA1
#define MYFTP_LIST_REPLY        0xA2
#define MYFTP_GET_REQUEST       0xB1
#define MYFTP_GET_REPLY_SUCCESS 0xB2
#define MYFTP_GET_REPLY_FAILURE 0xB3
#define MYFTP_PUT_REQUEST       0xC1
#define MYFTP_PUT_REPLY         0xC2
#define MYFTP_FILE_DATA         0xFF

struct myftp_msg {
  unsigned char protocol[5];
  unsigned char type;
  unsigned int length;
} __attribute__ ((packed));

void fatal_error( int argc, ... );

int open_socket( char *hostname, char *port, int flags, int action );

bool filename_valid( char *filename );

bool myftp_msg_ok(struct myftp_msg msg);

struct myftp_msg new_myftp_msg( unsigned char type );

int recv_myftp_msg( int sock_fd, struct myftp_msg *msg );

int send_myftp_msg( int sock_fd, struct myftp_msg *msg );

int recv_myftp_file( int sock_fd, char *filename );

int send_myftp_file( int sock_fd, char *filename, size_t file_size );

int read_all( int fd, char *buf, size_t length );

int write_all( int fd, char *buf, size_t length );

int copy_file( int src, int dst, size_t length, char *buf, size_t buf_size );

