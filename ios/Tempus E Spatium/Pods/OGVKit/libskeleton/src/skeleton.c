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

#include <assert.h>
#include <stdlib.h> 
#include <string.h>

#include "skeleton_private.h"
#include "skeleton_macros.h"
#include "skeleton_vector.h"

#ifdef _MSC_VER
#define inline __inline
#endif

static inline unsigned char* extract_uint16 (unsigned char *data, ogg_uint16_t* i)
{
  if (data == NULL || i == NULL) return NULL;
  *i = data[0] | data[1] <<8;
  return data + 2;
}

static inline unsigned char* extract_uint32(unsigned char *data, ogg_uint32_t* i) 
{
  if (data == NULL || i == NULL) return NULL;
  *i = data[0] | (data[1] << 8) | (data[2] << 16) | (data[3] << 24);
  return data + 4;
}

static inline unsigned char* extract_int32(unsigned char *data, ogg_int32_t* i)
{
  if (data == NULL || i == NULL) return NULL;
  *i = data[0] | (data[1] << 8) | (data[2] << 16) | (data[3] << 24);
  return data + 4;  
}

static inline unsigned char* extract_int64(unsigned char *data, ogg_int64_t* i) 
{
  ogg_uint32_t lo = -1;
  ogg_int32_t hi = -1;
  
  if (data == NULL || i == NULL) return NULL;

  data = extract_uint32(data, &lo);
  data = extract_int32(data, &hi);
  *i = (ogg_int64_t) lo + ((ogg_int64_t)hi << 32);

  return data;
}

static inline unsigned char* write16le (unsigned char *data_out, const ogg_uint16_t i)
{
    data_out[0]=i&0xff;
    data_out[1]=(i>>8)&0xff;
    
    return data_out + 2;
}

static inline unsigned char* write32le (unsigned char *data_out, const ogg_uint32_t i)
{
    data_out[0]=i&0xff;
    data_out[1]=(i>>8)&0xff;
    data_out[2]=(i>>16)&0xff;
    data_out[3]=(i>>24)&0xff;
    
    return data_out + 4;
}

static inline unsigned char* write64le (unsigned char *data_out, const ogg_int64_t i)
{
    ogg_uint32_t hi=i>>32;
    data_out[0]=i&0xff;
    data_out[1]=(i>>8)&0xff;
    data_out[2]=(i>>16)&0xff;
    data_out[3]=(i>>24)&0xff;
    data_out[4]=hi&0xff;
    data_out[5]=(hi>>8)&0xff;
    data_out[6]=(hi>>16)&0xff;
    data_out[7]=(hi>>24)&0xff;
    
    return data_out + 8;
}


static inline unsigned char*
read_var_length (unsigned char* p, ogg_int64_t* num) {
  int shift        = 0;
  ogg_int64_t byte = 0;

  *num = 0;
  do {
    byte = (ogg_int64_t)(*p);
    *num |= ((byte & 0x7f) << shift);
    shift += 7;
    p++;
  } while ((byte & 0x80) != 0x80);
  return p;
}

static inline unsigned char*
write_var_length (unsigned char* p, const unsigned char* limit, const ogg_int64_t n)
{
#if DEBUG
  unsigned char* before_p = p;
#endif
  ogg_int64_t k = n;
  do {
    unsigned char b = (unsigned char)(k & 0x7f);
    k >>= 7;
    if (k == 0) {
      // Last byte, add terminating bit.
      b |= 0x80;
    }
    *p = b;
    p++;
  } while (k && p < limit);

#if DEBUG
  ogg_int64_t t;
  read_var_length (before_p, &t);
  assert (t == n);
#endif

  return p;
}

static inline int bits_required (ogg_int64_t n) 
{
  int count = 0;
  while (n) {
    n = n >> 1;
    count++;
  }
  return count;
}

static inline int bytes_required (ogg_int64_t n) 
{
  int bits = bits_required(n);
  int bytes = bits / 7;
  return bytes + (((bits % 7) != 0 || bits == 0) ? 1 : 0);
}

static ogg_int64_t compressed_length (const KeyFrameInfo* k, ogg_int64_t num_keys) 
{
  ogg_int64_t length  = 0;
  unsigned int i      = 1;
  
  length = bytes_required(k[0].offset) + bytes_required(k[0].time_ms);
  for (i=1; i < num_keys; ++i) 
  {
    ogg_int64_t off_diff = k[i].offset - k[i-1].offset;
    ogg_int64_t time_diff = k[i].time_ms - k[i-1].time_ms;
    length += bytes_required(off_diff) + bytes_required(time_diff);
  }
  return length;
}


OggSkeleton* oggskel_new () 
{
  OggSkeleton* skeleton = NULL;
  
  skeleton = _ogg_calloc (1, sizeof (OggSkeleton));
  if (skeleton == NULL) 
  {
    return NULL;
  }
  
  skeleton->track_vect = oggskel_vect_new ();
  if (skeleton->track_vect == NULL)
  {
    _ogg_free (skeleton);
    return NULL;
  }
  
  skeleton->indexing  = 0;
  skeleton->finished  = 0;
  skeleton->state     = SKELETON_INITIALISED;
  skeleton->packetno  = 0;
  
  return skeleton;
}

OggSkeletonError oggskel_destroy (OggSkeleton* skeleton) 
{
  if (skeleton == NULL) 
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  oggskel_vect_destroy (skeleton->track_vect);
  _ogg_free (skeleton);

  return SKELETON_ERR_OK;
}

static int encode_fishead (OggSkeleton *skeleton, ogg_packet *op)
{  
  size_t          fishead_size  = 0;
  unsigned char * packet        = NULL;
  
  if (skeleton == NULL) 
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if (op == NULL)
  {
    return SKELETON_ERR_BAD_PACKET;
  }

  if (skeleton->indexing)
  {
    /* indexing is enabled */
    skeleton->fishead.ver_maj = SKELETON_VERSION_MAJOR;
    skeleton->fishead.ver_min = SKELETON_VERSION_MINOR;
    fishead_size              = FISHEAD_3_2_SIZE;
  }
  else
  {
    /* indexing is not enabled thus creating version 3.0 skeleton bitstream */
    skeleton->fishead.ver_maj = 3;
    skeleton->fishead.ver_min = 0;
    fishead_size              = FISHEAD_3_0_SIZE;
  }

  op->packet = _ogg_calloc (fishead_size, sizeof (unsigned char));
  
  if (op->packet  == NULL)
  {
    return SKELETON_ERR_OUT_OF_MEMORY;
  }
  
  memcpy (op->packet, FISHEAD_MAGIC, FISHEAD_MAGIC_LEN);
  packet = write16le (op->packet+FISHEAD_MAGIC_LEN, skeleton->fishead.ver_maj);
  packet = write16le (packet, skeleton->fishead.ver_min);
  packet = write64le (packet, skeleton->fishead.ptime_num);
  packet = write64le (packet, skeleton->fishead.ptime_denum);
  packet = write64le (packet, skeleton->fishead.btime_num);
  packet = write64le (packet, skeleton->fishead.btime_denum);
  memcpy (packet, skeleton->fishead.UTC, sizeof (skeleton->fishead.UTC));

  if (skeleton->indexing)
  {
    packet += sizeof (skeleton->fishead.UTC);
    packet = write64le (packet, skeleton->fishead.first_sample_num);
    packet = write64le (packet, skeleton->fishead.first_sample_denum);
    packet = write64le (packet, skeleton->fishead.last_sample_num);
    packet = write64le (packet, skeleton->fishead.last_sample_denum);
    packet = write64le (packet, skeleton->fishead.segment_len);
    write64le (packet, skeleton->fishead.nh_offset);
  }

  op->b_o_s       = 1;
  op->e_o_s       = 0;
  op->bytes       = fishead_size;
  op->granulepos  = 0;
  op->packetno    = skeleton->packetno;
  
  return 1;
}

static int encode_fisbone (const FisBone *fisbone, ogg_int64_t packetno, ogg_packet *op)
{
  size_t          bone_size = FISBONE_SIZE;
  unsigned char * packet    = NULL;
  
  if (fisbone == NULL) 
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if (op == NULL)
  {
    return SKELETON_ERR_BAD_PACKET;
  }
  
  /* check whether the mandatory Content-type is set for the track */
  if (!fisbone->msg_fields || !strstr (fisbone->msg_fields, "Content-type:"))
  {
    return SKELETON_ERR_NO_CONTENT_TYPE;
  }
  
  bone_size += strlen (fisbone->msg_fields);
  
  op->packet = _ogg_calloc (bone_size, sizeof (unsigned char));
  if (op->packet == NULL)
  {
    return SKELETON_ERR_OUT_OF_MEMORY;
  }

  memcpy (op->packet, FISBONE_MAGIC, FISBONE_MAGIC_LEN);
  packet = write32le (op->packet+FISBONE_MAGIC_LEN, fisbone->msg_header_offset);
  packet = write32le (packet, fisbone->serial_no);
  packet = write32le (packet, fisbone->num_headers);
  packet = write64le (packet, fisbone->granule_num);
  packet = write64le (packet, fisbone->granule_denum);
  packet = write64le (packet, fisbone->start_granule);
  packet = write32le (packet, fisbone->preroll);
  *(packet) = fisbone->granule_shift;
  memcpy (packet+FISBONE_SIZE, fisbone->msg_fields, bone_size - FISBONE_SIZE);

  op->b_o_s       = 0;
  op->e_o_s       = 0;
  op->bytes       = bone_size;
  op->granulepos  = -1;
  op->packetno    = packetno;

  return 1;
}

static int encode_skeleton_eos (ogg_int64_t packetno, ogg_packet *op)
{
  if (op == NULL)
  {
    return SKELETON_ERR_BAD_PACKET;
  }

  op->b_o_s       = 0;
  op->e_o_s       = 1;
  op->bytes       = 0;
  op->granulepos  = 0;
  op->packetno    = packetno;
  
  return 0;
}

static int encode_index (const Index *index, ogg_int64_t packetno, ogg_packet *op)
{
  size_t          index_size  = INDEX_KEYPOINT_OFFSET, i = 0;
  unsigned char * p           = NULL;
  unsigned char * limit       = NULL;
  ogg_int64_t     prev_offset = 0, prev_time = 0;  
  
  if (index == NULL) 
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if (op == NULL)
  {
    return SKELETON_ERR_BAD_PACKET;
  }
  
  /* adjust the size of the packet with the number of keypoints */
  index_size += compressed_length (index->keypoints, index->num_keys);

  /* allocate packet */
  op->packet = _ogg_calloc (index_size, sizeof(unsigned char));
  if (op->packet == NULL)
  {
    return SKELETON_ERR_OUT_OF_MEMORY;
  }
  
  /* write out the index packet's header */
  memcpy (op->packet, INDEX_MAGIC, INDEX_MAGIC_LEN);
  p = write32le (op->packet+INDEX_MAGIC_LEN, index->serial_no);
  p = write64le (p, index->num_keys);
  p = write64le (p, index->ptime_denum);
  
  limit = op->packet + index_size;
  /* write out the keypoint pairs */
  for (i = 0; i < index->num_keys; ++i)
  {
    ogg_int64_t     off_diff, time_diff;
    unsigned char * expected  = NULL;
    KeyFrameInfo    k         = index->keypoints[i];
    
    off_diff  = k.offset - prev_offset;
    time_diff = k.time_ms - prev_time;
    
    expected = p + bytes_required (off_diff);
    p = write_var_length (p, limit, off_diff);
    assert (p == expected);

    expected = p + bytes_required (time_diff);
    p = write_var_length (p, limit, time_diff);
    assert (p == expected);

    prev_offset = k.offset;
    prev_time   = k.time_ms;
  }
  
  /* set various properties of the packet */
  op->b_o_s       = 0;
  op->e_o_s       = 0;
  op->bytes       = index_size;
  op->granulepos  = -1;
  op->packetno    = packetno;
  
  return 1;
}

int oggskel_encode_header (OggSkeleton* skeleton, ogg_packet *op)
{
  int ret = SKELETON_ERR_GENERIC;
  
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;    
  }
  
  if (op == NULL)
  {
    return SKELETON_ERR_BAD_PACKET;
  }
  
  switch (skeleton->state)
  {
    case SKELETON_INITIALISED:
      ret = encode_fishead (skeleton, op);
      skeleton->state = SKELETON_HEAD;
      break;
    case SKELETON_HEAD:
      ret = SKELETON_ERR_GENERIC;
      if (skeleton->track_vect->size > skeleton->packetno)
      {
        FisBone *bone = skeleton->track_vect->tracks[skeleton->packetno].bone;
        ret = encode_fisbone (bone, ++skeleton->packetno, op);
      }
      else
      {
        /* no packet was produced + no more packets remain */
        ret = encode_skeleton_eos (++skeleton->packetno, op);
        skeleton->state = SKELETON_EOS;
      }
      break;
    default:
      ret = SKELETON_ERR_GENERIC;
  }

  return ret;
}

int oggskel_encode_add_stream (OggSkeleton* skeleton, ogg_int32_t serial_no)
{
  FisBone *bone = NULL;
  int ret = -1;
  
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  bone = _ogg_calloc (1, sizeof (FisBone));
  if (bone == NULL)
  {
    return SKELETON_ERR_OUT_OF_MEMORY;
  }
  
  ret = oggskel_vect_add_bone (skeleton->track_vect, bone, serial_no);
  if (ret != 1)
  {
    _ogg_free (bone);
    return ret;
  } 
  
  return SKELETON_ERR_OK;
}


static int decode_fishead (OggSkeleton *skeleton, 
                           const ogg_packet *op) 
{
  ogg_uint32_t version;
  unsigned char* pk = NULL;
  
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if (op == NULL || op->packet == NULL)
  {
    return SKELETON_ERR_BAD_PACKET;
  }

  pk = op->packet + FISHEAD_MAGIC_LEN;
  pk = extract_uint16 (pk, &skeleton->fishead.ver_maj);
  pk = extract_uint16 (pk, &skeleton->fishead.ver_min); 

  version = SKELETON_VERSION (skeleton->fishead.ver_maj, 
                              skeleton->fishead.ver_min);

  /* check whether we can decode this skeleton bitstream */
  if (version > SKELETON_VERSION(SKELETON_VERSION_MAJOR, SKELETON_VERSION_MINOR))
  {
    return SKELETON_ERR_UNSUPPORTED_VERSION;
  }

  /* ensure that the ogg packet's size is either 64, 80 or 112 bytes! */
  if ((version == SKELETON_VERSION(4,0)) && (op->bytes != FISHEAD_4_0_SIZE)) {
    return SKELETON_ERR_MALICIOUS_FISHEAD;
  } else if (skeleton->fishead.ver_maj == 3) {
    if ((skeleton->fishead.ver_min >= 2) && (op->bytes != FISHEAD_3_2_SIZE))
      return SKELETON_ERR_MALICIOUS_FISHEAD;
    else if ((skeleton->fishead.ver_min == 0) && (op->bytes != FISHEAD_3_0_SIZE))
      return SKELETON_ERR_MALICIOUS_FISHEAD;
  }
    
  pk = extract_int64 (pk, &skeleton->fishead.ptime_num);
  pk = extract_int64 (pk, &skeleton->fishead.ptime_denum); 
  pk = extract_int64 (pk, &skeleton->fishead.btime_num);
  pk = extract_int64 (pk, &skeleton->fishead.btime_denum);
  memcpy (skeleton->fishead.UTC, pk, sizeof (skeleton->fishead.UTC));
  
  if (version >= SKELETON_VERSION(3,2)) 
  {
    
    pk += sizeof (skeleton->fishead.UTC);
    if (skeleton->fishead.ver_maj == 3) {
      pk = extract_int64 (pk, &skeleton->fishead.first_sample_num);
      pk = extract_int64 (pk, &skeleton->fishead.first_sample_denum);
      pk = extract_int64 (pk, &skeleton->fishead.last_sample_num);
      pk = extract_int64 (pk, &skeleton->fishead.last_sample_denum);
    }

    pk = extract_int64 (pk, &skeleton->fishead.segment_len);
    extract_int64 (pk, &skeleton->fishead.nh_offset);
  }

  return 1;
}

static int decode_fisbone (OggSkeleton *skeleton, 
                           const ogg_packet *op) 
{
  FisBone       * current_bone = NULL;
  int             ret = -1;    
  unsigned char * pk = NULL;
  
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if (op == NULL || op->packet == NULL)
  {
    return SKELETON_ERR_BAD_PACKET;
  }
  
  if (op->bytes < FISBONE_SIZE)
  {
    return SKELETON_ERR_MALICIOUS_FISBONE;
  }
  
  current_bone = _ogg_calloc (1, sizeof(FisBone));
  if (current_bone == NULL)
  {
    return SKELETON_ERR_OUT_OF_MEMORY;
  }
  
  pk = op->packet + FISHEAD_MAGIC_LEN;
  pk = extract_uint32 (pk, &current_bone->msg_header_offset);
  pk = extract_int32 (pk, &current_bone->serial_no);
  pk = extract_uint32 (pk, &current_bone->num_headers);
  pk = extract_int64 (pk, &current_bone->granule_num);
  pk = extract_int64 (pk, &current_bone->granule_denum);
  pk = extract_int64 (pk, &current_bone->start_granule);
  pk = extract_uint32 (pk, &current_bone->preroll);
  current_bone->granule_shift = *(pk);
  current_bone->msg_fields    = _ogg_calloc (op->bytes-FISBONE_SIZE, sizeof(char));
  if (current_bone->msg_fields == NULL)
  {
    _ogg_free (current_bone);
    return SKELETON_ERR_OUT_OF_MEMORY;
  }

  memcpy (current_bone->msg_fields, op->packet + FISBONE_SIZE, (op->bytes - FISBONE_SIZE));

  ret = 
    oggskel_vect_add_bone (skeleton->track_vect, current_bone, 
                           current_bone->serial_no);
  
  if (ret < 0)
    _ogg_free (current_bone);
    
  return ret;
}

static int decode_index (OggSkeleton* skeleton, const ogg_packet *op)
{
  ogg_int64_t i = 0, offset = 0, time = 0, time_mult = 1000;
  Index * current_index       = NULL;
  ogg_int64_t min_packet_size = -1;
  unsigned char *p            = NULL;
  int ret = -1;
  
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }

  if (op == NULL || op->packet == NULL)
  {
    return SKELETON_ERR_BAD_PACKET;
  }
  
  current_index = _ogg_calloc (1, sizeof (Index));
  if (current_index == NULL)
  {
    return SKELETON_ERR_OUT_OF_MEMORY;
  }
  
  p = op->packet + INDEX_MAGIC_LEN;
  p = extract_int32 (p, &current_index->serial_no);
  p = extract_int64 (p, &current_index->num_keys);
  p = extract_int64 (p, &current_index->ptime_denum);  
  
  /* index packet structure changed in version 4.0 */
  if (
      SKELETON_VERSION(skeleton->fishead.ver_maj, skeleton->fishead.ver_min) 
      == SKELETON_VERSION(4,0)
     )
  {
    p = extract_int64 (p, &current_index->first_sample_num);
    p = extract_int64 (p, &current_index->last_sample_num);
  }
  
  /* check whether presentation time denumerator is 0 */
  if (current_index->ptime_denum == 0)
  {
    ret = SKELETON_ERR_DENUM_ZERO;
    goto fin;
  }
  
  min_packet_size = 
    INDEX_KEYPOINT_OFFSET + current_index->num_keys * MIN_KEY_POINT_SIZE;
  if (min_packet_size > op->bytes)
  {
    ret = SKELETON_ERR_MALICIOUS_INDEX;
    goto fin;
  }

  current_index->keypoints = 
    _ogg_calloc (current_index->num_keys, sizeof (KeyFrameInfo));

  if (current_index->keypoints == NULL)
  {
    ret = SKELETON_ERR_OUT_OF_MEMORY;
    goto fin;
  }
  
  /* read in keypoints */
  for (i = 0; i < current_index->num_keys; ++i)
  {
    ogg_int64_t offset_delta = 0, time_delta = 0;
    
    p = read_var_length (p, &offset_delta);
    offset += offset_delta;

    p = read_var_length (p, &time_delta);
    time += time_delta;
    
    /* store (offset, time_ms) pair in keypoints array */
    current_index->keypoints[i].offset = offset;
    current_index->keypoints[i].time_ms = 
        (time * time_mult) / current_index->ptime_denum;
  }
  
  /* add index structure to the trackInfo struct */
  ret = 
    oggskel_vect_add_index (skeleton->track_vect, current_index, 
                            current_index->serial_no);

fin:  
  if (ret < 0)
    _ogg_free (current_index);
  
  return ret;
}

int oggskel_decode_header (OggSkeleton* skeleton, const ogg_packet* op)
{
  int ret = -1;
  
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }

  if (op == NULL)
  {
    return SKELETON_ERR_BAD_PACKET;
  }
  
  /* we've got the skeleton EOS packet - presumably */
  if (op->e_o_s)
  {
    if (skeleton->state != SKELETON_HEAD)
      return -1;
      
    skeleton->finished = 1;
    skeleton->state = SKELETON_EOS;
    /* 
     * EOS packet does not comply with the Skeleton specification:
     * "The skeleton eos page MUST contain one packet of length zero"
     */
    if (op->bytes != 0)
    {
      return SKELETON_WARN_EOS_NOT_EMTPY;
    }
    
    return SKELETON_ERR_OK;
  }
  
  /* identify the type of skeleton packet */
  if (memcmp (op->packet, FISHEAD_MAGIC, FISHEAD_MAGIC_LEN) == 0) 
  { 
    /* assure that the fishead packet is BOS */
    ret = decode_fishead (skeleton, op);
    if (ret)
    {
      if (!op->b_o_s)
        ret = SKELETON_WARN_FISHEAD_NOT_BOS;
      else if (skeleton->state != SKELETON_INITIALISED)
        ret = SKELETON_WARN_BAD_ORDER;
      
      skeleton->state = SKELETON_HEAD;
    }
  }
  else if (memcmp (op->packet, FISBONE_MAGIC, FISBONE_MAGIC_LEN) == 0)
  {
    ret = decode_fisbone (skeleton, op);
    
    if (ret && skeleton->state != SKELETON_HEAD)
      ret = SKELETON_WARN_BAD_ORDER;
  }
  else if (memcmp (op->packet, INDEX_MAGIC, INDEX_MAGIC_LEN) == 0)
  {
    skeleton->indexing = 1;
    ret = decode_index (skeleton, op);
    if (ret && skeleton->state != SKELETON_HEAD)
      ret = SKELETON_WARN_BAD_ORDER;
  }
  else
  {
    /* unknown packet type */
    ret = SKELETON_ERR_GENERIC;
  }

  return ret;
}
