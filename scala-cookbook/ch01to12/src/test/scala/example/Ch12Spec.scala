package example

import java.io.{BufferedWriter, File, FileInputStream, FileOutputStream, FileWriter, IOException, PrintWriter}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scala.io.Source

class Ch12Spec extends AnyFlatSpec with Matchers {
  "12.01" should "open and read files" in {
    val filename = "/home/lihe/Projects/playground/scala-cookbook/build.sbt"

    val bufferedSource = Source.fromFile(filename)
    for (line <- bufferedSource.getLines()) {
      println(line)
    }
    bufferedSource.close
  }

  "12.02" should "write to text file" in {
    val pw = new PrintWriter(new File("pw.txt"))
    pw.write("12.02 hello world")
    pw.close

    val file = new File("bw.txt")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write("12.02 buffered writer: haha")
    bw.close
  }

  "12.03" should "write to binary file" in {
    var in = None: Option[FileInputStream]
    var out = None: Option[FileOutputStream]
    try {
      in = Some(new FileInputStream("/home/lihe/Projects/playground/scala-cookbook/1.pdf"))
      out = Some(new FileOutputStream("/home/lihe/Projects/playground/scala-cookbook/1.copy.pdf"))
      var c = 0
      while ( {
        c = in.get.read; c != -1
      }) {
        out.get.write(c)
      }
    } catch {
      case e: IOException => e.printStackTrace
    } finally {
      println("enter finally ...")
      if (in.isDefined) in.get.close
      if (out.isDefined) out.get.close
    }
  }

  "12.08" should "list files under directory" in {
    /*def getListOfFiles(dir: String): List[File] = {
      val d = new File(dir)
      if (d.exists && d.isDirectory) {
        d.listFiles(_.isFile).toList
      } else {
        List[File]()
      }
    }*/

    val files = new File("/home/lihe/Projects/playground/scala-cookbook/ch01")
      .listFiles(_.isFile).toList
    files.map(_.getName).foreach(println)
  }

  "12.09" should "list sub directories under directory" in {
    val folders = new File("/home/lihe/Projects/playground/scala-cookbook/ch01")
      .listFiles(_.isDirectory).toList
    folders.map(_.getName).foreach(println)
  }

  "12.10" should "execute external command" in {
    import sys.process._
    "ls -al".!

    val exitCode = "ls -al".!
    exitCode shouldBe 0

    val exitCode2 = Seq("ls", "-l", "-a").!
    exitCode2 shouldBe 0

    val exitCode3 = Seq("ls", "-l ").!
    (exitCode3 != 0) shouldBe true
  }


}
