package fr.ribesg.bukkit.pure.minecraft.r1_8;

import net.minecraft.server.r1_8.bqc;
import net.minecraft.server.r1_8.brn;

/**
 * @author coelho
 * @author Ribesg
 */
public class NmsDummyMapStorage extends brn /* MapStorage */ {

    public NmsDummyMapStorage() {
        super(null);
    }

    @Override
    public void a(final String arg0, final bqc arg1) {
        // NOP
    }

    @Override
    public int a(final String arg0) {
        return 0; // NOP
    }

    @SuppressWarnings("rawtypes")
    @Override
    public bqc a(final Class arg0, final String arg1) {
        return null; // NOP
    }
}
