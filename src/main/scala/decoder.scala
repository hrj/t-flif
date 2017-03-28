package hrj.leaves

import scala.sys.process._

case class TileOptions(name: String, width: Int, height: Int)
case class DecodeOptions(top: Int, bottom:Int, left: Int, right: Int, downsample: Int)

case class DecodeResult()

object Decoder {
  def decode(tileOpts: TileOptions, decodeOpts: DecodeOptions) : DecodeResult = {
    val topTile = (decodeOpts.top / tileOpts.height) + 1
    val bottomTile = (decodeOpts.bottom / tileOpts.height) + 1

    val leftTile = (decodeOpts.left / tileOpts.width) + 1
    val rightTile = (decodeOpts.right / tileOpts.width) + 1
    for (r <- topTile to bottomTile) {
      for (c <- leftTile to rightTile) {
        val fileName = s"${tileOpts.name}-$r-$c.flif"
        val outFileName = s"out-$r-$c.png"
        val command = s"flif -s ${decodeOpts.downsample} -d $fileName $outFileName"
        println(command)
        val result = command.!
        println(result)
      }
    }

    DecodeResult()
  }
}
