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
 
#ifndef __OGG_SKELETON_QUERY_H__
#define __OGG_SKELETON_QUERY_H__

#ifdef __cplusplus
extern "C" {
#endif

OggSkeletonError oggskel_get_ver_maj (const OggSkeleton *skeleton, ogg_uint16_t *ver_maj);
OggSkeletonError oggskel_get_ver_min (const OggSkeleton *skeleton, ogg_uint16_t *ver_min);

OggSkeletonError oggskel_get_ptime_num (const OggSkeleton *skeleton, ogg_int64_t *pres_num);
OggSkeletonError oggskel_set_ptime_num (OggSkeleton *skeleton, ogg_int64_t pres_num);

OggSkeletonError oggskel_get_ptime_denum (const OggSkeleton *skeleton, ogg_int64_t *pres_denum);
OggSkeletonError oggskel_set_ptime_denum (OggSkeleton *skeleton, ogg_int64_t pres_denum);

OggSkeletonError oggskel_get_btime_num (const OggSkeleton *skeleton, ogg_int64_t *btime_num);
OggSkeletonError oggskel_set_btime_num (OggSkeleton *skeleton, ogg_int64_t btime_num);

OggSkeletonError oggskel_get_btime_denum (const OggSkeleton *skeleton, ogg_int64_t *btime_denum);
OggSkeletonError oggskel_set_btime_denum (OggSkeleton *skeleton, ogg_int64_t btime_denum);

OggSkeletonError oggskel_get_utc (const OggSkeleton *skeleton, char **UTC);
OggSkeletonError oggskel_set_utc (OggSkeleton *skeleton, const char *UTC);

OggSkeletonError oggskel_get_segment_len (const OggSkeleton *skeleton, ogg_int64_t *len);
OggSkeletonError oggskel_get_non_header_offset (const OggSkeleton *skeleton, ogg_int64_t *offset);

OggSkeletonError oggskel_get_num_headers (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_uint32_t *num_headers);
OggSkeletonError oggskel_set_num_headers (OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_uint32_t num_headers);

OggSkeletonError oggskel_get_granule_num (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *granule_num);
OggSkeletonError oggskel_set_granule_num (OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t granule_num);

OggSkeletonError oggskel_get_granule_denum (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *granule_denum);
OggSkeletonError oggskel_set_granule_denum (OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t granule_denum);

OggSkeletonError oggskel_get_start_granule (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *start_granule);
OggSkeletonError oggskel_set_start_granule (OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t start_granule);

OggSkeletonError oggskel_get_preroll (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_uint32_t *preroll);
OggSkeletonError oggskel_set_preroll (OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_uint32_t preroll);

OggSkeletonError oggskel_get_granule_shift (const OggSkeleton *skeleton, ogg_int32_t serial_no, unsigned char *granule_shift);
OggSkeletonError oggskel_set_granule_shift (OggSkeleton *skeleton, ogg_int32_t serial_no, unsigned char granule_shift);

OggSkeletonError oggskel_get_msg_header (const OggSkeleton *skeleton, ogg_int32_t serial_no, char **msg_header);
OggSkeletonError oggskel_set_msg_header (OggSkeleton *skeleton, ogg_int32_t serial_no, const char *msg_header);

OggSkeletonError oggskel_get_keypoint_offset (const OggSkeleton *skeleton, 
                                              ogg_int32_t *serial_no,
                                              size_t       len,
                                              ogg_int64_t  time_ms, 
                                              ogg_int64_t *offset);

OggSkeletonError oggskel_get_first_sample_num (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *first_sample_num);
OggSkeletonError oggskel_get_first_sample_denum (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *first_sample_denum);
OggSkeletonError oggskel_get_last_sample_num (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *last_sample_num);
OggSkeletonError oggskel_get_last_sample_denum (const OggSkeleton *skeleton, ogg_int32_t serial_no, ogg_int64_t *last_sample_denum);

#ifdef __cplusplus
}
#endif

#endif /* __OGG_SKELETON_QUERY_H__  */
