package org.scalar.scholar

/**
 * Created by mg on 2/09/15.
 */
object Test
{
   def main(args: Array[String])
   {
      val profile = Profile("j2MGGBAAAAAJ")

      println(profile.name)
      println(profile.email)
      println(profile.citations)
      println(profile.h)
      println(profile.i10)
      println(profile.recent_citations)
      println(profile.recent_h)
      println(profile.recent_i10)
   }
}
