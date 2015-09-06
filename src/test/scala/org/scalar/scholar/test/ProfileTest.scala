package org.scalar.scholar.test

import org.scalar.scholar.Profile
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

   it should "have 13025 citations" in {  // 2015-09-04
      newton.citations should equal (13038)
   }

   it should "have at most as many recent citations than total citations" in {
      newton.recent_citations should equal (4574)
   }

   it should "have a h-index of 45" in { // 2015-09-06
      newton.h should equal (45)
   }

   it should "have a recent h-index at most as high as its global h-index" in {
      newton.recent_h should be <= newton.h
   }

   it should "have a i-10 index of 99" in { // 2015-09-06
      newton.i10 should equal (99)
   }

   it should "have a recent i10-index of 61" in {
      newton.recent_i10 should be (61)
   }

   it should "present a correct history" in {
      newton.history should equal (Map(2007 -> 639,
                                       2008 -> 729,
                                       2009 -> 697,
                                       2010 -> 674,
                                       2011 -> 760,
                                       2012 -> 808,
                                       2013 -> 919,
                                       2014 -> 865,
                                       2015 -> 535))
   }

   it should "have 100 publications immediatly available from the main Profile webpage" in {
      newton.publications.take(100).size should equal (100)
   }
}
