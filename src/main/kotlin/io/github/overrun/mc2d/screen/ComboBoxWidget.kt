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

import io.github.overrun.mc2d.Minecraft2D
import io.github.overrun.mc2d.text.IText
import io.github.overrun.mc2d.util.Coordinator
import io.github.overrun.mc2d.util.DrawHelper
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import java.awt.Graphics

/**
 * @author squid233
 * @since 2020/10/16
 */
class ComboBoxWidget(screen: Screen, vararg itemContents: IText) : ScreenWidget() {
    private val items: MutableMap<IText?, ComboBoxItem?>
    var selectedItem: ComboBoxItem? = null
        private set

    override fun render(g: Graphics) {
        DrawHelper.fillRect(g, Screens.BG_COLOR, x, y, Minecraft2D.width, y shl 2, Coordinator.U_L)
        for (i in items.values) {
            i!!.render(g)
        }
    }

    fun setSelectedItem(item: ComboBoxItem?): ComboBoxWidget {
        selectedItem = item
        return this
    }

    fun setSelectedItem(itemContent: IText?): ComboBoxWidget {
        return setSelectedItem(getItems()[itemContent])
    }

    fun getItems(): Map<IText?, ComboBoxItem?> {
        return items
    }

    override val x: Int
        get() = 0
    override val y: Int
        get() = Minecraft2D.height / 6

    init {
        items = Object2ObjectArrayMap(itemContents.size)
        for (i in itemContents.indices) {
            val t = itemContents[i]
            items[t] = screen.addButton(
                ComboBoxItem(y + i * 25, t) { w: AbstractButtonWidget? -> setSelectedItem(w as ComboBoxItem?) }
            ) as ComboBoxItem
        }
    }
}