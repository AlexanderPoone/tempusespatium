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
 
#ifndef __OGG_SKELETON_CONSTANTS_H__
#define __OGG_SKELETON_CONSTANTS_H__

/** @file
 * General constants used by libskeleton
 */
 
/**
 * Definition of error return values
 */
typedef enum _OggSkeletonError {
  SKELETON_ERR_OK                 = 0,
  SKELETON_ERR_GENERIC            = -1,
  SKELETON_ERR_BAD_SKELETON       = -2,
  SKELETON_ERR_VERSION            = -3,
  SKELETON_ERR_OUT_OF_MEMORY      = -4,
  SKELETON_ERR_EOS_AWAITING       = -5,
  SKELETON_WARN_EOS_NOT_EMTPY     = -6,
  SKELETON_ERR_MALICIOUS_FISBONE  = -7,
  SKELETON_ERR_BAD_SERIAL_NO      = -8,
  SKELETON_ERR_DENUM_ZERO         = -9,
  SKELETON_ERR_MALICIOUS_INDEX    = -10,
  SKELETON_ERR_UNSUPPORTED_VERSION= -11,
  SKELETON_WARN_FISHEAD_NOT_BOS   = -12,
  SKELETON_ERR_BAD_PACKET         = -13,
  SKELETON_WARN_BAD_ORDER         = -14,
  SKELETON_ERR_NO_CONTENT_TYPE    = -15,
  SKELETON_ERR_BAD_ARG            = -16,
  SKELETON_ERR_OUT_OF_RANGE       = -17,
  SKELETON_ERR_MALICIOUS_FISHEAD  = -18,
  SKELETON_ERR_NO_INDEX           = -19  
} OggSkeletonError;

#endif /* __OGG_SKELETON_CONSTANTS_H__ */
