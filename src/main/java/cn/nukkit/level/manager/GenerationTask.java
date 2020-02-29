package cn.nukkit.level.manager;

import cn.nukkit.level.chunk.Chunk;
import cn.nukkit.level.chunk.LockableChunk;
import cn.nukkit.level.generator.Generator;
import com.google.common.base.Preconditions;
import net.daporkchop.lib.random.PRandom;
import net.daporkchop.lib.random.impl.FastJavaPRandom;
import net.daporkchop.lib.random.impl.FastPRandom;

import java.util.function.Function;

/**
 * Delegates chunk generation to a {@link Generator}.
 *
 * @author DaPorkchop_
 */
public final class GenerationTask implements Function<Chunk, Chunk> {
    private final Generator generator;

    public GenerationTask(Generator generator) {
        this.generator = Preconditions.checkNotNull(generator, "generator");
    }

    @Override
    public Chunk apply(Chunk chunk) {
        if (Preconditions.checkNotNull(chunk, "chunk").isGenerated()) {
            return chunk;
        }

        PRandom random = new FastPRandom(chunk.getX() * 3053330778986901431L ^ chunk.getZ() * 1517227374085824433L ^ chunk.getLevel().getSeed());
        LockableChunk lockable = chunk.writeLockable();

        lockable.lock();
        try {
            this.generator.generate(random, lockable, chunk.getX(), chunk.getZ());
            chunk.setGenerated();
        } finally {
            lockable.unlock();
        }
        return chunk;
    }
}