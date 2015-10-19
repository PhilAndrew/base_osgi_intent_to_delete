package com.baseosgi.akkaosgi

//import aQute.bnd.annotation.component._
import com.baseosgi.Printer
import org.osgi.framework.{BundleContext, BundleActivator}

import scala.util.{Success, Try}

class Activator extends BundleActivator {

  val printer = new SysoutPrinter()

  override def start(context: BundleContext) {
    val t: Try[String] = Success("test")
    println(t.get)
    printer.print("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzStart OSGi Bundle!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
  }

  override def stop(context: BundleContext) {
    printer.print("Stop OSGi Bundle")
  }
}

class SysoutPrinter extends Printer {

  /*  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
      val p = new java.io.PrintWriter(f)
      try { op(p) } finally { p.close() }
    }
  */
  def print(text : String) = {
    /*val data = Array("Five","strings","in","a","file!")
    printToFile(new File("c:\\home\\projects\\universe.txt")) { p =>
      data.foreach(p.println)
    }*/
    println(text)
  }

}


// DS Annotation Demo
/*
@Component
class DeclarativeServicePrinter extends Printer {

  val printer = new SysoutPrinter()

  def print(text : String) = printer.print(text)

}

@Component
class Client {

  var printer : Printer = null

  @Reference
  def setPrinter(printer : Printer) = this.printer = printer

  def unsetPrinter(printer : Printer) = this.printer = null

  @Activate
  def start = printer.print("Start DS Client Component from Universe Hello World...")

  @Deactivate
  def stop = printer.print("Stop DS Client Component from Universe Hello World...")

}*/