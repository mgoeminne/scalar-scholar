package org.scalar.scholar

/**
 * Created by mg on 2/09/15.
 */
object Test
{
   def main(args: Array[String])
   {
      val profile = Profile("j2MGGBAAAAAJ")

      profile.publications.head.detailed.metadata foreach println
   }
}
