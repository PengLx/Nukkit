package cn.nukkit.entity;

import cn.nukkit.level.chunk.Chunk;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public abstract class HangingEntity<T extends Entity> extends Entity {
    protected int direction;

    public HangingEntity(EntityType<T> type, Chunk chunk, CompoundTag nbt) {
        super(type, chunk, nbt);
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.setMaxHealth(1);
        this.setHealth(1);

        if (this.namedTag.contains("Direction")) {
            this.direction = this.namedTag.getByte("Direction");
        } else if (this.namedTag.contains("Dir")) {
            int d = this.namedTag.getByte("Dir");
            if (d == 2) {
                this.direction = 0;
            } else if (d == 0) {
                this.direction = 2;
            }
        }

    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putByte("Direction", this.getDirection().getHorizontalIndex());
        this.namedTag.putInt("TileX", (int) this.x);
        this.namedTag.putInt("TileY", (int) this.y);
        this.namedTag.putInt("TileZ", (int) this.z);
    }

    @Override
    public BlockFace getDirection() {
        return BlockFace.fromIndex(this.direction);
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        if (!this.isAlive()) {

            this.despawnFromAll();
            if (!this.isPlayer) {
                this.close();
            }

            return true;
        }

        if (this.lastYaw != this.yaw || this.lastX != this.x || this.lastY != this.y || this.lastZ != this.z) {
            this.despawnFromAll();

            this.direction = (int) (this.yaw / 90);

            this.lastYaw = this.yaw;
            this.lastX = this.x;
            this.lastY = this.y;
            this.lastZ = this.z;

            this.spawnToAll();

            return true;
        }

        return false;
    }

    protected boolean isSurfaceValid() {
        return true;
    }

}