/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package io.ktor.network.selector

import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

public actual fun SelectorManager(dispatcher: CoroutineContext): SelectorManager = NoopSelectorManager

public actual interface SelectorManager : CoroutineScope, Closeable {
    /**
     * Notifies the selector that selectable has been closed.
     *
     * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.network.selector.SelectorManager.notifyClosed)
     */
    public actual fun notifyClosed(selectable: Selectable)

    /**
     * Suspends until [interest] is selected for [selectable]
     * May cause manager to allocate and run selector instance if not yet created.
     *
     * Only one selection is allowed per [interest] per [selectable] but you can
     * select for different interests for the same selectable simultaneously.
     * In other words you can select for read and write at the same time but should never
     * try to read twice for the same selectable.
     *
     * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.network.selector.SelectorManager.select)
     */
    public actual suspend fun select(
        selectable: Selectable,
        interest: SelectInterest
    )

    public actual companion object
}

/**
 * Select interest kind
 *
 * [Report a problem](https://ktor.io/feedback/?fqname=io.ktor.network.selector.SelectInterest)
 */
public actual enum class SelectInterest {
    READ,
    WRITE,
    ACCEPT,
    CONNECT;

    public actual companion object {
        public actual val AllInterests: Array<SelectInterest>
            get() = entries.toTypedArray()
    }
}

// TODO: how coroutine context should be used?
private object NoopSelectorManager : SelectorManager {
    override val coroutineContext: CoroutineContext get() = EmptyCoroutineContext

    override fun notifyClosed(selectable: Selectable) {
        error("not supported")
    }

    override suspend fun select(selectable: Selectable, interest: SelectInterest) {
        error("not supported")
    }

    override fun close() {
        // no-op so it can be called in common code
    }
}
