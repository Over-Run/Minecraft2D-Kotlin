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

import io.github.overrun.mc2d.text.LiteralText
import io.github.overrun.mc2d.text.TranslatableText
import io.github.overrun.mc2d.util.Coordinator
import io.github.overrun.mc2d.util.DrawHelper
import io.github.overrun.mc2d.util.Images
import java.awt.Graphics
import kotlin.system.exitProcess

/**
 * @author squid233
 * @since 2020/10/12
 */
class TitleScreen(parent: Screen?) : Screen(parent) {
    override fun render(g: Graphics) {
        super.render(g)
        DrawHelper.drawCenterImage(g, Images.LOGO, 10)
    }

    init {
        addButton(ButtonWidget(-100, 62, 200, Coordinator.U_M, TranslatableText("button.mc2d.singleplayer")))
        addButton(ButtonWidget(-100, 88, 200, Coordinator.U_M, TranslatableText("button.mc2d.multiplayer")))
        addButton(ButtonWidget(-100, 114, 200, Coordinator.U_M, LiteralText("Mods")))
        addButton(
            ButtonWidget(-100, 140, 200, Coordinator.U_M, TranslatableText("button.mc2d.options"),
                { open(Screens.OPTIONS_SCREEN) })
        )
        addButton(
            ButtonWidget(-100, 166, 200, Coordinator.U_M, TranslatableText("button.mc2d.exit_game"),
                { exitProcess(0) })
        )
        addButton(
            LanguageButtonWidget(60, 192, Coordinator.U_M) { open(Screens.LANG_SCREEN) })
    }
}