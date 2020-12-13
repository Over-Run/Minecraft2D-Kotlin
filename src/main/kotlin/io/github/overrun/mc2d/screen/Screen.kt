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

package io.github.overrun.mc2d.screen

import io.github.overrun.mc2d.client.Mc2dClient
import io.github.overrun.mc2d.util.DrawHelper
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import java.awt.Graphics
import java.awt.event.KeyEvent

/**
 * @author squid233
 * @since 2020/10/16
 */
abstract class Screen protected constructor(private val parent: Screen?) {
    protected val widgets: ObjectList<ScreenWidget> = ObjectArrayList(6)
    protected val buttons: ObjectList<ButtonWidget> = ObjectArrayList(6)
    var client: Mc2dClient
        protected set

    open fun render(g: Graphics) {
        DrawHelper.drawDirtBackground(g)
        DrawHelper.drawDefaultBackground(g)
        for (w in widgets) {
            w.render(g)
        }
    }

    fun onKeyDown(e: KeyEvent) {
        if (e.keyChar.toInt() == KeyEvent.VK_ESCAPE) {
            close()
        }
    }

    fun addWidget(widget: ScreenWidget): ScreenWidget {
        widgets.add(0, widget)
        return widget
    }

    fun addButton(widget: ButtonWidget): ButtonWidget {
        buttons.add(0, widget)
        return addWidget(widget) as ButtonWidget
    }

    protected fun close() {
        parent?.let { open(parent) }
    }

    fun open(screen: Screen) {
        Screens.openScreen = screen
    }

    fun onMouseClicked() {
        for (b in buttons) {
            if (b.isHover && b.isEnable) {
                b.action.invoke(b)
                break
            }
        }
    }

    init {
        client = Mc2dClient.instance
    }
}