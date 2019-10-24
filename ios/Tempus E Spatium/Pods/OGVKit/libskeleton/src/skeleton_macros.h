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
 
#ifndef __OGG_SKELETON_MACROS_H__
#define __OGG_SKELETON_MACROS_H__

#define SKELETON_VERSION_MAJOR 4
#define SKELETON_VERSION_MINOR 0

#define SKELETON_VERSION(major, minor) (((major)<<16)|(minor))

#define FISHEAD_MAGIC "fishead\0"
#define FISHEAD_MAGIC_LEN 8
#define FISHEAD_3_0_SIZE 64
#define FISHEAD_3_2_SIZE 112
#define FISHEAD_4_0_SIZE 80

#define FISBONE_MAGIC "fisbone\0"
#define FISBONE_MAGIC_LEN 8
#define FISBONE_SIZE 52

#define INDEX_MAGIC "index\0"
#define INDEX_MAGIC_LEN 6
#define INDEX_KEYPOINT_OFFSET 26

// Maximum possible size of one uncompressed keypoint entry in the index. This
// takes into account the maximum possible values for all fields, and the number
// of bytes required to encode their maximum values with variable byte encoding.
#define MAX_KEY_POINT_SIZE 24
 
// Minimum possible size of a compressed keypoint.
#define MIN_KEY_POINT_SIZE 2

/**
 * macros for obtaining a type's max and min values
 * http://www.fefe.de/intof.html 
 */
#define SKELETON_TYPE_HALF_MAX_SIGNED(type) ((type)1 << (sizeof(type)*8-2))
#define SKELETON_TYPE_MAX_SIGNED(type) (SKELETON_TYPE_HALF_MAX_SIGNED(type) - 1 + SKELETON_TYPE_HALF_MAX_SIGNED(type))
#define SKELETON_TYPE_MIN_SIGNED(type) (-1 - SKELETON_TYPE_MAX_SIGNED(type))
#define SKELETON_TYPE_MIN(type) ((type)-1 < 1?SKELETON_TYPE_MIN_SIGNED(type):(type)0)
#define SKELETON_TYPE_MAX(type) ((type)~SKELETON_TYPE_MIN(type))

#endif /* __OGG_SKELETON_MACROS_H__ */
