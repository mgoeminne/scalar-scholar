package org.scalar.scholar

import org.jsoup.Jsoup

/**
 * A Scholar profile, corresponding to an author. This profile may be associated
 * to publications the author submitted. When available, details are provided about these
 * publications, such that the journal in which they have been submitted, or the URL
 * to use for downloading a copy of them.
 *
 * All methods of this class require at least one HTTP query to Google Scholar.
 * Please take measures for limiting the number of queries sent in a short period.
 */
case class Profile(id: String)
{
   private val CITATION_INDEX = 0
   private val RECENT_CITATION_INDEX = 1
   private val H_INDEX = 2
   private val RECENT_H_INDEX = 3
   private val I10_INDEX = 4
   private val RECENT_I10_INDEX = 5


   /**
    * A XML representation of the page associated to the profile.
    * Thanks to this variable, most properties can be retrieved without
    * any additional access to the Google
    */
   private lazy val content = Jsoup.connect("https://scholar.google.com/citations?hl=en&user=" + id).get

   /**
    *
    * The name of the author associated to the profile
    */
   lazy val name: String = content.select("div#gsc_prf_in").first().text

   /**
    * The h index associated to the profile.
    * This is a measure of the productivity and the "popularity" of an author,
    * in term of citations.
    *
    *  Its value is the greatest value ''n'' so that the author
    *  has ''n'' publications cited at least ''n'' times.
    */
   lazy val h: Int = {
      import scala.collection.JavaConversions._
      content.select("td.gsc_rsb_std").iterator.toList(H_INDEX).text.toInt
   }

   /**
    * The recent h index associated to the profile.
    * It corresponds to the h index of the profile, considering
    * only the citations of the last 5 years. This value may therefore decrease
    * over time.
    */
   lazy val recent_h: Int = {
      import scala.collection.JavaConversions._
      content.select("td.gsc_rsb_std").iterator.toList(RECENT_H_INDEX).text.toInt
   }

   /**
    * The number of times the author has been cited.
    * It should correspond to the sum of the citations of
    * all the author's publications.
    */
   lazy val citations: Int = {
      import scala.collection.JavaConversions._
      content.select("td.gsc_rsb_std").iterator.toList(CITATION_INDEX).text.toInt
   }

   /**
    * the recent number of citations for the author.
    * It corresponds to the number of citations in the last 5 years.
    */
   lazy val recent_citations: Int = {
      import scala.collection.JavaConversions._
      content.select("td.gsc_rsb_std").iterator.toList(RECENT_CITATION_INDEX).text.toInt
   }

   /**
    * The number of publications that have at least 10 citations.
    */
   lazy val i10: Int = {
      import scala.collection.JavaConversions._
      content.select("td.gsc_rsb_std").iterator.toList(I10_INDEX).text.toInt
   }

   /**
    * The recent number of publications that have at least 10 citations in the last 5 years.
    */
   lazy val recent_i10: Int = {
      import scala.collection.JavaConversions._
      content.select("td.gsc_rsb_std").iterator.toList(RECENT_I10_INDEX).text.toInt
   }

   /**
    * The current position of the author. May be empty.
    */
   lazy val position: String = content.select("div.gsc_prf_il").first().text

   /**
    * The verified author's email address, if any.
    */
   lazy val email: Option[String] = content.select("div#gsc_prf_ivh").first().text match {
      case "No verified email" => None
      case s: String => Some(s)
   }

   /**
    * The number of citations, detailed by year.
    */
   lazy val history: Map[Int, Int] = ???

   /**
    * @return All the publications associated to the profile.
    */
   def publications(): Stream[Publication] = ???
}