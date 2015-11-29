/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.view;

import com.controller.ProcessFile;
import com.model.Node;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ahmad
 */
@ManagedBean
@Named
@ViewScoped
public class GedView implements Serializable {

    /**
     * Creates a new instance of GedView
     */
    public GedView() {
    }

    private UploadedFile file;
    private String outputXML;

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getOutputXML() {
        return outputXML;
    }

    public void setOutputXML(String outputXML) {
        this.outputXML = outputXML;
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();

        if (uploadedFile.getSize() > 0) {

            try {

                ProcessFile processFile = new ProcessFile();
                String outPut = processFile.ParseFile(uploadedFile.getInputstream());
                if (outPut != null) {
                    this.outputXML = outPut;

                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", uploadedFile.getFileName() + " Was Parsed Successfully."));

                } else {

                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Some Error occurred."));

                    //FacesContext.getCurrentInstance().addMessage()
                }

            } catch (Exception ex) {
                Logger.getLogger(GedView.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

   

}
