package com.github.madjichan.santaplugin;

import com.github.madjichan.santaplugin.config.EntityConfiguration;
import com.github.madjichan.santaplugin.config.SantaConfiguration;
import com.github.madjichan.santaplugin.present.PresentLoot;
import com.github.madjichan.santaplugin.present.Presents;
import com.github.madjichan.santaplugin.santa.Santa;
import com.github.madjichan.santaplugin.santa.trajectory.LineTrajectory;
import com.github.madjichan.santaplugin.santa.trajectory.RotateTrajectory;
import com.github.madjichan.santaplugin.santa.trajectory.SquareTrajectory;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class SantaPlugin extends JavaPlugin {
    private Santa santa;

    @Override
    public void onEnable() {
        saveResource("config.yml", false);
        saveDefaultConfig();
        SantaConfiguration config = SantaConfiguration.parse(getConfig());

        Presents initPresents = Presents.getInstance(this);

        /* PresentLoot.PresentMaterialRecord diamondLoot = new PresentLoot.PresentMaterialRecord(Material.DIAMOND, 1);
        PresentLoot.PresentMaterialRecord AcaciaLogLoot = new PresentLoot.PresentMaterialRecord(Material.ACACIA_LOG, 4);
        PresentLoot.PresentItemRecord[] diamondItemLoot = new PresentLoot.PresentItemRecord[] {new PresentLoot.PresentItemRecord(1, 1)};
        PresentLoot.PresentItemRecord[] AcaciaLogItemLoot = new PresentLoot.PresentItemRecord[] {new PresentLoot.PresentItemRecord(3, 1), new PresentLoot.PresentItemRecord(2, 2), new PresentLoot.PresentItemRecord(1, 3)};
        PresentLoot loot = new PresentLoot(new PresentLoot.PresentMaterialRecord[] {diamondLoot,AcaciaLogLoot}, new PresentLoot.PresentItemRecord[][] {diamondItemLoot, AcaciaLogItemLoot}); */
        // initPresents.setLootGenerator(loot);

        PresentLoot loot = new PresentLoot(config.table);
        initPresents.setLootGenerator(loot);

        LiteralArgumentBuilder<CommandSourceStack> santa_cmd, santaSpawn_cmd, santaStart_cmd,
                santaStartLine_cmd, santaStartRotation_cmd, santaStartSquare_cmd, santaStop_cmd;

        santaStartLine_cmd = Commands.literal("line").then(Commands.argument("startLocation", ArgumentTypes.finePosition(true)).then(Commands.argument("targetLocation", ArgumentTypes.finePosition(true)).executes(ctx -> {
            final FinePositionResolver startResolver = ctx.getArgument("startLocation", FinePositionResolver.class);
            final FinePosition startFinePosition = startResolver.resolve(ctx.getSource());

            final FinePositionResolver targetResolver = ctx.getArgument("targetLocation", FinePositionResolver.class);
            final FinePosition targetFinePosition = targetResolver.resolve(ctx.getSource());

            Vector startPos = new Vector(startFinePosition.x(), startFinePosition.y(), startFinePosition.z());
            Vector targetPos = new Vector(targetFinePosition.x(), targetFinePosition.y(), targetFinePosition.z());

            CommandSender sender = ctx.getSource().getSender();
            if(!(sender instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }
            LineTrajectory line = new LineTrajectory(player.getWorld(), startPos, targetPos, config.speed / 20.0); // , 5.0
            this.santa.start(line);
            return Command.SINGLE_SUCCESS;
        })));

        santaStartRotation_cmd = Commands.literal("rotation").then(Commands.argument("startLocation", ArgumentTypes.finePosition(true)).then(Commands.argument("targetLocation", ArgumentTypes.finePosition(true)).executes(ctx -> {
            final FinePositionResolver startResolver = ctx.getArgument("startLocation", FinePositionResolver.class);
            final FinePosition startFinePosition = startResolver.resolve(ctx.getSource());

            final FinePositionResolver targetResolver = ctx.getArgument("targetLocation", FinePositionResolver.class);
            final FinePosition targetFinePosition = targetResolver.resolve(ctx.getSource());

            Vector startPos = new Vector(startFinePosition.x(), startFinePosition.y(), startFinePosition.z());
            Vector targetPos = new Vector(targetFinePosition.x(), targetFinePosition.y(), targetFinePosition.z());

            CommandSender sender = ctx.getSource().getSender();
            if(!(sender instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }
            RotateTrajectory rotation = new RotateTrajectory(new Location(player.getWorld(), startPos.getX(), startPos.getY(), startPos.getZ()), targetPos, config.speed / 20.0, config.rotationRadius);
            this.santa.start(rotation);
            return Command.SINGLE_SUCCESS;
        })));

        santaStartSquare_cmd = Commands.literal("square").then(Commands.argument("flyHeight", DoubleArgumentType.doubleArg()).then(Commands.argument("startLocation", ArgumentTypes.finePosition(true)).then(Commands.argument("leftCorner", ArgumentTypes.finePosition(true)).then(Commands.argument("rightCorner", ArgumentTypes.finePosition(true)).executes(ctx -> {
            final FinePositionResolver startResolver = ctx.getArgument("startLocation", FinePositionResolver.class);
            final FinePosition startFinePosition = startResolver.resolve(ctx.getSource());

            final FinePositionResolver leftResolver = ctx.getArgument("leftCorner", FinePositionResolver.class);
            final FinePosition leftFinePosition = leftResolver.resolve(ctx.getSource());

            final FinePositionResolver rightResolver = ctx.getArgument("rightCorner", FinePositionResolver.class);
            final FinePosition rightFinePosition = rightResolver.resolve(ctx.getSource());

            double flyHeight = ctx.getArgument("flyHeight", double.class);

            Vector startPos = new Vector(startFinePosition.x(), startFinePosition.y(), startFinePosition.z());
            Vector leftPos = new Vector(leftFinePosition.x(), leftFinePosition.y(), leftFinePosition.z());
            Vector rightPos = new Vector(rightFinePosition.x(), rightFinePosition.y(), rightFinePosition.z());

            CommandSender sender = ctx.getSource().getSender();
            if(!(sender instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }

            Location startLoc = new Location(player.getWorld(), startPos.getX(), startPos.getY(), startPos.getZ());
            Location leftCornerLoc = new Location(player.getWorld(),  leftPos.getX(),  leftPos.getY(), leftPos.getZ());
            Location rightCornerLoc = new Location(player.getWorld(), rightPos.getX(), rightPos.getY(), rightPos.getZ());
            SquareTrajectory rotation = new SquareTrajectory(startLoc, leftCornerLoc, rightCornerLoc,  flyHeight, config.speed / 20.0, config.rotationRadius);
            this.santa.start(rotation);
            return Command.SINGLE_SUCCESS;
        })))));

        santaSpawn_cmd = Commands.literal("spawn").executes(ctx -> {
            CommandSender sender = ctx.getSource().getSender();
            if(!(sender instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }
            this.santa = Santa.spawn(this, player.getLocation());
            return Command.SINGLE_SUCCESS;
        });

        santaStart_cmd = Commands.literal("start").then(santaStartLine_cmd).then(santaStartRotation_cmd).then(santaStartSquare_cmd);

        santaStop_cmd = Commands.literal("stop").executes(ctx -> {
            CommandSender sender = ctx.getSource().getSender();
            if(!(sender instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }
            this.santa.stop();
            return Command.SINGLE_SUCCESS;
        });

        santa_cmd = Commands.literal("santa")
                .then(santaSpawn_cmd)
                .then(santaStart_cmd)
                .then(santaStop_cmd);

        LiteralArgumentBuilder<CommandSourceStack> present_cmd, presentSpawn_cmd;

        presentSpawn_cmd = Commands.literal("spawn").then(Commands.argument("position", ArgumentTypes.finePosition(true)).executes(ctx -> {
            final FinePositionResolver positionResolver = ctx.getArgument("position", FinePositionResolver.class);
            final FinePosition positionFinePosition = positionResolver.resolve(ctx.getSource());

            CommandSender sender = ctx.getSource().getSender();
            if(!(sender instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }

            Location loc = new Location(player.getWorld(), positionFinePosition.x(), positionFinePosition.y(), positionFinePosition.z());
            Presents presents = Presents.getInstance(null);
            presents.spawn(loc);

            return Command.SINGLE_SUCCESS;
        }));

        present_cmd = Commands.literal("present").then(presentSpawn_cmd);

        LiteralArgumentBuilder<CommandSourceStack> giveItemFromConfig_cmd, summonEntityFromConfig_cmd;

        giveItemFromConfig_cmd = Commands.literal("giveitemfromconfig").then(Commands.argument("tagName", StringArgumentType.word()).executes(ctx -> {
            CommandSender sender = ctx.getSource().getSender();
            if(!(sender instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }

            String tagName = ctx.getArgument("tagName", String.class);

            player.give(SantaConfiguration.getInstance().items.get(tagName).configure(null));

            return Command.SINGLE_SUCCESS;
        }));

        summonEntityFromConfig_cmd = Commands.literal("summonentityfromconfig").then(Commands.argument("tagName", StringArgumentType.word()).executes(ctx -> {
            CommandSender sender = ctx.getSource().getSender();
            if(!(sender instanceof Player player)) {
                return Command.SINGLE_SUCCESS;
            }

            String tagName = ctx.getArgument("tagName", String.class);

            EntityConfiguration entConfig = SantaConfiguration.getInstance().entities.get(tagName);
            Entity nEnt = player.getWorld().spawnEntity(player.getLocation(), entConfig.getEntityType());
            entConfig.configure(nEnt);

            return Command.SINGLE_SUCCESS;
        }));

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(santa_cmd.build());
            commands.registrar().register(present_cmd.build());
            commands.registrar().register(giveItemFromConfig_cmd.build());
            commands.registrar().register(summonEntityFromConfig_cmd.build());
        });
    }
}
