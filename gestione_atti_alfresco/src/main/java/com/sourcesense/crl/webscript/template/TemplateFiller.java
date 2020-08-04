/*
 *  * #%L
 *  * Alfresco Repository
 *  * %%
 *  * Copyright (C) 2005 - 2016 Alfresco Software Limited
 *  * %%
 *  * This file is part of the Alfresco software. 
 *  * If the software was purchased under a paid Alfresco license, the terms of 
 *  * the paid license agreement will prevail.  Otherwise, the software is 
 *  * provided under the following open source license terms:
 *  * 
 *  * Alfresco is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU Lesser General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  * 
 *  * Alfresco is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU Lesser General Public License for more details.
 *  * 
 *  * You should have received a copy of the GNU Lesser General Public License
 *  * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 *  * #L%
 */
package com.sourcesense.crl.webscript.template;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Componente per la gestione dei template.
 */
public class TemplateFiller {

    private static Log logger = LogFactory.getLog(TemplateFiller.class);

    /**
     * Questo metodo si occupa nel sostituire nel template delle chiavi cone i valori forniti nei parametri.
     * @param documentByteArray il documento da modificare
     * @param replacements le sostituzioni da applicare al documento in formato chiave:valore
     * @return Il documento modificato con tutte le sostituzioni fornite nei parametri
     */
    public static byte[] searchAndReplace(byte[] documentByteArray, HashMap<String, String> replacements) {
        logger.info("Enter 'searchAndReplace' method");

        try {
            HWPFDocument document = new HWPFDocument(new ByteArrayInputStream(documentByteArray)); 
            Range docRange = document.getRange();
            int numParagraphs = docRange.numParagraphs();
            logger.info("docRange="+docRange+" and numParagraphs="+numParagraphs); 
            Set<String> keySet = replacements.keySet();
            logger.info("keySet="+keySet);

            Paragraph paragraph = null;
            CharacterRun charRun = null;
            Iterator<String> keySetIterator = null;
            int numCharRuns = 0;
            String text = null;
            String key = null;
            String value = null; 
            for (int i = 0; i < numParagraphs; i++) {
                paragraph = docRange.getParagraph(i);
                logger.info("paragraph n. "+i); 
                numCharRuns = paragraph.numCharacterRuns();
                logger.info("numCharRuns="+numCharRuns);

                for (int j = 0; j < numCharRuns; j++) {
                    charRun = paragraph.getCharacterRun(j);
                    logger.info("charRun n. "+j); 
                    text = charRun.text();
                    logger.info("text="+text); 
                    keySetIterator = keySet.iterator();
                    while (keySetIterator.hasNext()) { 
                        key = keySetIterator.next(); 

                        if (text.contains(key)) {
                            logger.info("text contains key="+key); 
                            if (replacements.get(key) != null) {
                                value = replacements.get(key);
                            } else {
                                value = "";
                            }
                            /* TODO Attenzione
                               L'uso di indexof sulla variabile text non e' consentito: se nella charRun
                               vengono restituite piu' chiavi da sostituire con del testo, il codice
                               non si comporta correttamente in quanto l'indice non restituisce la posizione
                               iniziale della key nel charRun
                             */
                            int start = text.indexOf(key);
                            charRun.replaceText(key, value, start);
                            logger.info(key+" replaced with "+value+" starting from "+start);
                        }
                    }
                }
            }

            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            logger.info("ostream size=="+ostream.size());
            document.write(ostream);

            logger.info("Enter 'searchAndReplace' method");

            return ostream.toByteArray();
        } catch (Exception e) {
            logger.error("Exception details: " + e.getMessage());
            return null; 
        }
    }


    public static byte[] searchAndReplaceFooter(byte[] documentByteArray, HashMap<String, String> replacements) {

        try {

            HWPFDocument document = new HWPFDocument(new ByteArrayInputStream(documentByteArray));

            HeaderStories headerStore = new HeaderStories(document);
            Range docRange = headerStore.getOddFooterSubrange();

            if (docRange != null) {
                int numParagraphs = docRange.numParagraphs(); 
                Set<String> keySet = replacements.keySet();

                Paragraph paragraph = null;
                CharacterRun charRun = null;
                Iterator<String> keySetIterator = null;
                int numCharRuns = 0;
                String text = null;
                String key = null;
                String value = null; 
                for (int i = 0; i < numParagraphs; i++) {
                    paragraph = docRange.getParagraph(i); 
                    numCharRuns = paragraph.numCharacterRuns();

                    for (int j = 0; j < numCharRuns; j++) {

                        charRun = paragraph.getCharacterRun(j); 
                        text = charRun.text(); 
                        keySetIterator = keySet.iterator();
                        while (keySetIterator.hasNext()) { 
                            key = keySetIterator.next();
                            if (text.contains(key)) { 
                                if (replacements.get(key) != null) {
                                    value = replacements.get(key);
                                } else {
                                    value = "";
                                }
                                int start = text.indexOf(key);
                                charRun.replaceText(key, value, start);

                            }
                        }
                    }
                }

                ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                document.write(ostream);
                return ostream.toByteArray();
            } else {
                return documentByteArray;
            }


        } catch (Exception e) {
            logger.error("Exception details: " + e.getMessage());
            return null; 
        }


    }


}
