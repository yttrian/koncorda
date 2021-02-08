package command.check

import org.yttr.koncorda.command.CommandCall
import org.yttr.koncorda.command.check.CommandCheck

object Useless : CommandCheck {
    override suspend fun check(call: CommandCall): Boolean = true
}
