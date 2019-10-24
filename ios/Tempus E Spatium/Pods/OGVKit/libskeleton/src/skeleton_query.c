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
 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "skeleton_macros.h"
#include "skeleton_private.h"
#include "skeleton_vector.h"

#ifdef _MSC_VER
#define inline __inline
#endif

static inline OggSkeletonError
getter_error_check (const OggSkeleton *skeleton, void *input)
{
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if (skeleton->finished == 0)
  {
    return SKELETON_ERR_EOS_AWAITING;
  }
  
  if (input == NULL)
  {
    return SKELETON_ERR_BAD_ARG;
  }
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_ver_maj (const OggSkeleton *skeleton, ogg_uint16_t *ver_maj)
{
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, ver_maj)) < 0)
    return ret;

  *ver_maj = skeleton->fishead.ver_maj;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_ver_min (const OggSkeleton *skeleton, ogg_uint16_t *ver_min)
{
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, ver_min)) < 0)
    return ret;
  
  *ver_min = skeleton->fishead.ver_min;
  
  return SKELETON_ERR_OK;  
}

OggSkeletonError 
oggskel_get_ptime_num (const OggSkeleton *skeleton, ogg_int64_t *pres_num)
{
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, pres_num)) < 0)
    return ret;
  
  *pres_num = skeleton->fishead.ptime_num;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_set_ptime_num (OggSkeleton *skeleton, ogg_int64_t pres_num)
{
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }

  skeleton->fishead.ptime_num = pres_num;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_ptime_denum (const OggSkeleton *skeleton, ogg_int64_t *pres_denum)
{
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, pres_denum)) < 0)
    return ret;

  *pres_denum = skeleton->fishead.ptime_denum;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_set_ptime_denum (OggSkeleton *skeleton, ogg_int64_t pres_denum)
{
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }

  skeleton->fishead.ptime_denum = pres_denum;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_btime_num (const OggSkeleton *skeleton, ogg_int64_t *btime_num)
{
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, btime_num)) < 0)
    return ret;
    
  *btime_num = skeleton->fishead.btime_num;

  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_set_btime_num (OggSkeleton *skeleton, ogg_int64_t btime_num)
{
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }

  skeleton->fishead.btime_num = btime_num;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_btime_denum (const OggSkeleton *skeleton, ogg_int64_t *btime_denum)
{
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, btime_denum)) < 0)
    return ret;
    
  *btime_denum = skeleton->fishead.btime_denum;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_set_btime_denum (OggSkeleton *skeleton, ogg_int64_t btime_denum)
{
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  skeleton->fishead.btime_denum = btime_denum;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_utc (const OggSkeleton *skeleton, char **UTC)
{
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }

  if (skeleton->finished == 0)
  {
    return SKELETON_ERR_EOS_AWAITING;
  }

  *UTC = _ogg_calloc (20, sizeof (char));
  if (*UTC == NULL)
  {
    return SKELETON_ERR_OUT_OF_MEMORY;
  }
  
  memcpy (*UTC, skeleton->fishead.UTC, 20);

  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_set_utc (OggSkeleton *skeleton, const char *UTC)
{
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if (strlen (UTC) != 20) 
  {
    return SKELETON_ERR_BAD_ARG;
  }
  
  memcpy (skeleton->fishead.UTC, UTC, 20);
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_segment_len (const OggSkeleton *skeleton, ogg_int64_t *len)
{
  OggSkeletonError ret  = -1;
  ogg_uint32_t version  = 0;
  
  if ((ret = getter_error_check (skeleton, len)) < 0)
    return ret;
  
  version = SKELETON_VERSION (skeleton->fishead.ver_maj, skeleton->fishead.ver_min);
  if (version < SKELETON_VERSION(3,2))
  {
    return -1;
  }
    
  *len = skeleton->fishead.segment_len;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_non_header_offset (const OggSkeleton *skeleton, ogg_int64_t *offset)
{
  OggSkeletonError ret  = -1;
  ogg_uint32_t version  = 0;
  
  if ((ret = getter_error_check (skeleton, offset)) < 0)
    return ret;
  
  version = SKELETON_VERSION (skeleton->fishead.ver_maj, skeleton->fishead.ver_min);
  if (version < SKELETON_VERSION(3,2))
  {
    return -1;
  }
  
  *offset = skeleton->fishead.nh_offset;
  
  return SKELETON_ERR_OK;
}


OggSkeletonError 
oggskel_get_granule_shift (const OggSkeleton *skeleton, ogg_int32_t serial_no, unsigned char *granule_shift)
{
  FisBone *bone = NULL;
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, granule_shift)) < 0)
    return ret;

  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }
  
  *granule_shift = bone->granule_shift;
  return SKELETON_ERR_OK;  
}

OggSkeletonError 
oggskel_set_granule_shift (OggSkeleton *skeleton, ogg_int32_t serial_no, unsigned char granule_shift)
{
  FisBone *bone = NULL;
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }
  
  bone->granule_shift = granule_shift;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_num_headers (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_uint32_t *num_headers)
{
  FisBone *bone = NULL;
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, num_headers)) < 0)
    return ret;

  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }
  
  *num_headers = bone->num_headers;
  
  return SKELETON_ERR_OK; 
}

OggSkeletonError 
oggskel_set_num_headers (OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_uint32_t num_headers)
{
  FisBone *bone = NULL;

  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }
  
  bone->num_headers = num_headers;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_granule_num (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *granule_num)
{
  FisBone *bone = NULL;
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, granule_num)) < 0)
    return ret;

  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }
  
  *granule_num = bone->granule_num;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_set_granule_num (OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t granule_num)
{
  FisBone *bone = NULL;
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }

  bone->granule_num = granule_num;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_granule_denum (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *granule_denum)
{
  FisBone *bone;
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, granule_denum)) < 0)
    return ret;

  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }
  
  *granule_denum = bone->granule_denum;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_set_granule_denum (OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t granule_denum)
{
  FisBone *bone = NULL;
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }

  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }

  bone->granule_denum = granule_denum;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_start_granule (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *start_granule)
{
  FisBone *bone = NULL;
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, start_granule)) < 0)
    return ret;
 
  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }
  
  *start_granule = bone->start_granule;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_set_start_granule (OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t start_granule)
{
  FisBone *bone = NULL;
  
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }

  bone->start_granule = start_granule;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_preroll (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_uint32_t *preroll)
{
  FisBone *bone = NULL;
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, preroll)) < 0)
    return ret;

  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }
  
  *preroll = bone->preroll;
  
  return SKELETON_ERR_OK;  
}

OggSkeletonError 
oggskel_set_preroll (OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_uint32_t preroll)
{
  FisBone *bone = NULL;
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }
  
  bone->preroll = preroll;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_msg_header (const OggSkeleton *skeleton, ogg_int32_t serial_no, char **msg_header)
{
  FisBone *bone        = NULL;
  size_t   fields_size = 0;
  
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }
  
  fields_size = strlen (bone->msg_fields);
  *msg_header = _ogg_calloc (fields_size, sizeof (char));
  if (*msg_header == NULL)
    return SKELETON_ERR_OUT_OF_MEMORY;
  
  memcpy (*msg_header, bone->msg_fields, fields_size);
  
  return SKELETON_ERR_OK; 
}

OggSkeletonError 
oggskel_set_msg_header (OggSkeleton *skeleton, ogg_int32_t serial_no, const char *msg_header)
{
  FisBone *bone = NULL;
  size_t len = 0;
  
  if (skeleton == NULL)
  {
    return SKELETON_ERR_BAD_SKELETON;
  }
  
  if (msg_header == NULL) 
  {
    return SKELETON_ERR_BAD_ARG;
  }
  
  if ((bone = oggskel_vect_get_bone (skeleton->track_vect, serial_no)) == NULL)
  {
    return SKELETON_ERR_BAD_SERIAL_NO;
  }
  
  len = strlen (msg_header)+1;
  if (bone->msg_fields != NULL && strlen(bone->msg_fields) != len)
  {
    _ogg_free (bone->msg_fields);
    bone->msg_fields = NULL;
  }
  bone->msg_fields = _ogg_calloc (len, sizeof (char));
  if (bone->msg_fields == NULL)
  {
    return SKELETON_ERR_OUT_OF_MEMORY;
  }
  
  memcpy (bone->msg_fields, msg_header, len);
  
  return SKELETON_ERR_OK;
}

static KeyFrameInfo*
get_seek_keypoint (Index *index, ogg_int64_t target)
{
  ogg_int64_t start = 0;
  ogg_int64_t end   = 0;
  
  if (index == NULL)
    return NULL;
  
  /* binary search for the last keypoint with less time than target */
  end = index->num_keys - 1;
  while (end > start)
  {
    ogg_int64_t mid = (start + end + 1) >> 1;
    if (index->keypoints[mid].time_ms == target)
    {
      start = mid;
      break;
    }
    else if (index->keypoints[mid].time_ms < target)
    {
      start = mid;
    }
    else
    {
      end = mid - 1;
    }
  }
  
  return &(index->keypoints[start]);
}

OggSkeletonError 
oggskel_get_keypoint_offset (const OggSkeleton *skeleton, 
                             ogg_int32_t *serial_no,
                             size_t       len, 
                             ogg_int64_t  time_ms, 
                             ogg_int64_t *offset)
{
  Index             * index       = NULL;
  OggSkeletonError    ret         = -1;
  size_t              i           = 0;
  ogg_int64_t         min_offset  = SKELETON_TYPE_MAX(ogg_int64_t);
  
  if ((ret = getter_error_check (skeleton, offset)) < 0)
    return ret;
  
  if (!skeleton->indexing)
  {
    return SKELETON_ERR_NO_INDEX;
  }

  if (serial_no == NULL || len == 0)
  {
    return -1;
  }
    
  if (skeleton->fishead.ver_maj == 3)
  {
    /* check whether the requested time is indexed at all! */
    if
    (
      (skeleton->fishead.last_sample_num < time_ms)
      ||
      (time_ms < skeleton->fishead.first_sample_num)
    )
    {
      return SKELETON_ERR_OUT_OF_RANGE;
    }
  }
  
  /* find the nearest keypoint that is before or at the given time_ms */
  for (i = 0; i < len; ++i)
  {
    KeyFrameInfo *kf = NULL;
    
    /* TODO: what if one track is not indexed? 
      is it possible at all that we have partially indexed ogg? :) */
    if ((index = oggskel_vect_get_index (skeleton->track_vect, serial_no[i])) == NULL)
    {
      /* SKELETON_ERR_NO_INDEX */
      return SKELETON_ERR_BAD_SERIAL_NO;
    }

    if (skeleton->fishead.ver_maj == 4)
    {
      /* check whether the requested time is indexed at all! */
      if
      (
        (index->last_sample_num < time_ms)
        ||
        (time_ms < index->first_sample_num)
      )
      {
        return SKELETON_ERR_OUT_OF_RANGE;
      }
    }
    
    kf = get_seek_keypoint (index, time_ms);
    
    if (!kf || kf->offset > min_offset)
      continue;
      
    min_offset = kf->offset;
  }
  
  /* something went really wrong! */
  if (min_offset == SKELETON_TYPE_MAX(ogg_int64_t))
    return -1;
    
  *offset = min_offset;
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_first_sample_num (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *first_sample_num)
{
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, first_sample_num)) < 0)
    return ret;

  if (skeleton->fishead.ver_maj < 4)
  {
    *first_sample_num = skeleton->fishead.first_sample_num;
  }
  else
  {
    Index *index = NULL;
    if ((index = oggskel_vect_get_index (skeleton->track_vect, serial_no)) == NULL)
    {
      return SKELETON_ERR_BAD_SERIAL_NO;
    }
    
    *first_sample_num = index->first_sample_num;
  }
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_first_sample_denum (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *first_sample_denum)
{
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, first_sample_denum)) < 0)
    return ret;

  if (skeleton->fishead.ver_maj < 4)
  {
    *first_sample_denum = skeleton->fishead.first_sample_denum;
  }
  else
  {
    Index *index = NULL;
    if ((index = oggskel_vect_get_index (skeleton->track_vect, serial_no)) == NULL)
    {
      return SKELETON_ERR_BAD_SERIAL_NO;
    }
    
    *first_sample_denum = index->ptime_denum;
  }
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_last_sample_num (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *last_sample_num)
{
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, last_sample_num)) < 0)
    return ret;

  if (skeleton->fishead.ver_maj < 4)
  {
    *last_sample_num = skeleton->fishead.last_sample_num;
  }
  else
  {
    Index *index = NULL;
    if ((index = oggskel_vect_get_index (skeleton->track_vect, serial_no)) == NULL)
    {
      return SKELETON_ERR_BAD_SERIAL_NO;
    }
    
    *last_sample_num = index->last_sample_num;
  }
  
  return SKELETON_ERR_OK;
}

OggSkeletonError 
oggskel_get_last_sample_denum (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *last_sample_denum)
{
  OggSkeletonError ret = -1;
  
  if ((ret = getter_error_check (skeleton, last_sample_denum)) < 0)
    return ret;

  if (skeleton->fishead.ver_maj < 4)
  {
    *last_sample_denum = skeleton->fishead.last_sample_denum;
  }
  else
  {
    Index *index = NULL;
    if ((index = oggskel_vect_get_index (skeleton->track_vect, serial_no)) == NULL)
    {
      return SKELETON_ERR_BAD_SERIAL_NO;
    }
    
    *last_sample_denum = index->ptime_denum;
  }
  
  return SKELETON_ERR_OK;
}
