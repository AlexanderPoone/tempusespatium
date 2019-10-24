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

#include <stdlib.h>
#include "skeleton_vector.h"

static TrackInfo* 
find_track_info (const TrackVect *vect, ogg_int32_t serial_no)
{
  int i = 0;
  for (i = 0; i < vect->size; ++i)
  {
    if (vect->tracks[i].serial_no == serial_no)
      return &(vect->tracks[i]);
  }

  return NULL;
}

static TrackInfo*
get_track_nfo (TrackVect *vect, ogg_int32_t serial_no)
{
  TrackInfo *t_nfo = NULL;
  
  if ((t_nfo = find_track_info (vect, serial_no)) == NULL)
  {
    t_nfo = 
      _ogg_realloc (vect->tracks, sizeof (TrackInfo)*(vect->size+1));
    
    if (t_nfo == NULL)
    {
      return NULL;
    }
    
    vect->tracks = t_nfo;
    vect->size++;

    t_nfo = &(vect->tracks[vect->size - 1]);
    /* initialise the new trackInfo */
    t_nfo->serial_no  = serial_no;
    t_nfo->bone       = NULL;
    t_nfo->index      = NULL;
  }
  
  return t_nfo;
}

static int
del_track_nfo (TrackVect *vect, ogg_int32_t serial_no)
{
  TrackInfo *t_nfo = NULL;
  if ((t_nfo = find_track_info (vect, serial_no)) == NULL)
    return -1;
  
  if (t_nfo->bone != NULL)
  {
    if (t_nfo->bone->msg_fields != NULL)
      _ogg_free (t_nfo->bone->msg_fields);

    _ogg_free (t_nfo->bone);
  }
  if (t_nfo->index != NULL)
  {
    if (t_nfo->index->keypoints != NULL)
      _ogg_free (t_nfo->index->keypoints);
      
    _ogg_free (t_nfo->index);
  }  
  
  return 0;
}

TrackVect* oggskel_vect_new ()
{
  TrackVect *vect;
  
  vect = _ogg_calloc (1, sizeof (TrackVect));
  if (vect == NULL)
    return NULL;
  
  /* initialise the vector with 2 empty tracks 
  vect->tracks = _ogg_calloc (sizeof (TrackInfo), 2);
  if (vect->tracks == NULL)
  {
    _ogg_free (vect);
    return NULL;
  }
  */
  
  return vect;
}

void oggskel_vect_destroy (TrackVect *vect)
{
  int i = 0;
  
  if (vect == NULL)
  {
    return;
  }
  
  if (vect->tracks)
  {
    for (i = 0; i < vect->size; ++i)
    {
      if (vect->tracks[i].bone != NULL)
      {
        if (vect->tracks[i].bone->msg_fields != NULL)
          _ogg_free (vect->tracks[i].bone->msg_fields);
        _ogg_free (vect->tracks[i].bone);
      }
      if (vect->tracks[i].index != NULL)
      {
        if (vect->tracks[i].index->keypoints != NULL)
          _ogg_free (vect->tracks[i].index->keypoints);
        
        _ogg_free (vect->tracks[i].index);
      }
    }

    _ogg_free (vect->tracks);
  }
  
  _ogg_free (vect);
}

FisBone* oggskel_vect_get_bone (const TrackVect *vect, ogg_int32_t serial_no)
{
  TrackInfo *t_nfo = NULL;
  
  if (vect == NULL)
  {
    return NULL;
  }
  
  t_nfo = find_track_info (vect, serial_no);
  
  return (t_nfo == NULL) ? NULL : t_nfo->bone;
}

int oggskel_vect_add_bone (TrackVect *vect, FisBone *bone, ogg_int32_t serial_no)
{
  TrackInfo * t_nfo = NULL;
  
  if (vect == NULL)
  {
    return -1;
  }
  
  if (bone == NULL)
  {
    return -1;
  }

  t_nfo = get_track_nfo (vect, serial_no);
  if (t_nfo == NULL)
  {
    return SKELETON_ERR_OUT_OF_MEMORY;
  }
  
  if (t_nfo->bone != NULL)
  {
    /* fisbone info is already added!!! */
    return -1;
  }
  
  t_nfo->bone = bone;
  
  return 1;
}

int oggskel_vect_add_index (TrackVect *vect, Index *index, ogg_int32_t serial_no)
{
  TrackInfo * t_nfo = NULL;
  
  if (vect == NULL)
  {
    return -1;
  }
  
  if (index == NULL)
  {
    return -1;
  }
  
  t_nfo = get_track_nfo (vect, serial_no);
  if (t_nfo == NULL)
  {
    return SKELETON_ERR_OUT_OF_MEMORY;
  }

  if (t_nfo->index != NULL)
  {
    /* index sturct is already set for this track!!! */
    return -1;
  }
  
  t_nfo->index = index;
  
  return 1;
}

Index* oggskel_vect_get_index (const TrackVect *vect, ogg_int32_t serial_no)
{
  TrackInfo *t_nfo = NULL;
  
  if (vect == NULL)
  {
    return NULL;
  }

  t_nfo = find_track_info (vect, serial_no);
  
  return (t_nfo == NULL) ? NULL : t_nfo->index;
}
