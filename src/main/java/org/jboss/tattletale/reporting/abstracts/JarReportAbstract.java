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
package org.jboss.tattletale.reporting.abstracts;

import org.jboss.tattletale.Version;
import org.jboss.tattletale.core.Archive;
import org.jboss.tattletale.core.Location;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javassist.bytecode.ClassFile;
import org.jboss.tattletale.reporting.common.*;
/**
 * JAR report
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author <a href="mailto:torben.jaeger@jit-consulting.de">Torben Jaeger</a>
 */
public abstract class JarReportAbstract extends ArchiveReport
{
   /** DIRECTORY */
   private static final String DIRECTORY = "jar";

   /** File name */
   private String filename;

   /** The level of depth from the main output directory that this jar report would sit */
   private int depth;
   
   public JarReportAbstract(Archive archive) 
   {
      this(archive, 1);
   }
   
   public JarReportAbstract(Archive archive,int i) 
   {
      this(archive, 1);
   }
  


   /**
    * returns a Jar report specific writer.
    * Jar reports don't use a index.html but a html per archive.
    *
    * @return the BufferedWriter
    * @throws IOException if an error occurs
    */
   @Override
   protected BufferedWriter getBufferedWriter() throws IOException
   {
      return getBufferedWriter(getFilename());
   }

   private String getFilename()
   {
      return filename;
   }

   private void setFilename(String filename)
   {
      this.filename = filename;
   }
   
}
