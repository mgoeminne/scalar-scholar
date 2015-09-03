package org.scalar.scholar

import java.net.URL
import java.time.Year

/**
 * A research publication
 * @param title         The publication title, as it appears in the first page.
 * @param authors       An ordered sequence of authors involved in the publication.
 * @param description   A short textual description of the publication, that generally
 *                      includes the Journal in which the publication has been submitted (if relevant),
 *                      the nature of the publication, the year of publication, etc. All these properties are optional
 *                      and are presented in a single plain text that may be empty. More detailed information can be
 *                      found by calling specialised methods.
 * @param citations     The number of times the publication has been cited.
 * @param year          The year at which the publication has been submitted, if available.
 *
 */
case class Publication(title: String,
                       authors: Seq[String],
                       description: String,
                       citations: Int,
                       year: Option[Int])
{
   def pub_abstract(): Option[String] = ???
   def date: Option[String] = ???

   def history: Map[Year, Int] = ???
   def gs_url: URL = ???
   def preprint: Option[URL] = ???
   def reference: Reference = ???
}
