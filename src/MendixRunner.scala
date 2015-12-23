import sys.process._
import java.io.File
import java.nio.file.{Paths, Path}

/**
  * Created by imu on 12/23/2015.
  */
object MendixRunner {

  val mxPath: Path = Paths.get("C:\\Program Files\\Mendix\\")

  val modelerPath: Path = Paths.get("modeler")

  val versionSelectorFolderName = "Version Selector"

  def main(args: Array[String]): Unit = {
    val selectedVersion = getUserSelectedVersion()
    val dtap = getUserSelectedEnv()

    val cmd = Paths.get(this.mxPath.toString(), selectedVersion, modelerPath.toString(), "Modeler.exe --environment=" + dtap)
    execCmd(cmd.toString())
    println("bye")
    System.exit(0)
  }

  def getUserSelectedVersion(): String = {
    val dirList = getSubFolders(this.mxPath)
    val versionList: List[Path] = dirList.map((file) => {
      val myPath = Paths.get(file.getAbsolutePath)
      val relPath = mxPath.relativize(myPath)
      relPath
    }).filter((path) => {
      path.toString().compareTo(versionSelectorFolderName) != 0
    })
    var i = 0;
    versionList.foreach((path) => {
      println(s"[$i]: ${path.toString}")
      i += 1
    })

    val selection = scala.io.StdIn.readLine(s"Choose version [0-${versionList.size-1}]>")
    versionList(selection.toInt).toString
  }

  def getUserSelectedEnv(): String = {
    scala.io.StdIn.readLine("Choose environment [DTAP]>");
  }

  def getSubFolders(dir: Path): List[File] = {
    val d = new File(dir.toString)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isDirectory).toList
    } else {
      List[File]()
    }
  }

  def execCmd(cmd: String): Unit = {
    println(cmd)
    val pb = Process(cmd)
    pb.run()
  }
}
