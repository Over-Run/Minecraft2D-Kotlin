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

import com.google.common.collect.BiMap
import com.google.common.collect.ImmutableBiMap
import io.github.overrun.mc2d.lang.Language
import io.github.overrun.mc2d.option.Options
import io.github.overrun.mc2d.text.IText
import io.github.overrun.mc2d.text.LiteralText
import io.github.overrun.mc2d.text.TranslatableText
import io.github.overrun.mc2d.util.Constants
import io.github.overrun.mc2d.util.Coordinator
import io.github.overrun.mc2d.util.DrawHelper.drawCenteredText
import java.awt.Graphics

/**
 * @author squid233
 * @since 2020/10/13
 */
class LanguageScreen(parent: Screen) : Screen(parent) {
    private val map: BiMap<String, IText> = ImmutableBiMap.of(
        "en_us", genItem("en_us"),
        "zh_cn", genItem("zh_cn")
    )

    private fun genItem(locale: String) = LiteralText(
        "${Language[locale, "language.name"]} (${Language[locale, "language.region"]})"
    )

    override fun render(g: Graphics) {
        super.render(g)
        drawCenteredText(g, TranslatableText("options.mc2d.choose_lang"), 5)
        drawCenteredText(
            g, TranslatableText("options.mc2d.current_lang", map[Options[Options.LANG, "en_us"]]), 25
        )
        drawCenteredText(g, TranslatableText("options.mc2d.lang_warning"), 45)
    }

    init {
        val cbw = ComboBoxWidget(
            this,
            map["en_us"]!!,
            map["zh_cn"]!!
        ).setSelectedItem(map[Options[Options.LANG, "en_us"]])
        addWidget(cbw)
        addButton(
            ButtonWidget(
                -210, 30, 200, Coordinator.D_M, TranslatableText(Constants.DONE),
                {
                    Options.setAndSave(Options.LANG, map.inverse()[cbw.selectedItem!!.text])
                    close()
                })
        )
        addButton(
            ButtonWidget(
                10, 30, 200, Coordinator.D_M, TranslatableText(Constants.CANCEL),
                {
                    cbw.setSelectedItem(map[Options.get(Options.LANG, "en_us")])
                    close()
                })
        )
    }
}