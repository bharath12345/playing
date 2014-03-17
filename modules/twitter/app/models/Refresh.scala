package models

import scala.concurrent.duration.{FiniteDuration, DurationInt}

/**
 * Created by bharadwaj on 14/03/14.
 */
sealed trait Refresh {
  def duration: FiniteDuration
  def period: Int
}
case class Refresh3() extends Refresh {
  def duration = 3.seconds
  def period = 0
}
case class Refresh30() extends Refresh {
  def duration = 30.seconds
  def period = 1
}
case class Refresh300() extends Refresh {
  def duration = 5.minutes
  def period = 2
}
case class Refresh1800() extends Refresh {
  def duration = 30.minutes
  def period = 3
}
case class Refresh10800() extends Refresh {
  def duration = 3.hours
  def period = 4
}

object Refresh {
  def apply(i: Int) = {
    i match {
      case 0 => Refresh3()
      case 1 => Refresh30()
      case 2 => Refresh300()
      case 3 => Refresh1800()
      case 4 => Refresh10800()
    }
  }
}

