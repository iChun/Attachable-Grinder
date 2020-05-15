package me.ichun.mods.attachablegrinder.common.entity;

import me.ichun.mods.attachablegrinder.common.AttachableGrinder;
import me.ichun.mods.attachablegrinder.common.grinder.GrinderProperties;
import me.ichun.mods.ichunutil.common.entity.LatchedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;

public class GrinderEntity extends LatchedEntity<LivingEntity>
{
    public static final DamageSource GRINDER = new DamageSource("grinder");

    public int ticks;
    public int remainingYield;
    public int timeBetweenYield;
    public boolean explodable;

    public GrinderProperties.Properties properties = null;

    public GrinderEntity(EntityType<? extends GrinderEntity> type, World world)
    {
        super(type, world);
        noClip = true;

        remainingYield = AttachableGrinder.config.grinderYield + rand.nextInt(AttachableGrinder.config.randomExtraYield + 1);
        timeBetweenYield = AttachableGrinder.config.frequency;
        explodable = AttachableGrinder.config.grinderYield > 0;
    }

    @Override
    public void tick()
    {
        super.tick(); //handles all the parent stuff.

        if(parent == null)
        {
            return;
        }

        if(properties == null)
        {
            this.properties = AttachableGrinder.grinderProperties.getFor(parent.getType().getRegistryName());
        }

        ticks++;

        if(!world.isRemote)
        {
            if(properties != null)
            {
                if(!parent.isAlive())
                {
                    unableToFindParent(true);
                    return;
                }

                if(explodable && remainingYield <= 0)
                {
                    remove();
                    parent.remove();

                    Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, parent) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
                    world.createExplosion(parent, getPosX(), getPosY(), getPosZ(), (float)(((float)AttachableGrinder.config.explosionMagnitude / 100F) + properties.explosionBonus), explosion$mode);
                }
                if(ticks % timeBetweenYield == (timeBetweenYield - 1))
                {
                    if(!(parent instanceof PlayerEntity))
                    {
                        LootTable lootTable = world.getServer().getLootTableManager().getLootTableFromLocation(new ResourceLocation(properties.lootTable));
                        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)this.world)).withRandom(this.rand).withParameter(LootParameters.THIS_ENTITY, parent).withParameter(LootParameters.POSITION, new BlockPos(parent)).withParameter(LootParameters.DAMAGE_SOURCE, GRINDER).withNullableParameter(LootParameters.KILLER_ENTITY, GRINDER.getTrueSource()).withNullableParameter(LootParameters.DIRECT_KILLER_ENTITY, GRINDER.getImmediateSource());
                        lootTable.generate(lootcontext$builder.build(LootParameterSets.ENTITY), this::grind);
                        remainingYield--;
                    }
                }
                for (Entity entity : parent.getPassengers())
                {
                    if(entity.ticksExisted % 40 == 0)
                    {
                        entity.attackEntityFrom(GRINDER, 2);
                    }
                }

            }
            else
            {
                entityDropItem(AttachableGrinder.Items.GRINDER.get());
                remove();
                return;
            }
        }
    }

    @Override
    public void unableToFindParent(boolean hasId)
    {
        if(!world.isRemote)
        {
            entityDropItem(AttachableGrinder.Items.GRINDER.get());
            remove();
        }
    }

    public void grind(ItemStack stack)
    {
        if(stack.isEmpty())
        {
            return;
        }

        double x = parent.getPosX();
        double y = parent.getPosY() + (properties.isVertical ? properties.offsetUp - 0.3D : properties.offsetUp);
        double z = parent.getPosZ();
        ItemEntity item = new ItemEntity(this.world, x, y, z, stack);
        item.setDefaultPickupDelay();


        double tossPower = AttachableGrinder.config.tossPower / 100F;
        if(properties.isVertical)
        {
            float amp = (float)((tossPower * 2 * (rand.nextInt(AttachableGrinder.config.tossMultiplier) + 1)));
            double mX = (double)(-MathHelper.sin(parent.renderYawOffset / 180.0F * (float)Math.PI) * MathHelper.cos(parent.rotationPitch / 180.0F * (float)Math.PI));
            double mZ = (double)(MathHelper.cos(parent.renderYawOffset / 180.0F * (float)Math.PI) * MathHelper.cos(parent.rotationPitch / 180.0F * (float)Math.PI));

            float f2 = MathHelper.sqrt(mX * mX + mZ * mZ);
            mX /= (double)f2;
            mZ /= (double)f2;
            mX += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)1.5F;
            mZ += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)1.5F;
            mX *= (double)amp;
            mZ *= (double)amp;
            if(properties.flip)
            {
                mX *= -1D;
                mZ *= -1D;
            }
            item.setMotion(mX, ((2 * tossPower) * ((rand.nextInt(3)))), mZ);
        }
        else
        {
            double mY = properties.flip ? -((2 * tossPower) * (rand.nextInt(5) + 1)) : ((2 * tossPower) * (rand.nextInt(5) + 1));

            double mX = (tossPower * (rand.nextInt(AttachableGrinder.config.tossMultiplier) + 1)) * (rand.nextInt(2) == 0 ? 1D : -1D);
            double mZ = (tossPower * (rand.nextInt(AttachableGrinder.config.tossMultiplier) + 1)) * (rand.nextInt(2) == 0 ? 1D : -1D);
            item.setMotion(mX, mY, mZ);
        }

        world.addEntity(item);
        world.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.2F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);
    }

    @Override
    protected void readAdditional(CompoundNBT tag)
    {
        super.readAdditional(tag);
        remainingYield = tag.getInt("remainingYield");
        timeBetweenYield = tag.getInt("timeBetweenYield");
        explodable = tag.getBoolean("explodable");
    }

    @Override
    protected void writeAdditional(CompoundNBT tag)
    {
        super.writeAdditional(tag);
        tag.putInt("remainingYield", remainingYield);
        tag.putInt("timeBetweenYield", timeBetweenYield);
        tag.putBoolean("explodable", explodable);
    }
}
