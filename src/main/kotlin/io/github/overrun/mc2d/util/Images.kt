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

package io.github.overrun.mc2d.util

import java.awt.image.BufferedImage

/**
 * @author squid233
 * @since 2020/11/24
 */
object Images {
    val OPTIONS_BACKGROUND = guiImg("options_background")
    val LOGO = guiImg("logo")
    val BUTTON = guiImg("button")
    val BUTTON_HOVER = guiImg("button_hover")
    val BUTTON_DISABLE = guiImg("button_disable")
    val LANG_BUTTON = guiImg("lang_button")
    val LANG_BUTTON_HOVER = guiImg("lang_button_hover")
    private fun guiImg(nm: String): BufferedImage {
        return ImgUtil.readImage(ResourceLocation("textures/gui/$nm.png"))!!
    }
}