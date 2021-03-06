/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.tattletale.reporting.xml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.core.NestableArchive;
import org.jboss.tattletale.profiles.Profile;
import org.jboss.tattletale.reporting.abstracts.DependantsReportAbstract;
import org.jboss.tattletale.reporting.common.ReportStatus;
/**
 * Depends On report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public class DependsOnReport extends DependantsReportAbstract
{
   /** NAME */
   private static final String NAME = "Depends On";

   /** DIRECTORY */
   private static final String DIRECTORY = "dependson";



   /**
    * write out the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an error occurs
    */
   public void writeBodyContent(BufferedWriter bw) throws IOException
   {
      bw.write("<elements>" + Dump.newLine());

     
     // bw.write("     <th>Archive</th>" + Dump.newLine());
     // bw.write("     <th>Depends On</th>" + Dump.newLine());
    

      boolean odd = true;

      for (Archive archive : archives)
      {
         String archiveName = archive.getName();
         int finalDot = archiveName.lastIndexOf(".");
         String extension = archiveName.substring(finalDot + 1);
 
          bw.write("  <element>" + Dump.newLine());
         
         
         bw.write("     <Archive>../" + extension + "/" + archiveName + ".xml" +
               archiveName + "</Archive>" + Dump.newLine());
         bw.write("     <Depends_on>");

         SortedSet<String> result = new TreeSet<String>();

         for (String require : getRequires(archive))
         {

            boolean found = false;
            Iterator<Archive> ait = archives.iterator();
            while (!found && ait.hasNext())
            {
               Archive a = ait.next();

               if (a.doesProvide(require) && (getCLS() == null || getCLS().isVisible(archive, a)))
               {
                  result.add(a.getName());
                  found = true;
               }
            }

            if (!found)
            {
               Iterator<Profile> kit = getKnown().iterator();
               while (!found && kit.hasNext())
               {
                  Profile profile = kit.next();

                  if (profile.doesProvide(require))
                  {
                     found = true;
                  }
               }
            }

            if (!found)
            {
               result.add(require);
            }
         }

         if (result.size() == 0)
         {
            bw.write("none");
         }
         else
         {
            Iterator<String> resultIt = result.iterator();
            while (resultIt.hasNext())
            {
               String r = resultIt.next();
               if (r.endsWith(".jar") || r.endsWith(".war") || r.endsWith(".ear"))
               {
                  bw.write("../" + extension + "/" + r + ".xml" + r );
               }
               else
               {
                  if (!isFiltered(archive.getName(), r))
                  {
                     bw.write("<i>" + r + "</i>");
                     status = ReportStatus.YELLOW;
                  }
                  else
                  {
                     bw.write("<i>" + r + "</i>");
                  }
               }
               
            }
         }

         bw.write("</Depends_on>" + Dump.newLine());
         bw.write("  </element>" + Dump.newLine());

         odd = !odd;

      }

      bw.write("</elements>" + Dump.newLine());
   }

   private SortedSet<String> getRequires(Archive archive)
   {
      SortedSet<String> requires = new TreeSet<String>();
      if (archive instanceof NestableArchive)
      {
         NestableArchive nestableArchive = (NestableArchive) archive;
         List<Archive> subArchives = nestableArchive.getSubArchives();
         for (Archive sa : subArchives)
         {
            requires.addAll(getRequires(sa));
         }

         requires.addAll(nestableArchive.getRequires());
      }
      else
      {
         requires.addAll(archive.getRequires());
      }
      return requires;
   }

   /**
    * write out the header of the report's content
    *
    * @param bw the writer to use
    * @throws IOException if an error occurs
    */
   public void writeBodyHeader(BufferedWriter bw) throws IOException
   {
      bw.write("<reporting>" + Dump.newLine());
      bw.write(Dump.newLine());

      bw.write("<h1>" + NAME + "</h1>" + Dump.newLine());

      bw.write("../index.xml" + Dump.newLine());
      

   }


}
