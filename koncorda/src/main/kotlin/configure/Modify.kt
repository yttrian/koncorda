package org.yttr.koncorda.configure

import net.dv8tion.jda.api.JDABuilder

interface Modify {
    class Config

    fun JDABuilder.configure(): Unit
}
