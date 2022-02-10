import java.io.File
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO
import java.io.PrintWriter
import scala.collection.mutable.ArrayBuffer
import java.awt.Color
object Main {
  def main(args:Array[String]): Unit = {
    val oldFile = new File("Output.png")
    if(oldFile.isFile())
      oldFile.delete()
    if(args.isEmpty)
      throw new IllegalArgumentException("Missing arguments")
    val path = args(0)
    var width = 2
    for(i <- args.indices) {
      args(i) match {
        case "-w" => width = args(i+1).toInt
        case _ =>
      }
    }
    val dir = new File(path)
    val files = dir.listFiles.filter(_.isFile())
      .filter(a => ".*(\\.png|\\.jpg)".r.matches(a.getName))
      .map(_.getPath()).toList.sorted
    println(files)
    
    val images = files.map(p => ImageIO.read(new File(p))).toIndexedSeq
    var maxW = 0
    var maxH = 0

    for(i <- images) {
      if(i.getWidth() > maxW)
        maxW = i.getWidth()
      if(i.getHeight() > maxH)
        maxH = i.getHeight()
    }

    val height = ((images.length.toDouble)/(width.toDouble)).ceil.toInt
    val whitespace = new BufferedImage(maxW,maxH,BufferedImage.TYPE_INT_ARGB)
    var graphics = whitespace.getGraphics()
    graphics.setColor(Color.WHITE)
    graphics.drawRect(0,0,maxW,maxH)
    graphics.dispose()

    val output = new BufferedImage(maxW * width,maxH * height,BufferedImage.TYPE_INT_ARGB)
    graphics = output.getGraphics()
    graphics.setColor(new Color(1f,1f,1f,0f))

    val split = images.grouped(width).toIndexedSeq
    val f1 = new File("log.txt")
    // val writer = new PrintWriter(f1)

    for(i <- 0 until split.length) {
      for(j <- 0 until width) {
        if(!split.indices.contains(j) || !split(j).indices.contains(i)) {
          graphics.fillRect(j*maxW,i*maxH,maxW,maxH)
        }
        else {
          graphics.drawImage(split(j)(i),j*maxW,i*maxH,null)
        }
      }
    }
    graphics.dispose()

    val f = new File("Output.png")
    ImageIO.write(output,"png",f)
  }
}
