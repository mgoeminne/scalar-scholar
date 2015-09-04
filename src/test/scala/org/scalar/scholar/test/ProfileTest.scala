package org.scalar.scholar.test

import org.scalar.scholar.Profile
import org.scalatest.Matchers._
import org.scalatest._

import scala.io.Source

class ProfileTest extends FlatSpec with Matchers {
   val newton = Profile(Source.fromURL(getClass.getResource("/newton.html")))

   "Sir Isaac Newton's profile" should "have 'isaac newton' as name" in {
      newton.name should be ("isaac newton")
   }

   it should "have 'University of Cambridge' as position" in {
      newton.position should be ("University of Cambridge")
   }

   it should "have no verified email" in {
      newton.email should be (None)
   }

   it should "have at least 13025 citations" in {  // 2015-09-04
      newton.citations should equal (13025)
   }

   it should "have at most as many recent citations than total citations" in {
      newton.recent_citations should be <= newton.citations
   }

   it should "have a h-index of at least 45" in { // 2015-09-04
      newton.h should equal (45)
   }

   it should "have a recent h-index at most as high as its global h-index" in {
      newton.recent_h should be <= newton.h
   }

   it should "have a i-10 index of at least 99" in { // 2015-09-04
      newton.i10 should equal (99)
   }

   it should "have a recent i10-index at most as high as its global i10-index" in {
      newton.recent_i10 should be <= newton.i10
   }

   it should "present a correct history" in {
      newton.history should equal (Map(2007 -> 639,
                                       2008 -> 730,
                                       2009 -> 697,
                                       2010 -> 674,
                                       2011 -> 761,
                                       2012 -> 808,
                                       2013 -> 919,
                                       2014 -> 864,
                                       2015 -> 522))
   }
}
