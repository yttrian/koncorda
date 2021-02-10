package org.yttr.koncorda

import kotlinx.coroutines.future.await
import net.dv8tion.jda.api.requests.RestAction

/**
 * Shortcut to submit and await a RestAction to avoid dealing queue's callbacks.
 * Queue should be preferred if the result is not needed.
 */
suspend fun <T> RestAction<T>.await(): T = submit().await()
