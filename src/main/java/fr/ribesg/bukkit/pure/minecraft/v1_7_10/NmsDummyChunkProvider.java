package fr.ribesg.bukkit.pure.minecraft.v1_7_10;

import net.minecraft.server.release_1_7_10.*;

import java.util.List;

/**
 * @author coelho
 * @author Ribesg
 */
/*
 * - (apx)                               is the obfuscated class  name for Chunk
 * - (apu.c())                           is the obfuscated method name for IChunkProvider.saveExtraData()
 * - (apu.e())                           is the obfuscated method name for IChunkProvider.canSave()
 * - (apu.a(ahb, String, int, int, int)) is the obfuscated method name for IChunkProvider.??? // FIXME!
 * - (apu.d(int, int))                   is the obfuscated method name for IChunkProvider.provideChunk(int, int)
 * - (apu.a(apu, int, int))              is the obfuscated method name for IChunkProvider.??? // FIXME!
 * - (apu.g())                           is the obfuscated method name for IChunkProvider.getLoadedChunkCount()
 * - (sx)                                is the obfuscated class  name for EnumCreatureType
 * - (apu.a(sx, int, int, int))          is the obfuscated method name for IChunkProvider.getPossibleCreatures(EnumCreatureType, int, int, int)
 * - (apu.f())                           is the obfuscated method name for IChunkProvider.makeString()
 * - (apu.c(int, int))                   is the obfuscated method name for IChunkProvider.loadChunk(int, int)
 * - (apu.a(int, int))                   is the obfuscated method name for IChunkProvider.chunkExists(int, int)
 * - (apu.e(int, int))                   is the obfuscated method name for IChunkProvider.recreateStructures(int, int)
 * - (qk)                                is the obfuscated class  name for IProgressUpdate
 * - (apu.a(boolean, qk))                is the obfuscated method name for IChunkProvider.saveChunks(boolean, IProgressUpdate)
 * - (apu.d())                           is the obfuscated method name for IChunkProvider.unloadQueuedChunks()
 */
public class NmsDummyChunkProvider implements apu /* IChunkProvider */ {

    protected apx chunk;

    @Override
    public void c() {
        // TODO?
    }

    @Override
    public boolean e() {
        return false; // TODO?
    }

    @Override
    public agt a(final ahb arg0, final String arg1, final int arg2, final int arg3, final int arg4) {
        return null; // TODO?
    }

    @Override
    public apx d(final int x, final int y) {
        return this.c(x, y); // TODO?
    }

    @Override
    public void a(final apu arg0, final int arg1, final int arg2) {
        // TODO?
    }

    @Override
    public int g() {
        return 0; // TODO?
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List a(final sx arg0, final int arg1, final int arg2, final int arg3) {
        return null; // TODO?
    }

    @Override
    public String f() {
        return null; // TODO?
    }

    @Override
    public apx c(final int x, final int y) {
        // TODO?
        if (this.chunk == null) {
            return null;
        }
        if (this.chunk.g != x || this.chunk.h != y) {
            return new net.minecraft.server.release_1_7_10.apw(this.chunk.e, x, y);
        }
        return this.chunk;
    }

    @Override
    public boolean a(final int x, final int y) {
        // TODO?
        return this.chunk != null && !(this.chunk.g != x || this.chunk.h != y);
    }

    @Override
    public void e(final int arg0, final int arg1) {
        // TODO?
    }

    @Override
    public boolean a(final boolean arg0, final qk arg1) {
        return false; // TODO?
    }

    @Override
    public boolean d() {
        return false; // TODO?
    }
}
