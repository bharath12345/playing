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

/*
  Total number of data points in 3 hours = 1 + (3 * 2) + (3 * 12) + (3 * 60 * 2) + (3 * 60 * 20)
                                          => 1 + 6 + 36 + 360 + 3600
                                          => 4003 data points in 3 hours
  Heroku postgres can accomodate max 10,000 data points. So it can accomodate data for approx 2.5 hours

  To make sure that the credit card does not get billed, do the following -
    - Keep the 3sec aggregated data only for one hour - so write a cron job to continuously purge the 3sec wala
        data-point. run it at minute boundary. this will mean that there will only be 3600 data-points of this
        type in the database (approx) at any point of time.
    - Keep 30sec aggregated data for 6 hours. So this will contribute 2 * 360 = 720 data points
    - Keep 5min aggregated data for 24 hours. So this will contribute 8 * 36 = 288 data points
    - Keep 30min aggregated data for 1 week. So this will contribute 7 * 8 * 6 = 336 data points
    - Keep 3hr aggregated data for 30 days. So this will contribute 30 * 8 = 240 data points

    Total for twitter data points = 3600 + 720 + 288 + 336 + 240 = 5184 data points
    This gives breathing space of about 4k rows in the Heroku free postgres instance
 */

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

