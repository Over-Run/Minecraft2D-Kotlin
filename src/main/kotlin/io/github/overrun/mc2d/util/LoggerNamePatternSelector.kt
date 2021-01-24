/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Minecrell <https://github.com/Minecrell>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.overrun.mc2d.util

import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.Node
import org.apache.logging.log4j.core.config.plugins.*
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required
import org.apache.logging.log4j.core.layout.PatternLayout
import org.apache.logging.log4j.core.layout.PatternMatch
import org.apache.logging.log4j.core.layout.PatternSelector
import org.apache.logging.log4j.core.pattern.PatternFormatter

/**
 * A [`PatternSelector`][PatternSelector] that selects patterns based on the logger name.
 * Can be used to log messages from different loggers using different patterns.
 *
 * Multiple logger names may be separated using comma in the
 * [`PatternMatch "key"`][PatternMatch.getKey] . The pattern will be applied
 * if the logger name matches at least one of them.
 *
 * **Example usage:**
 * ```
 *   <PatternLayout>
 *      <LoggerNamePatternSelector defaultPattern="[%d{HH:mm:ss} %level] [%logger]: %msg%n">
 *          <!-- Log root (empty logger name), "Main", and net.minecrell.* without logger prefix -->
 *          <PatternMatch key=",Main,net.minecrell." pattern="[%d{HH:mm:ss} %level]: %msg%n"/>
 *          <PatternMatch key="com.example.Logger" pattern="EXAMPLE: %msg%n"/>
 *      </LoggerNamePatternSelector>
 *  </PatternLayout>```
 *
 * @author Minecrell
 * @since 2020/12/21
 */
@Suppress("unused")
@Plugin(name = "LoggerNamePatternSelector", category = Node.CATEGORY, elementType = PatternSelector.ELEMENT_TYPE)
class LoggerNamePatternSelector private constructor(
    defaultPattern: String,
    properties: Array<PatternMatch>,
    alwaysWriteExceptions: Boolean,
    disableAnsi: Boolean,
    noConsoleNoAnsi: Boolean,
    config: Configuration
) : PatternSelector {
    companion object {
        private class LoggerNameSelector(
            private val name: String,
            val formatters: Array<PatternFormatter>
        ) {
            private val isPackage = name.endsWith(".")

            fun test(s: String) = if (isPackage) s.startsWith(name) else s == name
        }

        @JvmStatic
        @PluginFactory
        fun createSelector(
            @Required(message = "Default pattern is required") @PluginAttribute(value = "defaultPattern") defaultPattern: String,
            @PluginElement("PatternMatch") properties: Array<PatternMatch>,
            @PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) alwaysWriteExceptions: Boolean,
            @PluginAttribute("disableAnsi") disableAnsi: Boolean,
            @PluginAttribute("noConsoleNoAnsi") noConsoleNoAnsi: Boolean,
            @PluginConfiguration config: Configuration
        ) = LoggerNamePatternSelector(
            defaultPattern, properties, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi,
            config
        )
    }

    private val defaultFormatters: Array<PatternFormatter>
    private val formatters = ArrayList<LoggerNameSelector>()

    init {
        val parser = PatternLayout.createPatternParser(config)
        defaultFormatters = parser.parse(defaultPattern, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi)
            .toTypedArray()
        for (property in properties) {
            val formatters: Array<PatternFormatter> =
                parser.parse(property.pattern, alwaysWriteExceptions, disableAnsi, noConsoleNoAnsi)
                    .toTypedArray()
            for (name in property.key.split(",")) {
                this.formatters.add(LoggerNameSelector(name, formatters))
            }
        }
    }

    override fun getFormatters(event: LogEvent): Array<PatternFormatter> {
        val loggerName = event.loggerName
        loggerName?.let {
            for (selector in formatters) {
                if (selector.test(loggerName)) {
                    return selector.formatters
                }
            }
        }
        return defaultFormatters
    }
}