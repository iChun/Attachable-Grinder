package me.ichun.mods.attachablegrinder.common.entity;

import me.ichun.mods.attachablegrinder.common.Grinder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityGrinder extends Entity
{
    public static DamageSource grinder = new DamageSource("grinder");
    private static final DataParameter<Integer> PARENT_ID = EntityDataManager.createKey(EntityGrinder.class, DataSerializers.VARINT);

    public EntityLivingBase animal;
    public boolean firstUpdate;

    public int ticks;
    public int attachedType;

    public int remainingYield;
    public int timeBetweenYield;
    public boolean explodable;

    public EntityGrinder(World world)
    {
        super(world);
        setSize(0.6F, 0.1F);
        firstUpdate = true;
        ticks = 0;
        attachedType = 0;

        remainingYield = Grinder.config.grinderYield + rand.nextInt(Grinder.config.randomExtraYield + 1);
        timeBetweenYield = Grinder.config.frequency;
        explodable = Grinder.config.grinderYield > 0;
        ignoreFrustumCheck = true;
    }

    public EntityGrinder(World world, EntityLivingBase living)
    {
        this(world);
        animal = living;
        setPosition(living.posX, living.posY, living.posZ);
        setParent(living.getEntityId());
        attachedType = getMobType(living);
    }

    @Override
    public void onUpdate()
    {
        if(animal == null)
        {
            if(!worldObj.isRemote && !firstUpdate)
            {
                setDead();
                return;
            }
            else
            {
                Entity ent = worldObj.getEntityByID(getParent());
                if(ent instanceof EntityLivingBase)
                {
                    animal = (EntityLivingBase)ent;
                }
                if(animal == null && !firstUpdate)
                {
                    setDead();
                    return;
                }
            }
        }

        if(firstUpdate)
        {
            firstUpdate = false;

            if(!worldObj.isRemote)
            {
                Entity parent = null;
                double dist = -1D;
                List ents = worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.5D, 0.5D, 0.5D));
                for(int i = 0; i < ents.size(); i++)
                {
                    Entity ent = (Entity)ents.get(i);
                    if(!(ent instanceof EntityLivingBase) || !isMobType((EntityLivingBase)ent))
                    {
                        continue;
                    }
                    double mobDist = ent.getDistanceToEntity(this);
                    if(dist == -1 || mobDist < dist)
                    {
                        parent = ent;
                        dist = mobDist;
                    }
                }

                if(parent != null)
                {
                    animal = (EntityLivingBase)parent;
                    setParent(animal.getEntityId());
                }
                else
                {
                    setDead();
                    return;
                }
            }
        }

        if(animal != null)
        {
            ticks++;

            lastTickPosX = prevPosX = animal.prevPosX;
            lastTickPosY = prevPosY = animal.prevPosY;
            lastTickPosZ = prevPosZ = animal.prevPosZ;

            setPosition(posX, posY, posZ);
            posX = animal.posX;
            posY = animal.posY;
            posZ = animal.posZ;

            prevRotationYaw = animal.prevRotationYawHead;
            rotationYaw = animal.rotationYawHead;

            motionX = animal.motionX;
            motionY = animal.motionY;
            motionZ = animal.motionZ;

            if(animal.getRidingEntity() != null && !(animal.getRidingEntity() instanceof EntityMinecart))
            {
                motionX = animal.getRidingEntity().posX - animal.getRidingEntity().prevPosX;
                motionY = animal.getRidingEntity().posY - animal.getRidingEntity().prevPosY;
                motionZ = animal.getRidingEntity().posZ - animal.getRidingEntity().prevPosZ;

                lastTickPosX = prevPosX = prevPosX + motionX;
                lastTickPosY = prevPosY = prevPosY + motionY;
                lastTickPosZ = prevPosZ = prevPosZ + motionZ;

                posX += motionX;
                posY += motionY;
                posZ += motionZ;

                if(animal.getRidingEntity() instanceof EntityPig)
                {
                    prevRotationYaw = ((EntityPig)animal.getRidingEntity()).prevRenderYawOffset;
                    rotationYaw = ((EntityPig)animal.getRidingEntity()).renderYawOffset;
                }
            }
            if(!animal.isEntityAlive())
            {
                setDead();
                if((!explodable || remainingYield > Grinder.config.grinderYield) && !worldObj.isRemote)
                {
                    dropItem(Grinder.itemGrinder, 1);
                }
                return;
            }

            if(!worldObj.isRemote)
            {
                if(ticks % timeBetweenYield == (timeBetweenYield - 1) && canGrind())
                {
                    EntityItem item = new EntityItem(worldObj, posX, posY + getParentOffset(animal), posZ, animal instanceof EntitySkeleton ? (((EntitySkeleton)animal).getSkeletonType() == SkeletonType.WITHER && rand.nextFloat() < 0.15F ? new ItemStack(Items.COAL, 1, 0) : new ItemStack(Items.DYE, 1, 15)) : new ItemStack(getDrop(animal), 1, 0));
                    item.setPickupDelay(10);
                    if(this.animal instanceof EntityCow || this.animal instanceof EntityPig || this.animal instanceof EntityChicken)
                    {
                        item.motionY = (double)((2 * ((float)Grinder.config.tossPower / 100F)) * (rand.nextInt(5) + 1));
                        item.motionX = (double)(((float)Grinder.config.tossPower / 100F) * (rand.nextInt(Grinder.config.tossMultiplier) + 1)) * (rand.nextInt(2) == 0 ? 1D : -1D);
                        item.motionZ = (double)(((float)Grinder.config.tossPower / 100F) * (rand.nextInt(Grinder.config.tossMultiplier) + 1)) * (rand.nextInt(2) == 0 ? 1D : -1D);
                    }
                    else
                    {
                        float par7 = (float)((((float)Grinder.config.tossPower / 100F) * 2 * (rand.nextInt(Grinder.config.tossMultiplier) + 1)));
                        double par1 = (double)(-MathHelper.sin(animal.renderYawOffset / 180.0F * (float)Math.PI) * MathHelper.cos(animal.rotationPitch / 180.0F * (float)Math.PI));
                        double par5 = (double)(MathHelper.cos(animal.renderYawOffset / 180.0F * (float)Math.PI) * MathHelper.cos(animal.rotationPitch / 180.0F * (float)Math.PI));

                        float f2 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
                        par1 /= (double)f2;
                        par5 /= (double)f2;
                        par1 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)1.5F;
                        par5 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)1.5F;
                        par1 *= (double)par7;
                        par5 *= (double)par7;
                        item.motionX = par1;
                        item.motionZ = par5;
                        item.motionY = (double)((2 * ((float)Grinder.config.tossPower / 100F)) * ((rand.nextInt(3))));
                    }

                    worldObj.spawnEntityInWorld(item);
                    worldObj.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.2F, 1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.4F);

                    remainingYield--;
                }
                if(explodable && remainingYield <= 0)
                {
                    setDead();
                    animal.setDead();

                    worldObj.createExplosion(animal, posX, posY, posZ, (float)(((float)Grinder.config.explosionMagnitude / 100F) + (animal instanceof EntityCreeper ? 1.5D : animal instanceof EntityChicken ? -0.5D : 0.0D)), worldObj.getGameRules().getBoolean("mobGriefing"));
                }
                for (Entity entity : animal.getPassengers())
                {
                    if(entity.ticksExisted % 40 == 0)
                    {
                        entity.attackEntityFrom(grinder, 2);
                    }
                }
            }
        }
    }

    public double getParentOffset(EntityLivingBase ent)
    {
        return ent instanceof EntityPig ? 1.0D : ent instanceof EntityCow ? 1.4D : ent instanceof EntityChicken ? 0.4D : ent instanceof EntityCreeper ? 0.8D : ent instanceof EntityZombie ? 1.0D : ent instanceof EntitySkeleton && ((EntitySkeleton)ent).getSkeletonType() == SkeletonType.WITHER ? 1.4D : 1.2D;
    }

    public Item getDrop(EntityLivingBase ent)
    {
        if(ent instanceof EntityPig)
        {
            return ent.isBurning() ? Items.COOKED_PORKCHOP : Items.PORKCHOP;
        }
        else if(ent instanceof EntityCow)
        {
            if(ent instanceof EntityMooshroom && rand.nextFloat() < 0.15F)
            {
                return Item.getItemFromBlock(Blocks.RED_MUSHROOM);
            }
            return ent.isBurning() ? Items.COOKED_BEEF : Items.BEEF;
        }
        else if(ent instanceof EntityChicken)
        {
            return Items.FEATHER;
        }
        else if(ent instanceof EntityZombie)
        {
            return Items.ROTTEN_FLESH;
        }
        else if(ent instanceof EntityCreeper)
        {
            return Items.GUNPOWDER;
        }
        return Items.STICK;
    }

    public int getMobType(EntityLivingBase ent)
    {
        if(ent instanceof EntityPig)
        {
            return 1;
        }
        else if(ent instanceof EntityCow)
        {
            return 2;
        }
        else if(ent instanceof EntityChicken)
        {
            return 3;
        }
        else if(ent instanceof EntityZombie)
        {
            return 4;
        }
        else if(ent instanceof EntityCreeper)
        {
            return 5;
        }
        else if(ent instanceof EntitySkeleton)
        {
            return 6;
        }
        else if(ent instanceof EntityPlayer)
        {
            return 7;
        }
        return 0;
    }

    public boolean isMobType(EntityLivingBase ent)
    {
        return ent instanceof EntityPig && attachedType == 1 || ent instanceof EntityCow && attachedType == 2 || ent instanceof EntityChicken && attachedType == 3 || ent instanceof EntityZombie && attachedType == 4 || ent instanceof EntityCreeper && attachedType == 5 || ent instanceof EntitySkeleton && attachedType == 6 || ent instanceof EntityPlayer && attachedType == 7;
    }

    public boolean canGrind()
    {
        return !(animal == null || animal instanceof EntityPlayer);
    }

    @Override
    protected void entityInit()
    {
        getDataManager().register(PARENT_ID, -1); //behaviour
    }

    public void setParent(int i)
    {
        getDataManager().set(PARENT_ID, i);
    }

    public int getParent()
    {
        return getDataManager().get(PARENT_ID);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag)
    {
        attachedType = tag.getInteger("type");
        remainingYield = tag.getInteger("remainingYield");
        timeBetweenYield = tag.getInteger("timeBetweenYield");
        explodable = tag.getBoolean("explodable");
        ticks = tag.getInteger("ticks");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag)
    {
        tag.setInteger("remainingYield", remainingYield);
        tag.setInteger("timeBetweenYield", timeBetweenYield);
        tag.setBoolean("explodable", explodable);

        tag.setInteger("ticks", ticks);
        tag.setInteger("type", getMobType(animal));
    }
}
