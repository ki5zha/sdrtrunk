/*
 * *****************************************************************************
 * Copyright (C) 2014-2024 Dennis Sheirer
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

import java.lang.foreign.*;
import java.util.function.*;

import static java.lang.foreign.ValueLayout.*;
import static java.lang.foreign.MemoryLayout.PathElement.*;

/**
 * {@snippet lang=c :
 * struct {
 *     unsigned int firstSampleNum;
 *     int grChanged;
 *     int rfChanged;
 *     int fsChanged;
 *     unsigned int numSamples;
 * }
 * }
 */
public class sdrplay_api_StreamCbParamsT {

    sdrplay_api_StreamCbParamsT() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        sdrplay_api_h.C_INT.withName("firstSampleNum"),
        sdrplay_api_h.C_INT.withName("grChanged"),
        sdrplay_api_h.C_INT.withName("rfChanged"),
        sdrplay_api_h.C_INT.withName("fsChanged"),
        sdrplay_api_h.C_INT.withName("numSamples")
    ).withName("$anon$57:9");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final OfInt firstSampleNum$LAYOUT = (OfInt)$LAYOUT.select(groupElement("firstSampleNum"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned int firstSampleNum
     * }
     */
    public static final OfInt firstSampleNum$layout() {
        return firstSampleNum$LAYOUT;
    }

    private static final long firstSampleNum$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned int firstSampleNum
     * }
     */
    public static final long firstSampleNum$offset() {
        return firstSampleNum$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned int firstSampleNum
     * }
     */
    public static int firstSampleNum(MemorySegment struct) {
        return struct.get(firstSampleNum$LAYOUT, firstSampleNum$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned int firstSampleNum
     * }
     */
    public static void firstSampleNum(MemorySegment struct, int fieldValue) {
        struct.set(firstSampleNum$LAYOUT, firstSampleNum$OFFSET, fieldValue);
    }

    private static final OfInt grChanged$LAYOUT = (OfInt)$LAYOUT.select(groupElement("grChanged"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int grChanged
     * }
     */
    public static final OfInt grChanged$layout() {
        return grChanged$LAYOUT;
    }

    private static final long grChanged$OFFSET = 4;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int grChanged
     * }
     */
    public static final long grChanged$offset() {
        return grChanged$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int grChanged
     * }
     */
    public static int grChanged(MemorySegment struct) {
        return struct.get(grChanged$LAYOUT, grChanged$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int grChanged
     * }
     */
    public static void grChanged(MemorySegment struct, int fieldValue) {
        struct.set(grChanged$LAYOUT, grChanged$OFFSET, fieldValue);
    }

    private static final OfInt rfChanged$LAYOUT = (OfInt)$LAYOUT.select(groupElement("rfChanged"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int rfChanged
     * }
     */
    public static final OfInt rfChanged$layout() {
        return rfChanged$LAYOUT;
    }

    private static final long rfChanged$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int rfChanged
     * }
     */
    public static final long rfChanged$offset() {
        return rfChanged$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int rfChanged
     * }
     */
    public static int rfChanged(MemorySegment struct) {
        return struct.get(rfChanged$LAYOUT, rfChanged$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int rfChanged
     * }
     */
    public static void rfChanged(MemorySegment struct, int fieldValue) {
        struct.set(rfChanged$LAYOUT, rfChanged$OFFSET, fieldValue);
    }

    private static final OfInt fsChanged$LAYOUT = (OfInt)$LAYOUT.select(groupElement("fsChanged"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * int fsChanged
     * }
     */
    public static final OfInt fsChanged$layout() {
        return fsChanged$LAYOUT;
    }

    private static final long fsChanged$OFFSET = 12;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * int fsChanged
     * }
     */
    public static final long fsChanged$offset() {
        return fsChanged$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * int fsChanged
     * }
     */
    public static int fsChanged(MemorySegment struct) {
        return struct.get(fsChanged$LAYOUT, fsChanged$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * int fsChanged
     * }
     */
    public static void fsChanged(MemorySegment struct, int fieldValue) {
        struct.set(fsChanged$LAYOUT, fsChanged$OFFSET, fieldValue);
    }

    private static final OfInt numSamples$LAYOUT = (OfInt)$LAYOUT.select(groupElement("numSamples"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * unsigned int numSamples
     * }
     */
    public static final OfInt numSamples$layout() {
        return numSamples$LAYOUT;
    }

    private static final long numSamples$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * unsigned int numSamples
     * }
     */
    public static final long numSamples$offset() {
        return numSamples$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * unsigned int numSamples
     * }
     */
    public static int numSamples(MemorySegment struct) {
        return struct.get(numSamples$LAYOUT, numSamples$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * unsigned int numSamples
     * }
     */
    public static void numSamples(MemorySegment struct, int fieldValue) {
        struct.set(numSamples$LAYOUT, numSamples$OFFSET, fieldValue);
    }

    /**
     * Obtains a slice of {@code arrayParam} which selects the array element at {@code index}.
     * The returned segment has address {@code arrayParam.address() + index * layout().byteSize()}
     */
    public static MemorySegment asSlice(MemorySegment array, long index) {
        return array.asSlice(layout().byteSize() * index);
    }

    /**
     * The size (in bytes) of this struct
     */
    public static long sizeof() { return layout().byteSize(); }

    /**
     * Allocate a segment of size {@code layout().byteSize()} using {@code allocator}
     */
    public static MemorySegment allocate(SegmentAllocator allocator) {
        return allocator.allocate(layout());
    }

    /**
     * Allocate an array of size {@code elementCount} using {@code allocator}.
     * The returned segment has size {@code elementCount * layout().byteSize()}.
     */
    public static MemorySegment allocateArray(long elementCount, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(elementCount, layout()));
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, Arena arena, Consumer<MemorySegment> cleanup) {
        return reinterpret(addr, 1, arena, cleanup);
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code elementCount * layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, long elementCount, Arena arena, Consumer<MemorySegment> cleanup) {
        return addr.reinterpret(layout().byteSize() * elementCount, arena, cleanup);
    }
}

