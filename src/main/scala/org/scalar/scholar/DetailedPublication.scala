package org.scalar.scholar

import java.net.URL

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * A detailed view of a publication.
 */
class DetailedPublication private (val content: Document)
{
   /**
    * A dictionary of informations that relate to the publication.
    * Keys may vary, depending on the type of publication (article,
    * book chapter, etc.).
    */
   lazy val metadata: Map[String, String] = {
      import scala.collection.JavaConversions._
      val keys = content.select("div.gsc_field").iterator.toList.map(_.text)
      val values = content.select("div.gsc_value").iterator.toList.map(_.text)
      // TODO: ignore div#gsc_graph_bars so that the history is not included in the value

      (keys zip values).toMap
   }

}

object DetailedPublication
{
   def apply(url: URL) = new DetailedPublication(Jsoup.connect(url.toString).get)
}
