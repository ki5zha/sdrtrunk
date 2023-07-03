/*
 * *****************************************************************************
 * Copyright (C) 2014-2023 Dennis Sheirer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * ****************************************************************************
 */

// Generated by jextract

package io.github.dsheirer.source.tuner.sdrplay.api.v3_07;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.StructLayout;
import java.lang.invoke.VarHandle;

/**
 * {@snippet :
 * struct {
 *     unsigned char extRefOutputEn;
 * };
 * }
 */
public class sdrplay_api_Rsp2ParamsT {

    static final StructLayout $struct$LAYOUT = MemoryLayout.structLayout(
        Constants$root.C_CHAR$LAYOUT.withName("extRefOutputEn")
    );
    public static MemoryLayout $LAYOUT() {
        return sdrplay_api_Rsp2ParamsT.$struct$LAYOUT;
    }
    static final VarHandle extRefOutputEn$VH = $struct$LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("extRefOutputEn"));
    public static VarHandle extRefOutputEn$VH() {
        return sdrplay_api_Rsp2ParamsT.extRefOutputEn$VH;
    }
    /**
     * Getter for field:
     * {@snippet :
     * unsigned char extRefOutputEn;
     * }
     */
    public static byte extRefOutputEn$get(MemorySegment seg) {
        return (byte)sdrplay_api_Rsp2ParamsT.extRefOutputEn$VH.get(seg);
    }
    /**
     * Setter for field:
     * {@snippet :
     * unsigned char extRefOutputEn;
     * }
     */
    public static void extRefOutputEn$set(MemorySegment seg, byte x) {
        sdrplay_api_Rsp2ParamsT.extRefOutputEn$VH.set(seg, x);
    }
    public static byte extRefOutputEn$get(MemorySegment seg, long index) {
        return (byte)sdrplay_api_Rsp2ParamsT.extRefOutputEn$VH.get(seg.asSlice(index*sizeof()));
    }
    public static void extRefOutputEn$set(MemorySegment seg, long index, byte x) {
        sdrplay_api_Rsp2ParamsT.extRefOutputEn$VH.set(seg.asSlice(index*sizeof()), x);
    }
    public static long sizeof() { return $LAYOUT().byteSize(); }
    public static MemorySegment allocate(SegmentAllocator allocator) { return allocator.allocate($LAYOUT()); }
    public static MemorySegment allocateArray(long len, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(len, $LAYOUT()));
    }
    public static MemorySegment ofAddress(MemorySegment addr, SegmentScope scope) { return RuntimeHelper.asArray(addr, $LAYOUT(), 1, scope); }
}

