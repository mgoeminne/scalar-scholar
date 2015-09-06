package org.scalar.scholar

import java.io.File
import java.net.URL

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.io.Source

/**
 * A Scholar profile, corresponding to an author. This profile may be associated
 * to publications the author submitted. When available, details are provided about these
 * publications, such that the journal in which they have been submitted, or the URL
 * to use for downloading a copy of them.
 *
 * All methods of this class require at least one HTTP query to Google Scholar.
 * Please take measures for limiting the number of queries sent in a short period.
 */
class Profile private (val content: Document)
{
   private val CITATION_INDEX = 0
   private val RECENT_CITATION_INDEX = 1
   private val H_INDEX = 2
   private val RECENT_H_INDEX = 3
   private val I10_INDEX = 4
   private val RECENT_I10_INDEX = 5



   /**
    *
    * The name of the author associated to the profile
    */
   lazy val name: String = content.select("div#gsc_prf_in").first().text

   /**
    * The profile id.
    */
   lazy val id: String = ("""user=([^&]*)""".r.findFirstMatchIn(content.html)).get.group(1)


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
   lazy val history: Map[Int, Int] = {
      import scala.collection.JavaConversions._
      val years = content  .select("span.gsc_g_t")
                           .iterator
                           .toList
                           .map(_.text.toInt)

      val citations = content.select("span.gsc_g_al")
                        .iterator
                        .toList
                        .map(_.text.toInt)

      years.zip(citations).toMap
   }


   /**
    * @return All the publications associated to the profile.
    */
   def publications: Stream[SimplifiedPublication] = {
      val pubs: List[SimplifiedPublication] = extractPublications(content)

      if(pubs.size < Profile.EXPECTED_PUBLICATIONS) pubs.toStream
      else pubs.toStream #::: nextPublications(Profile.EXPECTED_PUBLICATIONS, Profile.EXPECTED_PUBLICATIONS)
   }

   private def nextPublications(offset:Int, size: Int): Stream[SimplifiedPublication] =
   {
      val url = "https://scholar.google.com/citations?hl=en&user=" + id + "&cstart=" + offset + "&pagesize=" + Profile.EXPECTED_PUBLICATIONS
      val pubcontent = Jsoup.connect(url).get

      val pubs = extractPublications(pubcontent)

      if(pubs.size < Profile.EXPECTED_PUBLICATIONS) pubs.toStream
      else pubs.toStream #::: nextPublications(offset + Profile.EXPECTED_PUBLICATIONS, Profile.EXPECTED_PUBLICATIONS)
   }

   private def extractPublications(content: Document): List[SimplifiedPublication] =
   {
      import scala.collection.JavaConversions._

      content.select("tr.gsc_a_tr")
         .iterator
         .toList
         .map(entry =>
      {
         val title = entry.select("a.gsc_a_at").first().text.toString
         val info = entry.select("div.gs_gray").iterator.toList
         val authors = info(0).text.split(",").map(_.trim)
         val description = info(1).text
         val citations = entry.select("a.gsc_a_ac").first.text.replaceAll("[^0-9]", "") match
         {
            case "" => 0
            case s: String => s.toInt
         }

         val year = entry.select("span.gsc_a_h").first.text.trim match
         {
            case "" => None
            case s: String => Some(s.toInt)
         }

         val url = new URL(Profile.url(id), entry.select("a.gsc_a_at").first.attr("href"))

         SimplifiedPublication(title, authors, description, citations, year, url)
      })
   }
}

object Profile
{

   protected val EXPECTED_PUBLICATIONS = 100

   /**
    * Creates a [[Profile]], based on an id representing the author.
    * @param id the author's id
    * @return the Profile associated to the id
    */
   def apply(id: String) = new Profile(Jsoup.connect(url(id).toString).get)

   /**
    * Creates a [[Profile]], based on a file containing the webpage associated to the author.
    * @param resource a IO object containing the webpage associated to the author
    * @return the Profile associated to the id
    */
   def apply(resource: Source) = new Profile(Jsoup.parse(resource.getLines().mkString))

   private def url(id: String) = new URL("https://scholar.google.com/citations?hl=en&user=" + id + "&pagesize=" + Profile.EXPECTED_PUBLICATIONS)
}