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
import org.jboss.tattletale.reporting.interfaces.Report;
import org.jboss.tattletale.reporting.interfaces.ReportInterface;
import org.jboss.tattletale.reporting.common.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.SortedSet;

/**
 * Dump
 *
 * @author Jesper Pedersen <jesper.pedersen@jboss.org>
 * @author Jay Balunas <jbalunas@jboss.org>
 */
public abstract class DumpAbstract implements ReportInterface
{
   /** New line character */
   private static final String NEW_LINE = System.getProperty("line.separator");

 
   /**
    * Simple static method to return the System property of line separator.
    *
    * @return - the line separator from System properties.
    */
   public static String newLine()
   {
      return NEW_LINE;
   }

   private static String getIndexHtmlSize(Report r)
   {
	   File indexFile = new File(r.getOutputDirectory().getAbsolutePath() + File.separator + r.getIndexName());
	   return ((indexFile.length() / 1024) + 1) + "KB";
   }

	@Override
	public void generate(SortedSet<Report> dependenciesReports,
	           SortedSet<Report> generalReports,
	           SortedSet<Report> archiveReports,
	           SortedSet<Report> customReports,
	           String outputDir)
	{
		generateIndex(dependenciesReports, generalReports, archiveReports, customReports, outputDir);
		
	}
}
