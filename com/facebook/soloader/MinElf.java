package com.facebook.soloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import kotlin.UShort;
import okhttp3.internal.ws.WebSocketProtocol;
/* loaded from: classes.dex */
public final class MinElf {
    public static final int DT_NEEDED = 1;
    public static final int DT_NULL = 0;
    public static final int DT_STRTAB = 5;
    public static final int ELF_MAGIC = 1179403647;
    public static final int PN_XNUM = 65535;
    public static final int PT_DYNAMIC = 2;
    public static final int PT_LOAD = 1;

    public static String[] extract_DT_NEEDED(File elfFile) throws IOException {
        FileInputStream is = new FileInputStream(elfFile);
        try {
            return extract_DT_NEEDED(is.getChannel());
        } finally {
            is.close();
        }
    }

    public static String[] extract_DT_NEEDED(FileChannel fc) throws IOException {
        long e_phoff;
        long e_phnum;
        int e_phentsize;
        long phdr;
        long d_tag;
        long dynStart;
        long d_tag2;
        long d_val;
        long e_phnum2;
        long p_type;
        long d_tag3;
        long p_type2;
        long p_memsz;
        long p_offset;
        long j;
        long p_type3;
        long p_offset2;
        long e_shoff;
        long sh_info;
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        if (getu32(fc, bb, 0) == 1179403647) {
            boolean is32 = true;
            if (getu8(fc, bb, 4) != 1) {
                is32 = false;
            }
            if (getu8(fc, bb, 5) == 2) {
                bb.order(ByteOrder.BIG_ENDIAN);
            }
            if (is32) {
                e_phoff = getu32(fc, bb, 28);
            } else {
                e_phoff = get64(fc, bb, 32);
            }
            if (is32) {
                e_phnum = (long) getu16(fc, bb, 44);
            } else {
                e_phnum = (long) getu16(fc, bb, 56);
            }
            if (is32) {
                e_phentsize = getu16(fc, bb, 42);
            } else {
                e_phentsize = getu16(fc, bb, 54);
            }
            if (e_phnum == WebSocketProtocol.PAYLOAD_SHORT_MAX) {
                if (is32) {
                    e_shoff = getu32(fc, bb, 32);
                } else {
                    e_shoff = get64(fc, bb, 40);
                }
                if (is32) {
                    sh_info = getu32(fc, bb, 28 + e_shoff);
                } else {
                    sh_info = getu32(fc, bb, 44 + e_shoff);
                }
                e_phnum = sh_info;
            }
            long dynStart2 = 0;
            long phdr2 = e_phoff;
            long i = 0;
            while (true) {
                if (i >= e_phnum) {
                    break;
                }
                if (is32) {
                    p_type3 = getu32(fc, bb, phdr2 + 0);
                } else {
                    p_type3 = getu32(fc, bb, phdr2 + 0);
                }
                if (p_type3 == 2) {
                    if (is32) {
                        p_offset2 = getu32(fc, bb, phdr2 + 4);
                    } else {
                        p_offset2 = get64(fc, bb, phdr2 + 8);
                    }
                    dynStart2 = p_offset2;
                } else {
                    phdr2 += (long) e_phentsize;
                    i++;
                }
            }
            if (dynStart2 != 0) {
                int nr_DT_NEEDED = 0;
                long dyn = dynStart2;
                long ptr_DT_STRTAB = 0;
                while (true) {
                    if (is32) {
                        phdr = phdr2;
                        d_tag = getu32(fc, bb, dyn + 0);
                    } else {
                        phdr = phdr2;
                        d_tag = get64(fc, bb, dyn + 0);
                    }
                    if (d_tag == 1) {
                        dynStart = dynStart2;
                        if (nr_DT_NEEDED != Integer.MAX_VALUE) {
                            nr_DT_NEEDED++;
                        } else {
                            throw new ElfError("malformed DT_NEEDED section");
                        }
                    } else {
                        dynStart = dynStart2;
                        if (d_tag == 5) {
                            if (is32) {
                                j = getu32(fc, bb, dyn + 4);
                            } else {
                                j = get64(fc, bb, dyn + 8);
                            }
                            ptr_DT_STRTAB = j;
                        }
                    }
                    long dyn2 = dyn + (is32 ? 8 : 16);
                    if (d_tag != 0) {
                        e_phnum = e_phnum;
                        phdr2 = phdr;
                        dynStart2 = dynStart;
                        dyn = dyn2;
                    } else if (ptr_DT_STRTAB != 0) {
                        long off_DT_STRTAB = 0;
                        long phdr3 = e_phoff;
                        int i2 = 0;
                        while (true) {
                            if (((long) i2) >= e_phnum) {
                                break;
                            }
                            if (is32) {
                                e_phnum2 = e_phnum;
                                p_type = getu32(fc, bb, phdr3 + 0);
                            } else {
                                e_phnum2 = e_phnum;
                                p_type = getu32(fc, bb, phdr3 + 0);
                            }
                            if (p_type == 1) {
                                if (is32) {
                                    p_type2 = getu32(fc, bb, phdr3 + 8);
                                } else {
                                    p_type2 = get64(fc, bb, phdr3 + 16);
                                }
                                if (is32) {
                                    p_memsz = getu32(fc, bb, phdr3 + 20);
                                    d_tag3 = d_tag;
                                } else {
                                    d_tag3 = d_tag;
                                    p_memsz = get64(fc, bb, phdr3 + 40);
                                }
                                if (p_type2 <= ptr_DT_STRTAB && ptr_DT_STRTAB < p_type2 + p_memsz) {
                                    if (is32) {
                                        p_offset = getu32(fc, bb, phdr3 + 4);
                                    } else {
                                        p_offset = get64(fc, bb, phdr3 + 8);
                                    }
                                    off_DT_STRTAB = p_offset + (ptr_DT_STRTAB - p_type2);
                                }
                            } else {
                                d_tag3 = d_tag;
                            }
                            phdr3 += (long) e_phentsize;
                            i2++;
                            e_phnum = e_phnum2;
                            d_tag = d_tag3;
                            dyn2 = dyn2;
                        }
                        if (off_DT_STRTAB != 0) {
                            String[] needed = new String[nr_DT_NEEDED];
                            int nr_DT_NEEDED2 = 0;
                            long dyn3 = dynStart;
                            do {
                                if (is32) {
                                    d_tag2 = getu32(fc, bb, dyn3 + 0);
                                } else {
                                    d_tag2 = get64(fc, bb, dyn3 + 0);
                                }
                                if (d_tag2 == 1) {
                                    if (is32) {
                                        d_val = getu32(fc, bb, dyn3 + 4);
                                    } else {
                                        d_val = get64(fc, bb, dyn3 + 8);
                                    }
                                    needed[nr_DT_NEEDED2] = getSz(fc, bb, off_DT_STRTAB + d_val);
                                    if (nr_DT_NEEDED2 != Integer.MAX_VALUE) {
                                        nr_DT_NEEDED2++;
                                    } else {
                                        throw new ElfError("malformed DT_NEEDED section");
                                    }
                                }
                                dyn3 += is32 ? 8 : 16;
                            } while (d_tag2 != 0);
                            if (nr_DT_NEEDED2 == needed.length) {
                                return needed;
                            }
                            throw new ElfError("malformed DT_NEEDED section");
                        }
                        throw new ElfError("did not find file offset of DT_STRTAB table");
                    } else {
                        throw new ElfError("Dynamic section string-table not found");
                    }
                }
            } else {
                throw new ElfError("ELF file does not contain dynamic linking information");
            }
        } else {
            throw new ElfError("file is not ELF");
        }
    }

    private static String getSz(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true) {
            long offset2 = 1 + offset;
            short b = getu8(fc, bb, offset);
            if (b == 0) {
                return sb.toString();
            }
            sb.append((char) b);
            offset = offset2;
        }
    }

    private static void read(FileChannel fc, ByteBuffer bb, int sz, long offset) throws IOException {
        int numBytesRead;
        bb.position(0);
        bb.limit(sz);
        while (bb.remaining() > 0 && (numBytesRead = fc.read(bb, offset)) != -1) {
            offset += (long) numBytesRead;
        }
        if (bb.remaining() <= 0) {
            bb.position(0);
            return;
        }
        throw new ElfError("ELF file truncated");
    }

    private static long get64(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 8, offset);
        return bb.getLong();
    }

    private static long getu32(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 4, offset);
        return ((long) bb.getInt()) & 4294967295L;
    }

    private static int getu16(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 2, offset);
        return bb.getShort() & UShort.MAX_VALUE;
    }

    private static short getu8(FileChannel fc, ByteBuffer bb, long offset) throws IOException {
        read(fc, bb, 1, offset);
        return (short) (bb.get() & 255);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class ElfError extends RuntimeException {
        ElfError(String why) {
            super(why);
        }
    }
}
