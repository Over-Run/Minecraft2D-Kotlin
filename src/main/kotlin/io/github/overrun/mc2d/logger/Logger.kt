/*
 * MIT License
 *
 * Copyright (c) 2020 Over-Run
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.overrun.mc2d.logger

import io.github.overrun.mc2d.option.Options
import java.io.IOException
import java.util.logging.ConsoleHandler
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger

/**
 * @author squid233
 * @since 2020/10/24
 */
class Logger(name: String?) {
    val javaLogger: Logger = Logger.getLogger(name ?: "Unknown")

    constructor(clazz: Class<*>) : this(clazz.simpleName)

    fun info(msg: String?) = javaLogger.info(msg)

    fun warn(msg: String?) = javaLogger.warning(msg)

    fun error(msg: String?) = javaLogger.severe(msg)

    fun debug(msg: String?) {
        if (logDebugEnable()) javaLogger.config(msg)
    }

    fun exception(msg: String?, throwable: Throwable) = javaLogger.log(Level.SEVERE, msg, throwable)

    companion object {
        fun logDebugEnable() = Options.getB(Options.DEBUG)
    }

    init {
        javaLogger.useParentHandlers = false
        javaLogger.level = Level.CONFIG
        val f = Formatter()
        val ch: ConsoleHandler = object : ConsoleHandler() {
            init {
                setOutputStream(System.out)
            }
        }
        ch.level = if (logDebugEnable()) Level.CONFIG else Level.INFO
        ch.formatter = f
        javaLogger.addHandler(ch)
        try {
            val fh = FileHandler("latest.log")
            fh.level = Level.INFO
            fh.formatter = f
            javaLogger.addHandler(fh)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            val fh = FileHandler("latest-debug.log")
            fh.level = Level.CONFIG
            fh.formatter = f
            javaLogger.addHandler(fh)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}