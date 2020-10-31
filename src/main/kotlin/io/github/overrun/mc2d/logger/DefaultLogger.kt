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

import io.github.overrun.mc2d.logger.DefaultLogger.Level.*
import io.github.overrun.mc2d.options.Options
import org.apache.commons.lang3.StringUtils
import java.time.LocalTime

/**
 * @author squid233
 * @since 2020/09/15
 */
class DefaultLogger internal constructor(override val name: String) : Logger {
    fun msg(msg: String?, level: Level = INFO, vararg params: Any? = E_M) {
        var dmp = msg ?: "null"
        if (params.isNotEmpty()) {
            for (element in params) {
                dmp = dmp.replaceFirst("{}", element.toString())
            }
        }
        println("${Style.C_LIGHT_BLUE}[${StringUtils.split(LocalTime.now().toString(), '.')[0]}] " +
                "${level.color}[${Thread.currentThread().name}/$level]${Style.CLEAR} ($name) ${level.msgColor}$dmp${Style.CLEAR}")
    }

    enum class Level constructor(internal val color: String, internal val msgColor: String = color) {
        INFO(Style.C_GREEN, Style.NONE),
        WARN(Style.C_YELLOW),
        ERROR(Style.CF_RED),
        FATAL(Style.C_RED),
        DEBUG(Style.CF_GREEN);
    }

    object Style {
        const val NONE = ""
        const val CLEAR = "\u001b[0m"
        const val C_RED = "\u001b[31m"
        const val C_GREEN = "\u001b[32m"
        const val C_YELLOW = "\u001b[33m"
        const val C_LIGHT_BLUE = "\u001b[36m"
        const val CF_RED = "\u001b[91m"
        const val CF_GREEN = "\u001b[92m"
    }

    companion object {
        private val E_M: Array<Any> = arrayOf()
    }

    override fun info(msg: String?) {
        msg(msg)
    }

    override fun warn(msg: String?) {
        msg(msg, WARN)
    }

    override fun error(msg: String?) {
        msg(msg, ERROR)
    }

    override fun fatal(msg: String?) {
        msg(msg, FATAL)
    }

    override fun debug(msg: String?) {
        if (Options.getB(Options.DEBUGGING)) msg(msg, DEBUG)
    }
}