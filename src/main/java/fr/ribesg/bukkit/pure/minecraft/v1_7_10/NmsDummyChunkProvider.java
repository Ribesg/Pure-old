package fr.ribesg.bukkit.pure.minecraft.v1_7_10;

import net.minecraft.server.release_1_7_10.*;

import java.util.List;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsDummyChunkProvider implements apu {

    protected apx chunk;

    public void c() {

    }

    public boolean e() {
        return false;
    }

    public agt a(final ahb arg0, final String arg1, final int arg2, final int arg3, final int arg4) {
        return null;
    }

    public apx d(final int x, final int y) {
        return this.c(x, y);
    }

    public void a(final apu arg0, final int arg1, final int arg2) {

    }

    public int g() {
        return 0;
    }

    @SuppressWarnings("rawtypes")
    public List a(final sx arg0, final int arg1, final int arg2, final int arg3) {
        return null;
    }

    public String f() {
        return null;
    }

    public apx c(final int x, final int y) {
        if (this.chunk == null) {
            return null;
        }
        if (this.chunk.g != x || this.chunk.h != y) {
            return new net.minecraft.server.release_1_7_10.apw(this.chunk.e, x, y);
        }
        return this.chunk;
    }

    public boolean a(final int x, final int y) {
        if (this.chunk == null) {
            return false;
        }
        if (this.chunk.g != x || this.chunk.h != y) {
            return false;
        }
        return true;
    }

    public void e(final int arg0, final int arg1) {

    }

    public boolean a(final boolean arg0, final qk arg1) {
        return false;
    }

    public boolean d() {
        return false;
    }
}
