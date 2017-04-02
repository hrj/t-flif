package hrj.tflif

import org.rogach.scallop._

class Conf(arguments: Seq[String], isServer: Boolean) extends ScallopConf(arguments) {
  val top = opt[Int](default = Option(0))
  val left = opt[Int](default = Option(0))
  val right = opt[Int](required = !isServer, validate = {x:Int => x > left()})
  val bottom = opt[Int](required = !isServer, validate = {x => x > top()})
  val downsample = opt[Int](required = !isServer, default = Option(1), validate = { x => Integer.bitCount(x) == 1 })

  val tileWidth = opt[Int](required = true)
  val tileHeight = opt[Int](required = true)
  val tileName = opt[String](default = Option("tile"))

  val verbose = opt[Boolean]()

  version("0.1")
  banner("tflif")
  verify()
}

object TFLIFApp extends App {
  override def main(args: Array[String]) {
    val (serverArgs, nonServerArgs) = args.partition(_ == "-s")
    val isServer = !serverArgs.isEmpty
    val conf = new Conf(nonServerArgs, isServer)

    if (conf.verbose()) {
      println(conf.summary)
    }

    if (!isServer) {
      val tileOpts = TileOptions(conf.tileName(), conf.tileWidth(), conf.tileHeight())
      val decodeOpts = DecodeOptions(conf.top(), conf.bottom(), conf.left(), conf.right(), conf.downsample())
      Decoder.decode(tileOpts, decodeOpts)
    } else {
      // start server
    }
  }
}
