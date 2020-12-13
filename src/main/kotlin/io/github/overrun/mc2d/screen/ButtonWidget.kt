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

import io.github.overrun.mc2d.text.IText
import io.github.overrun.mc2d.util.Coordinator
import io.github.overrun.mc2d.util.Coordinator.D_M
import io.github.overrun.mc2d.util.DrawHelper
import io.github.overrun.mc2d.util.Images
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import java.awt.Graphics
import java.awt.Image
import java.awt.Point
import java.util.*
import java.util.function.Consumer

/**
 * @author squid233
 * @since 2020/10/12
 */
open class ButtonWidget constructor(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    private val layout: Int = D_M,
    val text: IText,
    override val action: PressAction,
    tooltipSupplier: Consumer<List<IText?>?> = Consumer { }
) : AbstractButtonWidget() {
    val tooltips: List<IText?> = ObjectArrayList(1)
    override var isEnable: Boolean = true

    constructor(
        x: Int,
        y: Int,
        width: Int,
        layout: Int,
        text: IText
    ) : this(x, y, width, layout, text, {}) {
        false.apply { isEnable = this }
    }

    override fun render(g: Graphics) {
        super.render(g)
        val tY = (height shr 1) - (text.getPrevHeight(g) shr 1)
        DrawHelper.drawText(
            g, text,
            Coordinator.transformation(
                x + (width shr 1) - (text.getPrevWidth(g) shr 1),
                if (layout <= Coordinator.M_R) y + tY else y - tY,
                layout
            )
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ButtonWidget
        return x == other.x
                && y == other.y
                && width == other.width
                && layout == other.layout
                && text == other.text
                && action == other.action
                && tooltips == other.tooltips
                && isEnable == other.isEnable
    }

    override fun hashCode() = Objects.hash(x, y, width, layout, text, action, tooltips, isEnable)

    override val usualTexture: Image
        get() = Images.BUTTON
    override val hoverTexture: Image
        get() = Images.BUTTON_HOVER
    override val disableTexture: Image
        get() = Images.BUTTON_DISABLE
    override val height: Int
        get() = 25
    override val prevPos: Point
        get() = Coordinator.transformation(x, y, layout)

    init {
        tooltipSupplier.accept(tooltips)
    }
}