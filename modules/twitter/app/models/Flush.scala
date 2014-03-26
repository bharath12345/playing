package models

import scala.concurrent.duration.{FiniteDuration, DurationInt}

/**
 * Created by bharadwaj on 26/03/14.
 */

/*
  The above numbers are for collection of ONLY ONE SEARCH STUB. In this case, I have 4 => modi, rahul, kejri, india
  So, the below numbers have to be multiplied by 4

  -------

  Total number of data points in 3 hours = 1 + (3 * 2) + (3 * 12) + (3 * 60 * 2) + (3 * 60 * 20)
                                          => 1 + 6 + 36 + 360 + 3600
                                          => 4003 data points in 3 hours
  Heroku postgres can accomodate max 10,000 data points. So it can accomodate data for approx 2.5 hours

  -------

  To make sure that the credit card does not get billed, do the following -
    - Keep the 3sec aggregated data only for ONE HOUR - so write a cron job to continuously purge the 3sec wala
        data-point. run it at minute boundary. this will mean that there will only be   1200 data-points of this
        type in the database (approx) at any point of time.
    - Keep 30sec aggregated data for 3 Hours. So this will contribute = 360 data points
    - Keep 5min aggregated data for 24 Hours. So this will contribute 8 * 36 = 288 data points
    - Keep 30min aggregated data for 1 Week. So this will contribute 7 * 8 * 6 = 336 data points
    - Keep 3hr aggregated data for 30 Days. So this will contribute 30 * 8 = 240 data points

    Total for twitter data points = 1200 + 360 + 288 + 336 + 240 = 2424 data points

    ------

    So, with these numbers, just about 4 STUBS can be accommodated. But definitely not more.

 */

sealed trait Flush {
  def duration: FiniteDuration
}
case class FlushOneHour() extends Flush {
  def duration = 3.seconds//1.hour
}
case class FlushThreeHours() extends Flush {
  def duration = 3.hours
}
case class FlushOneDay() extends Flush {
  def duration = 24.hours
}
case class FlushOneWeek() extends Flush {
  def duration = (24 * 7).hours
}
case class FlushOneMonth() extends Flush {
  def duration = (24 * 30).hours
}
