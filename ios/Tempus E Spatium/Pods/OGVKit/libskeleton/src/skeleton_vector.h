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

#ifndef __OGG_SKELETON_VECTOR_H__
#define __OGG_SKELETON_VECTOR_H__

#include <ogg/os_types.h>
#include "skeleton_private.h"

typedef struct _TrackInfo {
  ogg_int32_t     serial_no;
  FisBone       * bone;
  Index         * index;
} TrackInfo;

struct _TrackVect {
  size_t        size;
  TrackInfo   * tracks;
};

TrackVect* oggskel_vect_new (void);

void oggskel_vect_destroy (TrackVect *vect);

int oggskel_vect_add_bone (TrackVect *vect, FisBone *bone, ogg_int32_t serial_no);
FisBone* oggskel_vect_get_bone (const TrackVect *vect, ogg_int32_t serial_no);

int oggskel_vect_add_index (TrackVect *vect, Index *bone, ogg_int32_t serial_no);
Index* oggskel_vect_get_index (const TrackVect *vect, ogg_int32_t serial_no);

#endif /* __OGG_SKELETON_VECTOR_H__ */
