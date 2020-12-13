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

import io.github.overrun.mc2d.util.Utils
import java.io.PrintWriter
import java.io.StringWriter
import java.time.LocalTime
import java.util.logging.Formatter
import java.util.logging.LogRecord

/**
 * @author squid233
 * @since 2020/11/22
 */
class Formatter : Formatter() {
    override fun format(record: LogRecord): String {
        var throwable = ""
        if (record.thrown != null) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            pw.println()
            record.thrown.printStackTrace(pw)
            pw.close()
            throwable = sw.toString()
        }
        return "[${LocalTime.now().toString().split(".")[0]}] [${Thread.currentThread().name}/${
            when (record.level.name) {
                "SEVERE" -> "ERROR"
                "WARNING" -> "WARN"
                "CONFIG" -> "DEBUG"
                else -> record.level.name
            }
        }] (${record.loggerName}) ${record.message ?: "null"}$throwable${Utils.newLine()}"
    }
}