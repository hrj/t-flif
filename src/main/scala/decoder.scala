package hrj.tflif

import scala.sys.process._
import java.awt.Rectangle

case class TileOptions(name: String, width: Int, height: Int)
case class DecodeOptions(top: Int, bottom:Int, left: Int, right: Int, downsample: Int) {
  def width = (right - left + 1) / downsample
  def height = (bottom - top + 1) / downsample
}

case class DecodeResult()

object Decoder {
  def decode(tileOpts: TileOptions, decodeOpts: DecodeOptions) : DecodeResult = {
    val topTile = (decodeOpts.top / tileOpts.height) + 1
    val bottomTile = (decodeOpts.bottom / tileOpts.height) + 1

    val leftTile = (decodeOpts.left / tileOpts.width) + 1
    val rightTile = (decodeOpts.right / tileOpts.width) + 1

    // TODO: result image type should be deduced / configurable
    val resultImg = new java.awt.image.BufferedImage(decodeOpts.width, decodeOpts.height, java.awt.image.BufferedImage.TYPE_4BYTE_ABGR)
    println(s"Decode wxh = ${decodeOpts.width} ${decodeOpts.height}")
    val resultRaster = resultImg.getRaster

    var dx = 0
    var dy = 0

    def mkSrcRect(r:Int, c:Int) = {
      val srcX = if (c == leftTile) ((decodeOpts.left % tileOpts.width)/decodeOpts.downsample) else 0
      val srcY = if (r == topTile) ((decodeOpts.top % tileOpts.height)/decodeOpts.downsample) else 0
      val srcWidth = ((if (c == rightTile) ((decodeOpts.right % tileOpts.width) + 1) else tileOpts.width)/ decodeOpts.downsample) - srcX
      val srcHeight = ((if (r == bottomTile) ((decodeOpts.bottom % tileOpts.height) + 1) else tileOpts.height) / decodeOpts.downsample) - srcY
      val srcRect = new Rectangle(srcX, srcY, srcWidth, srcHeight)
      srcRect
    }

    def readPng(pngFileName: String, r: Int, c: Int) = {
      val srcRect = mkSrcRect(r, c)
      // println("src rect: " + srcRect)

      val img = javax.imageio.ImageIO.read(new java.io.File(pngFileName))

      val raster = img.getData
      val data:Array[Int] = raster.getPixels(srcRect.x, srcRect.y, srcRect.width, srcRect.height, null)
      resultRaster.setPixels(dx, dy, srcRect.width, srcRect.height, data)
      dx += srcRect.width
      if (c == rightTile) {
        dy += srcRect.height
        dx = 0
      }
    }

    for (r <- topTile to bottomTile) {
      for (c <- leftTile to rightTile) {
        flifDecode(tileOpts, decodeOpts, r, c) match {
          case Some(pngFileName) => readPng(pngFileName, r, c)
          case None => println(s"Unable to decode $r, $c")
        }
      }
    }

    javax.imageio.ImageIO.write(resultImg, "PNG", new java.io.File("result.png"))

    DecodeResult()
  }

  private def flifDecode(tileOpts: TileOptions, decodeOpts: DecodeOptions, r: Int, c: Int) = {
    val fileName = s"${tileOpts.name}-$r-$c.flif"
    val outFileName = s"out-$r-$c.png"
    // TODO: Avoid overwrite
    val command = s"flif --overwrite -s ${decodeOpts.downsample} -d $fileName $outFileName"
    // println(command)
    val result = command.!
    if (result == 0) {
      Some(outFileName)
    } else {
      None
    }
  }

}
