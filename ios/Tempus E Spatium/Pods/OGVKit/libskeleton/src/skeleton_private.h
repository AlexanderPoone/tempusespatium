/**
   Copyright (c) 2010, Xiph.org Foundation

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions
   are met:

   - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

   - Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.

   - Neither the name of the Xiph.org Foundation nor the names of its
   contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE FOUNDATION
   OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
   SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
   LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
   DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
   THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
   OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
#ifndef __OGG_SKELETON_PRIVATE__
#define __OGG_SKELETON_PRIVATE__

#include "skeleton.h"

typedef struct _FisHead {
  ogg_uint16_t  ver_maj;
  ogg_uint16_t  ver_min;
  ogg_int64_t   ptime_num;
  ogg_int64_t   ptime_denum;
  ogg_int64_t   btime_num;
  ogg_int64_t   btime_denum;
  char          UTC[20];
  ogg_int64_t   first_sample_num;
  ogg_int64_t   first_sample_denum;
  ogg_int64_t   last_sample_num;
  ogg_int64_t   last_sample_denum;
  ogg_int64_t   segment_len;
  ogg_int64_t   nh_offset;
} FisHead;

typedef struct _FisBone {
  ogg_uint32_t    msg_header_offset;  
  ogg_int32_t     serial_no;
  ogg_uint32_t    num_headers;
  ogg_int64_t     granule_num;
  ogg_int64_t     granule_denum;
  ogg_int64_t     start_granule;
  ogg_uint32_t    preroll;
  unsigned char   granule_shift;
  char          * msg_fields;
} FisBone;

typedef struct _KeyFrameInfo {
  ogg_int64_t   offset;
  ogg_int64_t   time_ms;
} KeyFrameInfo;

typedef struct _Index {
  ogg_int32_t     serial_no;
  ogg_int64_t     num_keys;
  ogg_int64_t     ptime_denum;
  ogg_int64_t     first_sample_num;
  ogg_int64_t     last_sample_num;
  KeyFrameInfo  * keypoints;
} Index;

typedef struct _TrackVect TrackVect;

typedef enum {
  SKELETON_INITIALISED = 0,
  SKELETON_HEAD,
  SKELETON_EOS
} SkelState;

struct _OggSkeleton {
  FisHead       fishead;
  TrackVect   * track_vect;
  short         indexing;
  short         finished;
  SkelState     state;
  ogg_int64_t   packetno;
};

#endif /* __OGG_SKELETON_PRIVATE__ */
