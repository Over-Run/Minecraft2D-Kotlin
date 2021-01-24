/*
 * MIT License
 *
 * Copyright (c) 2020-2021 Over-Run
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

import io.github.overrun.mc2d.util.GlUtils.generateMipmap
import io.github.overrun.mc2d.util.Utils.putInt
import io.github.overrun.mc2d.util.Utils.use
import it.unimi.dsi.fastutil.objects.Object2IntMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import org.apache.logging.log4j.LogManager
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFWImage
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * @author squid233
 * @since 2021/01/07
 */
object ImageReader {
    @JvmStatic
    private val ID_MAP: Object2IntMap<String> = Object2IntOpenHashMap(16)
    @JvmStatic
    private var lastId = -999999
    @JvmStatic
    private val logger = LogManager.getLogger()

    /**
     * Read an image as [BufferedImage].
     *
     * @param path The image path.
     * @return Read [BufferedImage].
     */
    @JvmStatic
    fun read(path: String): BufferedImage? = try {
        ImageIO.read(ClassLoader.getSystemResource(path))
    } catch (t: Throwable) {
        logger.catching(t)
        null
    }

    @JvmStatic
    fun readImg(path: String) = MemoryStack.stackPush().use {
        val img = read(path)
        if (img == null) {
            Texture(2, 2, putInt(
                it.malloc(16), intArrayOf(
                    -524040, -16777216,
                    -16777216, -524040
                )
            ).flip())
        } else {
            val width = img.width
            val height = img.height
            val buf = BufferUtils.createByteBuffer(width * height shl 2)
            for (i in 0 until height) {
                for (j in 0 until width) {
                    val cm = img.colorModel
                    val o = img.raster.getDataElements(j, i, null)
                    buf.putInt(
                        cm.getAlpha(o) shl 24 or (cm.getBlue(o) shl 16) or (cm.getGreen(o) shl 8) or cm.getRed(o)
                    )
                }
            }
            buf.flip()
            Texture(width, height, buf)
        }
    }

    @JvmStatic
    fun readGlfwImg(path: String): GLFWImage.Buffer = GLFWImage.malloc().use {
        val buf = readImg(path)
        return@use GLFWImage.malloc(1).put(0, it.set(buf.width, buf.height, buf.buffer))
    }

    @JvmStatic
    fun loadTexture(path: String) = loadTexture(path, GL_NEAREST)

    @JvmStatic
    fun loadTexture(path: String, mode: Int) =
        if (ID_MAP.containsKey(path)) ID_MAP.getInt(path)
        else MemoryStack.stackPush().use {
            val buffer = it.mallocInt(1)
            glGenTextures(buffer)
            val id = buffer.get(0)
            bindTexture(id)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, mode)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mode)
            val img = read(path)
            if (img == null) {
                val pixels = it.malloc(16)
                pixels.asIntBuffer().put(intArrayOf(
                    -524040, -16777216,
                    -16777216, -524040))
                generateMipmap(GL_TEXTURE_2D, GL_RGBA, 2, 2, GL_RGBA, GL_UNSIGNED_BYTE, pixels)
            } else {
                val w = img.width
                val h = img.height
                val pixels = BufferUtils.createByteBuffer(w * h shl 2)
                val rawPixels = IntArray(w * h)
                img.getRGB(0, 0, w, h, rawPixels, 0, w)
                for (i in rawPixels.indices) {
                    val a = rawPixels[i] shr 24 and 255
                    val r = rawPixels[i] shr 16 and 255
                    val g = rawPixels[i] shr 8 and 255
                    val b = rawPixels[i] and 255
                    rawPixels[i] = a shl 24 or (b shl 16) or (g shl 8) or r
                }
                pixels.asIntBuffer().put(rawPixels)
                generateMipmap(GL_TEXTURE_2D, GL_RGBA, w, h, GL_RGBA, GL_UNSIGNED_BYTE, pixels)
            }
            ID_MAP[path] = id
            return@use id
        }

    @JvmStatic
    fun bindTexture(id: Int) {
        if (lastId != id) {
            glBindTexture(GL_TEXTURE_2D, id)
            lastId = id
        }
    }

    @JvmStatic
    fun withGlfwImg(img: GLFWImage.Buffer, block: (GLFWImage.Buffer) -> Unit) = img.use { block.invoke(it) }

    @JvmStatic
    fun withGlfwImg(path: String, block: (GLFWImage.Buffer) -> Unit) = withGlfwImg(readGlfwImg(path), block)
}
