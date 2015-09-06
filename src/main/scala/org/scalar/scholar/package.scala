package org.scalar

/**
 * This is a toolset implemented in Scala that provides functions for extracting papers associated to a given author,
 * as well as their citations, from Google Scholar.
 *
 * It is inspired by the [[https://cran.r-project.org/web/packages/scholar CRAN scholar package]].
 *
 *
 * On Google Scholar, paper authors have a "Profile", which is associated
 * to each of their publications. Google regularly browses the Web looking for
 * new available publications. Despite one must generally pay for reading these publications,
 * meta-informations (such as the abstract, or the number of pages) are typically freely available.
 *
 * Also, a pre-print version of most papers may be freely downloaded.
 *
 * This library is used by creating a [[scholar.Profile]] based on the author's id
 * and invoking its methods.
 *
 * An author's id may be known by looking at the end of the url attached to the author's GS webpage.
 *
 * {{{
 *    // Sir Newton is on Google Scholar
 *    val myProfile = Profile("j2MGGBAAAAAJ")
 *    val h = myProfile.h()
 * }}}
 */
package object scholar
{

}
