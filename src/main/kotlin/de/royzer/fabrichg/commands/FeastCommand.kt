package de.royzer.fabrichg.commands

import de.royzer.fabrichg.feast.Feast
import net.axay.fabrik.commands.PermissionLevel
import net.axay.fabrik.commands.command

val feastCommand = command("feast") {
    literal("start") {
        requiresPermissionLevel(4)
        runs {
            Feast.start()
        }
    }
}