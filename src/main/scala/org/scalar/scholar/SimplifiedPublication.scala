package org.scalar.scholar

import java.net.URL

/**
 * A simplified view of a publication. A more detailed view is available by looking in
 * the Detailed view
 * @param title         The publication title, as it appears in the first page.
 * @param authors       An ordered sequence of authors involved in the publication.
 * @param description   A short textual description of the publication, that generally
 *                      includes the Journal in which the publication has been submitted (if relevant),
 *                      the nature of the publication, the year of publication, etc. All these properties are optional
 *                      and are presented in a single plain text that may be empty. More detailed information can be
 *                      found by calling specialised methods.
 * @param citations     The number of times the publication has been cited.
 * @param year          The year at which the publication has been submitted, if available.
 * @param url           The url of the page containing a detailed view of the publication
 */
case class SimplifiedPublication(  title: String,
                                   authors: Seq[String],
                                   description: String,
                                   citations: Int,
                                   year: Option[Int],
                                   private val url: URL)
{
   lazy val detailed: DetailedPublication = DetailedPublication(url)
}
