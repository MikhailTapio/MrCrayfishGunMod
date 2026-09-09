package com.mrcrayfish.guns.common;

import com.mrcrayfish.guns.Reference;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple system to run synchronized delayed tasks. See {@link #runAfter(int, Runnable)} to add
 * a delayed task.
 * <p>
 * Author: MrCrayfish
 */
@EventBusSubscriber(modid = Reference.MOD_ID)
public class DelayedTask
{
    public static List<Impl> tasks = new ArrayList<>();

    @SubscribeEvent
    public static void onServerStart(ServerStartedEvent event)
    {
        tasks.clear();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event)
    {
        tasks.clear();
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event)
    {
        {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            Iterator<Impl> it = tasks.iterator();
            while(it.hasNext())
            {
                Impl impl = it.next();
                if(impl.executionTick <= server.getTickCount())
                {
                    impl.runnable.run();
                    it.remove();
                }
            }
        }
    }

    /**
     * Adds a new delayed task to the system.
     *
     * @param ticks the amount of ticks to delay the execution
     * @param run   a runnable get with the code to run
     */
    public static void runAfter(int ticks, Runnable run)
    {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if(!server.isSameThread())
        {
            throw new IllegalStateException("Tried to add a delayed task off the main thread");
        }
        tasks.add(new Impl(server.getTickCount() + ticks, run));
    }

    private static class Impl
    {
        private int executionTick;
        private Runnable runnable;

        private Impl(int executionTick, Runnable runnable)
        {
            this.executionTick = executionTick;
            this.runnable = runnable;
        }
    }
}
