/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller;

import com.model.Node;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author ahmad
 */
public class ProcessFile {

    public ProcessFile() {
    }
//I will iterate over the file lines
  //for every iteration I will read 2 lines
    //I will check the if the depth of the current line and the next line
    //if the current depth is equal to the next line I will print the current node with its closure tag
    //if the current depth is greater than the next line I will print the current node and start printing the closing tags
    //if the current depth is less than the next depth I will print the node but I wont print its close tag because it has childs
    public String ParseFile(InputStream CurrentFile) throws IOException {
        BufferedReader br = null;
        StringBuilder xmlBuilder = new StringBuilder();

        try {
            br = new BufferedReader(new InputStreamReader(CurrentFile, "UTF-8"));
            Node currentNode = null;
            Node nextNode = null;
            int[] spaceIndexes;
            String currentLine = "";
            String nextline;
            xmlBuilder.append("<gedcom>");
            HashMap<Integer, String> closeTags = new HashMap<Integer, String>();
            do {
                if (currentLine.equals("")) {
                    currentLine = br.readLine();
                }
                nextline = br.readLine();
                if (currentLine != null) {
                    if (currentNode == null) {
                        spaceIndexes = getSpaceIndexes(currentLine);
                        currentNode = createNode(currentLine, spaceIndexes);
                    }
                }
                if (nextline != null) {
                    spaceIndexes = getSpaceIndexes(nextline);
                    nextNode = createNode(nextline, spaceIndexes);
                    if (nextNode.getDepth() == currentNode.getDepth()) {
                        xmlBuilder.append(currentNode);

                    } else if (nextNode.getDepth() > currentNode.getDepth()) {
                        currentNode.setIsParent(true);
                        closeTags.put(currentNode.getDepth(), currentNode.GetCloseTag());
                        xmlBuilder.append(currentNode);

                    } else if (nextNode.getDepth() < currentNode.getDepth()) {
                        xmlBuilder.append(currentNode);
                        for (int i = currentNode.getDepth() - 1; i >= nextNode.getDepth(); i--) {
                            xmlBuilder.append(closeTags.get(i));

                            closeTags.remove(i);
                        }
                    }
                } else {
                    xmlBuilder.append(currentNode);
                    for (int i = currentNode.getDepth() - 1; i >= 0; i--) {
                        xmlBuilder.append(closeTags.get(i));

                        closeTags.remove(i);
                    }

                }
                currentNode = nextNode;
                currentLine = nextline;
            } while (currentLine != null);

            xmlBuilder.append("</gedcom>");
            return xmlFormatter(xmlBuilder.toString(), 10);

        } catch (IOException ex) {
            Logger.getLogger(ProcessFile.class.getName()).log(Level.SEVERE, null, ex);

            return null;
        } catch (Exception ex) {
            Logger.getLogger(ProcessFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;

        } finally {

            br.close();

        }

    }
// to format the xml out so it can be readable
    public String xmlFormatter(String input, int depth) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", depth);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new RuntimeException(e); // simple exception handling, please review it
        }
    }

    //this method to get the indexes of the first 2 spaces so I can get split the string and get the tag and the depth 
    public int[] getSpaceIndexes(String str) {
        int firstIndex = -1;
        int secondIndex = -1;
        if (str == null) {
            return null;
        }

        boolean firstSpaceFound = false;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                if (!firstSpaceFound) {
                    firstIndex = i;
                    firstSpaceFound = true;
                } else {
                    secondIndex = i;
                    return new int[]{firstIndex, secondIndex};

                }

            }
        }
        if (secondIndex == -1) {
            secondIndex = str.length();
        }
        return new int[]{firstIndex, secondIndex};
    }

    // this will create the node from the line
    // each line will represnt a node with
    //every node got depth,tag and value 
    //also every node got 2 extra attrs that shows whether the node is a parnet node and if the tag is ID or no.  
    private Node createNode(String line, int[] SpaceIndexes) {
        Node node = new Node();

        node.setDepth(Integer.parseInt(line.substring(0, SpaceIndexes[0])));

        String tag = line.substring(SpaceIndexes[0] + 1, SpaceIndexes[1]);
        if (tag.startsWith("@") && tag.endsWith("@")) {
            node.setTagIsID(true);
        }
        node.setTag(tag);
        if (line.length() == SpaceIndexes[1]) {
            node.setValue("");
        } else {
            node.setValue(line.substring(SpaceIndexes[1] + 1));
        }

        return node;
    }

}
