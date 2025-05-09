/*
* Copyright 2014-2021 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
*/

package io.ktor.client.engine.cio

import io.ktor.client.engine.*
import io.ktor.utils.io.*

@Suppress("DEPRECATION")
@OptIn(ExperimentalStdlibApi::class, InternalAPI::class)
@EagerInitialization
private val initHook = engines.append(CIO)
