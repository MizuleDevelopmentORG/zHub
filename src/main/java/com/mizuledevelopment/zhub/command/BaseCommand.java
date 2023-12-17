/*
 * This file is part of zHub, licensed under the GPLv3 License.
 *
 * Copyright (c) 2023 Mizule Development
 * Copyright (c) 2023 contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mizuledevelopment.zhub.command;

import cloud.commandframework.CommandManager;
import org.bukkit.command.CommandSender;

/**
 * A command, registered with Cloud's {@link CommandManager} and operates on a {@link org.bukkit.command.CommandSender}
 * sender.
 */
public abstract class BaseCommand {

    /**
     * Register this command with the {@link CommandManager}.
     *
     * @param commandManager The manager to register commands with. This assumes the manager
     *                       supports {@link CommandSender}s as a sender.
     */
    public abstract void register(final CommandManager<CommandSender> commandManager);
}
