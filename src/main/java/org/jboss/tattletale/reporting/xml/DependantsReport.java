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

import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.core.NestableArchive;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jboss.tattletale.reporting.abstracts.ClassDependantsReportAbstract;
import org.jboss.tattletale.reporting.common.*;
/**
 * Dependants report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public class DependantsReport extends ClassDependantsReportAbstract
{

   /** NAME */
   private static final String NAME = "Dependants";

   /** DIRECTORY */
   private static final String DIRECTORY = "dependants";

   

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
     // bw.write("     <th>Dependants</th>" + Dump.newLine());

      boolean odd = true;

      for (Archive archive : archives)
      {
         String archiveName = archive.getName();
         int finalDot = archiveName.lastIndexOf(".");
         String extension = archiveName.substring(finalDot + 1);

         
          bw.write("  <element>" + Dump.newLine());
         
         
         bw.write("     <Archive>../" + extension + "/" + archiveName + ".xml" +
               archiveName + "</Archive>" + Dump.newLine());
         bw.write("     <Dependants>");


         SortedSet<String> result = new TreeSet<String>();

         for (Archive a : archives)
         {

            for (String require : getRequires(archive))
            {

               if (archive.doesProvide(require) && (getCLS() == null || getCLS().isVisible(a, archive)))
               {
                  result.add(a.getName());
               }
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
               if (r.endsWith(".jar"))
               {
                  bw.write("../jar/" + r + ".xml" + r);
               }
               else
               {
                  bw.write("<i>" + r + "</i>");
               }

               if (resultIt.hasNext())
               {
                  bw.write(", ");
               }
            }
         }

         bw.write("</Dependants>" + Dump.newLine());
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
         requires.addAll(nestableArchive.getRequires());
         for (Archive sa : subArchives)
         {
            requires.addAll(getRequires(sa));
         }
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
